package com.seecoder.BlueWhale.po;

import com.seecoder.BlueWhale.enums.RoleEnum;
import com.seecoder.BlueWhale.repository.UserRepository;
import com.seecoder.BlueWhale.service.UserService;
import com.seecoder.BlueWhale.serviceImpl.UserServiceImpl;
import com.seecoder.BlueWhale.vo.UserVO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "phone")
    private String phone;

    @Basic
    @Column(name = "password")
    private String password;

    //必须注意，在Java中用驼峰，在MySQL字段中用连字符_
    @Basic
    @Column(name = "create_time")
    private Date createTime;

    @Basic
    @Column(name = "store_id")
    private Integer storeId;

    @Basic
    @Column(name = "address")
    private String address;

    @Basic
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleEnum role;


    public UserVO toVO(){
        UserVO userVO=new UserVO();
        userVO.setId(this.id);
        userVO.setAddress(this.address);
        userVO.setName(this.name);
        userVO.setRole(this.role);
        userVO.setStoreId(this.storeId);
        userVO.setPhone(this.phone);
        userVO.setPassword(this.password);
        userVO.setCreateTime(this.createTime);
        return userVO;
    }
}
