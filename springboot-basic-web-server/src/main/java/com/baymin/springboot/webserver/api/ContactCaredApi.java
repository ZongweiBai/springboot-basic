package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IContactCaredService;
import com.baymin.springboot.store.entity.ContactCared;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.INVALID_REQUEST;

@Api(value = "常用被照护人", tags = "常用被照护人")
@RestController
@RequestMapping(path = "/api/contactcared")
public class ContactCaredApi {

    @Autowired
    private IContactCaredService contactCaredService;

    @ApiOperation(value = "新增被照护人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "contactCared.sex", value = "M:女 F：男")
    })
    @PostMapping
    @ResponseBody
    public ContactCared saveContactCared(@RequestBody ContactCared contactCared) {
        if (Objects.isNull(contactCared)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return contactCaredService.saveContactCared(contactCared);
    }

    @ApiOperation(value = "删除被照护人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @DeleteMapping("/{userId}/{contactCaredId}")
    public void deleteContactCared(@PathVariable String userId,
                              @PathVariable String contactCaredId) {
        if (Objects.isNull(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        contactCaredService.deleteContactCared(userId, contactCaredId);
    }

    @ApiOperation(value = "更新被照护人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "contactCared.sex", value = "M:女 F：男")
    })
    @PutMapping
    @ResponseBody
    public ContactCared updateContactCared(@RequestBody ContactCared contactCared) {
        if (Objects.isNull(contactCared)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return contactCaredService.updateContactCared(contactCared);
    }

    @ApiOperation(value = "根据用户ID查询被照护人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping("/{userId}")
    @ResponseBody
    public List<ContactCared> queryContactCared(@PathVariable String userId) {
        if (Objects.isNull(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return contactCaredService.queryUserContactCared(userId);
    }

    @ApiOperation(value = "查询被照护人明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping("/{userId}/{contactCaredId}")
    @ResponseBody
    public ContactCared queryContactCaredDetail(@PathVariable String userId,
                                      @PathVariable String contactCaredId) {
        if (Objects.isNull(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return contactCaredService.queryContactCaredDetail(contactCaredId);
    }

}
