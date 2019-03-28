package com.kenick.sport.product.utils;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FastDFSUtil {
    public static void main(String[] args) {
        Boolean ret = testFastDFSConn();
        System.out.println(ret);
    }

    private final static Logger logger = LoggerFactory.getLogger(FastDFSUtil.class);
    private static String fastDFSNgnixIp = "";
    private static String fastDFSNginxPort = "";

    static{
        try {
            // 初始化fastDFS配置文件
            ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
            String resourcePath = resource.getClassLoader().getResource("fdfs_client.conf").getPath();
            ClientGlobal.init(resourcePath);

            Properties properties = new Properties();
            properties.load(new FileInputStream(resourcePath.substring(0)));
            fastDFSNgnixIp = properties.getProperty("nginx.http_ip");
            fastDFSNginxPort = properties.getProperty("nginx.http_port");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     *  测试fastDFS连接是否可用
     * @return
     */
    public static Boolean testFastDFSConn(){
        Boolean ret = false;
        TrackerServer trackerServer = null;
        try {
            // 获取存储服务器客户端
            TrackerClient trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            if(trackerServer != null){
                ret = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(trackerServer != null){
                try {
                    trackerServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    /**
     * 将图片上传到存储服务器上
     * @param fileByte 文件字节数组
     * @param fileName 文件名 包含扩展名
     * @return 返回路径 group1/M00/00/01/xxxx.jpg
     */
    public static String uploadPicToFastDFS(byte[] fileByte,String fileName){
        TrackerServer trackerServer = null;
        String picPath = "";
        try {
            TrackerClient trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, null);

            // 文件上传
            String extension = FilenameUtils.getExtension(fileName);
            picPath = storageClient1.upload_file1(fileByte, extension, null);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }finally {
            if(trackerServer != null){
                try {
                    trackerServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!"".equals(picPath)){
            picPath = "http://" + fastDFSNgnixIp + ":" + fastDFSNginxPort + "/" + picPath;
        }
        return picPath;
    }

    /**
     *  删除fastDFS上的文件 http://192.168.200.140:81/group1/M00/00/00/wKjIjFx_I9CANZYVAAAatgSQzuw207.png
     * @param fileId 文件id group1/M00/00/00/wKjIjFx_I9CANZYVAAAatgSQzuw207.png
     * @return 0:成功 非0失败
     */
    public static Boolean deleteFastDFSFile(String fileId){
        TrackerServer trackerServer = null;
        int ret = 1;
        try {
            TrackerClient trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, null);

            ret = storageClient1.delete_file1(fileId);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }finally {
            if(trackerServer != null){
                try {
                    trackerServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret == 0;
    }

    /**
     *  从fastDFS url中获取fastDFS文件id
     * @param httpUrl fastDFS访问地址
     * @return fastDFS文件id
     */
    public static String getFileIdFromUrl(String httpUrl){
        String fileId = "";
        String url = "http://192.168.200.140:81/group1/M00/00/00/wKjIjFx_I9CANZYVAAAatgSQzuw207.png";
        String[] urlArray = url.split("/");
        int i = 0;
        for(String urlPart:urlArray){
            if(i>2){
                fileId = fileId + "/" + urlPart;
            }
            i++;
        }
        return fileId.substring(1);
    }
}