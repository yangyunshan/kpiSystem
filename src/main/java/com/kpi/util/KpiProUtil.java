package com.kpi.util;

import com.kpi.pojo.EvidentFile;
import com.kpi.pojo.Item;
import com.kpi.pojo.User;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KpiProUtil {
    public static String getItemNameByItemId(String itemId) {
        if (itemId.equals("a1")) {
            return "年度教学评估排名";
        } else if (itemId.equals("a2")) {
            return "出版教材";
        }else if (itemId.equals("a3")) {
            return "教学项目";
        } else if (itemId.equals("a4")) {
            return "教学比赛";
        } else if (itemId.equals("a5")) {
            return "教学名师";
        } else if (itemId.equals("a6")) {
            return "校级教学成果奖";
        } else if (itemId.equals("a7")) {
            return "课程建设";
        } else if (itemId.equals("a8")) {
            return "班主任";
        } else if (itemId.equals("a9")) {
            return "教学论文";
        } else if (itemId.equals("b1")) {
            return "听课";
        } else if (itemId.equals("b2")) {
            return "组织课程教研讨论";
        } else if (itemId.equals("b3")) {
            return "参与课程教研讨论";
        } else if (itemId.equals("b4")) {
            return "野外实习教学-安全员老师";
        } else if (itemId.equals("b5")) {
            return "野外实习教学-主讲老师";
        } else if (itemId.equals("b6")) {
            return "走访/约谈学生";
        } else if (itemId.equals("b7")) {
            return "学生班会/学务指导";
        } else if (itemId.equals("b8")) {
            return "帮扶困难学生";
        } else if (itemId.equals("b9")) {
            return "指导学生学术成果发表论文";
        } else if (itemId.equals("b10")) {
            return "指导学生相关竞赛";
        } else if (itemId.equals("b11")) {
            return "学院产学研";
        } else if (itemId.equals("b12")) {
            return "校级以上创新创业项目";
        } else if (itemId.equals("b13")) {
            return "指导学生/班级获得荣誉";
        } else if (itemId.equals("b14")) {
            return "系集体活动/系务会";
        } else if (itemId.equals("b15")) {
            return "系教学/人才培养事务";
        } else if (itemId.equals("c1")) {
            return "国家级教学成果奖";
        } else if (itemId.equals("c2")) {
            return "省部级教学成果奖";
        } else if (itemId.equals("c3")) {
            return "教学名师";
        } else if (itemId.equals("c4")) {
            return "教学团队";
        } else if (itemId.equals("c5")) {
            return "规划教材";
        } else if (itemId.equals("c6")) {
            return "资源共享课、视频公开课、MOOC课程";
        } else if (itemId.equals("c7")) {
            return "教改项目";
        } else if (itemId.equals("c8")) {
            return "指导学生教育教学奖励国家级";
        } else if (itemId.equals("c9")) {
            return "指导学生教育教学奖励省部级、学会";
        } else {
            return "其他奖项";
        }
    }

    /**
     * 查询用户的所有信息以Map表示
     * */
    public static Map<String,Object> getAllInfosOfUser(User user) {
        Map<String,Object> infos = new HashMap<>();
        String tid = user.getId();
        String username = user.getName();
        String sex = user.getSex();
        String phone = user.getPhone();
        String email = user.getEmail();
        int role = user.getRole();
        int status = user.getStatus();
        float sScore = 0;
        List<Item> items = user.getItems();
        //存储用个用户下所有的Item信息
        List<Object> list = new ArrayList<>();

        for (Item item : items) {
            Map<String, Object> itemInfo = getInfosOfItem(item);
            sScore += item.getScore();
            list.add(itemInfo);
        }

        infos.put("tid",tid);
        infos.put("name",username);
        infos.put("sex",sex);
        infos.put("phone",phone);
        infos.put("email",email);
        infos.put("role",role);
        infos.put("score",sScore);
        infos.put("items",list);
        infos.put("status",status);

        return infos;
    }


    public static Map<String, Object> getInfosOfItem(Item item) {
        String itemid = "";
        String itemname = "";
        int item_DeptNo;
        int item_ParticipantNo;
        int item_Rank;
        int item_Count;
        String item_Description = "";
        float item_Score;
        List<String> item_FileName = new ArrayList<>();
        List<String> item_FilePath = new ArrayList<>();

        //存储一条item中的数据
        Map<String, Object> itemInfos = new HashMap<>();

        itemid = item.getId();
        itemname = item.getName();
        item_DeptNo = item.getDeptNo();
        item_ParticipantNo = item.getParticipantNo();
        item_Rank = item.getRank();
        item_Count = item.getCount();
        item_Description = item.getDescription();
        item_Score = item.getScore();
        List<EvidentFile> files = item.getFile();

        for (EvidentFile file : files) {
            item_FileName.add(file.getName());
            item_FilePath.add(file.getPath());
        }

//        item_FilePath = file.getFilePath();
        itemInfos.put("id",itemid);
        itemInfos.put("name",itemname);
        itemInfos.put("deptNo",item_DeptNo);
        itemInfos.put("participantNo",item_ParticipantNo);
        itemInfos.put("rank",item_Rank);
        itemInfos.put("count",item_Count);
        itemInfos.put("description",item_Description);
        itemInfos.put("score",item_Score);
        itemInfos.put("fileName",item_FileName);
        itemInfos.put("filePath",item_FilePath);
        return itemInfos;
    }

    /**
     * 删除磁盘文件
     * */
    public static boolean deleteFile(String filePath) {
        boolean flag = false;
        try {
            File file = new File(filePath);
            flag = file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return flag;
        }
    }

    /**
     * 获得User的简要信息
     * */
    public static Map<String,Object> getUserBriefInfo(User user) {
        Map<String,Object> result = new HashMap<>();

        if (user!=null) {
            String tid = user.getId();
            String username = user.getName();
            int status = user.getStatus();
            List<Item> items = user.getItems();
            Map<String,Object> itemResult = getItemBriefInfo(items);
            String kpi1Name = (String) itemResult.get("itemA");
            String kpi1Score = itemResult.get("sumA")+"";
            String kpi2Name = (String) itemResult.get("itemB");
            String kpi2Score = itemResult.get("sumB")+"";
            String kpi3Name = (String) itemResult.get("itemC");
            String kpi3Score = itemResult.get("sumC")+"";
            String kpi4Name = (String) itemResult.get("itemD");
            String kpi4Score = itemResult.get("sumD")+"";

            String b4Score = itemResult.get("b4Score")+"";
            String b5Score = itemResult.get("b5Score")+"";

            float sumScore = Float.parseFloat(kpi1Score)+Float.parseFloat(kpi2Score)+
                    Float.parseFloat(kpi3Score)+Float.parseFloat(kpi4Score);

            result.put("tid",tid);
            result.put("name",username);
            result.put("kpi1Name",kpi1Name);
            result.put("kpi1Score",kpi1Score);
            result.put("kpi2Name",kpi2Name);
            result.put("kpi2Score",kpi2Score);
            result.put("kpi3Name",kpi3Name);
            result.put("kpi3Score",kpi3Score);
            result.put("kpi4Name",kpi4Name);
            result.put("kpi4Score",kpi4Score);
            result.put("sumScore",sumScore);
            result.put("status",status);

            result.put("b4Score",b4Score);
            result.put("b5Score",b5Score);
            result.put("score",sumScore-(Float.parseFloat(b4Score)+Float.parseFloat(b5Score))/2);
        }

        return result;
    }

    /**
     * 处理Item类信息，并汇总
     * */
    public static Map<String,Object> getItemBriefInfo(List<Item> items) {
        Map<String,Object> result = new HashMap<>();
        String itemA = "";
        float sumA = 0;
        String itemB = "";
        float sumB = 0;
        String itemC = "";
        float sumC = 0;
        String itemD = "";
        float sumD = 0;

        float b4Score = 0;
        float b5Score = 0;

        for (Item item : items) {
            if (item.getId().startsWith("a")) {
                itemA += item.getName()+"||";
                sumA += item.getScore();
            } else if (item.getId().startsWith("b")) {
                itemB += item.getName()+"||";
                sumB += item.getScore();
                if (item.getId().equals("b4")) {
                    b4Score = item.getScore();
                }
                if (item.getId().equals("b5")) {
                    b5Score = item.getScore();
                }
            } else if (item.getId().startsWith("c")) {
                itemC += item.getName()+"||";
                sumC += item.getScore();
            } else {
                itemD += item.getName()+"||";
                sumD += item.getScore();
            }
        }

        result.put("itemA",itemA);
        result.put("sumA",sumA);
        result.put("itemB",itemB);
        result.put("sumB",sumB);
        result.put("itemC",itemC);
        result.put("sumC",sumC);
        result.put("itemD",itemD);
        result.put("sumD",sumD);
        result.put("b4Score",b4Score);
        result.put("b5Score",b5Score);

        return result;
    }

    /**
     * 查询用户的所有信息以Map表示,仅仅包含用户的登陆信息
     * */
    public static Map<String, Object> getUser(User user) {
        Map<String, Object> result = new HashMap<>();

        String tid = user.getId();
        String username = user.getName();
        String sex = user.getSex();
        String phone = user.getPhone();
        String email = user.getEmail();
        int role = user.getRole();
        int status = user.getStatus();

        result.put("tid",tid);
        result.put("name",username);
        result.put("sex",sex);
        result.put("phone",phone);
        result.put("email",email);
        result.put("role",role);
        result.put("status",status);

        return result;
    }

}
