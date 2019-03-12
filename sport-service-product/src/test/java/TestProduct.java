import com.kenick.sport.mapper.product.ProductMapper;
import com.kenick.sport.pojo.product.Product;
import com.kenick.sport.pojo.product.ProductQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

@ContextConfiguration(locations={"classpath:spring-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestProduct {
    private final static Logger logger = LoggerFactory.getLogger(TestProduct.class);

    @Resource
    private ProductMapper productMapper;

    @Test
    public void testSelectAll(){ // 测试查询所有
        List<Product> products = productMapper.selectByExample(new ProductQuery());
        for(Product product:products){
            logger.info(product.toString());
        }
    }

    @Test
    public void testSelectByName(){ // 测试查询符合条件的所有结果集
        ProductQuery productQuery = new ProductQuery();
        productQuery.createCriteria().andNameLike("%麦爵斯蒂%");
        List<Product> products = productMapper.selectByExample(productQuery);
        for(Product product:products){
            logger.info(product.toString());
        }
    }
}