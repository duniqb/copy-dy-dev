package cn.duniqb.copydy.service;


import cn.duniqb.copydy.dao.UsersMapper;
import cn.duniqb.copydy.model.Users;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    @SuppressWarnings("all")
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users user = new Users();
        user.setUsername(username);
        Users result = usersMapper.selectOne(user);

        return result != null;
    }

    @SuppressWarnings("all")
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUser(Users user) {
        // 全局 ID
        String userId = sid.nextShort();
        user.setId(userId);
        usersMapper.insert(user);
    }
}
