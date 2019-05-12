package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.upload.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 1. 校验图片类型
 * TODO 自定义日志格式
 */
@Service
@Slf4j
@EnableConfigurationProperties({UploadProperties.class})
public class UploadService {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private UploadProperties uploadProperties;


    public String uploadImage(MultipartFile file) throws LyException, IOException {
        try {
            //校验文件后缀名
            String contentType = file.getContentType();
            if (!uploadProperties.getAllowTypes().contains(contentType)) {
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            //校验文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            //保存文件到本地
//        File dest = new File("E:\\GitHub\\a-mall\\leyou\\upload-resource",file.getOriginalFilename());
//        try {
//            file.transferTo(dest);
//            return "image.leyou.com/" + file.getName();
//        } catch (IOException e) {
//            log.error("文件上传失败",e);
//            //抛出异常
//            throw new LyException(ExceptionEnum.UPLOAD_ERROR);
//        }
            //TODO 上传到FastDFS
            String fileName = file.getOriginalFilename();
            String extensions = StringUtils.substringAfterLast(fileName, ".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extensions, null);
            return uploadProperties.getBaseUrl() + storePath.getFullPath();
        }catch (Exception e){
            log.error("文件上传失败",e);
            throw new LyException(ExceptionEnum.UPLOAD_ERROR);
        }
    }
}
