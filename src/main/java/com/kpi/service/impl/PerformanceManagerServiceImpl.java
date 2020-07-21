package com.kpi.service.impl;

import com.kpi.dao.EvidentFileDAO;
import com.kpi.dao.ItemDAO;
import com.kpi.pojo.EvidentFile;
import com.kpi.pojo.Item;
import com.kpi.service.PerformanceManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PerformanceManagerServiceImpl implements PerformanceManagerService {
//    private ItemDAO itemDAO = SpringContextUtil.getBean(ItemDAO.class);
//    private EvidentFileDAO fileDAO = SpringContextUtil.getBean(EvidentFileDAO.class);

    @Autowired
    private ItemDAO itemDAO;

    @Autowired
    private EvidentFileDAO fileDAO;

    @Override
    public void addInfos(Item item, EvidentFile file) {
        itemDAO.addItem(item);
        fileDAO.addFile(file);
    }

    @Override
    public void removeInfos(String itemId, String tid) {
        itemDAO.deleteItemByItemIdAndTid(itemId,tid);
        fileDAO.deleteFileByItemIdAndTid(itemId,tid);
    }
}
