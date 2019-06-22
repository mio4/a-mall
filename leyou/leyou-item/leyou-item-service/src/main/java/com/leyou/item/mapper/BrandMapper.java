package com.leyou.item.mapper;

import com.leyou.common.mapper.BaseMapper;
import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends BaseMapper<Brand> {

    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Integer cid, @Param("bid") Long bid);

    @Select("select a.* from tb_brand a inner join tb_category_brand b on a.id = b.brand_id where b.category_id = #{cid}")
    List<Brand> selectBrandIdAndNameByCid(@Param("cid") Long cid);
}
