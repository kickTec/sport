package com.kenick.sport.product.serviceImpl;

import com.kenick.sport.mapper.product.ProductMapper;
import com.kenick.sport.mapper.product.SkuMapper;
import com.kenick.sport.pojo.product.Product;
import com.kenick.sport.pojo.product.ProductQuery;
import com.kenick.sport.pojo.product.Sku;
import com.kenick.sport.pojo.product.SkuQuery;
import com.kenick.sport.product.utils.FastDFSUtil;
import com.kenick.sport.service.product.ProductService;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Date;
import java.util.List;

@Service("productService")
public class ProductServiceImpl implements ProductService {
    @Resource
    private JmsTemplate jmsTemplate;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private SkuMapper skuMapper;

    @Resource
    private Jedis jedis;

    @Override
    public List<Product> selectProductByQuery(String productName, Long brandId, Boolean isShow, Integer pageNo, Integer pageSize) {
        // 查询条件
        ProductQuery productQuery = new ProductQuery();
        ProductQuery.Criteria criteria = productQuery.createCriteria();
        if(productName != null && !"".equals(productName)){
            criteria.andNameLike("%"+productName+"%");
        }
        if(brandId != null){
            criteria.andBrandIdEqualTo(brandId);
        }
        if(isShow != null){
            criteria.andIsShowEqualTo(isShow);
        }

        // 分页
        if(pageNo == null || pageNo <1){
            pageNo = 1;
        }
        if(pageSize == null || pageSize<0){
            pageSize = 5;
        }
        productQuery.setPageNo(pageNo);
        productQuery.setPageSize(pageSize);
        productQuery.setOrderByClause("id desc"); // 根据id降序

        return productMapper.selectByExample(productQuery);
    }

    @Override
    public Integer getProductTotalSize(String productName, Long brandId, Boolean isShow) {
        ProductQuery productQuery = new ProductQuery();
        ProductQuery.Criteria criteria = productQuery.createCriteria();
        if(productName != null){
            criteria.andNameLike("%"+productName+"%");
        }
        if(brandId != null){
            criteria.andBrandIdEqualTo(brandId);
        }
        if(isShow != null){
            criteria.andIsShowEqualTo(isShow);
        }
        return productMapper.countByExample(productQuery);
    }

    @Override
    public Integer saveProduct(Product product) {
        // 设置商品信息
        Long pno = jedis.incr("pno");
        product.setId(pno);
        product.setCreateTime(new Date()); // 商品录入时间
        product.setIsShow(false); // 默认下架
        int lines = productMapper.insert(product);

        if(lines > 0){ // 商品保存成功，初始化库存信息
            String[] colors = product.getColors().split(",");
            String[] sizes = product.getSizes().split(",");
            for(String size:sizes){
                for(String color:colors){
                    Sku sku = new Sku();
                    sku.setColorId(Long.parseLong(color));
                    sku.setCreateTime(new Date());
                    sku.setDeliveFee(0f);
                    sku.setMarketPrice(0f);
                    sku.setPrice(0f);
                    sku.setProductId(product.getId());
                    sku.setSize(size);
                    sku.setStock(0);
                    sku.setUpperLimit(0);
                    skuMapper.insert(sku);
                }
            }
        }
        return lines;
    }

    @Override
    public Integer deleteProductById(Long productId) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product != null){
            // 删除图片资源
            String imgUrl = product.getImgUrl();
            if(imgUrl != null){
                String[] imgUrlArray = imgUrl.split(",");
                for(String url:imgUrlArray){
                    if(url.contains("http")){
                        String fileId = FastDFSUtil.getFileIdFromUrl(url);
                        FastDFSUtil.deleteFastDFSFile(fileId);
                    }
                }
            }

            // 删除该商品下的库存信息
            SkuQuery skuQuery = new SkuQuery();
            skuQuery.createCriteria().andProductIdEqualTo(productId);
            skuMapper.deleteByExample(skuQuery);
        }
        return productMapper.deleteByPrimaryKey(productId);
    }

    @Override
    public void isShow(String[] ids) throws Exception{
        Product productShow = new Product();
        productShow.setIsShow(true);

        for(final String id:ids){
            // 修改商品isShow的状态
            productShow.setId(Long.parseLong(id));
            productMapper.updateByPrimaryKeySelective(productShow);

            jmsTemplate.send(new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    TextMessage textMessage = session.createTextMessage(String.valueOf(id));
                    return textMessage;
                }
            });
        }
    }

    @Override
    public Product selectProductById(Long productId) {
        return productMapper.selectByPrimaryKey(productId);
    }
}
