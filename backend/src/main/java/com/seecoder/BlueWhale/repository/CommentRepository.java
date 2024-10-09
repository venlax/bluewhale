package com.seecoder.BlueWhale.repository;

import com.seecoder.BlueWhale.po.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
        List<Comment> findByCommodityId(Integer commodityId);

        List<Comment> findByUserId(Integer userId);

        Comment findByOrderId(Integer orderId);

        List<Comment> findByStoreId(Integer storeId);

        @Query("SELECT COUNT(DISTINCT c.userId) FROM Comment c WHERE c.commodityId = :commodityId")
        Integer countDistinctUserIdByCommodityId(Integer commodityId);
        @Query("SELECT COUNT(DISTINCT c.userId) FROM Comment c WHERE c.storeId = :storeId")
        Integer countDistinctUserIdByStoreId(Integer storeId);


        //void deleteById(Integer id);

}
