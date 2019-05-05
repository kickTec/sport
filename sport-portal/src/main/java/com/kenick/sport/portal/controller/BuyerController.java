package com.kenick.sport.portal.controller;

import com.kenick.sport.common.utils.LoginUtil;
import com.kenick.sport.pojo.order.Detail;
import com.kenick.sport.pojo.order.Order;
import com.kenick.sport.pojo.product.Color;
import com.kenick.sport.pojo.product.Product;
import com.kenick.sport.pojo.product.Sku;
import com.kenick.sport.portal.pojo.PreOrderGood;
import com.kenick.sport.service.buyer.BuyerService;
import com.kenick.sport.service.buyer.DetailService;
import com.kenick.sport.service.buyer.OrderService;
import com.kenick.sport.service.login.SessionProviderService;
import com.kenick.sport.service.product.SkuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/buyer")
public class BuyerController {

    @Resource
    private SkuService skuService;

    @Resource
    private BuyerService buyerService;

    @Resource
    private OrderService orderService;

    @Resource
    private DetailService detailService;

    @Resource
    private SessionProviderService sessionProviderService;

    @RequestMapping(value = "/trueBuy",method = {RequestMethod.POST})
    public String trueBuy(String orderInfo, Model model){
        List<PreOrderGood> preOrderGoodList = new ArrayList<>();
        int productAmount = 0; // 商品数量
        Float productPrice = 0.0F; // 总商品金额
        Float cashBack = 0.0F; // 返现金额
        Float fee = 10.00F; // 运费
        Float payPrice = 0.0F; // 应付总额

        if(orderInfo != null && !"".equals(orderInfo)){
            Boolean exceed = false; // 下单时库存是否超过
            String[] orderInfoArray = orderInfo.split("\\|");
            for(String preOrder:orderInfoArray){
                if(!"".equals(preOrder)){
                    String[] productArray = preOrder.split("_");
                    Long skuId = Long.parseLong(productArray[0]); // 下单库存id
                    Float skuPrice = Float.parseFloat(productArray[1]); // 下单库存价格
                    Integer skuAmount = Integer.parseInt(productArray[2]); // 下单库存数量

                    // 下单数量大于库存数量
                    Sku sku = skuService.selectSkuBySkuId(skuId);
                    if(skuAmount > sku.getStock()){
                        exceed = true;
                        break;
                    }

                    productAmount = productAmount + skuAmount;
                    productPrice = productPrice + skuPrice * skuAmount;

                    Product product = sku.getProduct();
                    Color color = sku.getColor();
                    PreOrderGood preOrderGood = new PreOrderGood();
                    preOrderGood.setGoodNum(skuAmount);
                    preOrderGood.setGoodPrice(skuPrice);
                    preOrderGood.setSkuId(sku.getId());
                    preOrderGood.setImgUrl(product.getImages()[0]);
                    preOrderGood.setProductId(product.getId());

                    String goodDesc = product.getName()+" "+sku.getSize()+" "+color.getName();
                    preOrderGood.setGoodDesc(goodDesc);
                    if(skuAmount > sku.getStock()){
                        preOrderGood.setHave(false);
                    }else{
                        preOrderGood.setHave(true);
                    }
                    preOrderGoodList.add(preOrderGood);
                }
            }

            if(exceed){
                return "redirect:/shopping/toCart";
            }
        }
        if(productPrice > 49){
            fee = 0.0F;
        }
        if(productPrice >0 && productPrice < 49){
            fee = 10.0F;
        }
        if(productPrice > 0){
            payPrice = productPrice - cashBack + fee;
        }

        model.addAttribute("preOrderGoodList", preOrderGoodList);
        model.addAttribute("productAmount", productAmount);
        model.addAttribute("productPrice", productPrice);
        model.addAttribute("cashBack", cashBack);
        model.addAttribute("fee", fee);
        model.addAttribute("payPrice", payPrice);
        return "order";
    }

    @RequestMapping(value = "/submitOrder",method = {RequestMethod.POST})
    public String submitOrder(Float orderPrice, Float fee, Float totalPrice,
                              Integer paymentWay, Integer paymentCash, String note,
                              String skuIdAmountAll, HttpServletRequest request, HttpServletResponse response,Model model){
        // 从redis中获取订单id
        Long orderIdFromRedis = buyerService.getOrderIdFromRedis();

        // 获取购买者id
        String username = sessionProviderService.getAttributeForUsername(LoginUtil.getCSESSIONID(request, response));
        Long buyerId = buyerService.getBuyerIdByUsername(username);

        // 填充订单表
        Order order = new Order();
        order.setId(orderIdFromRedis);
        order.setDeliverFee(fee);
        order.setTotalPrice(totalPrice);
        order.setOrderPrice(orderPrice);
        order.setPaymentWay(paymentWay);
        order.setPaymentCash(paymentCash);
        order.setNote(note);
        order.setIsPaiy(0);
        order.setOrderState(0);
        order.setCreateDate(new Date());
        order.setBuyerId(buyerId);

        // 填充详细表
        List<Detail> detailList = new ArrayList<>();
        if(skuIdAmountAll != null && !"".equals(skuIdAmountAll)){
            String[] skuInfoArray = skuIdAmountAll.split("\\|");
            for(String skuInfo:skuInfoArray){
                if(skuInfo != null && !"".equals(skuInfo)){
                    String[] skuIdAmount = skuInfo.split("_");
                    if(skuIdAmount.length == 2){
                        Long skuId = Long.parseLong(skuIdAmount[0]);
                        int amount = Integer.parseInt(skuIdAmount[1]);
                        Sku sku = skuService.selectSkuBySkuId(skuId);

                        Detail detail = new Detail();
                        detail.setOrderId(orderIdFromRedis);
                        detail.setProductId(sku.getProduct().getId());
                        detail.setProductName(sku.getProduct().getName());
                        detail.setColor(sku.getColor().getName());
                        detail.setSize(sku.getSize());
                        detail.setPrice(sku.getPrice());
                        detail.setAmount(amount);
                        detailList.add(detail);
                    }
                }
            }
        }
        orderService.saveOrder(order);
        detailService.saveDetailList(detailList);
        model.addAttribute("orderId", orderIdFromRedis);
        model.addAttribute("totalPrice", totalPrice);
        return "success";
    }
}