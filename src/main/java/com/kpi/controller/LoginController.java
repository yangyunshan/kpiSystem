package com.kpi.controller;

import com.kpi.model.ResultModel;
import com.kpi.model.UserModel;
import com.kpi.pojo.User;
import com.kpi.service.UserService;
import com.kpi.spring.util.SpringContextUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
public class LoginController {
    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResultModel login(@RequestBody UserModel userModel, HttpSession session) {
        UserService userService = SpringContextUtil.getBean(UserService.class);
        boolean isLogin = userService.login(userModel.getId(),userModel.getPassword());
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");
        int role = userService.queryUserInfoById(userModel.getId()).getRole();
        if (isLogin) {
            User user = userService.queryUserInfoById(userModel.getId());
            String username = userService.queryUserInfoById(userModel.getId()).getName();
            session.setAttribute("username",username);
            session.setAttribute("role",role+"");
            session.setAttribute("isLogin","true");
            session.setAttribute("tid",userModel.getId());
            resultModel.setFlag("success");
        } else {
            resultModel.setFlag("fail");
        }
        resultModel.setOther(role+"");
        return resultModel;
    }

    @ResponseBody
    @RequestMapping(value = "/html/getLoginInfo",method = RequestMethod.GET)
    public UserModel getLoginInfo(HttpSession session) {
        String isLogin = (String) session.getAttribute("isLogin");
        UserModel userModel = SpringContextUtil.getBean("userModel");
        if (isLogin!=null && isLogin.equals("true")) {
            String tid = (String) session.getAttribute("tid");
            String username = (String) session.getAttribute("username");
            userModel.setId(tid);
            userModel.setName(username);
            System.out.println(userModel.getName());
        }
        return userModel;
    }

    @ResponseBody
    @RequestMapping(value = "/html/logout",method = RequestMethod.POST)
    public ResultModel logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");
        String isLogin = (String) session.getAttribute("isLogin");
        if (isLogin!=null && isLogin.equals("true")) {
            session.removeAttribute("isLogin");
            session.removeAttribute("username");
            session.removeAttribute("role");
            session.removeAttribute("tid");
            resultModel.setFlag("1");
            resultModel.setOther(request.getRequestURL().toString());
//            response.sendRedirect(request.getContextPath()+"/index.html");
        } else {
            resultModel.setFlag("0");
        }
        return resultModel;
    }
}
