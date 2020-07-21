package com.kpi.dao;

import com.kpi.pojo.EvidentFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EvidentFileDAO {
    int addFile(@Param("file") EvidentFile file);

    int updateFile(@Param("file") EvidentFile file);

    List<EvidentFile> queryFileByTid(@Param("tid") String tid);
    EvidentFile queryFileById(@Param("id") int id);
    List<EvidentFile> queryFileByItemId(@Param("itemId") String id);
    List<EvidentFile> queryFileByItemIdAndTid(@Param("itemId")String itemId,@Param("tid")String tid);

    int deleteFileById(@Param("id") int id);
    int deleteFileByTid(@Param("tid") String tid);
    int deleteFileByItemId(@Param("itemId") String id);
    int deleteFileByItemIdAndTid(@Param("itemId") String id, @Param("tid") String tid);
}
