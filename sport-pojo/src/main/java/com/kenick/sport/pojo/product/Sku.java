package com.kenick.sport.pojo.product;

import java.io.Serializable;
import java.util.Date;

public class Sku implements Serializable {
    /**
     * ID
     */
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    private Product product; // 用于页面回显

    /**
     * 颜色ID
     */
    private Long colorId;

    private Color color; // 用于页面回显

    /**
     * 尺码
     */
    private String size;

    /**
     * 市场价
     */
    private Float marketPrice;

    /**
     * 售价
     */
    private Float price;

    /**
     * 运费 默认10元
     */
    private Float deliveFee;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 购买限制
     */
    private Integer upperLimit;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getColorId() {
        return colorId;
    }

    public void setColorId(Long colorId) {
        this.colorId = colorId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size == null ? null : size.trim();
    }

    public Float getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Float marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getDeliveFee() {
        return deliveFee;
    }

    public void setDeliveFee(Float deliveFee) {
        this.deliveFee = deliveFee;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(Integer upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Sku{" +
                "id=" + id +
                ", productId=" + productId +
                ", colorId=" + colorId +
                ", color=" + color +
                ", size='" + size + '\'' +
                ", marketPrice=" + marketPrice +
                ", price=" + price +
                ", deliveFee=" + deliveFee +
                ", stock=" + stock +
                ", upperLimit=" + upperLimit +
                ", createTime=" + createTime +
                '}';
    }
}