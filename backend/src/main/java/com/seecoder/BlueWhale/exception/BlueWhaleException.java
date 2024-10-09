package com.seecoder.BlueWhale.exception;

/**
 * @Author: DingXiaoyu
 * @Date: 0:26 2023/11/26
 * 你可以在这里自定义Exception
 */
public class BlueWhaleException extends RuntimeException{

    public BlueWhaleException(String message){
        super(message);
    }
    public static BlueWhaleException phoneAlreadyExists(){
        return new BlueWhaleException("手机号已经存在!");
    }

    public static BlueWhaleException notLogin(){
        return new BlueWhaleException("未登录!");
    }

    public static BlueWhaleException phoneOrPasswordError(){
        return new BlueWhaleException("手机号或密码错误!");
    }

    public static BlueWhaleException fileUploadFail(){
        return new BlueWhaleException("文件上传失败!");
    }

    public static BlueWhaleException storeNameExist() {return new BlueWhaleException("商店名已经存在!");}

    public static BlueWhaleException storeNotExist() { return new BlueWhaleException("商店不存在!"); }
    public static BlueWhaleException notStoreStaff() { return new BlueWhaleException("非该商店工作人员!"); }

    public static BlueWhaleException commodityNotExist() { return new BlueWhaleException("商品不存在!");}

    public static BlueWhaleException insufficientPermissions() {return new BlueWhaleException("权限不够!");}

    public static BlueWhaleException commodityNotInStore() {return new BlueWhaleException("商店中不存在该商品");}

    public static BlueWhaleException notCustomer() {return new BlueWhaleException("非顾客无法购买");}
    public static BlueWhaleException inventoryNotEnough() {return new BlueWhaleException("库存不足!");}


    public static BlueWhaleException orderNotExist() { return new BlueWhaleException("订单不存在!"); }

    public static BlueWhaleException notOrderOwner() { return new BlueWhaleException("非订单所有者!"); }


    public static BlueWhaleException orderNotGET() { return new BlueWhaleException("订单未确认收货"); }

    public static BlueWhaleException commentNotExist() { return new BlueWhaleException("评论不存在!"); }

    public static BlueWhaleException couponNotExist() {
        return new BlueWhaleException("优惠券不存在!");
    }

    public static BlueWhaleException couponUsed() {
        return new BlueWhaleException("优惠券已使用!");
    }


    public static BlueWhaleException orderStatusError() {
        return new BlueWhaleException("订单状态错误!");
    }

    public static BlueWhaleException couponAlreadyRedeem() {
        return new BlueWhaleException("该优惠券组下优惠券已领取!");
    }

    public static BlueWhaleException couponGroupEmpty() { return new BlueWhaleException("优惠券已领完!");}

    public static BlueWhaleException couponAmountIllegal() {
        return new BlueWhaleException("优惠券数量非法!");
    }

    public static BlueWhaleException deliveryInfoAlreadyExists() {
        return new BlueWhaleException("配送信息已存在!");
    }

    public static BlueWhaleException couponRedeemTimeout() {
        return new BlueWhaleException("优惠券领取超时!");
    }

    public static BlueWhaleException couponRedeemInterrupted() {
        return new BlueWhaleException("优惠券领取被中断!");
    }
}
