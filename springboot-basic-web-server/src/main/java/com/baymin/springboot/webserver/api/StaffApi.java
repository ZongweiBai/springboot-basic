package com.baymin.springboot.webserver.api;

import com.baymin.springboot.common.exception.ErrorCode;
import com.baymin.springboot.common.exception.ErrorInfo;
import com.baymin.springboot.common.exception.WebServerException;
import com.baymin.springboot.common.util.BigDecimalUtil;
import com.baymin.springboot.common.util.DateUtil;
import com.baymin.springboot.service.*;
import com.baymin.springboot.store.entity.*;
import com.baymin.springboot.store.enumconstant.IncomeType;
import com.baymin.springboot.store.payload.OrderCarePlanVo;
import com.baymin.springboot.store.payload.StaffIncomeInfoVo;
import com.baymin.springboot.store.payload.StaffRankInfoVo;
import com.baymin.springboot.store.payload.StaffRankVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.baymin.springboot.common.exception.ErrorDescription.INVALID_REQUEST;

@Api(value = "护士/护工接口", tags = "护士/护工接口")
@RestController
@RequestMapping(path = "/api/staff")
public class StaffApi {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IStaffIncomeService staffIncomeService;

    @Autowired
    private IStaffService staffService;

    @Autowired
    private IOrderCarePlanService orderCarePlanService;

    @Autowired
    private ISysManageService sysManageService;

    @Autowired
    private ICarePlanService carePlanService;

    @Autowired
    private IWithdrawService withdrawService;

    @ApiOperation(value = "查询护士/护工明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping("/detail/{staffId}/")
    @ResponseBody
    public ServiceStaff queryStaffDetail(@PathVariable String staffId) {
        if (Objects.isNull(staffId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }

        ServiceStaff staff = staffService.findById(staffId);

        if (Objects.nonNull(staff)) {
            staff.setIdpId(null);
        }
        return staff;
    }

    @ApiOperation(value = "查询护士/护工业绩")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping("/income/{staffId}/")
    @ResponseBody
    public StaffIncomeInfoVo queryStaffIncome(@PathVariable String staffId) {
        if (Objects.isNull(staffId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        List<StaffIncome> incomeList = staffIncomeService.queryStaffIncome(staffId);
        double income = staffIncomeService.sumIncomeByDate(staffId, DateUtil.monthFirst(), DateUtil.monthLast());
        double totalIncome = incomeList.stream()
                .filter(staffIncome -> staffIncome.getIncomeType() == IncomeType.INCOME)
                .map(StaffIncome::getIncome).reduce(0.0, BigDecimalUtil::add);

        return new StaffIncomeInfoVo(totalIncome, income, incomeList);
    }

    @ApiOperation(value = "查询护士/护工钱包")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping("/wallet/{staffId}/")
    @ResponseBody
    public UserWallet queryUserWallet(@PathVariable String staffId) {
        if (Objects.isNull(staffId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        return withdrawService.queryUserWalletByUserId(staffId, "S");
    }

    @ApiOperation(value = "查询护士/护工订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "status", value = "INIT：待服务  PROCESSING：服务中  FINISHED：已完成")
    })
    @GetMapping("/order/{staffId}/")
    @ResponseBody
    public List<Order> queryUserOrder(@PathVariable String staffId,
                                      @RequestParam(name = "status") String status) {
        if (Objects.isNull(staffId) || Objects.isNull(status)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        List<Order> orderList = orderService.queryUserOrder(staffId, status, "staff", null, null);
        return orderList;
    }

    @ApiOperation(value = "查询护士/护工业绩排行榜")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping("/rank/{staffId}/")
    @ResponseBody
    public StaffRankInfoVo queryStaffRank(@PathVariable String staffId) {
        if (Objects.isNull(staffId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        List<StaffRankVo> staffRankVoList = staffIncomeService.queryStaffRank();
        int myRank = 0;
        if (CollectionUtils.isNotEmpty(staffRankVoList)) {
            for (StaffRankVo staffRankVo : staffRankVoList) {
                myRank++;
                if (StringUtils.equals(staffRankVo.getStaffId(), staffId)) {
                    break;
                }
            }
        }

        return new StaffRankInfoVo(myRank, staffRankVoList);
    }

    @ApiOperation(value = "护士/护工开始服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "orderId", value = "订单ID")
    })
    @PutMapping("/servicestart/{orderId}")
    public void staffChangeRequest(@PathVariable String orderId) {
        if (Objects.isNull(orderId) || StringUtils.isBlank(orderId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        orderService.serviceStart(orderId);
    }

    @ApiOperation(value = "查询系统默认的照护计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header")
    })
    @GetMapping("/careplan")
    public List<CarePlan> querySysCarePlan(@RequestParam("typeId") String typeId, @RequestParam("caseId") String caseId,
                                           @RequestParam("key") String key) {
        return carePlanService.queryCarePlan(typeId, caseId, key);
    }

    @ApiOperation(value = "查询订单的照护计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "orderId", value = "订单ID"),
            @ApiImplicitParam(name = "queryType", value = "查询类型 basic(基础信息，不传即默认) detail(包含执行记录)")
    })
    @GetMapping("/careplan/{orderId}")
    public OrderCarePlanVo queryOrderCarePlan(@PathVariable String orderId, @RequestParam(value = "queryType") String queryType) {
        if (StringUtils.isBlank(orderId)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        OrderCarePlan carePlan = orderCarePlanService.findByOrderId(orderId);
        if (Objects.isNull(carePlan)) {
            return null;
        }
        SysDict typeDict = sysManageService.getSysDictById(carePlan.getTypeId());
        SysDict caseDict = sysManageService.getSysDictById(carePlan.getCaseId());
        ServiceStaff staff = staffService.findById(carePlan.getStaffId());

        OrderCarePlanVo carePlanVo;
        if (StringUtils.equals("detail", queryType)) {
            List<OrderCarePlanRecord> recordList = orderCarePlanService.findRecordByCarePlanId(carePlan.getId());
            Map<String, List<OrderCarePlanRecord>> recordMap = recordList.stream().collect(Collectors.groupingBy(OrderCarePlanRecord::getOrderPlanSubId));
            carePlanVo = new OrderCarePlanVo(carePlan, recordMap);
        } else {
            carePlanVo = new OrderCarePlanVo(carePlan, null);
        }
        if (Objects.nonNull(typeDict)) {
            carePlanVo.setTypeName(typeDict.getCodeValue());
        }
        if (Objects.nonNull(caseDict)) {
            carePlanVo.setCaseName(caseDict.getCodeValue());
        }
        if (Objects.nonNull(staff)) {
            carePlanVo.setStaffUserName(staff.getUserName());
        }
        return carePlanVo;
    }

    @ApiOperation(value = "保存订单的照护计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "orderId", value = "订单ID")
    })
    @PostMapping("/careplan/{orderId}")
    public void saveOrderCarePlan(@PathVariable String orderId, @RequestBody OrderCarePlan orderCarePlan) {
        if (StringUtils.isBlank(orderId) || Objects.isNull(orderCarePlan)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        orderCarePlan.setOrderId(orderId);
        orderCarePlanService.saveOrderCarePlan(orderCarePlan);
    }

    @ApiOperation(value = "开始执行/结束执行订单的照护计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "careplanId", value = "执行计划ID"),
            @ApiImplicitParam(name = "careplanSubId", value = "执行计划子项ID"),
            @ApiImplicitParam(name = "operate", value = "start(开始执行) finish(结束执行)")
    })
    @PostMapping("/careplanrecord/{careplanId}/{careplanSubId}")
    public void saveOrderCarePlanRecord(@PathVariable String careplanId,
                                        @PathVariable String careplanSubId,
                                        @RequestParam(name = "operate") String operate) {
        if (StringUtils.isBlank(careplanId) || StringUtils.isBlank(careplanSubId) || StringUtils.isBlank(operate)
                || (!StringUtils.equals("start", operate)) && !StringUtils.equals("finish", operate)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        orderCarePlanService.saveOrderCarePlanRecord(careplanId, careplanSubId, operate);
    }

    @ApiOperation(value = "开启/关闭接单功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Bearer access_token", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "operation", value = "操作类型 enable:开启接单 disable:关闭接单")
    })
    @PutMapping("/notification/{staffId}")
    public void modiftNofitication(@PathVariable String staffId, @RequestParam String operation) {
        if (StringUtils.isBlank(staffId) || StringUtils.isBlank(operation)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }
        if (!StringUtils.equals("enable", operation) && !StringUtils.equals("disable", operation)) {
            throw new WebServerException(HttpStatus.BAD_REQUEST, new ErrorInfo(ErrorCode.invalid_request.name(), INVALID_REQUEST));
        }

        Boolean enableNotification = StringUtils.equals("enable", operation);
        staffService.changeNotification(staffId, enableNotification);
    }

}
