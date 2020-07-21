package com.kpi.dao;

import com.kpi.pojo.Item;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemDAO {
    int addItem(@Param("item") Item item);

    int updateItem(@Param("item") Item item);

    List<Item> queryItemById(@Param("itemId") String itemId);
    List<Item> queryItemByName(@Param("itemName") String itemName);
    List<Item> queryItemByTid(@Param("tid") String tid);
    float querySumScoreByTid(@Param("tid") String tid);
    Item queryItemByItemIdAndTid(@Param("itemId")String itemId,@Param("tid")String tid);

    int deleteItemById(@Param("itemId") String itemId);
    int deleteItemByName(@Param("itemName") String itemName);
    int deleteItemByTid(@Param("tid") String tid);
    int deleteItemByItemIdAndTid(@Param("itemId") String id, @Param("tid") String tid);
}
