package com.leyou.upload;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FdfsTest {

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    /**
     * group1/M00/00/00/wKhcgVzYJTOAHZYpAAAOvjITv6M35.jpeg
     * M00/00/00/wKhcgVzYJTOAHZYpAAAOvjITv6M35.jpeg
     * @throws FileNotFoundException
     */
    @Test
    public void testUpload() throws FileNotFoundException {
        File file = new File("E:\\GitHub\\a-mall\\info\\Picture\\1.jpeg");
        // 上传并且生成缩略图
        //流 文件大小 后缀名 null
        StorePath storePath = this.storageClient.uploadFile(
                new FileInputStream(file), file.length(), "jpeg", null);
        // 带分组的路径
        System.out.println(storePath.getFullPath());
        // 不带分组的路径
        System.out.println(storePath.getPath());
    }

    /**
     * group1/M00/00/00/wKhcgVzYKSuAZgMnAAAaaHFxBD036.jpeg
     * M00/00/00/wKhcgVzYKSuAZgMnAAAaaHFxBD036.jpeg
     * M00/00/00/wKhcgVzYKSuAZgMnAAAaaHFxBD036_60x60.jpeg
     * @throws FileNotFoundException
     */
    @Test
    public void testUploadAndCreateThumb() throws FileNotFoundException {
        File file = new File("E:\\GitHub\\a-mall\\info\\Picture\\2.jpeg");
        // 上传并且生成缩略图
        StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(
                new FileInputStream(file), file.length(), "jpeg", null);
        // 带分组的路径
        System.out.println(storePath.getFullPath());
        // 不带分组的路径
        System.out.println(storePath.getPath());
        // 获取缩略图路径
        String path = thumbImageConfig.getThumbImagePath(storePath.getPath());
        System.out.println(path);
    }
}