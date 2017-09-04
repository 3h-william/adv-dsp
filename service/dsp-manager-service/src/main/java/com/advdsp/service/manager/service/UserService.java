package com.advdsp.service.manager.service;

import com.advdsp.service.manager.db.dao.TokenDao;
import com.advdsp.service.manager.db.dao.UserDao;
import com.advdsp.service.manager.db.dto.TokenDto;
import com.advdsp.service.manager.db.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 */
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public List<UserDto> getAll() {
        return userDao.getAll();
    }

    public UserDto getUserID(String user_id) {
        return (UserDto) userDao.getObjectByID(user_id);
    }

    public void saveObject(UserDto userDto) {
        userDao.saveObject(userDto);
    }

    public void updateObjectById(UserDto userDto) {
        userDao.updateObjectById(userDto);
    }

    public void deleteById(String user_id) {
        userDao.deleteById(user_id);
    }
}
