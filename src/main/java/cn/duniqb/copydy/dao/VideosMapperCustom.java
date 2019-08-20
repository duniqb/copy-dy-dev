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
     * 条件查询所有视频
     *
     * @param videoDesc
     * @param userId
     * @return
     */
    List<VideosVO> queryAllVideos(@Param("videoDesc") String videoDesc, @Param("userId") String userId);

    /**
     * 查询我关注的人发的视频
     *
     * @param userId
     * @return
     */
    List<VideosVO> queryMyFollowVideos(String userId);

    /**
     * 查询点赞视频
     *
     * @param userId
     * @return
     */
    List<VideosVO> queryMyLikeVideos(@Param("userId") String userId);


    /**
     * 对喜欢的数量累加
     *
     * @param videoId
     */
    void addVideoLikeCount(String videoId);

    /**
     * 对喜欢的数量累减
     *
     * @param videoId
     */
    void reduceVideoLikeCount(String videoId);
}