package com.kpi.dao;

import com.kpi.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDAO {
    int addUser(@Param("user") User user);

    int updateUser(@Param("user") User user);

    User queryUserById(@Param("id") String id);
    User queryUserByName(@Param("userName") String userName);
    List<User> queryAllUser();
    User queryUserInfoById(@Param("id")String id);//查询用户信息，包括密码账户等，不包括含有那些item等
    List<User> queryAllUserInfo();//查询所有用户信息，包括密码账户等，不包括含有那些item等

    int deleteUserById(@Param("id") String id);
    int deleteUserByName(@Param("userName") String userName);
}
