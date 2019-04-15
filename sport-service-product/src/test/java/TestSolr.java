import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Iterator;

@ContextConfiguration(locations={"classpath:spring-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSolr {

    @Resource
    private SolrServer solrServer;

    @Test
    public void testSolrJ() throws Exception{
        SolrInputDocument document = new SolrInputDocument();
            document.addField("product_id",5555);
        document.addField("product_brandId",4555);
        document.addField("name_ik","spring管理solr");
        solrServer.add(document);
        solrServer.commit();

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.set("q","name_ik:管理");
        QueryResponse queryResponse = solrServer.query(solrQuery);
        SolrDocumentList results = queryResponse.getResults();
        for(SolrDocument solrDocument:results){
            System.out.println(solrDocument);
        }
    }
}
