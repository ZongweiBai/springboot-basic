package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IOrderService;
import com.baymin.springboot.store.entity.Evaluate;
import com.baymin.springboot.store.entity.Invoice;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.entity.OrderStaffChange;
import com.baymin.springboot.store.payload.UserOrderRequest;
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

    @ApiOperation(value = "用户下单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "request.orderType", value = "HOSPITAL_CARE：医院陪护  HOME_CARE：居家照护 REHABILITATION：康复护理"),
            @ApiImplicitParam(name = "request.payway", value = "支付方式 ONLINE_WECHAT  OFFLINE")
    })
    @PostMapping
    @ResponseBody
    public Map<String, Object> saveOrder(@RequestBody UserOrderRequest request) {
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
            @ApiImplicitParam(name = "status", value = "INIT：代付款  PROCESSING：服务中  FINISHED：已完成"),
            @ApiImplicitParam(name = "ownerType", value = "user：普通用户  staff：护工/护士")
    })
    @GetMapping("/{userId}/")
    @ResponseBody
    public Map<String, Object> queryUserOrder(@PathVariable String userId,
                                              @RequestParam(name = "status") String status,
                                              @RequestParam(name = "ownerType") String ownerType) {
        if (Objects.isNull(userId) || Objects.isNull(status) || Objects.isNull(ownerType)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        List<Order> orderList = orderService.queryUserOrder(userId, status, ownerType);
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("orders", orderList);
        return reMap;
    }

    @ApiOperation(value = "查询用户订单详情")
    @GetMapping("/{userId}/{orderId}/")
    @ResponseBody
    public Map<String, Object> queryOrderDetail(@PathVariable String userId,
                                                @PathVariable String orderId) {
        if (Objects.isNull(orderId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        Map<String, Object> reMap = orderService.queryOrderDetail(orderId);
        return reMap;
    }

    @ApiOperation(value = "订单评价")
    @PostMapping("/evaluate")
    public void orderEvaluate(@RequestBody Evaluate evaluate) {

        if (Objects.isNull(evaluate)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }

        orderService.orderEvaluate(evaluate);
    }

    @ApiOperation(value = "订单开票申请")
    @ApiImplicitParams({
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
    @PostMapping("/staffchange")
    public void staffChangeRequest(@RequestBody OrderStaffChange staffChange) {
        if (Objects.isNull(staffChange)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        orderService.staffChangeRequest(staffChange);
    }

    @ApiOperation(value = "护士/护工开始服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID")
    })
    @PostMapping("/servicestart")
    public void staffChangeRequest(@RequestBody Map<String, String> request) {
        if (Objects.isNull(request) || StringUtils.isBlank(request.get("orderId"))) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        orderService.serviceStart(request.get("orderId"));
    }

}
