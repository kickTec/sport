package com.kenick.sport.solr.serviceImpl;

import com.kenick.sport.pojo.page.Pagination;
import com.kenick.sport.pojo.product.Product;
import com.kenick.sport.pojo.product.ProductQuery;
import com.kenick.sport.service.product.SolrService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("solrService")
public class SolrServiceImpl implements SolrService {

    @Resource
    private SolrServer solrServer;

    @Override
    public Long searchProductSum(String content) {
        QueryResponse queryResponse = null;
        try {
            SolrQuery solrQuery = new SolrQuery();
            solrQuery.setQuery("product_name:" + content);
            queryResponse = solrServer.query(solrQuery);
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return queryResponse == null ? 0 : queryResponse.getResults().getNumFound();
    }

    @Override
    public List<Product> searchProductList(String content, Integer pageNo, Integer pageSize) {
        List<Product> productList = new ArrayList<>();

        // 商品数量
        Long productSum = searchProductSum(content);

        // pageNo pageSize异常检测
        if(pageNo == null || pageNo <0){
            pageNo = 1;
        }

        if(pageSize == null || pageSize <= 0){
            pageSize = 5;
        }

        // 计算起始行
        Long maxPage = productSum % pageSize == 0 ? productSum/pageSize : productSum/pageSize + 1;
        Long startLine = pageNo > maxPage ? (maxPage-1)*pageSize : (pageNo -1)*pageSize;

        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery("product_name:" + content);
        solrQuery.setStart(startLine.intValue());
        solrQuery.setRows(pageSize);
        solrQuery.setSort("product_price", SolrQuery.ORDER.asc);

        // 设置高亮
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("product_name");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");

        try {
            QueryResponse queryResponse = solrServer.query(solrQuery);
            solrDocument2List(productList, queryResponse);
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        return productList;
    }

    // 将solrDocumentList转换为List<Product>
    private void solrDocument2List(List<Product> productList, QueryResponse queryResponse) {
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        for (SolrDocument solrDocument : solrDocumentList) {
            String product_id = solrDocument.get("product_id").toString();
            String product_price = solrDocument.get("product_price").toString();
            String product_imgUrl = solrDocument.get("product_imgUrl").toString();
            String product_brandId = solrDocument.get("product_brandId").toString();

            String product_name = solrDocument.get("product_name").toString();
            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
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
            productList.add(product);
        }
    }

    @Override
    public Pagination selectProductListFromSolr(String keyword,String brandId,String price,Integer pageNo)throws Exception {
        // 分页相关数据
        int validPageNo = Pagination.cpn(pageNo);
        int pageSize = 8;
        ProductQuery productQuery = new ProductQuery();
        productQuery.setPageNo(validPageNo);
        productQuery.setPageSize(pageSize);

        // 查询solr总数和商品集合
        SolrQuery solrQuery = new SolrQuery();

        // 根据名称查询
        if(keyword == null || "".equals(keyword)){
            solrQuery.setQuery("product_name:*");
        }else{
            solrQuery.setQuery("product_name:"+keyword);
        }
        if(brandId != null && !"".equals(brandId)){
            solrQuery.addFilterQuery("product_brandId:"+brandId);
        }
        if(price != null && !"".equals(price)){
            String[] priceArray = price.split("-");
            if(priceArray.length == 1){
                solrQuery.addFilterQuery("product_price:["+priceArray[0]+" TO *]");
            }else if(priceArray.length == 2){
                solrQuery.addFilterQuery("product_price:["+priceArray[0]+" TO "+priceArray[1]+"]");
            }
        }

        // 查询结果集和排序
        solrQuery.setStart(productQuery.getStartRow());
        solrQuery.setRows(productQuery.getPageSize());
        solrQuery.setSort("product_price", SolrQuery.ORDER.asc);

        // 设置高亮
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("product_name");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");

        QueryResponse queryResponse = solrServer.query(solrQuery);
        List<Product> productList = new ArrayList<>();
        solrDocument2List(productList, queryResponse);

        Long total = queryResponse.getResults().getNumFound();
        return new Pagination(validPageNo, pageSize, total.intValue(),productList);
    }
}
