package com.jd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jd.entity.BrowseHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BrowseHistoryMapper extends BaseMapper<BrowseHistory> {

    @Select("SELECT h.*, p.name as product_name, p.cover_image, p.price FROM browse_history h JOIN products p ON h.product_id = p.id WHERE h.user_id = #{userId} ORDER BY h.viewed_at DESC LIMIT #{limit}")
    List<BrowseHistory> findRecentByUser(@Param("userId") Long userId, @Param("limit") int limit);
}
