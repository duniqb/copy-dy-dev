package cn.duniqb.copydy.service.impl;


import cn.duniqb.copydy.common.utils.PageResult;
import cn.duniqb.copydy.dao.*;
import cn.duniqb.copydy.model.SearchRecords;
import cn.duniqb.copydy.model.UsersLikeVideos;
import cn.duniqb.copydy.model.Videos;
import cn.duniqb.copydy.model.vo.VideosVO;
import cn.duniqb.copydy.service.VideoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideosMapper videosMapper;

    @Autowired
    private VideosMapperCustom videosMapperCustom;

    @Autowired
    private SearchRecordsMapper searchRecordsMapper;

    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    /**
     * 保存视频
     *
     * @param video
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveVideo(Videos video) {
        String id = sid.nextShort();
        video.setId(id);
        videosMapper.insertSelective(video);

        return id;
    }

    /**
     * 修改视频封面
     *
     * @param videoId
     * @param coverPath
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateVideo(String videoId, String coverPath) {

        Videos video = new Videos();
        video.setId(videoId);
        video.setCoverPath(coverPath);
        videosMapper.updateByPrimaryKeySelective(video);
    }

    /**
     * 分页查询视频列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PageResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize) {

        // 保存热搜词记录
        String desc = video.getVideoDesc();
        if (isSaveRecord != null && isSaveRecord == 1) {
            SearchRecords record = new SearchRecords();
            String recordId = sid.nextShort();
            record.setId(recordId);
            record.setContent(desc);
            searchRecordsMapper.insert(record);
        }

        // 开始分页
        PageHelper.startPage(page, pageSize);
        List<VideosVO> list = videosMapperCustom.queryAllVideos(desc);

        // 包装Page对象
        PageInfo<VideosVO> pageList = new PageInfo<>(list);

        // 封装后的视频结果
        PageResult pageResult = new PageResult();
        pageResult.setPage(page);
        pageResult.setTotal(pageList.getPages());
        pageResult.setRows(list);
        pageResult.setRecords(pageList.getTotal());

        return pageResult;
    }

    /**
     * 获取热搜词
     *
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<String> getHotWords() {

        return searchRecordsMapper.getHotWords();
    }

    /**
     * 用户喜欢视频：需要改动 3 个表
     *
     * @param userId
     * @param videoId
     * @param videoCreaterId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    @SuppressWarnings("all")
    public void userLikeVideo(String userId, String videoId, String videoCreaterId) {
        System.out.println("用户喜欢视频..");
        // 保存用户和视频的点赞关联关系表
        String likeId = sid.nextShort();
        UsersLikeVideos ulv = new UsersLikeVideos();
        ulv.setId(likeId);
        ulv.setUserId(userId);
        ulv.setVideoId(videoId);
        usersLikeVideosMapper.insert(ulv);

        // 视频喜欢数量累加
        videosMapperCustom.addVideoLikeCount(videoId);

        // 用户受喜欢数量累加
        usersMapper.addReceiveLikeCount(userId);
    }

    /**
     * 用户不喜欢视频
     *
     * @param userId
     * @param videoId
     * @param videoCreaterId
     */
    @SuppressWarnings("all")
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userUnLikeVideo(String userId, String videoId, String videoCreaterId) {
        System.out.println("用户不喜欢视频..");

        // 删除用户和视频的点赞关联关系表
        Example example = new Example(UsersLikeVideos.class);
        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("videoId", videoId);

        usersLikeVideosMapper.deleteByExample(example);

        // 视频喜欢数量累减
        videosMapperCustom.reduceVideoLikeCount(videoId);

        // 用户受喜欢数量累减
        usersMapper.reduceReceiveLikeCount(userId);
    }
}
