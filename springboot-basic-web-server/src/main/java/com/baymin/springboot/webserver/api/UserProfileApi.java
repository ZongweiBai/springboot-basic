package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.service.IRedisService;
import com.baymin.springboot.service.IUserProfileService;
import com.baymin.springboot.store.entity.UserProfile;
import com.baymin.springboot.webserver.exception.WebServerException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.baymin.springboot.common.exception.ErrorDescription.RECORD_NOT_EXIST;

/**
 * Created by Baymin on 2017/4/11.
 */
@RestController
@RequestMapping(path = "/userprofile")
public class UserProfileApi {

    @Resource
    private IUserProfileService userProfileService;

    @Resource
    private IRedisService redisService;

    @GetMapping(path = "/{account}")
    public UserProfile queryByAccount(@PathVariable("account") String account) {
        UserProfile userProfile = userProfileService.findByAccount(account);
        if (userProfile == null) {
            String description = String.format(RECORD_NOT_EXIST, "userProfile", account);
            throw new WebServerException(HttpStatus.NOT_FOUND, new ErrorInfo(ErrorCode.not_found.name(), description));
        }
        return userProfile;
    }

    @PostMapping
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
