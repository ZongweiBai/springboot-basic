package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IOrderRefundService;
import com.baymin.springboot.service.IOrderService;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import com.baymin.springboot.store.payload.OrderDetailVo;
import com.baymin.springboot.store.payload.UserOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.INVALID_REQUEST;

@Api(value = "订单", tags = "订单")
@RestController
@RequestMapping(path = "/api/order")
public class UserOrderApi {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderRefundService orderRefundService;

    @ApiOperation(value = "用户下单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "request.orderType", value = "HOSPITAL_CARE：医院陪护  HOME_CARE：居家照护 REHABILITATION：康复护理"),
            @ApiImplicitParam(name = "request.payway", value = "支付方式 ONLINE_WECHAT  OFFLINE")
    })
    @PostMapping
    @ResponseBody
    public Map<String, Object> saveOrder(@RequestBody UserOrderVo request) {
        if (Objects.isNull(request)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        Order order = orderService.saveUserOrder(request);

        Map<String, Object> reMap = new HashMap<>();
        reMap.put("order", order);
        return reMap;
    }

    @ApiOperation(value = "查询用户订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "status", value = "INIT：代付款  PROCESSING：服务中  FINISHED：已完成 UNINVOCIED:可开票订单")
    })
    @GetMapping("/{userId}/")
    @ResponseBody
    public List<Order> queryUserOrder(@PathVariable String userId,
                                      @RequestParam(name = "status") String status) {
        if (Objects.isNull(userId) || Objects.isNull(status)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return orderService.queryUserOrder(userId, status, "user");
    }

    @ApiOperation(value = "查询用户订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping("/{userId}/{orderId}/")
    @ResponseBody
    public OrderDetailVo queryOrderDetail(@PathVariable String userId,
                                          @PathVariable String orderId) {
        if (Objects.isNull(orderId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return orderService.queryOrderDetail(orderId);
    }

    @ApiOperation(value = "订单评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping("/evaluate")
    public void orderEvaluate(@RequestBody Evaluate evaluate) {

        if (Objects.isNull(evaluate)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }

        orderService.orderEvaluate(evaluate);
    }

    @ApiOperation(value = "查询订单评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping("/evaluate/{orderId}")
    public List<Evaluate> queryOrderEvaluate(@PathVariable(value = "orderId") String orderId) {
        return orderService.queryOrderEvaluate(orderId, CommonDealStatus.AGREE);
    }

    @ApiOperation(value = "查询用户的订单评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping("/evaluate")
    public List<Evaluate> queryEvaluate(@RequestParam(value = "orderId", required = false) String orderId,
                                        @RequestParam(value = "userId", required = false) String userId) {
        return orderService.queryEvaluate(orderId, userId);
    }

    @ApiOperation(value = "订单开票申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "invoice.invoiceType", value = "发票类型 E：电子  P：纸质"),
            @ApiImplicitParam(name = "invoice.headerType", value = "抬头类型 C:企业单位  P:个人")
    })
    @PostMapping("/invoice")
    public void saveInvoiceRequest(@RequestBody Invoice invoice) {
        if (Objects.isNull(invoice)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        orderService.saveInvoiceRequest(invoice);
    }

    @ApiOperation(value = "申请更换照护人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping("/staffchange")
    public void staffChangeRequest(@RequestBody OrderStaffChange staffChange) {
        if (Objects.isNull(staffChange)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        orderService.staffChangeRequest(staffChange);
    }

    @ApiOperation(value = "换人申请查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping("/staffchange/{userId}")
    @ResponseBody
    public List<OrderStaffChange> queryUserStaffChangeRequest(@PathVariable String userId) {
        if (StringUtils.isBlank(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return orderService.queryUserStaffChange(userId);
    }

    @ApiOperation(value = "申请退款")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @PostMapping("/refund")
    public OrderRefund orderRefundRequest(@RequestBody OrderRefund orderRefund) {
        if (Objects.isNull(orderRefund)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        orderRefund.setApplyType("USER");

        return orderRefundService.saveOrderRefund(orderRefund);
    }

    @ApiOperation(value = "退款申请查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping("/refund/{userId}")
    @ResponseBody
    public List<OrderRefund> queryUserOrderRefundRequest(@PathVariable String userId) {
        if (StringUtils.isBlank(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return orderRefundService.queryUserOrderRefund(userId);
    }

    @ApiOperation(value = "订单完成")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "orderId", value = "订单ID")
    })
    @PutMapping("/complete/{orderId}")
    public void orderCompleted(@PathVariable String orderId) {
        if (Objects.isNull(orderId) || StringUtils.isBlank(orderId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        orderService.orderCompleted(orderId);
    }

}
