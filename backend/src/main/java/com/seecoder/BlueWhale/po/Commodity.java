package com.seecoder.BlueWhale.po;

import com.seecoder.BlueWhale.enums.CommodityTypeEnum;
import com.seecoder.BlueWhale.vo.CommodityVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "commodity", indexes = {
        @Index(name = "idx_store_id", columnList = "store_id")
})
public class Commodity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "store_id")
    private Integer storeId;

    @Basic
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CommodityTypeEnum type;

    // 用EnumType来泛型处理嵌套类型
    @Basic
    @Column(name = "sub_type")
    @Enumerated(EnumType.STRING)
    private CommodityTypeEnum.CommoditySubTypeEnum subType;


    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "inventory")
    private Integer inventory;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "price", scale = 2)
    private BigDecimal price;

    @Basic
    @Column(name = "score", scale = 1)
    private BigDecimal score;
    public CommodityVO toVO(){
        CommodityVO commodityVO = new CommodityVO();
        commodityVO.setId(this.id);
        commodityVO.setType(this.type);
        commodityVO.setName(this.name);
        commodityVO.setStoreId(this.storeId);
        commodityVO.setInventory(this.inventory);
        commodityVO.setPrice(this.price);
        commodityVO.setDescription(this.description);
        commodityVO.setScore(this.score);
        commodityVO.setSubType(this.subType);
        return commodityVO;
    }
}
