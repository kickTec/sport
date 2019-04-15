package com.kenick.sport.cms.mq;

import com.kenick.sport.pojo.product.Color;
import com.kenick.sport.pojo.product.Product;
import com.kenick.sport.pojo.product.Sku;
import com.kenick.sport.service.CmsService;
import com.kenick.sport.service.product.StaticPageService;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @ClassName: CustomMessageListener
 * @Company: http://www.itcast.cn/
 * @Description: activemq消费者---监听容器中的消息并且进行消费
 * @author kenick
 * @date 2016年12月14日 下午3:51:11
 */
public class CustomMessageListener implements MessageListener {
	@Resource
    private CmsService cmsService;

	@Resource
    private StaticPageService staticPageService;

	@Override
	public void onMessage(Message message) {
		// 该消息就在message中 -- 上架消息处理
		ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
		try {
			// 获取消息 商品id
			String productIdStr = activeMQTextMessage.getText();

			// 进行业务逻辑处理
            Long productId = Long.parseLong(productIdStr);
            Product product = cmsService.selectProductById(productId); // 商品
            List<Sku> skus = cmsService.selectSkuListByProductIdAnStockMoreThanZero(productId); // 库存
            // 颜色
            Set<Color> colors = new HashSet<>();
            for(Sku sku:skus){
                colors.add(sku.getColor());
            }
            HashMap<String, Object> rootMap = new HashMap<>();
            rootMap.put("product",product);
            rootMap.put("skus",skus);
            rootMap.put("colors",colors);

            staticPageService.freeMarkerProductDetail(rootMap,productId+"");
        } catch (Exception e) {
			e.printStackTrace();
		}
	}
}