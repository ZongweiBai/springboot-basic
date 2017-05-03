package com.baymin.springboot.rest;

import com.baymin.springboot.entity.UserProfile;
import com.baymin.springboot.exception.ErrorCode;
import com.baymin.springboot.exception.ErrorInfo;
import com.baymin.springboot.exception.SpringBootException;
import com.baymin.springboot.service.IUserProfileService;
import com.baymin.springboot.service.impl.UserProfileServiceImpl;
import com.baymin.springboot.util.SpringContextUtil;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.baymin.springboot.exception.ErrorDescription.RECORD_NOT_EXIST;

/**
 * Created by Baymin on 2017/4/11.
 */
@Path("/userProfile")
public class UserProfileApi {

    @Resource
    private IUserProfileService userProfileService;

    @GET
    @Path("/{account}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public UserProfile sayHello(@PathParam("account") String account) {
        userProfileService = SpringContextUtil.getBean(UserProfileServiceImpl.class);
        UserProfile userProfile = userProfileService.findByAccount(account);
        if (userProfile == null) {
            String description = String.format(RECORD_NOT_EXIST, "userProfile", account);
            throw new SpringBootException(Response.Status.NOT_FOUND, new ErrorInfo(ErrorCode.not_found.name(), description));
        }
        return userProfile;
    }

}
