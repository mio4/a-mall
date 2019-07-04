package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {

    //商品相关异常
    GOODS_NOT_FOUND(404,"商品不存在"),
    GOODS_SAVE_FAILED(400,"商品保存失败"),
    GOODS_DELETE_FAILED(400,"商品删除失败"),
    GOODS_DETAIL_NOT_FOUND(400,"商品详情查询失败"),
    GOODS_SKU_NOT_FOUND(400,"商品SKU不存在"),
    GOODS_STOCK_NOT_FOUND(400,"商品库存不存在"),

    PRICE_CANNOT_BE_NULL(400,"价格不能为空"),
    CATEGORY_NOT_FOUND(404,"商品分类没有查找"),
    BRAND_NOT_FOUND(404,"品牌不存在"),

    SPEC_GROUP_NOT_FOUND(404,"商品规格组查询失败"),
    SPEC_PARAM_NOT_FOUND(404,"商品规格参数查询失败"),
    BRAND_SAVE_ERROR(500,"新增品牌失败"),

    //上传相关异常
    UPLOAD_ERROR(500,"文件上传失败"),
    INVALID_FILE_TYPE(400,"文件类型不匹配"),

    //用户相关异常
    INVALID_USER_DATA_TYPE(400,"无效用户数据类型"),
    INVALID_VERIFY_CODE(400,"无效的验证码"),
    INVALID_USERNAME_OR_PASSWORD(400,"无效的用户名或者密码"),

    //内部错误
    CREATE_TOKEN_FAILED(500,"用户凭证生成失败"),
    ;
    private int code;
    private String msg;

}
