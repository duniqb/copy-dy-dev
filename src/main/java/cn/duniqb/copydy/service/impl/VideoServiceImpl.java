package cn.duniqb.copydy.service.impl;


import cn.duniqb.copydy.common.utils.PageResult;
import cn.duniqb.copydy.dao.SearchRecordsMapper;
import cn.duniqb.copydy.dao.VideosMapper;
import cn.duniqb.copydy.dao.VideosMapperCustom;
import cn.duniqb.copydy.model.SearchRecords;
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
}
