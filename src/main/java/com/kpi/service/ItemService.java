package com.kpi.service;

import com.kpi.pojo.Item;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface ItemService {
    int addItem(Item item);

    int updateItem(Item item);

    List<Item> queryItemById(String itemId);
    List<Item> queryItemByName(String itemName);
    List<Item> queryItemByTid(String tid);
    float querySumScoreByTid(String tid);
    Item queryItemByItemIdAndTid(@Param("itemId")String itemId, @Param("tid")String tid);

    int deleteItemById(String itemId);
    int deleteItemByName(String itemName);
    int deleteItemByTid(String tid);
    int deleteItemByItemIdAndTid(String id, String tid);
}
