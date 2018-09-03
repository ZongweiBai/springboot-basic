package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IAddressService;
import com.baymin.springboot.store.entity.Address;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.INVALID_REQUEST;

@Api(value = "常用地址", tags = "常用地址")
@RestController
@RequestMapping(path = "/api/address")
public class AddressApi {

    private IAddressService addressService;

    @ApiOperation(value = "新增常用地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address.addressType", value = "地址类型 H：医院 M：居家")
    })
    @PostMapping
    @ResponseBody
    public Address saveAddress(@RequestBody Address address) {
        if (Objects.isNull(address)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return addressService.saveAddress(address);
    }

    @ApiOperation(value = "删除常用地址")
    @DeleteMapping("/{userId}/{addressId}")
    public void deleteAddress(@PathVariable String userId,
                              @PathVariable String addressId) {
        if (Objects.isNull(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        addressService.deleteAddress(userId, addressId);
    }

    @ApiOperation(value = "更新常用地址")
    @PutMapping
    @ResponseBody
    public Address updateAddress(@RequestBody Address address) {
        if (Objects.isNull(address)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return addressService.updateAddress(address);
    }

    @ApiOperation(value = "根据用户ID查询地址")
    @GetMapping("/{userId}")
    @ResponseBody
    public List<Address> queryAddress(@PathVariable String userId) {
        if (Objects.isNull(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return addressService.queryUserAddress(userId);
    }

    @ApiOperation(value = "查询地址明细")
    @GetMapping("/{userId}/{addressId}")
    @ResponseBody
    public Address queryAddressDetail(@PathVariable String userId,
                                      @PathVariable String addressId) {
        if (Objects.isNull(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return addressService.queryAddressDetail(addressId);
    }

}
