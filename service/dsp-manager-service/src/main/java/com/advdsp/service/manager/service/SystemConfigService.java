package com.advdsp.service.manager.service;

import com.advdsp.service.manager.db.dao.SystemConfigDao;
import com.advdsp.service.manager.db.dto.SystemConfigDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class SystemConfigService {
    @Autowired
    private SystemConfigDao systemConfigDao;

    public void saveObject(SystemConfigDto systemConfigDto) {
        systemConfigDao.saveObject(systemConfigDto);
    }

    public SystemConfigDto getObjectByID(Integer id) {
        return (SystemConfigDto) systemConfigDao.getObjectByID(id);
    }

    public void updateObjectById(SystemConfigDto dto){
        systemConfigDao.updateObjectById(dto);
    }

    public List<SystemConfigDto> getAll(){
        return systemConfigDao.getAll();
    }

    public void deleteById(Integer customer_id){
        systemConfigDao.deleteById(customer_id);
    }
}
