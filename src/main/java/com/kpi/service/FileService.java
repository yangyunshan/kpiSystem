package com.kpi.service;

import com.kpi.pojo.EvidentFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface FileService {
    int addFile(EvidentFile file);

    int updateFile(EvidentFile file);

    List<EvidentFile> queryFileByTid(String tid);
    EvidentFile queryFileById(int id);
    List<EvidentFile> queryFileByItemId(String id);
    List<EvidentFile> queryFileByItemIdAndTid(@Param("itemId")String itemId, @Param("tid")String tid);

    int deleteFileById(int id);
    int deleteFileByTid(String tid);
    int deleteFileByItemId(String id);
    int deleteFileByItemIdAndTid(String id, String tid);
}
