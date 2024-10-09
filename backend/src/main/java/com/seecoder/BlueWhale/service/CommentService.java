package com.seecoder.BlueWhale.service;

import com.seecoder.BlueWhale.vo.CommentVO;

import java.math.BigDecimal;
import java.util.List;

public interface CommentService {



    Boolean commentOrder(CommentVO commentVO);

    List<CommentVO> getCommentsByCommodityId(Integer commodityId);


    List<CommentVO> getCommentsByUserId(Integer userId);

    List<CommentVO> getCommentsByStoreId(Integer storeId);


    CommentVO getCommentByOrderId(Integer orderId);

    BigDecimal getScoreByCommodityId(Integer commodityId);

    BigDecimal getScoreByStoreId(Integer storeId);

    Integer getCommentCountByCommodityId(Integer commodityId);

    Integer getCommentCountByStoreId(Integer storeId);
}
