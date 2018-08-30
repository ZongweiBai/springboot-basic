package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IOrderService;
import com.baymin.springboot.store.entity.Order;
import com.baymin.springboot.store.payload.UserOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.INVALID_REQUEST;

@RestController
@RequestMapping(path = "/order/user")
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

    @GetMapping("/{userId}")
    @ResponseBody
    public Map<String, Object> queryUserOrder(@PathVariable String userId, @RequestParam(name = "status") String status) {
        if (Objects.isNull(userId) || Objects.isNull(status)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return orderService.queryUserOrder(userId, status);
    }

}
