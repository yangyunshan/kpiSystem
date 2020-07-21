package com.kpi.service;

import com.kpi.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface UserService {
    int addUser(User user);

    int updateUser(User user);

    User queryUserById(String id);
    User queryUserByName(String userName);
    List<User> queryAllUser();
    User queryUserInfoById(String id);//查询用户信息，包括密码账户等，不包括含有那些item等
    List<User> queryAllUserInfo();//查询所有用户信息，包括密码账户等，不包括含有那些item等

    int deleteUserById(String id);
    int deleteUserByName(String userName);

    boolean login(String tid, String password);
}
