package cn.duniqb.copydy.dao;

import cn.duniqb.copydy.model.SearchRecords;
import cn.duniqb.copydy.common.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
    /**
     * 获取热搜词
     *
     * @return
     */
    List<String> getHotWords();
}