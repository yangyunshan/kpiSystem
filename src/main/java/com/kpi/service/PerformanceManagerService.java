package com.kpi.service;

import com.kpi.pojo.EvidentFile;
import com.kpi.pojo.Item;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface PerformanceManagerService {
    void addInfos(Item item, EvidentFile file);
    void removeInfos(String itemId, String tid);
}
