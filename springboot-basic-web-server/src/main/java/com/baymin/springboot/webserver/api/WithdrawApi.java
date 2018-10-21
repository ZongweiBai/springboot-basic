package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IStaffService;
import com.baymin.springboot.service.IWithdrawService;
import com.baymin.springboot.store.entity.OrderRefund;
import com.baymin.springboot.store.entity.Withdraw;
import com.baymin.springboot.store.enumconstant.WithdrawResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.INVALID_REQUEST;

@Api(value = "提现管理", tags = "提现管理")
@RestController
@RequestMapping(path = "/api/withdraw")
public class WithdrawApi {

    @Autowired
    private IStaffService staffService;

    @Autowired
    private IWithdrawService withdrawService;

    @ApiOperation(value = "保存提现申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping
    public void queryOrderCarePlan(@RequestBody Withdraw withdraw) {
        if (Objects.isNull(withdraw) || StringUtils.isBlank(withdraw.getUserId())
                || StringUtils.isBlank(withdraw.getUserType()) || withdraw.getWithdrawAmount() <= 0) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        withdrawService.saveWithdraw(withdraw);
    }

    @ApiOperation(value = "提现申请查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping("/{userId}")
    @ResponseBody
    public List<Withdraw> queryUserOrderRefundRequest(@PathVariable String userId,
                                                      @RequestParam(value = "userType", defaultValue = "S") String userType,
                                                      @RequestParam(value = "result", required = false) WithdrawResult result) {
        if (StringUtils.isBlank(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return withdrawService.queryByUserIdAndType(userId, userType, result);
    }

}
