package cn.duniqb.copydy.service.impl;


import cn.duniqb.copydy.dao.BgmMapper;
import cn.duniqb.copydy.model.Bgm;
import cn.duniqb.copydy.service.BgmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BgmServiceImpl implements BgmService {

    @Autowired
    private BgmMapper bgmMapper;

    /**
     * 查询背景音乐列表
     *
     * @return
     */
    @Override
    public List<Bgm> queryBgmList() {

        return bgmMapper.selectAll();
    }


}
