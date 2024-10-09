package com.seecoder.BlueWhale.serviceImpl;

import com.seecoder.BlueWhale.enums.RoleEnum;
import com.seecoder.BlueWhale.exception.BlueWhaleException;
import com.seecoder.BlueWhale.po.*;
import com.seecoder.BlueWhale.repository.*;
import com.seecoder.BlueWhale.service.CommentService;
import com.seecoder.BlueWhale.service.CommodityService;
import com.seecoder.BlueWhale.service.StoreService;
import com.seecoder.BlueWhale.util.CommonUtil;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.util.TokenUtil;
import com.seecoder.BlueWhale.vo.StoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: venlax
 *  店铺服务实现
 */
@Service
public class StoreServiceImpl implements StoreService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    CommodityRepository commodityRepository;

    @Autowired
    CommodityPicRepository commodityPicRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CouponGroupRepository couponGroupRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    CommonUtil commonUtil;

    @Autowired
    CommodityService commodityService;

    /**
     * 创建店铺
     * @param storeVO
     * @return 是否创建成功
     */
    @Override
    public Boolean createStore(StoreVO storeVO) {
        User user=securityUtil.getCurrentUser();
        if (storeRepository.existsByName(storeVO.getName())) {
            throw BlueWhaleException.storeNameExist();
        }
        if (user.getRole() == RoleEnum.MANAGER) {
            Store store = storeVO.toPO();
            store.setCreateTime(new Date());
            storeRepository.save(store);
            return true;
        } else {
            throw BlueWhaleException.insufficientPermissions();
        }
    }

    /**
     * 获取所有店铺
     * @return 所有店铺
     */
    @Override
    public List<StoreVO> getAllStores() {

        return storeRepository.findAll().stream()
                .map(Store::toVO)
                .collect(Collectors.toList());
    }

    /**
     * 获取店铺
     * @param id
     * @return 店铺信息
     */
    @Override
    public StoreVO getStore(Integer id) {
        return storeRepository.findById(id).map(Store::toVO).orElse(null);
    }

    /**
     * 更新店铺信息
     * @param storeVO
     * @return 是否更新成功
     */
    @Override
    public Boolean updateInformation(StoreVO storeVO) {
     if (storeVO == null || storeVO.getId() == null) {
         throw BlueWhaleException.storeNotExist();
     }
     User user = securityUtil.getCurrentUser();

     if (user.getRole() != RoleEnum.MANAGER) {
         throw BlueWhaleException.insufficientPermissions();
     }

     Store store = storeRepository.findById(storeVO.getId()).orElse(null);

     if (store == null) {
         throw BlueWhaleException.storeNotExist();
     }

     store.setIntroduction(storeVO.getIntroduction() == null ? store.getIntroduction() : storeVO.getIntroduction());
     store.setName(storeVO.getName() == null ? store.getName() : storeVO.getName());
     store.setLogo(storeVO.getLogo() == null ? store.getLogo() : storeVO.getLogo());
     store.setAddress(storeVO.getAddress() == null ? store.getAddress() : storeVO.getAddress());
     storeRepository.save(store);
     return true;
    }

    /**
     * 删除店铺
     * @param id
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteStore(Integer id) {
        if (id == null) {
            throw BlueWhaleException.storeNotExist();
        }
        User user = securityUtil.getCurrentUser();

        if (user.getRole() != RoleEnum.MANAGER) {
            throw BlueWhaleException.insufficientPermissions();
        }

        Store store = storeRepository.findById(id).orElse(null);

        if (store == null) {
            throw BlueWhaleException.storeNotExist();
        }

        List<User> users = userRepository.findByStoreId(id);

        for (User user_ : users) {
            user_.setStoreId(null);
            userRepository.save(user_);
        }

        List<Commodity> commodities = commodityRepository.findByStoreId(id);

        for (Commodity commodity : commodities) {
            commodityService.deleteCommodity(commodity.getId());
        }

        List<Comment> comments = commentRepository.findByStoreId(id);

        for (Comment comment : comments) {
            comment.setStoreId(null);
            commentRepository.save(comment);
        }

        List<Order> orders = orderRepository.findByStoreId(id);
        for (Order order : orders) {
            order.setStoreId(null);
            orderRepository.save(order);
        }

        List<CouponGroup> couponGroups = couponGroupRepository.findByStoreId(id);

        for (CouponGroup couponGroup : couponGroups) {
            List<Coupon> coupons = couponRepository.findByGroupId(couponGroup.getId());
            for (Coupon coupon : coupons) {
                couponRepository.deleteById(coupon.getId());
            }
            couponGroupRepository.deleteById(couponGroup.getId());
        }


        storeRepository.deleteById(id);
        return true;
    }
}
