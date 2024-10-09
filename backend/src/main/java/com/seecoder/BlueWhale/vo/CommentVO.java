package com.seecoder.BlueWhale.vo;

import com.seecoder.BlueWhale.po.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentVO {

    private Integer id;
    private Integer userId;
    private Integer commodityId;
    private String content;
    private BigDecimal score;
    private Date time;
    private Integer storeId;
    private Integer orderId;
    private String userName;

    public Comment toPO(){
        Comment comment = new Comment();
        comment.setId(this.id);
        comment.setUserId(this.userId);
        comment.setStoreId(this.storeId);
        comment.setCommodityId(this.commodityId);
        comment.setOrderId(this.orderId);
        comment.setContent(this.content);
        comment.setScore(this.score);
        comment.setTime(this.time);
        return comment;
    }
}
