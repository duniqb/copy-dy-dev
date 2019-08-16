package cn.duniqb.copydy.dao;

import cn.duniqb.copydy.model.Videos;
import cn.duniqb.copydy.common.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface VideosMapper extends MyMapper<Videos> {
}