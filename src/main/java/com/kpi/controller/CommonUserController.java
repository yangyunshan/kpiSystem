package com.kpi.controller;

import com.kpi.model.FileModel;
import com.kpi.model.ItemModel;
import com.kpi.model.ResultModel;
import com.kpi.model.UserModel;
import com.kpi.pojo.EvidentFile;
import com.kpi.pojo.Item;
import com.kpi.pojo.User;
import com.kpi.service.FileService;
import com.kpi.service.ItemService;
import com.kpi.service.PerformanceManagerService;
import com.kpi.service.UserService;
import com.kpi.spring.util.SpringContextUtil;
import com.kpi.util.KpiProUtil;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CommonUserController {
    @ResponseBody
    @RequestMapping(value = "/html/addInfoWithoutFile",method = RequestMethod.POST)
    // 这里的MultipartFile对象变量名跟表单中的file类型的input标签的name相同，所以框架会自动用MultipartFile对象来接收上传过来的文件，当然也可以使用@RequestParam("img")指定其对应的参数名称
    public ResultModel addInfosWithoutFile(HttpServletRequest request, HttpSession session, String itemid, String deptNo, String participantNo, String rank, String count,
                                String description, String score)
            throws Exception {
        String isLogin = (String) session.getAttribute("isLogin");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");
        if (isLogin!=null && isLogin.equals("true")) {
            String tid = (String) session.getAttribute("tid");
//            //删除原来的信息，重新提交（用户多次提交只保留最后一次的信息）
            PerformanceManagerService performanceManagerService = SpringContextUtil.getBean(PerformanceManagerService.class);

            performanceManagerService.removeInfos(itemid,tid);

            Item item = SpringContextUtil.getBean("item");
            String itemname = KpiProUtil.getItemNameByItemId(itemid);
            item.setTid(tid);
            item.setId(itemid);
            item.setName(itemname);
            item.setDeptNo(Integer.parseInt(deptNo));
            item.setParticipantNo(Integer.parseInt(participantNo));
            item.setRank(Integer.parseInt(rank));
            item.setCount(Integer.parseInt(count));
            item.setDescription(description);
            item.setScore(Float.parseFloat(score));

            String projectName = request.getContextPath();
            String path = session.getServletContext().getRealPath("files")+"/"+tid;
            String relatePath = path.substring(path.lastIndexOf("files")+"files".length());

            EvidentFile evidentFile = SpringContextUtil.getBean("file");
            evidentFile.setTid(tid);
            evidentFile.setItemId(itemid);
            evidentFile.setName("~");
            evidentFile.setPath(projectName+"/files"+relatePath+"/~");

            performanceManagerService.addInfos(item,evidentFile);
            resultModel.setFlag("success");
        } else {
            resultModel.setFlag("fail");
        }
        return resultModel;
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "/html/addInfoWithFile",method = RequestMethod.POST)
    // 这里的MultipartFile对象变量名跟表单中的file类型的input标签的name相同，所以框架会自动用MultipartFile对象来接收上传过来的文件，当然也可以使用@RequestParam("img")指定其对应的参数名称
    public ResultModel addInfosWithFile(HttpServletRequest request, MultipartFile[] files,
                                        HttpSession session, String itemid, String deptNo,
                                        String participantNo, String rank, String count,
                         String description, String score)
            throws Exception {

        String isLogin = (String) session.getAttribute("isLogin");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");
        String projectName = request.getContextPath();

        if (isLogin!=null && isLogin.equals("true")) {
            String tid = (String) session.getAttribute("tid");

            //删除原来的信息，重新提交（用户多次提交只保留最后一次的信息）
            PerformanceManagerService performanceManagerService = SpringContextUtil.getBean(PerformanceManagerService.class);
            FileService fileService = SpringContextUtil.getBean(FileService.class);
            List<EvidentFile> temp = fileService.queryFileByItemIdAndTid(itemid,tid);

            String realPath = request.getSession().getServletContext().getRealPath("");

            if (temp!=null) {
                for (EvidentFile file_ss : temp) {
                    String pathTemp = file_ss.getPath();
                    String path1 = pathTemp.substring(pathTemp.indexOf("files"));
                    KpiProUtil.deleteFile(realPath+path1);
                }
            }
            performanceManagerService.removeInfos(itemid,tid);

            //先上传文件，再提交信息
            try {
                EvidentFile evidentFile = SpringContextUtil.getBean("file");

                for (MultipartFile file : files) {
                    if (file.getSize()>0) {
                        String path = session.getServletContext().getRealPath("files")+"/"+tid;
                        String relatePath = path.substring(path.lastIndexOf("files")+"files".length());
                        String fileName = file.getOriginalFilename();

                        String wholeName = path+"/"+fileName;

                        File fileTemp = new File(wholeName);
                        if (!fileTemp.exists()) {
                            fileTemp.mkdirs();
                        }
                        file.transferTo(fileTemp);

                        evidentFile.setTid(tid);
                        evidentFile.setItemId(itemid);
                        evidentFile.setName(fileName);
                        evidentFile.setPath(projectName+"/files"+relatePath+"/"+fileName);
                        fileService.addFile(evidentFile);
                    }
                }

                Item item = SpringContextUtil.getBean("item");
                String itemname = KpiProUtil.getItemNameByItemId(itemid);
                item.setTid(tid);
                item.setId(itemid);
                item.setName(itemname);
                item.setDeptNo(Integer.parseInt(deptNo));
                item.setParticipantNo(Integer.parseInt(participantNo));
                item.setRank(Integer.parseInt(rank));
                item.setCount(Integer.parseInt(count));
                item.setDescription(description);
                item.setScore(Float.parseFloat(score));

                ItemService itemService = SpringContextUtil.getBean(ItemService.class);
                itemService.addItem(item);
            } catch (Exception e) {
                List<EvidentFile> temps = fileService.queryFileByItemIdAndTid(itemid,tid);
                if (temps!=null) {
                    for (EvidentFile file_ss : temp) {
                        String pathTemp = file_ss.getPath();
                        String path1 = pathTemp.substring(pathTemp.indexOf("files"));
                        KpiProUtil.deleteFile(realPath+path1);
                    }
                }
                performanceManagerService.removeInfos(itemid,tid);
            }

            resultModel.setFlag("success");
        } else {
            resultModel.setFlag("fail");
        }
        return resultModel;
    }

    /**
     * 普通用户获取自己的信息
     * */
    @ResponseBody
    @RequestMapping(value = "/html/getUserInfo",method = RequestMethod.GET)
    public UserModel getUserInfo(HttpSession session)
            throws Exception {
        String isLogin = (String) session.getAttribute("isLogin");
        UserModel userModel = SpringContextUtil.getBean("userModel");
        if (isLogin!=null && isLogin.equals("true")) {
            String tid = (String) session.getAttribute("tid");
            UserService userService = SpringContextUtil.getBean(UserService.class);
            User user = userService.queryUserInfoById(tid);
            userModel.setId(tid);
            userModel.setName(user.getName());
            userModel.setPassword(user.getPassword());
            userModel.setPhone(user.getPhone());
            userModel.setEmail(user.getEmail());
        }
        return userModel;
    }

    @ResponseBody
    @RequestMapping(value = "/html/updateUserInfo",method = RequestMethod.POST)
    public ResultModel updateUserInfo(HttpSession session, @RequestBody UserModel user) {
        String isLogin = (String) session.getAttribute("isLogin");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");

        if (isLogin!=null && isLogin.equals("true")) {
            String tid = (String) session.getAttribute("tid");
            UserService userService = SpringContextUtil.getBean(UserService.class);
            User teacher = userService.queryUserInfoById(tid);
            teacher.setPassword(user.getPassword());
            teacher.setPhone(user.getPhone());
            teacher.setEmail(user.getEmail());
            userService.updateUser(teacher);
            resultModel.setFlag("success");
        } else {
            resultModel.setFlag("fail");
        }
        return resultModel;
    }

    @ResponseBody
    @RequestMapping(value = "/html/getSumScore",method = RequestMethod.POST)
    public ResultModel getSumScore(HttpSession session) {
        String isLogin = (String) session.getAttribute("isLogin");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");
        if (isLogin!=null && isLogin.equals("true")) {
            String tid = (String) session.getAttribute("tid");
            UserService userService = SpringContextUtil.getBean(UserService.class);
            User user = userService.queryUserById(tid);
            Map<String, Object> info = KpiProUtil.getAllInfosOfUser(user);
            Float score = (Float) info.get("sScore");
            resultModel.setFlag(score.toString());
        } else {
            resultModel.setFlag("0");
        }
        return resultModel;
    }

    @ResponseBody
    @RequestMapping(value = "/html/initCommonUserInfo",method = RequestMethod.GET)
    public Map<String, Object> initCommonUserInfo(HttpSession session) {
        String isLogin = (String) session.getAttribute("isLogin");
        Map<String,Object> userInfo = new HashMap<>();
        if (isLogin!=null && isLogin.equals("true")) {
            String tid = (String)session.getAttribute("tid");
            UserService userService = SpringContextUtil.getBean(UserService.class);
            User user = userService.queryUserById(tid);
            userInfo = KpiProUtil.getAllInfosOfUser(user);
        } else {
            userInfo = null;
        }
        return userInfo;
    }

    /**
     * 用户自己修改绩效信息
     * */
    @ResponseBody
    @RequestMapping(value = "/html/updateInfoWithoutFileByCommon",method = RequestMethod.POST)
    // 这里的MultipartFile对象变量名跟表单中的file类型的input标签的name相同，所以框架会自动用MultipartFile对象来接收上传过来的文件，当然也可以使用@RequestParam("img")指定其对应的参数名称
    public ResultModel updateInfosWithoutFile(HttpSession session, String itemid, String deptNo, String participantNo, String rank, String count,
                                              String description, String score)
            throws Exception {
        String isLogin = (String) session.getAttribute("isLogin");
        String role = (String) session.getAttribute("role");
        String tid = (String) session.getAttribute("tid");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");
        if (isLogin!=null && isLogin.equals("true") && role.equals("0")) {
            String itemname = KpiProUtil.getItemNameByItemId(itemid);
            Item item = SpringContextUtil.getBean("item");
            item.setTid(tid);
            item.setId(itemid);
            item.setName(itemname);
            item.setDeptNo(Integer.parseInt(deptNo));
            item.setParticipantNo(Integer.parseInt(participantNo));
            item.setRank(Integer.parseInt(rank));
            item.setCount(Integer.parseInt(count));
            item.setDescription(description);
            item.setScore(Float.parseFloat(score));

            ItemService itemService = SpringContextUtil.getBean(ItemService.class);
            itemService.updateItem(item);
            resultModel.setFlag("success");
        } else {
            resultModel.setFlag("fail");
        }

        return resultModel;
    }

    @ResponseBody
    @RequestMapping(value = "/html/deleteData",method = RequestMethod.POST)
    public ResultModel deleteData(HttpServletRequest request, HttpSession session, @RequestBody ItemModel itemModel)
            throws Exception {
        String isLogin = (String) session.getAttribute("isLogin");
        String role = (String) session.getAttribute("role");
        String tid = (String) session.getAttribute("tid");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");
        if (isLogin!=null && isLogin.equals("true") && role.equals("0")) {
            FileService fileService = SpringContextUtil.getBean(FileService.class);
            List<EvidentFile> files = fileService.queryFileByItemIdAndTid(itemModel.getItemid(),tid);
            String realPath = request.getSession().getServletContext().getRealPath("");
//首先删除文件
            if (files!=null) {
                for (EvidentFile file_ss : files) {
                    String pathTemp = file_ss.getPath();
                    String path1 = pathTemp.substring(pathTemp.indexOf("files"));
                    KpiProUtil.deleteFile(realPath+path1);
                }
            }

            PerformanceManagerService performanceManagerService = SpringContextUtil.getBean(PerformanceManagerService.class);
            performanceManagerService.removeInfos(itemModel.getItemid(),tid);
            resultModel.setFlag("success");
        } else {
            resultModel.setFlag("fail");
        }
        return resultModel;
    }

    @ResponseBody
    @RequestMapping(value = "/html/freshItem",method = RequestMethod.POST)
    public ResultModel freshItem(HttpServletRequest request, HttpSession session, @RequestBody ItemModel itemModel)
            throws Exception {
        String isLogin = (String) session.getAttribute("isLogin");
        String role = (String) session.getAttribute("role");
        String tid = (String) session.getAttribute("tid");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");
        if (isLogin != null && isLogin.equals("true") && role.equals("0")) {
            ItemService itemService = SpringContextUtil.getBean(ItemService.class);
            Item item = itemService.queryItemByItemIdAndTid(itemModel.getItemid(),tid);
            if (item!=null) {
                resultModel.setFlag("1");
            } else {
                resultModel.setFlag("0");
            }
        }
        return resultModel;
    }

}
