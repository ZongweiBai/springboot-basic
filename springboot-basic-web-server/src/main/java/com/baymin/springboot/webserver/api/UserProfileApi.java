package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IUserProfileService;
import com.baymin.springboot.store.entity.UserProfile;
import com.baymin.springboot.store.payload.UserProfileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.INVALID_REQUEST;

@Api(value = "用户接口", tags = "用户接口")
@RestController
@RequestMapping(path = "/api/user")
public class UserProfileApi {

    @Autowired
    private IUserProfileService userProfileService;

    @ApiOperation(value = "查询用户明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping("/detail/{userId}/")
    @ResponseBody
    public UserProfileVo queryStaffDetail(@PathVariable String userId) {
        if (Objects.isNull(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }

        UserProfile userProfile = userProfileService.findById(userId);

        return new UserProfileVo(userProfile);
    }
}
