package com.leyou.search.pojo;

public class SearchRequest {
    private static final int DEFAULT_SIZE = 20; //默认一页多少条数据
    private static final int DEFAULT_PAGE = 1; //默认页号

    private String key; //搜索字段

    private Integer page; //当前页号

    public String getKey(){
        return this.key;
    }

    public void setKey(String key){
        this.key = key;
    }

    public Integer getPage(){
        if(page == null){
            return DEFAULT_PAGE;
        }
        return Math.max(DEFAULT_PAGE,page);
    }

    public void setPage(Integer page){
        this.page = page;
    }

    public int getSize(){
        return DEFAULT_SIZE;
    }
}
