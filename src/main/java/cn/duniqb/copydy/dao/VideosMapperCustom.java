package cn.duniqb.copydy.dao;

import cn.duniqb.copydy.common.utils.MyMapper;
import cn.duniqb.copydy.model.Videos;
import cn.duniqb.copydy.model.vo.VideosVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义 mapper
 */
@Mapper
@Component
public interface VideosMapperCustom extends MyMapper<Videos> {
    /**
     * 查询所有视频
     *
     * @return
     */
    List<VideosVO> queryAllVideos(@Param("videoDesc") String videoDesc);
}