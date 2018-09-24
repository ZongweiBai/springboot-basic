package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IFeedbackService;
import com.baymin.springboot.service.ISysManageService;
import com.baymin.springboot.store.entity.Feedback;
import com.baymin.springboot.store.entity.SysDict;
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

@Api(value = "其他API", tags = "其他API")
@RestController
@RequestMapping(path = "/api/sys")
public class SystemApi {

    @Autowired
    private IFeedbackService feedBackService;

    @Autowired
    private ISysManageService sysManageService;

    @ApiOperation(value = "用户反馈")
    @PostMapping("/feedback")
    @ResponseBody
    public void feedback(@RequestBody Feedback feedback) {
        if (Objects.isNull(feedback)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        feedBackService.saveFeedback(feedback);
    }

    @ApiOperation(value = "根据类型查询照护计划选项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictName", value = "CARE_PLAN_TYPE：明细类型  CARE_PLAN_CASE：适用病症")
    })
    @ResponseBody
    @RequestMapping(value = "/dict/{dictName}", method = RequestMethod.GET)
    public List<SysDict> getSysDictByDictName(@PathVariable("dictName") String dictName) {
        return sysManageService.getSysDictByDictName(dictName);
    }

}
