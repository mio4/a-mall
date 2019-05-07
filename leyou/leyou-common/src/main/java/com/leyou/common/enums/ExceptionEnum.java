package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {

    PRICE_CANNOT_BE_NULL(400,"价格不能为空"),
    CATEGORY_NOT_FOUND(404,"商品分类没有查找"),
    BRAND_NOT_FOUND(404,"品牌不存在"),
    BRAND_SAVE_ERROR(500,"新增品牌失败"),
    UPLOAD_ERROR(500,"文件上传失败"),
    INVALID_FILE_TYPE(400,"文件类型不匹配"),
    ;
    private int code;
    private String msg;
}
