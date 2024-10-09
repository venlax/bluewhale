package com.seecoder.BlueWhale.vo;
import com.seecoder.BlueWhale.enums.CommodityTypeEnum;
import com.seecoder.BlueWhale.po.Commodity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommodityVO {
    private Integer id;

    private Integer storeId;

    private CommodityTypeEnum type;

    private CommodityTypeEnum.CommoditySubTypeEnum subType;

    private String name;

    private Integer inventory;

    private List<String> picLink;

    private String description;

    private BigDecimal price;

    private BigDecimal score;
    public Commodity toPO(){
        Commodity commodity =new Commodity();
        commodity.setId(this.id);
        commodity.setType(this.type);
        commodity.setName(this.name);
        commodity.setStoreId(this.storeId);
        commodity.setInventory((this.inventory == null || this.inventory < 0) ? 0 : this.inventory);
        commodity.setDescription(this.description);
        commodity.setPrice(this.price);
        commodity.setScore(this.score);
        commodity.setSubType(this.subType);
        return commodity;
    }
}
