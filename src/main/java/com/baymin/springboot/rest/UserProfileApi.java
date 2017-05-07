package com.baymin.springboot.rest;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.SpringBootException;
import com.baymin.springboot.common.util.SpringContextUtil;
import com.baymin.springboot.service.IRedisService;
import com.baymin.springboot.persistence.entity.UserProfile;
import com.baymin.springboot.service.IUserProfileService;
import com.baymin.springboot.service.impl.RedisServiceImpl;
import com.baymin.springboot.service.impl.UserProfileServiceImpl;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.baymin.springboot.common.exception.ErrorDescription.RECORD_NOT_EXIST;

/**
 * Created by Baymin on 2017/4/11.
 */
@Path("/userProfile")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserProfileApi {

    @Resource
    private IUserProfileService userProfileService;

    @Resource
    private IRedisService redisService;

    @GET
    @Path("/{account}")
    public UserProfile queryByAccount(@PathParam("account") String account) {
        userProfileService = SpringContextUtil.getBean(UserProfileServiceImpl.class);
        UserProfile userProfile = userProfileService.findByAccount(account);
        if (userProfile == null) {
            String description = String.format(RECORD_NOT_EXIST, "userProfile", account);
            throw new SpringBootException(Response.Status.NOT_FOUND, new ErrorInfo(ErrorCode.not_found.name(), description));
        }
        return userProfile;
    }

    @POST
    public void saveUserProfile(UserProfile userProfile) {
        userProfileService = SpringContextUtil.getBean(UserProfileServiceImpl.class);
        redisService = SpringContextUtil.getBean(RedisServiceImpl.class);
        userProfileService.saveUserProfile(userProfile);

        String userCount = redisService.get("userCount");
        if (StringUtils.isNotBlank(userCount)) {
            redisService.set("userCount", String.valueOf(Integer.valueOf(userCount) + 1));
        } else {
            redisService.set("userCount", "1");
        }
    }

}
