package com.seecoder.BlueWhale.serviceImpl;

import com.seecoder.BlueWhale.enums.RoleEnum;
import com.seecoder.BlueWhale.exception.BlueWhaleException;
import com.seecoder.BlueWhale.po.Comment;
import com.seecoder.BlueWhale.po.Coupon;
import com.seecoder.BlueWhale.po.CouponGroup;
import com.seecoder.BlueWhale.po.User;
import com.seecoder.BlueWhale.repository.CouponGroupRepository;
import com.seecoder.BlueWhale.repository.CouponRepository;
import com.seecoder.BlueWhale.repository.StoreRepository;
import com.seecoder.BlueWhale.service.CouponService;
import com.seecoder.BlueWhale.util.CommonUtil;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.vo.CommentVO;
import com.seecoder.BlueWhale.vo.CouponGroupVO;
import com.seecoder.BlueWhale.vo.CouponVO;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: venlax
 * 实现了优惠券的功能。
 */

@Service
public class CouponServiceImpl implements CouponService {
    @Autowired
    CouponGroupRepository couponGroupRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    CommonUtil commonUtil;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    private RedissonClient redissonClient; // 需要运行redis服务才能启动项目



    public CouponServiceImpl(SecurityUtil securityUtil, CouponRepository couponRepository, CouponGroupRepository couponGroupRepository){
        this.securityUtil = securityUtil;
        this.couponRepository =couponRepository;
        this.couponGroupRepository = couponGroupRepository;
    }

    /**
     * 创建优惠券组
     * @param storeId
     * @param amount
     * @return 优惠券组
     */
    private CouponGroup createCouponGroup(Integer storeId, Integer amount) {
        CouponGroup couponGroup = new CouponGroup();
        couponGroup.setStoreId(storeId);
        couponGroup.setAmount(amount);
        couponGroup.setRestAmount(amount);
        couponGroupRepository.save(couponGroup);
        assert Objects.nonNull(couponGroup.getId());
        return couponGroup;
    }

    // 将Coupon转换为CouponVO
    private CouponVO couponToVO(Coupon coupon) {
        CouponVO res = coupon.toVO();
        CouponGroup couponGroup = couponGroupRepository.findById(coupon.getGroupId()).orElse(null);
        if (couponGroup.getStoreId() == null) {
            res.setStoreName(null);
        } else {
            res.setStoreName(commonUtil.getStoreNameById(couponGroup.getStoreId()));
        }
        res.setIsExpired(coupon.getExpirationTime().before(new Date()));
        return res;
    }


    /**
     * 发布优惠券组
     * @param storeId
     * @param amount
     * @param couponVO
     *
     * @return 是否发布成功
     */
    @Override
    public Boolean distributeCouponGroup(Integer storeId, Integer amount, CouponVO couponVO) {
        if (amount < 0) {
            throw BlueWhaleException.couponAmountIllegal();
        }

        User user = securityUtil.getCurrentUser();

        // 权限检查
        if (storeId == null) {
            if (user.getRole() != RoleEnum.CEO && user.getRole() != RoleEnum.MANAGER) {
                throw BlueWhaleException.insufficientPermissions();
            }
        } else {
            if (user.getRole() == RoleEnum.CUSTOMER) {
                throw BlueWhaleException.insufficientPermissions();
            } else if (user.getRole() == RoleEnum.STAFF) {
                if (!user.getStoreId().equals(storeId)) {
                    throw BlueWhaleException.notStoreStaff();
                }
            }
        }

        CouponGroup couponGroup = createCouponGroup(storeId, amount);

        for (int i = 0; i < amount; i++) {
            CouponVO coupon = new CouponVO();
            coupon.setUserId(null);
            coupon.setGroupId(couponGroup.getId());
            coupon.setReduction(couponVO.getReduction());
            coupon.setThreshold(couponVO.getThreshold());
            coupon.setExpirationTime(couponVO.getExpirationTime());
            coupon.setIsUsed(false);
            coupon.setCouponType(couponVO.getCouponType());
            couponRepository.save(coupon.toPO());
        }

        return true;
    }

    /**
     * 领取优惠券
     * @param groupId
     * @return 是否领取成功
     */
    @Override
    @Transactional
    public Boolean redeemCoupon(Integer groupId) {
        User user = securityUtil.getCurrentUser();

        CouponGroup couponGroup = couponGroupRepository.findById(groupId).orElse(null);

        if (couponGroup == null) {
            throw BlueWhaleException.couponNotExist();
        }

        List<Coupon> userCoupons = couponRepository.findByUserIdAndGroupId(user.getId(), groupId);

        if (!userCoupons.isEmpty()) {
            throw BlueWhaleException.couponAlreadyRedeem();
        }

        if (couponGroup.getRestAmount() == 0) {
            throw BlueWhaleException.couponGroupEmpty();
        }


        // 使用分布式锁来保证并发安全性
        String lockKey = "redeemCoupon_" + groupId;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(10, TimeUnit.SECONDS); // 尝试获取锁，等待10秒
            if (isLocked) {

                List<Coupon> coupons = couponRepository.findByGroupId(groupId);

                for (Coupon coupon : coupons) {
                    if (coupon.getUserId() == null) {
                        coupon.setUserId(user.getId());
                        couponRepository.save(coupon);
                        couponGroup.setRestAmount(couponGroup.getRestAmount() - 1);
                        couponGroupRepository.save(couponGroup);
                        return true;
                    }
                }
            } else {
                throw BlueWhaleException.couponRedeemTimeout();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw BlueWhaleException.couponRedeemInterrupted();
        } finally {
            lock.unlock();
        }

        return false;
    }


    /**
     * 获取优惠券组
     * @return 优惠券组列表
     */
    @Override
    public List<CouponGroupVO> getCouponGroups() {
        User user = securityUtil.getCurrentUser();

        if (user.getRole() == RoleEnum.STAFF) {
            return couponGroupRepository.findByStoreId(user.getStoreId()).stream().map(CouponGroup::toVO).collect(Collectors.toList());
        } else {
            return couponGroupRepository.findAll().stream().map(CouponGroup::toVO).collect(Collectors.toList());
        }
    }

    /**
     * 获取优惠券
     * @param groupId
     * @return 优惠券列表
     */
    @Override
    public List<CouponVO> getCouponsByGroupId(Integer groupId) {
        return couponRepository.findByGroupId(groupId).stream().map(this::couponToVO).collect(Collectors.toList());
    }

    /**
     * 获取用户的优惠券
     * @param userId
     * @return 优惠券列表
     */
    @Override
    public List<CouponVO> getCouponsByUserId(Integer userId) {
        User user = securityUtil.getCurrentUser();

        if (user.getRole() == RoleEnum.CUSTOMER) {
            if (!user.getId().equals(userId)) {
                throw BlueWhaleException.insufficientPermissions();
            }
        }
        return couponRepository.findByUserId(userId).stream().map(this::couponToVO).collect(Collectors.toList());
    }
}