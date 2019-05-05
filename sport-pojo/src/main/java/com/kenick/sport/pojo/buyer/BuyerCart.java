package com.kenick.sport.pojo.buyer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BuyerCart implements Serializable {
    private List<BuyerItem> buyerItems = new ArrayList<>(); // 购物项集，小计可通过计算得出
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
        Double totalPrice = 0D;
        if(this.buyerItems != null){
            for(BuyerItem buyerItem:buyerItems){
                if(buyerItem.getSelected()){
                    totalPrice  = totalPrice + buyerItem.getSkuPrice()*buyerItem.getAmount();
                }
            }
        }
        return totalPrice;
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
        int totalNum = 0;
        if(this.buyerItems != null){
            for(BuyerItem buyerItem:buyerItems){
                if(buyerItem.getSelected()){
                    totalNum = totalNum + buyerItem.getAmount();
                }
            }
        }
        return totalNum;
    }

    public void setProductAmount(Integer productAmount) {
        this.productAmount = productAmount;
    }

    public Double getFinalTotalPrice() {
        Double price = this.getProductTotalPrice() - this.fee;
        return price<0?0:price;
    }

    public void setFinalTotalPrice(Double finalTotalPrice) {
        this.finalTotalPrice = finalTotalPrice;
    }

    /**
     *  购物车中添加购物项
     * @param newBuyerItem 新购物项
     * @param merge true:skuId相同，amount累加 false:skuId相同，移除旧项，增加新项
     */
    public void addBuyerItem(BuyerItem newBuyerItem, Boolean merge){
        if(this.buyerItems != null){
            if(merge){ // 新项合并
                BuyerItem oldBuyerItem = null;
                for(BuyerItem buyerItem:buyerItems){
                    if(buyerItem.equals(newBuyerItem)){
                        oldBuyerItem = buyerItem;
                        break;
                    }
                }
                if(oldBuyerItem == null){
                    this.buyerItems.add(newBuyerItem);
                }else{
                    this.buyerItems.remove(oldBuyerItem);
                    newBuyerItem.setAmount(oldBuyerItem.getAmount() + newBuyerItem.getAmount());
                    this.buyerItems.add(newBuyerItem);
                }
            }else{ // 新项替换
                BuyerItem oldBuyerItem = null;
                for(BuyerItem buyerItem:buyerItems){
                    if(buyerItem.equals(newBuyerItem)){
                        oldBuyerItem = buyerItem;
                        break;
                    }
                }
                if(oldBuyerItem != null){
                    this.buyerItems.remove(oldBuyerItem);
                }
                this.buyerItems.add(newBuyerItem);
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