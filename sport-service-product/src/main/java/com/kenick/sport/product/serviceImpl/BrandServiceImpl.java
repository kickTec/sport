package com.kenick.sport.product.serviceImpl;

import com.kenick.sport.mapper.product.BrandMapper;
import com.kenick.sport.pojo.product.Brand;
import com.kenick.sport.pojo.product.BrandQuery;
import com.kenick.sport.product.utils.FastDFSUtil;
import com.kenick.sport.service.product.BrandService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("brandService")
public class BrandServiceImpl implements BrandService {
    @Resource
    private BrandMapper brandMapper;

    @Resource
    private Jedis jedis;

    @Override
    public List<Brand> selectBrandListByQueryNoPage(BrandQuery brandQuery) {
        return brandMapper.queryBrandListByQueryAndNoPage(brandQuery);
    }

    @Override
    public Integer selectBrandTotalByQuery(BrandQuery brandQuery) {
        return brandMapper.queryBrandSumByQueryAndPage(brandQuery);
    }

    @Override
    public List<Brand> selectBrandListByQueryAndPage(BrandQuery brandQuery, Integer pageNo, Integer pageSize) {
        if(pageNo == null || pageNo < 0){
            pageNo = 1;
        }

        if(pageSize == null || pageSize < 0){
            pageSize = 5;
        }
        brandQuery.setMaxNum(pageSize);

        // 计算最大页码
        int totalPage;
        Integer totalNum = brandMapper.queryBrandSumByQueryAndPage(brandQuery);
        if(totalNum % pageSize == 0 ){
            totalPage = totalNum / pageSize;
        }else{
            totalPage = totalNum / pageSize + 1;
        }

        // 计算起始行
        int startLine;
        if(pageNo > totalPage){
            startLine = (totalPage-1)*pageSize;
        }else{
            startLine = (pageNo-1)*pageSize;
        }
        if(startLine > totalNum){
            startLine = totalNum;
        }
        if(startLine < 0){
            startLine = 0;
        }
        brandQuery.setStartLine(startLine);

        return brandMapper.queryBrandListByQueryAndPage(brandQuery);
    }

    @Override
    public Brand selectBrandById(String brandId) {
        return brandMapper.queryBrandById(brandId);
    }

    @Override
    public List<Brand> selectBrandListFromRedis() {
        List<Brand> brandList = new ArrayList<>();
        Map<String, String> brandMap = jedis.hgetAll("brand");
        Set<String> keySet = brandMap.keySet();
        for(String brandId:keySet){
            String brandName = brandMap.get(brandId);
            Brand brand = new Brand();
            brand.setId(Long.parseLong(brandId));
            brand.setName(brandName);
            brandList.add(brand);
        }
        return brandList;
    }

    @Override
    public String selectBrandNameFromRedis(Long brandId) {
        Map<String, String> brandMap = jedis.hgetAll("brand");
        return brandMap.get(brandId+"");
    }

    @Override
    public Integer updateBrand(Brand brand) {
        if(brand.getId() == null){ // id不存在
            return -2;
        }

        Brand oldBrand = this.selectBrandById(brand.getId() + "");
        if(oldBrand == null){ // 无次id品牌信息
            return -3;
        }

        if(brand == oldBrand){ // 要修改的品牌信息与原信息相同
            return -1;
        }

        // 品牌信息保存到redis中
        jedis.hset("brand",String.valueOf(brand.getId()),brand.getName());

        return brandMapper.updateBrand(brand);
    }

    @Override
    public Boolean addBrand(Brand brand) {
        // 品牌信息保存到jedis中
        jedis.hset("brand",String.valueOf(brand.getId()),brand.getName());

        Integer lines = brandMapper.insertBrand(brand);
        return lines>0;
    }

    @Override
    public Boolean deleteBrand(Integer brandId) {
        // 先删除服务器资源
        Brand brand = brandMapper.queryBrandById(brandId+"");
        String imgUrl = brand.getImgUrl();
        if(imgUrl.contains("http")){ // fastDFS文件删除 由于分布式部署,本地文件需要在应用层删除
            String fileID = FastDFSUtil.getFileIdFromUrl(imgUrl);
            FastDFSUtil.deleteFastDFSFile(fileID);
        }

        // 删除数据库
        Integer integer = brandMapper.deleteBrandById(brandId);
        return integer>0;
    }

    @Override
    public Boolean deleteBrandByArray(String[] idArray) {
        List<Integer> idList = new ArrayList<>();
        for (String id : idArray) {
            idList.add(Integer.parseInt(id));
        }
        Integer lines = brandMapper.deleteBrandByList(idList);
        return lines == idList.size();
    }
}