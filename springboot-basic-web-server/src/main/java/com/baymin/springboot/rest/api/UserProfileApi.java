package com.baymin.springboot.rest.api;

import org.apache.commons.lang3.StringUtils;
import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.rest.exception.SpringBootException;
import com.baymin.springboot.service.IRedisService;
import com.baymin.springboot.service.IUserProfileService;
import com.baymin.springboot.store.entity.UserProfile;

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
        UserProfile userProfile = userProfileService.findByAccount(account);
        if (userProfile == null) {
            String description = String.format(RECORD_NOT_EXIST, "userProfile", account);
            throw new SpringBootException(Response.Status.NOT_FOUND, new ErrorInfo(ErrorCode.not_found.name(), description));
        }
        return userProfile;
    }

    @GET
    @Path("/{account}")
    public String queryByAccountForTest(@PathParam("account") String account) {
        UserProfile userProfile = new UserProfile();
        userProfile.setId(account);
        userProfile.setAccount(account);
        return userProfile.toString();
    }

    @POST
    public void saveUserProfile(UserProfile userProfile) {
        userProfileService.saveUserProfile(userProfile);

        String userCount = redisService.get("userCount");
        if (StringUtils.isNotBlank(userCount)) {
            redisService.set("userCount", String.valueOf(Integer.valueOf(userCount) + 1));
        } else {
            redisService.set("userCount", "1");
        }
    }

}
