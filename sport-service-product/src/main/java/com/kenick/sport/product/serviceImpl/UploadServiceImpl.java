package com.kenick.sport.product.serviceImpl;

import com.kenick.sport.product.utils.FastDFSUtil;
import com.kenick.sport.service.upload.UploadService;
import org.springframework.stereotype.Service;

@Service("uploadService")
public class UploadServiceImpl implements UploadService {

    @Override
    public String fastDFSUploadFile(byte[] fileByte, String fileName) {
        return FastDFSUtil.uploadPicToFastDFS(fileByte,fileName);
    }

    @Override
    public Boolean fastDFSIsConn() {
        return FastDFSUtil.testFastDFSConn();
    }

}