package com.kenick.sport.common.utils;
import java.util.ArrayList;
import java.util.List;

/**
 *  分页相关工具类
 */
public class PaginationUtil {
    private  Integer pageNo = 1; // 当前页
    private  Integer pageSize = 5; // 每页大小
    private  Integer totalSize; // 总数量

    public PaginationUtil(){}

    public PaginationUtil(Integer pageNo,Integer pageSize,Integer totalSize){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
    }

    /**
     *  获取最后一页
     * @return
     */
    public  Integer getLastPage(){
        int lastPage = 1; // 默认第一页
        if(totalSize > pageSize){
            lastPage = (totalSize % pageSize == 0?(totalSize/pageSize):(totalSize/pageSize+1));
        }
        return lastPage;
    }

    /**
     *  获取上一页
     * @return
     */
    public Integer getPrePage(){
        int prePage = 1;
        if(pageNo > 1){
            prePage = pageNo -1;
        }
        return prePage;
    }

    /**
     *  获取下一页
     * @return
     */
    public Integer getNextPage(){
        Integer lastPage = this.getLastPage();
        return  lastPage>pageNo?pageNo+1:pageNo;
    }

    /**
     *  获取中间num页
     * @return 中间页码字符串,以逗号分隔 2,3,4,5,
     */
    public String getCenterPageStr(Integer num){
        String centerPages = "";
        int startPage = 1; // 起始页码
        int endPage = 1; // 终止页码

        int lastPage = this.getLastPage();
        int nextPart = lastPage - pageNo; // 后续页码
        if(nextPart >= (num-1)){ // 后续页码数大于等于num-1
            startPage = pageNo;
            endPage = pageNo + (num-1);
        }else{ // 后续页码数小于num-1
            startPage = (pageNo - (num-nextPart-1)) >0 ?(pageNo - (num-nextPart -1)) :1;
            endPage = lastPage;
        }

        for(int i=startPage;i<=endPage;i++){
            centerPages = centerPages + i + ",";
        }
        return centerPages;
    }

    /**
     *  获取中间num页
     * @return 中间页码字符串,以逗号分隔 2,3,4,5,
     */
    public List<String> getCenterPageList(Integer num){
        List<String> centerPages = new ArrayList<>();
        int startPage = 1; // 起始页码
        int endPage = 1; // 终止页码

        int lastPage = this.getLastPage();
        int nextPart = lastPage - pageNo; // 后续页码
        if(nextPart >= (num-1)){ // 后续页码数大于等于num-1
            startPage = pageNo;
            endPage = pageNo + (num-1);
        }else{ // 后续页码数小于num-1
            startPage = (pageNo - (num-nextPart-1)) >0 ?(pageNo - (num-nextPart -1)) :1;
            endPage = lastPage;
        }

        for(int i=startPage;i<=endPage;i++){
            centerPages.add(i+"");
        }
        return centerPages;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public static void main(String[] args) {
        int pageNo = 10; // 当前页
        int pageSize = 10; // 每页大小
        int totalSize = 100; // 当前查询数据大小
        PaginationUtil paginationUtil = new PaginationUtil(pageNo, pageSize, totalSize);
        System.out.println("当前页码:"+ pageNo);
        System.out.println("每页显示数:"+pageSize);
        System.out.println("总数据:"+totalSize);
        System.out.println("上一页:" + paginationUtil.getPrePage());
        System.out.println("中间页:"+paginationUtil.getCenterPageStr(5));
        System.out.println("下一页:" + paginationUtil.getNextPage());
        System.out.println("尾页:" + paginationUtil.getLastPage());
    }
}
