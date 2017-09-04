package com.advdsp.service.manager.db.dao;

import com.advdsp.service.manager.db.dto.TokenDto;

/**
 */
public interface TokenDao extends BaseDao {

    TokenDto getObjectByToken(String token);

    void deleteTokenByUserID(String user_ID);
}
