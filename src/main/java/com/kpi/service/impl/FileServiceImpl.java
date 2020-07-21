package com.kpi.service.impl;

import com.kpi.dao.EvidentFileDAO;
import com.kpi.pojo.EvidentFile;
import com.kpi.service.FileService;
import com.kpi.spring.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FileServiceImpl implements FileService {
//    private EvidentFileDAO fileDAO = SpringContextUtil.getBean(EvidentFileDAO.class);

    @Autowired
    private EvidentFileDAO fileDAO;

    @Override
    public int addFile(EvidentFile file) {
        return fileDAO.addFile(file);
    }

    @Override
    public int updateFile(EvidentFile file) {
        return fileDAO.updateFile(file);
    }

    @Override
    public List<EvidentFile> queryFileByTid(String tid) {
        return fileDAO.queryFileByTid(tid);
    }

    @Override
    public EvidentFile queryFileById(int id) {
        return fileDAO.queryFileById(id);
    }

    @Override
    public List<EvidentFile> queryFileByItemId(String id) {
        return fileDAO.queryFileByItemId(id);
    }

    @Override
    public List<EvidentFile> queryFileByItemIdAndTid(String itemId, String tid) {
        return fileDAO.queryFileByItemIdAndTid(itemId,tid);
    }

    @Override
    public int deleteFileById(int id) {
        return fileDAO.deleteFileById(id);
    }

    @Override
    public int deleteFileByTid(String tid) {
        return fileDAO.deleteFileByTid(tid);
    }

    @Override
    public int deleteFileByItemId(String id) {
        return fileDAO.deleteFileByItemId(id);
    }

    @Override
    public int deleteFileByItemIdAndTid(String id, String tid) {
        return fileDAO.deleteFileByItemIdAndTid(id,tid);
    }
}
