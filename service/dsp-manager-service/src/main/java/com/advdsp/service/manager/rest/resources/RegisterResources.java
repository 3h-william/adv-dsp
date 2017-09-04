package com.advdsp.service.manager.rest.resources;

import com.advdsp.service.dsp.model.WrapResponseModel;
import com.advdsp.service.manager.db.dto.TokenDto;
import com.advdsp.service.manager.db.dto.UserDto;
import com.advdsp.service.manager.rest.model.login.LoginModel;
import com.advdsp.service.manager.rest.model.login.LoginResponseModel;
import com.advdsp.service.manager.rest.model.register.UserModel;
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


@Path("/register")
@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class RegisterResources
        extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(RegisterResources.class.getName());

    /**
     * @return
     */
    @POST
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel newRegister(UserModel userModel) {
        logger.info("[register:new] user = " + userModel.toString());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            if (StringUtils.isEmpty(userModel.getUser_name()) || StringUtils.isEmpty(userModel.getUser_id()) || StringUtils.isEmpty(userModel.getPassword())) {
                throw new IllegalArgumentException("用户名密码为空");
            }
            UserDto old_userDto = userService.getUserID(userModel.getUser_id());
            if (null != old_userDto) {
                throw new IllegalArgumentException("用户已存在");
            }
            UserDto new_userDto =  parseUserModelToDto(userModel);
            new_userDto.setApprove(0);
            new_userDto.setIs_administrator(0);
            userService.saveObject(new_userDto);
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setMessage(t.getMessage());
            logger.error("[register:new] failed", t);
        }
        return wrapResponseModel;
    }
}
