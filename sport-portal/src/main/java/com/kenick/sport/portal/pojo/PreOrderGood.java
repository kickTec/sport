package com.kenick.sport.portal.pojo;

import java.io.Serializable;

/**
 *  预下单商品
 */
public class PreOrderGood implements Serializable {
    private Long productId; // 商品id
    private Long skuId; // 库存id
    private String imgUrl; // 商品图片
    private String goodDesc; // 商品描述
    private Float goodPrice; // 商品价格
    private Integer goodNum; // 商品数量
    private Boolean isHave; // 是否有货

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGoodDesc() {
        return goodDesc;
    }

    public void setGoodDesc(String goodDesc) {
        this.goodDesc = goodDesc;
    }

    public Float getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(Float goodPrice) {
        this.goodPrice = goodPrice;
    }

    public Integer getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(Integer goodNum) {
        this.goodNum = goodNum;
    }

    public Boolean getHave() {
        return isHave;
    }

    public void setHave(Boolean have) {
        isHave = have;
    }

    @Override
    public String toString() {
        return "PreOrderGood{" +
                "productId=" + productId +
                ", skuId=" + skuId +
                ", imgUrl='" + imgUrl + '\'' +
                ", goodDesc='" + goodDesc + '\'' +
                ", goodPrice=" + goodPrice +
                ", goodNum=" + goodNum +
                ", isHave=" + isHave +
                '}';
    }
}
