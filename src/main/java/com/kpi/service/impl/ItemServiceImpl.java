package com.kpi.service.impl;

import com.kpi.dao.ItemDAO;
import com.kpi.pojo.Item;
import com.kpi.service.ItemService;
import com.kpi.spring.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {
//    private ItemDAO itemDAO = SpringContextUtil.getBean(ItemDAO.class);
    @Autowired
    private ItemDAO itemDAO;

    @Override
    public int addItem(Item item) {
        return itemDAO.addItem(item);
    }

    @Override
    public int updateItem(Item item) {
        return itemDAO.updateItem(item);
    }

    @Override
    public List<Item> queryItemById(String itemId) {
        return itemDAO.queryItemById(itemId);
    }

    @Override
    public List<Item> queryItemByName(String itemName) {
        return itemDAO.queryItemByName(itemName);
    }

    @Override
    public List<Item> queryItemByTid(String tid) {
        return itemDAO.queryItemByTid(tid);
    }

    @Override
    public float querySumScoreByTid(String tid) {
        return itemDAO.querySumScoreByTid(tid);
    }

    @Override
    public Item queryItemByItemIdAndTid(String itemId, String tid) {
        return itemDAO.queryItemByItemIdAndTid(itemId,tid);
    }

    @Override
    public int deleteItemById(String itemId) {
        return itemDAO.deleteItemById(itemId);
    }

    @Override
    public int deleteItemByName(String itemName) {
        return itemDAO.deleteItemByName(itemName);
    }

    @Override
    public int deleteItemByTid(String tid) {
        return itemDAO.deleteItemByTid(tid);
    }

    @Override
    public int deleteItemByItemIdAndTid(String id, String tid) {
        return itemDAO.deleteItemByItemIdAndTid(id,tid);
    }
}
