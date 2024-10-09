package com.seecoder.BlueWhale.vo;

import com.seecoder.BlueWhale.enums.RoleEnum;
import com.seecoder.BlueWhale.po.DeliveryInfo;
import com.seecoder.BlueWhale.po.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserVO {

    private Integer id;

    private String name;

    private String phone;

    private String password;

    private Integer storeId;

    private String address;

    private RoleEnum role;

    private Date createTime;

    private String storeName;

    private List<DeliveryInfo> deliveryInfos;
    public User toPO(){
        User user=new User();
        user.setId(this.id);
        user.setAddress(this.address);
        user.setName(this.name);
        user.setPhone(this.phone);
        user.setRole(this.role);
        user.setStoreId(this.storeId);
        user.setPassword(this.password);
        user.setCreateTime(this.createTime);
        return user;
    }
}
