package com.advdsp.service.manager.service;

import com.advdsp.service.manager.db.dao.AppConfigDao;
import com.advdsp.service.manager.db.dto.AppConfigDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class AppConfigService {
    @Autowired
    private AppConfigDao appConfigDao;

    public List<AppConfigDto> getAll() {
        return appConfigDao.getAll();
    }

    public List<AppConfigDto> getNoAdxConfig(Integer channel_id, Integer app_id) {
        return appConfigDao.getNoAdxConfig(channel_id, app_id);
    }

    public void saveObject(AppConfigDto customerDto) {
        appConfigDao.saveObject(customerDto);
    }

    public AppConfigDto getObjectByID(Integer id) {
        return (AppConfigDto) appConfigDao.getObjectByID(id);
    }

    public void updateObjectById(AppConfigDto dto){
        appConfigDao.updateObjectById(dto);
    }

    public void deleteById(Integer customer_id){
        appConfigDao.deleteById(customer_id);
    }

}
