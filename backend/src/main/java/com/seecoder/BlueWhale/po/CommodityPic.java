package com.seecoder.BlueWhale.po;

import com.seecoder.BlueWhale.vo.StoreVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "commodity_pic", indexes = {
        @Index(name = "idx_commodity_id", columnList = "commodity_id")
})
public class CommodityPic {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;


    @Column(name = "commodity_id")
    private Integer commodityId;

    @Column(name = "pic_link")
    private String picLink;

}
