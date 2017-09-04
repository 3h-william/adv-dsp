package com.advdsp.service.manager.service;

import com.advdsp.service.manager.db.dao.CustomerConfigDao;
import com.advdsp.service.manager.db.dto.CustomerConfigDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class CustomerConfigService {
    @Autowired
    private CustomerConfigDao customerConfigDao;

    public void saveObject(CustomerConfigDto customerDto) {
        customerConfigDao.saveObject(customerDto);
    }

    public CustomerConfigDto getObjectByID(Integer id) {
        return (CustomerConfigDto) customerConfigDao.getObjectByID(id);
    }

    public void updateObjectById(CustomerConfigDto dto){
        customerConfigDao.updateObjectById(dto);
    }

    public List<CustomerConfigDto> getAll(){
        return customerConfigDao.getAll();
    }

    public void deleteById(Integer customer_id){
        customerConfigDao.deleteById(customer_id);
    }
}
