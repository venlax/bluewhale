package com.seecoder.BlueWhale.vo;
import com.seecoder.BlueWhale.po.Store;
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
public class StoreVO {
    private Integer id;
    private String logo;
    private Date createTime;
    private String name;
    private String address;
    private String introduction;
    private BigDecimal score;

    public Store toPO(){
        Store store =new Store();
        store.setId(this.id);
        store.setLogo(this.logo);
        store.setCreateTime(this.createTime);
        store.setName(this.name);
        store.setAddress(this.address);
        store.setIntroduction(this.introduction);
        store.setScore(this.score);
        return store;
    }
}
