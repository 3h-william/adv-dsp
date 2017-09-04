package com.advdsp.service.send.service;

import com.advdsp.service.send.db.dao.AdxConfigDao;
import com.advdsp.service.send.db.dao.AppBaseConfigDao;
import com.advdsp.service.send.db.dto.AdxConfigDto;
import com.advdsp.service.send.db.dto.AppBaseConfigDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class AdxConfigService {
    @Autowired
    private AdxConfigDao adxConfigDao;

    public List<AdxConfigDto> getAll() {
        return adxConfigDao.getAll();
    }
}
