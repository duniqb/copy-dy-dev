package cn.duniqb.copydy.service.impl;


import cn.duniqb.copydy.dao.BgmMapper;
import cn.duniqb.copydy.model.Bgm;
import cn.duniqb.copydy.service.BgmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Bgm> queryBgmList() {

        return bgmMapper.selectAll();
    }

    /**
     * 根据 id 查询 bgm 信息
     *
     * @param bgmId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Bgm queryBgmById(String bgmId) {
        return bgmMapper.selectByPrimaryKey(bgmId);
    }


}
