package com.leyou.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "leyou.upload")
@Data
public class UploadProperties {
    private String baseUrl;
    private List<String> allowTypes;
}
