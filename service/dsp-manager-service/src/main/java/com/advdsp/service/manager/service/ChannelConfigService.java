package com.advdsp.service.manager.service;

import com.advdsp.service.manager.db.dao.ChannelConfigDao;
import com.advdsp.service.manager.db.dto.ChannelConfigDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class ChannelConfigService {
    @Autowired
    private ChannelConfigDao channelConfigDao;

    public void saveObject(ChannelConfigDto customerDto) {
        channelConfigDao.saveObject(customerDto);
    }

    public List<ChannelConfigDto> getAll() {
        return channelConfigDao.getAll();
    }

    public ChannelConfigDto getObjectByID(Integer id) {
        return (ChannelConfigDto) channelConfigDao.getObjectByID(id);
    }

    public void updateObjectById(ChannelConfigDto dto){
        channelConfigDao.updateObjectById(dto);
    }

    public void deleteById(Integer customer_id){
        channelConfigDao.deleteById(customer_id);
    }

}
