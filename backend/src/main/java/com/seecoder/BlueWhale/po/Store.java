package com.seecoder.BlueWhale.po;
import com.seecoder.BlueWhale.enums.RoleEnum;
import com.seecoder.BlueWhale.vo.StoreVO;
import com.seecoder.BlueWhale.vo.UserVO;
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
public class Store {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "logo")
    private String logo;

    @Basic
    @Column(name = "create_time")
    private Date createTime;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "address")
    private String address;

    @Basic
    @Column(name = "introduction")
    private String introduction;

    @Basic
    @Column(name = "score", scale = 1)
    private BigDecimal score;

    public StoreVO toVO(){
        StoreVO storeVO = new StoreVO();
        storeVO.setId(this.id);
        storeVO.setLogo(this.logo);
        storeVO.setCreateTime(this.createTime);
        storeVO.setName(this.name);
        storeVO.setAddress(this.address);
        storeVO.setIntroduction(this.introduction);
        storeVO.setScore(this.score);
        return storeVO;
    }

}
