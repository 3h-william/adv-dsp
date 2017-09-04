package com.advdsp.service.send.service;

import com.advdsp.service.send.db.dao.SystemConfigDao;
import com.advdsp.service.send.db.dto.SystemConfigDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class SystemConfigService {

    @Autowired
    private SystemConfigDao systemConfigDao;

    public List<SystemConfigDto> getAll() {
        return systemConfigDao.getAll();
    }
}
