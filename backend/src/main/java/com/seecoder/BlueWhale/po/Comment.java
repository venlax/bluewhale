package com.seecoder.BlueWhale.po;

import com.seecoder.BlueWhale.vo.CommentVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comment", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_store_id", columnList = "store_id"),
        @Index(name = "idx_commodity_id", columnList = "commodity_id")
})
public class Comment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "user_id")
    private Integer userId;

    @Basic
    @Column(name = "store_id")
    private Integer storeId;

    @Basic
    @Column(name = "commodity_id")
    private Integer commodityId;

    @Basic
    @Column(name = "order_id")
    private Integer orderId;

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "score",scale = 1)
    private BigDecimal score;

    @Basic
    @Column(name = "time")
    private Date time;

    public CommentVO toVO(){
        CommentVO commentVO = new CommentVO();
        commentVO.setId(this.id);
        commentVO.setUserId(this.userId);
        commentVO.setStoreId(this.storeId);
        commentVO.setCommodityId(this.commodityId);
        commentVO.setOrderId(this.orderId);
        commentVO.setContent(this.content);
        commentVO.setScore(this.score);
        commentVO.setTime(this.time);
        return commentVO;
    }
}
