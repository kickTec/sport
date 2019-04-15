import com.kenick.sport.pojo.product.Product;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ContextConfiguration(locations={"classpath:spring-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSolr {
    private final static Logger logger = LoggerFactory.getLogger(TestSolr.class);

    @Resource
    private SolrServer solrServer;

    @Test
    public void testSolrSelectList(){
        try {
            SolrQuery solrQuery = new SolrQuery();
            solrQuery.setQuery("product_name:2016");
            solrQuery.setSort("product_price", SolrQuery.ORDER.asc);

            // 设置高亮
            solrQuery.setHighlight(true);
            solrQuery.addHighlightField("product_name");
            solrQuery.setHighlightSimplePre("<font color='red'>");
            solrQuery.setHighlightSimplePost("</font>");

            QueryResponse query = solrServer.query(solrQuery);
            logger.info("查询总数:{}", query.getResults().getNumFound());
            SolrDocumentList solrDocumentList = query.getResults(); // 普通结果集
            for(SolrDocument solrDocument:solrDocumentList){
                String product_id = solrDocument.get("product_id").toString();
                String product_price = solrDocument.get("product_price").toString();
                String product_imgUrl = solrDocument.get("product_imgUrl").toString();
                String product_brandId = solrDocument.get("product_brandId").toString();

                String product_name = solrDocument.get("product_name").toString();
                Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
                Set<String> keySet = highlighting.keySet();
                for(String key:keySet){
                    if(product_id.equals(key)){
                        product_name = highlighting.get(key).get("product_name").get(0);
                        break;
                    }
                }

                Product product = new Product();
                product.setId(Long.parseLong(product_id));
                product.setName(product_name);
                product.setPrice(product_price);
                product.setImgUrl(product_imgUrl);
                product.setBrandId(Long.parseLong(product_brandId));
                logger.info("商品:{}", product);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
    }
}