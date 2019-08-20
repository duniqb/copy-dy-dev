package cn.duniqb.copydy.service;


import cn.duniqb.copydy.common.utils.PageResult;
import cn.duniqb.copydy.model.Videos;

import java.util.List;


public interface VideoService {

    /**
     * 保存视频
     *
     * @param video
     */
    String saveVideo(Videos video);

    /**
     * 修改视频：封面
     *
     * @param videoId
     * @param coverPath
     * @return
     */
    void updateVideo(String videoId, String coverPath);

    /**
     * 分页查询视频列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    PageResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize);

    /**
     * 获取热搜词
     *
     * @return
     */
    List<String> getHotWords();

    /**
     * 用户喜欢视频
     *
     * @param userId
     * @param videoId
     * @param videoCreaterId
     */
    void userLikeVideo(String userId, String videoId, String videoCreaterId);

    /**
     * 用户不喜欢视频
     *
     * @param userId
     * @param videoId
     * @param videoCreaterId
     */
    void userUnLikeVideo(String userId, String videoId, String videoCreaterId);

    /**
     * 查询我喜欢的视频列表
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PageResult queryMyLikeVideos(String userId, Integer page, Integer pageSize);

    /**
     * 查询我关注的人的视频列表
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PageResult queryMyFollowVideos(String userId, Integer page, Integer pageSize);
}
