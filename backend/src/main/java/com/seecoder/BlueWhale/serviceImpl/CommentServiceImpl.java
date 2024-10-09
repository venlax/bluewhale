package com.seecoder.BlueWhale.serviceImpl;

import com.seecoder.BlueWhale.enums.PaymentStateEnum;
import com.seecoder.BlueWhale.enums.RoleEnum;
import com.seecoder.BlueWhale.exception.BlueWhaleException;
import com.seecoder.BlueWhale.po.*;
import com.seecoder.BlueWhale.repository.*;
import com.seecoder.BlueWhale.service.CommentService;
import com.seecoder.BlueWhale.util.SecurityUtil;
import com.seecoder.BlueWhale.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * @Author: venlax
 *  评论服务实现
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CommodityRepository commodityRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SecurityUtil securityUtil;
    private CommentVO commentToVO(Comment comment) {
        User user = userRepository.findById(comment.getUserId()).orElse(null);
        CommentVO res = comment.toVO();
        res.setUserName(user.getName());
        return res;
    }


    /**
     * 评论订单
     * @param commentVO
     * @return 是否评论成功
     */
    @Override
    public Boolean commentOrder(CommentVO commentVO) {
        User user = securityUtil.getCurrentUser();
        if (user == null || !user.getRole().equals(RoleEnum.CUSTOMER)) {
            throw BlueWhaleException.notCustomer();
        }
        Order order = orderRepository.findById(commentVO.getOrderId()).orElse(null);
        if (order == null) {
            throw BlueWhaleException.orderNotExist();
        }
        if (!order.getUserId().equals(user.getId())) {
            throw BlueWhaleException.notOrderOwner();
        }
        if (order.getPaymentState() != PaymentStateEnum.UNCOMMENT) {
            throw BlueWhaleException.orderNotGET();
        }

        Comment comment = commentVO.toPO();
        comment.setTime(new Date());
        commentRepository.save(comment);

        Store store = storeRepository.findById(order.getStoreId()).orElse(null);
        if (store == null) {
            throw BlueWhaleException.storeNotExist();
        }
        store.setScore(getScoreByStoreId(store.getId()));
        storeRepository.save(store);

        Commodity commodity = commodityRepository.findById(order.getCommodityId()).orElse(null);
        if (commodity == null) {
            throw BlueWhaleException.commodityNotExist();
        }
        commodity.setScore(getScoreByCommodityId(commodity.getId()));
        commodityRepository.save(commodity);
        return true;
    }

    /**
     * 获取商品的评论
      * @param commodityId
     * @return 评论列表
     */

    @Override
    public List<CommentVO> getCommentsByCommodityId(Integer commodityId) {
        List<Comment> comments = commentRepository.findByCommodityId(commodityId);
        return comments.stream().map(this::commentToVO).collect(Collectors.toList());
    }


    /**
     * 获取用户的评论
     * @param userId
     * @return 评论列表
     */

    @Override
    public List<CommentVO> getCommentsByUserId(Integer userId) {
        List<Comment> comments = commentRepository.findByUserId(userId);
        return comments.stream().map(this::commentToVO).collect(Collectors.toList());
    }

    /**
     * 获取订单的评论
     * @param orderId
     * @return 评论信息
     */

    @Override
    public CommentVO getCommentByOrderId(Integer orderId) {
        Comment comment = commentRepository.findByOrderId(orderId);
        if (comment == null) {
            throw BlueWhaleException.commentNotExist();
        }

        return commentToVO(comment);
    }

    /**
     * 获取店铺的评论
     * @param storeId
     * @return 评论列表
     */

    @Override
    public List<CommentVO> getCommentsByStoreId(Integer storeId) {
        List<Comment> comments = commentRepository.findByStoreId(storeId);
        return comments.stream().map(this::commentToVO).collect(Collectors.toList());
    }

    /**
     * 获取商品的评分
     * @param commodityId
     * @return 评分
     */
    @Cacheable(value = "scoreByCommodityId", key = "#commodityId")
    @Override
    public BigDecimal getScoreByCommodityId(Integer commodityId) {
        List<Comment> comments = commentRepository.findByCommodityId(commodityId);
        if (comments.size() == 0) {
            return new BigDecimal(0);
        }
        return comments.stream().map(Comment::getScore).reduce(BigDecimal::add).orElse(new BigDecimal(0)).divide(new BigDecimal(comments.size()), ROUND_HALF_UP);
    }

    /**
     * 获取店铺的评分
     * @param storeId
     * @return 评分
     */
    @Cacheable(value = "scoreByStoreId", key = "#storeId")
    @Override
    public BigDecimal getScoreByStoreId(Integer storeId) {
        List<Comment> comments = commentRepository.findByStoreId(storeId);
        if (comments.size() == 0) {
            return new BigDecimal(0);
        }
        return comments.stream().map(Comment::getScore).reduce(BigDecimal::add).orElse(new BigDecimal(0)).divide(new BigDecimal(comments.size()), ROUND_HALF_UP);
    }

    /**
     * 获取商品的评论数
     * @param commodityId
     * @return 评论数
     */
    @Cacheable(value = "commentCountByCommodityId", key = "#commodityId")
    @Override
    public Integer getCommentCountByCommodityId(Integer commodityId) {
        return commentRepository.countDistinctUserIdByCommodityId(commodityId);
    }

    /**
     * 获取店铺的评论数
     * @param storeId
     * @return 评论数
     */
    @Cacheable(value = "commentCountByStoreId", key = "#storeId")
    @Override
    public Integer getCommentCountByStoreId(Integer storeId) {
        return commentRepository.countDistinctUserIdByStoreId(storeId);
    }
}
