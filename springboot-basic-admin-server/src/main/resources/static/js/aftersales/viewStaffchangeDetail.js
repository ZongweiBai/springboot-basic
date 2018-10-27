$(function () {

    loadDataInfo(changeId);

});

function applyFund() {
    $("#form-menu-add").Validform({
        tiptype: 2,
        callback: function (form) {
            $.ajax({
                type: "POST",
                url: contextPath + "afterSales/dealStaffChange",
                data: $('#form-menu-add').serialize(),
                beforeSend: function () {
                    tip.showLoading();
                },
                error: function () {
                    tip.hideLoading();
                    tip.alertError("处理失败");
                },
                success: function (data) {
                    tip.hideLoading();
                    if (data.result == "200") {
                        tip.alertSuccess("处理成功", function () {
                            tip.closeIframe();
                        });
                    } else {
                        tip.alertError(data.message);
                    }
                }
            });
            return false;
        }
    });
}

function rejectFund() {
    // var dealDesc = $("#dealDesc").val();
    // if (dealDesc == "") {
    //     tip.alertError("处理备注不能为空")
    //     return;
    // }
    $.ajax({
        type: "POST",
        url: contextPath + "afterSales/dealStaffChange",
        data: {
            "id": $("#id").val(),
            "dealStatus": "REJECT",
            "dealDesc": dealDesc
        },
        beforeSend: function () {
            tip.showLoading();
        },
        error: function () {
            tip.hideLoading();
            tip.alertError("保存失败");
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == "200") {
                tip.alertSuccess("保存成功", function () {
                    tip.closeIframe();
                });
            } else {
                tip.alertError(data.message);
            }
        }
    });
    return false;
}

function loadDataInfo(changeId) {
    $.ajax({
        type: "GET",
        url: contextPath + "afterSales/viewChangeDetail",
        data: {
            "changeId": changeId
        },
        beforeSend: function () {
            tip.showLoading();
        },
        error: function () {
            tip.hideLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == "200") {
                var info = data.info;
                var order = info.order;
                var orderExt = info.orderExt;
                var userProfile = info.user;
                var change = info.change;

                var careType = "";
                if (order.careType == "HOSPITAL_CARE") {
                    careType = "医院陪护";
                } else if (order.careType == "HOME_CARE") {
                    careType = "居家照护";
                } else if (order.careType == "REHABILITATION") {
                    careType = "康复护理";
                }
                var payWay = "";
                if (order.payWay == "PAY_ONLINE_WITH_WECHAT") {
                    payWay = "微信支付";
                } else {
                    payWay = "线下支付";
                }
                var status = "";
                if (order.status == "ORDER_UN_PAY") {
                    status = "已下单待付款";
                } else if (order.status == "ORDER_PAYED") {
                    status = "已付款待指派";
                } else if (order.status == "ORDER_ASSIGN") {
                    status = "已指派待服务";
                } else if (order.status == "ORDER_PROCESSING") {
                    status = "服务中";
                } else if (order.status == "ORDER_FINISH") {
                    status = "已完成";
                }
                $("#orderId").html(order.id);
                $("#careType").html(careType);
                $("#userName").html(userProfile.nickName);
                $("#userMobile").html(userProfile.account);
                $("#orderTime").html(order.orderTime);
                $("#totalFee").html(order.totalFee);
                $("#status").html(status);
                $("#payWay").html(payWay);
                $("#payTime").html(order.payTime);
                $("#serviceAddress").html(orderExt.serviceAddress);

                var dealStatusTd= "";
                if (change.dealStatus == "APPLY") {
                    dealStatusTd = "已申请";
                } else if (change.dealStatus == "AGREE") {
                    dealStatusTd = "已同意";
                } else if (change.dealStatus == "REJECT") {
                    dealStatusTd = "已驳回";
                } else if (change.dealStatus == "COMPLETED") {
                    dealStatusTd = "已完成";
                }
                $("#createTime").html(change.createTime);
                $("#changeDesc").html(change.changeDesc);
                $("#dealTime").html(change.dealTime);
                $("#dealStatusTd").html(dealStatusTd);
                $("#dealDescTd").html(change.dealDesc);

                $("#id").val(change.id)
                $("#oldStaffId").val(change.oldStaffId)

                if (change.dealStatus != "APPLY") {
                    $("#dealDiv").hide();
                    $("#agreeButton").hide();
                    $("#rejectButton").hide();
                }
            } else {
                tip.alertError(data.message);
            }
        }
    });
}

function selectStaff(staffType) {
    if (staffType == null || staffType == "") {
        $("#serviceStaffId").empty();
        $("#serviceStaffId").append('<option value="">筛选</option>');
    } else {
        $.ajax({
            type: "GET",
            url: contextPath + "staff/queryStaffByType",
            data: {
                "serviceStaffType": staffType
            },
            beforeSend: function () {
                tip.showLoading();
            },
            success: function (data) {
                tip.hideLoading();
                if (data.result == 200) {
                    $("#newStaffId").empty();
                    var rows = data.rows;
                    for (var i = 0; i < rows.length; i++) {
                        var staff = rows[i];
                        $("#newStaffId").append('<option value="' + staff.id + '">' + staff.userName + '-' + staff.mobile + '</option>');
                    }
                } else {
                    tip.alertError("加载信息失败");
                }
            },
            error: function () {
                tip.hideLoading();
                tip.alertError("加载信息失败");
            }
        });
    }
}