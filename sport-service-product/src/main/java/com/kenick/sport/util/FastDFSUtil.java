package com.kenick.sport.util;

import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.util.Properties;

public class FastDFSUtil {
    public static void main(String[] args) {
        try {
            FileInputStream fileInputStream = new FileInputStream("e:/test.png");
            int available = fileInputStream.available();
            byte[] picByteArray = new byte[available];
            fileInputStream.read(picByteArray);
            String path = FastDFSUtil.uploadPicToFastDFS(picByteArray, "test.png");
            System.out.println("path:"+path);

//            String ret = deleteFastDFSFile("group1/M00/00/00/wKjIjFx_I9CANZYVAAAatgSQzuw207.png");
//            System.out.println("ret:"+ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final static Logger logger = LoggerFactory.getLogger(FastDFSUtil.class);
    private static StorageClient1 storageClient1;
    private static String fastDFSNgnixIp = "";
    private static String fastDFSNginxPort = "";

    static{
        try {
            // 初始化fastDFS配置文件
            ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
            String resourcePath = resource.getClassLoader().getResource("fdfs_client.conf").getPath();
            ClientGlobal.init(resourcePath);

            // 获取存储服务器客户端
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            storageClient1 = new StorageClient1(trackerServer, null);

            Properties properties = new Properties();
            properties.load(new FileInputStream(resourcePath.substring(0)));
            fastDFSNgnixIp = properties.getProperty("nginx.http_ip");
            fastDFSNginxPort = properties.getProperty("nginx.http_port");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 将图片上传到存储服务器上
     * @param fileByte 文件字节数组
     * @param fileName 文件名 包含扩展名
     * @return 返回路径 group1/M00/00/01/xxxx.jpg
     */
    public static String uploadPicToFastDFS(byte[] fileByte,String fileName){
        String picPath = "";
        try {
            // 文件上传
            String extension = FilenameUtils.getExtension(fileName);
            picPath = storageClient1.upload_file1(fileByte, extension, null);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        if(!"".equals(picPath)){
            picPath = "http://" + fastDFSNgnixIp + ":" + fastDFSNginxPort + "/" + picPath;
        }
        return picPath;
    }

    /**
     *  删除fastDFS上的文件
     * @param fileId 文件id group1/M00/00/00/wKjIjFx_I9CANZYVAAAatgSQzuw207.png
     * @return 0:成功 非0失败
     */
    public static String deleteFastDFSFile(String fileId){
        String ret = "";
        try {
            // int retNum = storageClient1.delete_file("group1",fileId);
            int retNum = storageClient1.delete_file1(fileId);
            ret = ret + retNum;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return ret;
    }
}