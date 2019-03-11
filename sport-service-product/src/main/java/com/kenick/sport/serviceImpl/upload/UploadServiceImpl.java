package com.kenick.sport.serviceImpl.upload;

import com.kenick.sport.service.upload.UploadService;
import com.kenick.sport.util.FastDFSUtil;
import org.springframework.stereotype.Service;

@Service("uploadService")
public class UploadServiceImpl implements UploadService {

    @Override
    public String fastDFSUploadFile(byte[] fileByte, String fileName) {
        return FastDFSUtil.uploadPicToFastDFS(fileByte,fileName);
    }

}