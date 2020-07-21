package com.kpi.service.impl;

import com.kpi.dao.UserDAO;
import com.kpi.pojo.User;
import com.kpi.service.UserService;
import com.kpi.spring.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
//    private UserDAO userDAO = SpringContextUtil.getBean("userDAO");

    @Autowired
    private UserDAO userDAO;

    @Override
    public int addUser(User user) {
        return userDAO.addUser(user);
    }

    @Override
    public int updateUser(User user) {
        return userDAO.updateUser(user);
    }

    @Override
    public User queryUserById(String id) {
        return userDAO.queryUserById(id);
    }

    @Override
    public User queryUserByName(String userName) {
        return userDAO.queryUserByName(userName);
    }

    @Override
    public List<User> queryAllUser() {
        return userDAO.queryAllUser();
    }

    @Override
    public User queryUserInfoById(String id) {
        return userDAO.queryUserInfoById(id);
    }

    @Override
    public List<User> queryAllUserInfo() {
        return userDAO.queryAllUserInfo();
    }

    @Override
    public int deleteUserById(String id) {
        return userDAO.deleteUserById(id);
    }

    @Override
    public int deleteUserByName(String userName) {
        return userDAO.deleteUserByName(userName);
    }

    @Override
    public boolean login(String tid, String password) {
        User user = userDAO.queryUserInfoById(tid);
        if (user!=null) {
            if (user.getPassword().equals(password)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
