package com.advdsp.service.manager.service;

import com.advdsp.service.manager.db.dao.AdvConfigDao;
import com.advdsp.service.manager.db.dto.AdvConfigDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class AdvConfigService {
    @Autowired
    private AdvConfigDao advConfigDao;

    public void saveObject(AdvConfigDto advConfigDto) {
        advConfigDao.saveObject(advConfigDto);
    }

    public AdvConfigDto getObjectByID(Integer id) {
        return (AdvConfigDto) advConfigDao.getObjectByID(id);
    }

    public void updateObjectById(AdvConfigDto dto) {
        advConfigDao.updateObjectById(dto);
    }

    public List<AdvConfigDto> getAll() {
        return advConfigDao.getAll();
    }

    public void deleteById(Integer customer_id) {
        advConfigDao.deleteById(customer_id);
    }
}
