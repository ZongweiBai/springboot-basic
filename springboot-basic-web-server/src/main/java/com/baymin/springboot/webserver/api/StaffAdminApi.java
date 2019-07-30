package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.common.util.DateUtil;
import com.baymin.springboot.service.IOrderService;
import com.baymin.springboot.store.entity.Order;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.baymin.springboot.common.exception.ErrorDescription.INVALID_REQUEST;

@Api(value = "督导接口", tags = "督导接口")
@RestController
@RequestMapping(path = "/api/staffadmin")
public class StaffAdminApi {

    @Autowired
    private IOrderService orderService;

    @ApiOperation(value = "查询督导开单记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "status", value = "INIT：待服务  PROCESSING：服务中  FINISHED：已完成"),
            @ApiImplicitParam(name = "createStartTime", value = "创建的开始时间 yyyy-MM-dd HH:mm"),
            @ApiImplicitParam(name = "createEndTime", value = "创建的结束时间 yyyy-MM-dd HH:mm")
    })
    @GetMapping("/order/{serviceAdminId}/")
    @ResponseBody
    public List<Order> queryUserOrder(@PathVariable String serviceAdminId,
                                      @RequestParam(name = "status", required = false) String status,
                                      @RequestParam(name = "createStartTime", required = false) String createStartTime,
                                      @RequestParam(name = "createEndTime", required = false) String createEndTime) {
        if (Objects.isNull(serviceAdminId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }

        Date minDate = DateUtil.dayBegin(createStartTime, "yyyy-MM-dd HH:mm");
        Date maxDate = DateUtil.dayEnd(createEndTime, "yyyy-MM-dd HH:mm");

        List<Order> orderList = orderService.queryUserOrder(serviceAdminId, status, "staffAdmin", minDate, maxDate);
        if (CollectionUtils.isNotEmpty(orderList)) {
            orderList = orderList.stream().filter(order -> Objects.nonNull(order.getPayTime())).collect(Collectors.toList());
        }
        return orderList;
    }

}
