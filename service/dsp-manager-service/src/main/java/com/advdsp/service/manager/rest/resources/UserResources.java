package com.advdsp.service.manager.rest.resources;

import com.advdsp.service.dsp.model.WrapResponseModel;
import com.advdsp.service.manager.db.dto.UserDto;
import com.advdsp.service.manager.rest.model.register.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


@Path("/user")
@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserResources
        extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(UserResources.class.getName());


    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getAll() {
        logger.info("[user:getAll] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        List<UserModel> userModels = new ArrayList<>();
        try {
            checkAdminAuth();
            List<UserDto> userDtos = userService.getAll();
            if (null != userDtos) {
                for (UserDto userDto : userDtos) {
                    userModels.add(parseUserDtoToModel(userDto));
                }
            }
            wrapResponseModel.setCode(successCode);
            wrapResponseModel.setData(userModels);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setMessage(t.getMessage());
            logger.error("[user:getAll] failed", t);
        }
        return wrapResponseModel;
    }


    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getOne() {
        logger.info("[user:getOne] uid = " + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            UserDto userDto = userService.getUserID(getUid());
            if (null == userDto) {
                throw new RuntimeException("没有此用户");
            }
            UserModel userModel = parseUserDtoToModel(userDto);
            wrapResponseModel.setCode(successCode);
            wrapResponseModel.setData(userModel);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setMessage(t.getMessage());
            logger.error("[user:getOne] failed", t);
        }
        return wrapResponseModel;
    }

    /**
     * @return
     */
    @POST
    @Path("/approve")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel approve(UserModel userModel) {
        logger.info("[user:approve] uid = " + getUid() + ", user = " + userModel.toString());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            if (StringUtils.isEmpty(userModel.getUser_id())) {
                throw new IllegalArgumentException("用户id为空");
            }
            checkAdminAuth();
            UserDto userDto = userService.getUserID(userModel.getUser_id());
            if (null == userDto) {
                throw new IllegalArgumentException("用户不存在");
            }
            userDto.setApprove(1);
            userService.updateObjectById(userDto);
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setMessage(t.getMessage());
            logger.error("[user:approve] failed", t);
        }
        return wrapResponseModel;
    }

    /**
     * @return
     */
    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel delete(UserModel userModel) {
        logger.info("[user:delete] uid = " + getUid() + ", user = " + userModel.toString());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            if (StringUtils.isEmpty(userModel.getUser_id())) {
                throw new IllegalArgumentException("用户id为空");
            }
            checkAdminAuth();
            UserDto userDto = userService.getUserID(userModel.getUser_id());
            if (null == userDto) {
                throw new IllegalArgumentException("用户不存在");
            }
            userService.deleteById(userDto.getUser_id());
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setMessage(t.getMessage());
            logger.error("[user:delete] failed", t);
        }
        return wrapResponseModel;
    }

    // 检查是否管理员权限
    private void checkAdminAuth() {
        UserDto operateUserDto = userService.getUserID(getUid());
        if (null == operateUserDto || 1 != operateUserDto.getIs_administrator()) {
            throw new IllegalArgumentException("没有权限操作");
        }
    }
}
