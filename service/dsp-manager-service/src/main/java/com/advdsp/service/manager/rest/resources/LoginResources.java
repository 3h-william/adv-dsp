package com.advdsp.service.manager.rest.resources;

import com.advdsp.service.dsp.model.WrapResponseModel;
import com.advdsp.service.manager.db.dto.AdxConfigDto;
import com.advdsp.service.manager.db.dto.TokenDto;
import com.advdsp.service.manager.db.dto.UserDto;
import com.advdsp.service.manager.rest.model.adx.AdxConfigModel;
import com.advdsp.service.manager.rest.model.login.LoginModel;
import com.advdsp.service.manager.rest.model.login.LoginResponseModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;
import java.security.SecureRandom;
import java.util.Date;


@Path("/login")
@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LoginResources
        extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(LoginResources.class.getName());


    /**
     * @return
     */

    @POST
    @Path("/access")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel access(LoginModel loginModel) {
        logger.info("[login:access] logininfo = " + loginModel.toString());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            if (StringUtils.isEmpty(loginModel.getUser_id()) || StringUtils.isEmpty(loginModel.getPassword())) {
                throw new IllegalArgumentException("password or user id is empty");
            }

            UserDto userDto = userService.getUserID(loginModel.getUser_id());
            if (null == userDto) {
                throw new RuntimeException("用户不存在");
            }
            if (!loginModel.getPassword().equals(userDto.getPassword())) {
                throw new RuntimeException("密码不正确");
            }

            TokenDto tokenDto = new TokenDto();
            tokenDto.setUser_id(loginModel.getUser_id());
            tokenDto.setToken(generateToken());
            // 如果该token之前存在，先删除 // TODO 不会调用
            if (null != tokenService.getObjectByToken(tokenDto.getToken())) {
                tokenService.deleteObjectByUserID(tokenDto.getUser_id());
            }
            tokenService.saveToken(tokenDto);
            LoginResponseModel loginResponseModel = new LoginResponseModel();
            loginResponseModel.setToken(tokenDto.getToken());
            wrapResponseModel.setData(loginResponseModel);
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setMessage(t.getMessage());
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[login:access] failed", t);
        }
        return wrapResponseModel;
    }


    public String generateToken() {
        SecureRandom random = new SecureRandom();
        long longToken = Math.abs(random.nextLong());
        return Long.toString(longToken, 16);
    }

}
