package com.kenick.sport.solr.mq;

import com.kenick.sport.pojo.product.Product;
import com.kenick.sport.service.product.ProductService;
import com.kenick.sport.service.product.SkuService;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;

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
	private SkuService skuService;

	@Resource
	private SolrServer solrServer;

	@Resource
    private ProductService productService;

	@Override
	public void onMessage(Message message) {
		// 该消息就在message中 -- 上架消息处理
		ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
		try {
			// 获取消息 商品id
			String productIdStr = activeMQTextMessage.getText();

			// 进行业务逻辑处理
            Long productId = Long.parseLong(productIdStr);
            Product product = productService.selectProductById(productId);
            Float price = skuService.selectLowestPriceByProductId(productId);

            // 保存商品信息到solr中
            SolrInputDocument document = new SolrInputDocument();
            document.addField("product_id", product.getId());
            document.addField("product_name", product.getName());
            document.addField("product_imgUrl", product.getImgUrl());
            document.addField("product_brandId", product.getBrandId());
            document.addField("product_price", price);

            solrServer.add(document);
            solrServer.commit();
        } catch (Exception e) {
			e.printStackTrace();
		}
	}
}