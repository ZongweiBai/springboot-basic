package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.service.IBasicItemService;
import com.baymin.springboot.store.entity.BasicItem;
import com.baymin.springboot.store.entity.ServiceProduct;
import com.baymin.springboot.store.entity.ServiceType;
import com.baymin.springboot.store.enumconstant.BasicItemType;
import com.baymin.springboot.store.enumconstant.CareType;
import com.baymin.springboot.store.payload.ServiceProductVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.baymin.springboot.common.exception.ErrorDescription.INVALID_REQUEST;

@Api(value = "服务项目", tags = "服务项目")
@RestController
@RequestMapping(path = "/api/basicitem")
public class BasicItemApi {

    @Autowired
    private IBasicItemService basicItemService;

    @ApiOperation(value = "查询服务项目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping
    @ResponseBody
    public List<ServiceType> queryServiceType() {
        return basicItemService.getAllServiceType();
    }

    @ApiOperation(value = "根据服务项目查询产品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping("/{serviceTypeId}")
    @ResponseBody
    public List<ServiceProductVo> queryProductByTypeId(@PathVariable("serviceTypeId") String serviceTypeId) {

        ServiceType serviceType = basicItemService.getServiceTypeById(serviceTypeId);
        if (Objects.isNull(serviceType)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }

        List<ServiceProduct> productList = basicItemService.getServiceProductByTypeId(serviceTypeId);
        if (CollectionUtils.isEmpty(productList)) {
            return new ArrayList<>();
        }

        List<ServiceProductVo> productResponses = new ArrayList<>();
        for (ServiceProduct serviceProduct : productList) {
            if (serviceType.getCareType() == CareType.REHABILITATION) {
                productResponses.add(new ServiceProductVo(serviceProduct, null));
            } else if (serviceType.getCareType() == CareType.HOSPITAL_CARE || serviceType.getCareType() == CareType.HOME_CARE) {
                List<BasicItem> basicItem = basicItemService.getAllUpcartItems();
                if (StringUtils.isNotBlank(serviceProduct.getBasicItems())) {
                    List<String> itemList = Arrays.asList(serviceProduct.getBasicItems().split(","));
                    for (BasicItem item : basicItem) {
                        if (itemList.contains(item.getId())) {
                            item.setChecked(true);
                        }
                    }
                }
                Map<BasicItemType, List<BasicItem>> itemMap = basicItem.stream().collect(Collectors.groupingBy(BasicItem::getBasicItemType));
                productResponses.add(new ServiceProductVo(serviceProduct, itemMap));
            }
        }
        return productResponses;
    }

}
