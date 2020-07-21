package com.kpi.controller;

import com.kpi.model.ItemModel;
import com.kpi.model.ResultModel;
import com.kpi.model.Temp;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@Controller
public class AdminController {
    @ResponseBody
    @RequestMapping(value = "/html/addUser",method = RequestMethod.POST)
    public ResultModel addUser(HttpSession session, @RequestBody UserModel user) {
        String isLogin = (String) session.getAttribute("isLogin");
        String role = (String)session.getAttribute("role");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");
        if (isLogin!=null && isLogin.equals("true") && role.equals("1")) {
            UserService userService = SpringContextUtil.getBean(UserService.class);
            User teacher = SpringContextUtil.getBean("user");
            teacher.setId(user.getId());
            teacher.setName(user.getName());
            teacher.setSex(user.getSex());
            teacher.setPassword(user.getPassword());
            teacher.setPhone(user.getPhone());
            teacher.setEmail(user.getEmail());
            teacher.setRole(user.getRole());

            userService.addUser(teacher);
            resultModel.setFlag("success");
        } else {
            resultModel.setFlag("fail");
        }

        return resultModel;
    }

    @ResponseBody
    @RequestMapping(value = "/html/updateUser",method = RequestMethod.POST)
    public ResultModel updateUser(HttpSession session, @RequestBody UserModel user) {
        String isLogin = (String) session.getAttribute("isLogin");
        String role = (String)session.getAttribute("role");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");
        if (isLogin!=null && isLogin.equals("true") && role.equals("1")) {
            UserService userService = SpringContextUtil.getBean(UserService.class);
            User teacher = userService.queryUserInfoById(user.getId());
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
    @RequestMapping(value = "/html/removeUser",method = RequestMethod.POST)
    public ResultModel removeUser(HttpSession session, @RequestBody UserModel user) {
        String isLogin = (String) session.getAttribute("isLogin");
        String role = (String)session.getAttribute("role");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");
        if (isLogin!=null && isLogin.equals("true") && role.equals("1")) {
            String tid = user.getId();
            UserService userService = SpringContextUtil.getBean(UserService.class);
            ItemService itemService = SpringContextUtil.getBean(ItemService.class);
            FileService fileService = SpringContextUtil.getBean(FileService.class);
            userService.deleteUserById(tid);
            itemService.deleteItemByTid(tid);

            //删除附件
            List<EvidentFile> files = fileService.queryFileByTid(tid);
            for (EvidentFile file :files) {
                String filePath = file.getPath();
                KpiProUtil.deleteFile(filePath);
            }

            fileService.deleteFileByTid(tid);

            resultModel.setFlag("success");
        } else {
            resultModel.setFlag("fail");
        }

        return resultModel;
    }

    @ResponseBody
    @RequestMapping(value = "/html/getSpecifyUserInfo",method = RequestMethod.POST)
    public Map<String, Object> getSpecifyUserInfo(HttpSession session, @RequestBody UserModel userModel) {
        String isLogin = (String) session.getAttribute("isLogin");
        String role = (String)session.getAttribute("role");
        Map<String, Object> info = new HashMap<>();

        if (isLogin!=null && isLogin.equals("true") && role.equals("1")) {
            UserService userService = SpringContextUtil.getBean(UserService.class);
            User user = userService.queryUserById(userModel.getId());
            System.out.println(user.getItems().size());
            info = KpiProUtil.getAllInfosOfUser(user);
            System.out.println(info.toString());
        }
        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/html/getAllUserInfo",method = RequestMethod.POST)
    public List<Object> getAllUserInfo(HttpSession session) {
        String isLogin = (String) session.getAttribute("isLogin");
        String role = (String)session.getAttribute("role");
        List<Object> list = new ArrayList<>();

        if (isLogin!=null && isLogin.equals("true") && role.equals("1")) {
            UserService userService = SpringContextUtil.getBean(UserService.class);
            List<User> users = userService.queryAllUser();
            for (User user : users) {
                Map<String, Object> info = KpiProUtil.getUserBriefInfo(user);
                list.add(info);
            }

        }
        return list;
    }


    @ResponseBody
    @RequestMapping(value = "/html/verifyInfo",method = RequestMethod.POST)
    public ResultModel verifyInfo(HttpSession session, @RequestBody UserModel userModel) {
        String isLogin = (String) session.getAttribute("isLogin");
        String role = (String)session.getAttribute("role");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");

        if (isLogin!=null && isLogin.equals("true") && role.equals("1")) {
            UserService userService = SpringContextUtil.getBean(UserService.class);
            User user = userService.queryUserInfoById(userModel.getId());
            user.setStatus(1);
            userService.updateUser(user);
            resultModel.setOther("1");
        }
        return resultModel;
    }

    @ResponseBody
    @RequestMapping(value = "/html/unverifyInfo",method = RequestMethod.POST)
    public ResultModel unverifyInfo(HttpSession session, @RequestBody UserModel userModel) {
        String isLogin = (String) session.getAttribute("isLogin");
        String role = (String)session.getAttribute("role");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");

        if (isLogin!=null && isLogin.equals("true") && role.equals("1")) {
            UserService userService = SpringContextUtil.getBean(UserService.class);
            User user = userService.queryUserInfoById(userModel.getId());
            user.setStatus(0);
            userService.updateUser(user);
            resultModel.setOther("0");
        }

        return resultModel;
    }

    /**
     * 获取用户信息，仅仅包含用户本身的登陆信息等
     * */
    @ResponseBody
    @RequestMapping(value = "/html/getAllUser",method = RequestMethod.POST)
    public List<Object> getAllUser(HttpSession session) {
        String isLogin = (String) session.getAttribute("isLogin");
        String role = (String)session.getAttribute("role");
        List<Object> list = new ArrayList<>();

        if (isLogin!=null && isLogin.equals("true") && role.equals("1")) {
            UserService userService = SpringContextUtil.getBean(UserService.class);
            List<User> users = userService.queryAllUserInfo();
            for (User user : users) {
                if (user.getRole()==0) {
                    Map<String,Object> info = KpiProUtil.getUser(user);
                    list.add(info);
                }
            }
        }
        return list;
    }

    /**
     * 供管理员提交其他项分数
     * */
    @ResponseBody
    @RequestMapping(value = "/html/updateOtherInfo",method = RequestMethod.POST)
    public ResultModel updateOtherInfo(HttpSession session, @RequestBody UserModel userModel)
            throws Exception {
        String isLogin = (String) session.getAttribute("isLogin");
        String role = (String)session.getAttribute("role");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");
        if (isLogin!=null && isLogin.equals("true") && role.equals("1")) {
            ItemService itemService = SpringContextUtil.getBean(ItemService.class);
            Item item = itemService.queryItemByItemIdAndTid("d1",userModel.getId());
            if (item!=null) {
                item.setScore(userModel.getScore());
                itemService.updateItem(item);
            } else {

            }
            resultModel.setFlag("1");
        } else {
            resultModel.setFlag("0");
        }

        return resultModel;
    }

    /**
     * 查询所有用户总分加起来的值
     * */
    @ResponseBody
    @RequestMapping(value = "/html/getAllScore",method = RequestMethod.POST)
    public ResultModel getAllScore(HttpSession session)
            throws Exception {
        String isLogin = (String) session.getAttribute("isLogin");
        String role = (String)session.getAttribute("role");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");
        if (isLogin!=null && isLogin.equals("true") && role.equals("1")) {
            UserService userService = SpringContextUtil.getBean(UserService.class);
            List<User> users = userService.queryAllUser();
            float sum = 0;
            for (User user : users) {
                Map<String, Object> info = KpiProUtil.getUserBriefInfo(user);
                sum += (Float) info.get("sumScore");
            }
            resultModel.setOther(sum+"");
        }

        return resultModel;
    }

    /**
     * 管理员修改用户绩效信息
     * */
    @ResponseBody
    @RequestMapping(value = "/html/addInfoWithFileByAdmin",method = RequestMethod.POST)
    // 这里的MultipartFile对象变量名跟表单中的file类型的input标签的name相同，所以框架会自动用MultipartFile对象来接收上传过来的文件，当然也可以使用@RequestParam("img")指定其对应的参数名称
    public ResultModel addInfoWithFileByAdmin(HttpServletRequest request, HttpSession session, MultipartFile[] files,
                                              String itemid, String deptNo, String participantNo, String rank, String count,
                                           String description, String score, String id)
            throws Exception {
        String isLogin = (String) session.getAttribute("isLogin");
        String role = (String) session.getAttribute("role");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");
        String projectName = request.getContextPath();
        if (isLogin!=null && isLogin.equals("true") && role.equals("1")) {
            String tid = id;
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

    @ResponseBody
    @RequestMapping(value = "/html/addInfoWithoutFileByAdmin",method = RequestMethod.POST)
    public ResultModel addInfoWithoutFileByAdmin(HttpServletRequest request, HttpSession session,
                                              String itemid, String deptNo, String participantNo, String rank, String count,
                                              String description, String score, String id)
            throws Exception {
        String isLogin = (String) session.getAttribute("isLogin");
        String role = (String) session.getAttribute("role");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");
        String projectName = request.getContextPath();
        if (isLogin!=null && isLogin.equals("true") && role.equals("1")) {
            String tid = id;
            //删除原来的信息，重新提交（用户多次提交只保留最后一次的信息）
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

    @ResponseBody
    @RequestMapping(value = "/html/updateInfoWithoutFileByAdmin",method = RequestMethod.POST)
    // 这里的MultipartFile对象变量名跟表单中的file类型的input标签的name相同，所以框架会自动用MultipartFile对象来接收上传过来的文件，当然也可以使用@RequestParam("img")指定其对应的参数名称
    public ResultModel updateInfoWithoutFileByAdmin(HttpServletRequest request, HttpSession session,
                                              String itemid, String deptNo, String participantNo, String rank, String count,
                                              String description, String score, String id)
            throws Exception {
        String isLogin = (String) session.getAttribute("isLogin");
        String role = (String) session.getAttribute("role");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");
        String projectName = request.getContextPath();
        if (isLogin!=null && isLogin.equals("true") && role.equals("1")) {
            String tid = id;
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

//管理员删除信息
    @ResponseBody
    @RequestMapping(value = "/html/deleteDataByAdmin",method = RequestMethod.POST)
    public ResultModel deleteDataByAdmin(HttpServletRequest request, HttpSession session, @RequestBody Temp temp)
            throws Exception {
        String isLogin = (String) session.getAttribute("isLogin");
        String role = (String) session.getAttribute("role");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");
        if (isLogin != null && isLogin.equals("true") && role.equals("1")) {
            FileService fileService = SpringContextUtil.getBean(FileService.class);
            List<EvidentFile> files = fileService.queryFileByItemIdAndTid(temp.getItemid(),temp.getId());
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
            performanceManagerService.removeInfos(temp.getItemid(),temp.getId());
            resultModel.setFlag("success");
        } else {
            resultModel.setFlag("fail");
        }
        return resultModel;
    }

    @ResponseBody
    @RequestMapping(value = "/html/freshItemByAdmin",method = RequestMethod.POST)
    public ResultModel freshItemByAdmin(HttpServletRequest request, HttpSession session, @RequestBody Temp temp)
            throws Exception {
        String isLogin = (String) session.getAttribute("isLogin");
        String role = (String) session.getAttribute("role");
        ResultModel resultModel = SpringContextUtil.getBean("resultModel");
        if (isLogin != null && isLogin.equals("true") && role.equals("1")) {
            ItemService itemService = SpringContextUtil.getBean(ItemService.class);
            Item item = itemService.queryItemByItemIdAndTid(temp.getItemid(),temp.getId());
            if (item!=null) {
                resultModel.setFlag("1");
            } else {
                resultModel.setFlag("0");
            }
        }
        return resultModel;
    }

    @ResponseBody
    @RequestMapping(value = "/html/getLoginInfoByAdmin",method = RequestMethod.POST)
    public UserModel getLoginInfoByAdmin(HttpSession session,@RequestBody UserModel userModelTemp) {
        String isLogin = (String) session.getAttribute("isLogin");
        UserModel userModel = SpringContextUtil.getBean("userModel");
        String role = (String) session.getAttribute("role");
        if (isLogin!=null && isLogin.equals("true") && role.equals("1")) {
            String tid = userModelTemp.getId();
            UserService userService = SpringContextUtil.getBean(UserService.class);
            User user = userService.queryUserInfoById(tid);
            userModel.setId(tid);
            userModel.setName(user.getName());
        }
        return userModel;
    }
}
