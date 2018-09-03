package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IAddressService;
import com.baymin.springboot.store.entity.Address;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.baymin.springboot.common.exception.ErrorDescription.INVALID_REQUEST;

@Api
@RestController
@RequestMapping(path = "/api/address")
public class AddressApi {

    private IAddressService addressService;

    @ApiResponses({
            @ApiResponse(code = 201,message = "Created"),
            @ApiResponse(code = 400,message = "parameters error",response = ErrorInfo.class),
            @ApiResponse(code = 500,message = "server error",response = ErrorInfo.class)
    })
    @PostMapping
    @ResponseBody
    public Address saveAddress(@RequestBody Address address) {
        if (Objects.isNull(address)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return addressService.saveAddress(address);
    }

    @DeleteMapping("/{userId}/{addressId}")
    public void deleteAddress(@PathVariable String userId,
                              @PathVariable String addressId) {
        if (Objects.isNull(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        addressService.deleteAddress(userId, addressId);
    }

    @PutMapping
    @ResponseBody
    public Address updateAddress(@RequestBody Address address) {
        if (Objects.isNull(address)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return addressService.updateAddress(address);
    }

    @GetMapping("/{userId}")
    @ResponseBody
    public List<Address> queryAddress(@PathVariable String userId) {
        if (Objects.isNull(userId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return addressService.queryUserAddress(userId);
    }

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
