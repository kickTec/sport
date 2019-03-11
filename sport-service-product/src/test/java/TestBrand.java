import com.kenick.sport.mapper.product.BrandMapper;
import com.kenick.sport.pojo.product.Brand;
import com.kenick.sport.pojo.product.BrandQuery;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(locations={"classpath:spring-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestBrand {
    private final static Logger logger = LoggerFactory.getLogger(TestBrand.class);

    @Resource
    private BrandMapper brandMapper;

    @Test
    public void testSelectAllNoPage(){ // 测试查询符合条件的所有结果集
        BrandQuery brandQuery = new BrandQuery();
        brandQuery.setName("丹璐斯");
        brandQuery.setIsDisplay(Short.parseShort("1"));
        List<Brand> brands = brandMapper.queryBrandListByQueryAndNoPage(brandQuery);
        logger.info("brands:{}", ArrayUtils.toString(brands));
    }

    @Test
    public void testBrandQuery(){ // 测试查询符合条件的品牌数目
        BrandQuery brandQuery = new BrandQuery();
        brandQuery.setIsDisplay(Short.parseShort("0"));
        Integer brandSize = brandMapper.queryBrandSumByQueryAndPage(brandQuery);
        logger.info("符合条件的品牌数目:{}",brandSize);
    }

    @Test
    public void testBrandListQuery(){ // 测试查询符合条件的品牌集合，分页
        BrandQuery brandQuery = new BrandQuery();
        brandQuery.setIsDisplay(Short.parseShort("1"));
        brandQuery.setStartLine(1);
        brandQuery.setMaxNum(5);
        List<Brand> brands = brandMapper.queryBrandListByQueryAndPage(brandQuery);
        logger.info("符合条件的品牌数目:{}",brands);
    }

    @Test
    public void testQueryBrandById(){ // 根据brandId查询
        Brand brand = brandMapper.queryBrandById("2");
        logger.info("brand:{}",brand);
    }

    @Test
    public void testUpdateBrand(){
        Brand brand = new Brand();
        brand.setId(2L);
        brand.setWebSite("2website");
        brand.setSort(2);
        brand.setIsDisplay(Short.parseShort("0"));
        Integer num = brandMapper.updateBrand(brand);
        System.out.println("受影响数量:" + num);
    }

    @Test
    public void testInsertBrand(){
        Brand brand = new Brand();
        brand.setName("test");
        brand.setImgUrl("/test/aa.jpg");
        brand.setDescription("test");
        brand.setSort(30);
        brand.setIsDisplay(Short.valueOf("1"));
        brand.setWebSite("www.kenick.com");
        Integer integer = brandMapper.insertBrand(brand);
        System.out.println(integer);
    }

    @Test
    public void testDeleteBrand(){
        Integer integer = brandMapper.deleteBrandById(113);
        System.out.println(integer);
    }

    @Test
    public void testDeleteBatch(){
        List<Integer> idList = new ArrayList<>();
        idList.add(111);
        idList.add(112);
        Integer lines = brandMapper.deleteBrandByList(idList);
        System.out.println(lines);
    }
}