package com.kenick.sport.service.upload;

public interface UploadService {
    /**
     * fastDFS上传图片
     * @param fileByte 图片字节码
     * @param fileName 图片名称
     * @return fastDFS文件id
     */
    String fastDFSUploadFile(byte[] fileByte,String fileName);
}
