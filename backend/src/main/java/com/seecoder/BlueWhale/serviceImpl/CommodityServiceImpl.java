package com.seecoder.BlueWhale.serviceImpl;

import com.seecoder.BlueWhale.enums.RoleEnum;
import com.seecoder.BlueWhale.enums.CommodityTypeEnum;
import com.seecoder.BlueWhale.exception.BlueWhaleException;
import com.seecoder.BlueWhale.po.*;
import com.seecoder.BlueWhale.repository.*;
import com.seecoder.BlueWhale.service.CommodityService;
import com.seecoder.BlueWhale.util.CommonUtil;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.util.TokenUtil;
import com.seecoder.BlueWhale.vo.CommodityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EnumType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: venlax
 *  商品服务实现
 */
@Service
public class
CommodityServiceImpl implements CommodityService {

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
    /**
     * 创建商品
     * @param commodityVO
     * @return 是否创建成功
     */

    @Override
    public Boolean createCommodity(CommodityVO commodityVO) {
        User user = securityUtil.getCurrentUser();


        if (!user.getStoreId().equals(commodityVO.getStoreId())) {
            throw BlueWhaleException.notStoreStaff();
        }
        Commodity commodity = commodityVO.toPO();
        commodityRepository.save(commodity);

        if (commodityVO.getPicLink() != null && !commodityVO.getPicLink().isEmpty()) {
            // 遍历图片链接列表
            for (String picLink : commodityVO.getPicLink()) {
                // 为每个链接创建CommodityPic实体
                CommodityPic commodityPic = new CommodityPic();
                commodityPic.setCommodityId(commodity.getId()); // 设置商品ID
                commodityPic.setPicLink(picLink); // 设置图片链接
                // 保存CommodityPic实体
                commodityPicRepository.save(commodityPic);
            }
        }
        return true;
    }

    /**
     * 获取商品图片
     * @param commodityId
     * @return 商品图片链接列表
     */
    private List<String> getCommodityPic(Integer commodityId) {
        List<CommodityPic> commodityPics = commodityPicRepository.findByCommodityId(commodityId);

        // 从每个CommodityPic实体中提取picLink，组成列表返回

        return commodityPics.stream()
                .map(CommodityPic::getPicLink)
                .collect(Collectors.toList());
    }

    /**
     * 获取商品列表
     * @param storeId
     * @return 商品列表
     */

    @Override
    public List<CommodityVO> getCommodityList(Integer storeId) {
        List<Commodity> commodityList = commodityRepository.findByStoreId(storeId);
        return commodityList.stream().map(commodity -> {
            CommodityVO commodityVO = commodity.toVO();
            commodityVO.setPicLink(getCommodityPic(commodity.getId()));
            return commodityVO;
        }).collect(Collectors.toList());
    }

    /**
     *  更新库存
     * @param storeId
     * @param id
     * @param newInventory
     * @return 是否更新成功
     */

    @Override
    public Boolean updateInventory(Integer storeId, Integer id,  Integer newInventory) {

        if (newInventory < 0) {
            return false;
        }

        User user = securityUtil.getCurrentUser();

        System.out.println(user.getStoreId());

        if (!user.getStoreId().equals(storeId)) {
            throw BlueWhaleException.notStoreStaff();
        }
        // 从数据库中查询商品实体

        Optional<Commodity> commodityOptional = commodityRepository.findById(id);
        if (commodityOptional.isEmpty()) {
            throw  BlueWhaleException.commodityNotExist();
        }

        Commodity commodity = commodityOptional.get();

        // 更新库存量
        commodity.setInventory(newInventory);

        // 保存更新后的商品实体到数据库
        commodityRepository.save(commodity);

        return true;
    }

    /**
     * 获取商品
     * @param id
     * @return 商品信息
     */
    @Override
    public CommodityVO getCommodity(Integer id) {
        if (id == null) return null;
        CommodityVO commodityVO =  commodityRepository.findById(id).map(Commodity::toVO).orElse(null);
        if (commodityVO != null) {
            commodityVO.setPicLink(getCommodityPic(commodityVO.getId()));
        }
        return commodityVO;
    }

    /**
     *  更新商品信息
     * @param commodityVO
     * @return 是否更新成功
     */
    @Override
    public Boolean updateInformation(CommodityVO commodityVO) {
        User user = securityUtil.getCurrentUser();

        if (user.getRole() != RoleEnum.STAFF) {
            throw BlueWhaleException.insufficientPermissions();
        }

        Commodity commodity = commodityRepository.findById(commodityVO.getId()).orElse(null);
        if (commodity == null) {
            throw BlueWhaleException.commodityNotExist();
        }
        commodity.setName(commodityVO.getName() == null ? commodity.getName() : commodityVO.getName());
        commodity.setPrice(commodityVO.getPrice() == null ? commodity.getPrice() : commodityVO.getPrice());
        commodity.setInventory(commodityVO.getInventory() == null ? commodity.getInventory() : commodityVO.getInventory());
        commodity.setDescription(commodityVO.getDescription() == null ? commodity.getDescription() : commodityVO.getDescription());
        commodity.setType(commodityVO.getType() == null ? commodity.getType() : commodityVO.getType());
        commodityRepository.save(commodity);
        if (commodityVO.getPicLink() != null && !commodityVO.getPicLink().isEmpty()) {
            commodityPicRepository.deleteAll(commodityPicRepository.findByCommodityId(commodity.getId()));
            for (String picLink : commodityVO.getPicLink()) {
                CommodityPic commodityPic = new CommodityPic();
                commodityPic.setCommodityId(commodity.getId());
                commodityPic.setPicLink(picLink);
                commodityPicRepository.save(commodityPic);
            }
        }


        return true;
    }


    /**
     * 删除商品
     * @param commodityId
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteCommodity(Integer commodityId) {
        User user = securityUtil.getCurrentUser();

        if (user.getRole() != RoleEnum.STAFF){
            throw BlueWhaleException.insufficientPermissions();
        }

        Commodity commodity = commodityRepository.findById(commodityId).orElse(null);
        if (commodity == null) {
            throw BlueWhaleException.commodityNotExist();
        }

        List<Comment> comments = commentRepository.findByCommodityId(commodityId);

        for (Comment comment : comments) {
            comment.setCommodityId(null);
            commentRepository.save(comment);
        }

        List<Order> orders = orderRepository.findByCommodityId(commodityId);

        for (Order order : orders) {
            order.setCommodityId(null);
            orderRepository.save(order);
        }

        commodityRepository.deleteById(commodityId);
        commodityPicRepository.deleteAll(commodityPicRepository.findByCommodityId(commodityId));
        return true;
    }

    /**
     * 搜索商品
     * @param keyword
     * @param type
     * @param subType
     * @param low
     * @param high
     * @return 商品列表
     */
    @Override
    public List<CommodityVO> searchCommodity(String keyword, CommodityTypeEnum type, CommodityTypeEnum.CommoditySubTypeEnum subType, BigDecimal low, BigDecimal high) {

        List<Commodity> commodityList = commodityRepository.findByCriteria(keyword, type, subType, low, high);
        return commodityList.stream().map(commodity -> {
            CommodityVO commodityVO = commodity.toVO();
            commodityVO.setPicLink(getCommodityPic(commodity.getId()));
            return commodityVO;
        }).collect(Collectors.toList());
    }
}
