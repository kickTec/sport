package com.kenick.sport.pojo.buyer;

import java.io.Serializable;
import java.util.List;

public class BuyerCart implements Serializable {
    private List<BuyerItem> buyerItems; // 购物项集，小计可通过计算得出
    private Double productTotalPrice = 0D; // 购物商品总金额
    private Double fee = 0D; // 运费
    private Integer productAmount = 0; // 商品数量
    private Double finalTotalPrice = 0D; // 最终总价格

    public List<BuyerItem> getBuyerItems() {
        return buyerItems;
    }

    public void setBuyerItems(List<BuyerItem> buyerItems) {
        this.buyerItems = buyerItems;
    }

    public Double getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(Double productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Integer getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(Integer productAmount) {
        this.productAmount = productAmount;
    }

    public Double getFinalTotalPrice() {
        return finalTotalPrice;
    }

    public void setFinalTotalPrice(Double finalTotalPrice) {
        this.finalTotalPrice = finalTotalPrice;
    }

    public void addBuyerItem(BuyerItem newBuyerItem){
        if(buyerItems != null){
            if(buyerItems.contains(newBuyerItem)){
                BuyerItem oldBuyerItem = new BuyerItem();
                for(BuyerItem buyerItem:buyerItems){
                    if(buyerItem.equals(newBuyerItem)){
                        oldBuyerItem = buyerItem;
                    }
                }
                oldBuyerItem.setAmount(oldBuyerItem.getAmount() + newBuyerItem.getAmount());
            }else{
                buyerItems.add(newBuyerItem);
            }
        }
    }

    @Override
    public String toString() {
        return "BuyerCart{" +
                "buyerItems=" + buyerItems +
                ", productTotalPrice=" + productTotalPrice +
                ", fee=" + fee +
                ", productAmount=" + productAmount +
                ", finalTotalPrice=" + finalTotalPrice +
                '}';
    }
}