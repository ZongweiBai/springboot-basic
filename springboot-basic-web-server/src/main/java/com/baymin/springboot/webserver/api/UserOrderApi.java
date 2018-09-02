package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IOrderService;
import com.baymin.springboot.store.entity.Evaluate;
import com.baymin.springboot.store.entity.Invoice;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.payload.UserOrderRequest;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.INVALID_REQUEST;

@RestController
@RequestMapping(path = "/order")
public class UserOrderApi {

    @Autowired
    private IOrderService orderService;

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

    @PostMapping("/evaluate")
    public void orderEvaluate(@RequestBody Evaluate evaluate) {

        if (Objects.isNull(evaluate)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }

        orderService.orderEvaluate(evaluate);
    }

    @PostMapping("/invoice")
    public void saveInvoiceRequest(@RequestBody Invoice invoice) {
        if (Objects.isNull(invoice)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        orderService.saveInvoiceRequest(invoice);
    }

}
