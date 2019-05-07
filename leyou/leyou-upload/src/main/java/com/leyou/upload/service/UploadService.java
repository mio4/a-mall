package com.leyou.upload.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import lombok.extern.slf4j.Slf4j;
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
public class UploadService {

    private static final List<String> allowTypes = Arrays.asList("image/jpeg","image/png","image/bmp");

    public String uploadImage(MultipartFile file) throws LyException, IOException {
        //校验文件后缀名
        String contentType = file.getContentType();
        if(!allowTypes.contains(contentType)){
            throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
        }
        //校验文件内容
        BufferedImage image = ImageIO.read(file.getInputStream());
        if(image == null){
            throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
        }
        //
        //保存文件到本地
        File dest = new File("E:\\GitHub\\a-mall\\leyou\\upload-resource",file.getOriginalFilename());
        try {
            file.transferTo(dest);
            return "image.leyou.com/" + file.getName();
        } catch (IOException e) {
            log.error("文件上传失败",e);
            //抛出异常
            throw new LyException(ExceptionEnum.UPLOAD_ERROR);
        }
    }
}
