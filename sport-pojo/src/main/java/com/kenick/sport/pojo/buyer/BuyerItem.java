package com.kenick.sport.pojo.buyer;

import java.io.Serializable;
import java.util.Objects;

public class BuyerItem implements Serializable {
    private Long skuId; // 库存id
    private String productUrl;
    private String productName;
    private String skuColor;
    private String skuSize;
    private Float skuPrice;
    private Boolean isHave = true; // 是否有货
    private Integer amount = 0; // 已选数量
    private Boolean selected = false; // 是否选中

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSkuColor() {
        return skuColor;
    }

    public void setSkuColor(String skuColor) {
        this.skuColor = skuColor;
    }

    public String getSkuSize() {
        return skuSize;
    }

    public void setSkuSize(String skuSize) {
        this.skuSize = skuSize;
    }

    public Float getSkuPrice() {
        return skuPrice;
    }

    public void setSkuPrice(Float skuPrice) {
        this.skuPrice = skuPrice;
    }

    public Boolean getHave() {
        return isHave;
    }

    public void setHave(Boolean have) {
        isHave = have;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuyerItem buyerItem = (BuyerItem) o;
        return Objects.equals(skuId, buyerItem.skuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skuId);
    }

    @Override
    public String toString() {
        return "BuyerItem{" +
                "skuId=" + skuId +
                ", productUrl='" + productUrl + '\'' +
                ", productName='" + productName + '\'' +
                ", skuColor='" + skuColor + '\'' +
                ", skuSize='" + skuSize + '\'' +
                ", skuPrice=" + skuPrice +
                ", isHave=" + isHave +
                ", amount=" + amount +
                ", selected=" + selected +
                '}';
    }
}
