$(function () {

    loadDataInfo(refundId);

});

function applyFund() {
    $("#form-menu-add").Validform({
        tiptype: 2,
        callback: function (form) {
            var refundDuration = $("#refundDuration").val();
            var refundFee = $("#refundFee").val();
            if (refundDuration > serviceDuration) {
                if (careType == 'HOSPITAL_CARE' || careType == 'HOME_CARE') {
                    tip.alertError("退款天数不能大于购买天数！")
                } else {
                    tip.alertError("退款次数不能大于购买次数！")
                }
                return false;
            }
            if (refundFee > totalFee) {
                tip.alertError("退款金额不能大于订单金额！")
                return false;
            }

            $.ajax({
                type: "POST",
                url: contextPath + "afterSales/dealOrderRefund",
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
    var dealDesc = $("#dealDesc").val();
    // if (dealDesc == "") {
    //     tip.alertError("处理备注不能为空")
    //     return;
    // }
    $.ajax({
        type: "POST",
        url: contextPath + "afterSales/dealOrderRefund",
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
}

var careType;
var serviceDuration;
var totalFee;

function loadDataInfo(refundId) {
    $.ajax({
        type: "GET",
        url: contextPath + "afterSales/getRefundInfo",
        data: {
            "refundId": refundId
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
                var refund = info.refund;

                $("#refundId").html(order.id);
                var dealStatusTd = "";
                if (refund.dealStatus == "APPLY") {
                    dealStatusTd = "已申请";
                } else if (refund.dealStatus == "AGREE") {
                    dealStatusTd = "已同意";
                } else if (refund.dealStatus == "REJECT") {
                    dealStatusTd = "已驳回";
                } else if (refund.dealStatus == "COMPLETED") {
                    dealStatusTd = "已完成";
                }
                $("#dealStatusTd").html(dealStatusTd);
                $("#orderIdTd").html(order.id);
                $("#totalFee").html(order.totalFee + " 元");
                $("#createTime").html(refund.createTime);
                $("#refundFeeTd").html(refund.refundFee + " 元");
                if (order.careType == 'HOSPITAL_CARE' || order.careType == 'HOME_CARE') {
                    $("#serviceDuration").html(orderExt.serviceDuration + "天");
                    serviceDuration = orderExt.serviceDuration;
                } else if (order.careType == 'REHABILITATION') {
                    $("#serviceDuration").html(orderExt.serviceNumber + "次");
                    serviceDuration = orderExt.serviceNumber;
                } else {
                    $("#serviceDuration").html(orderExt.serviceDuration + "次");
                    serviceDuration = orderExt.serviceDuration;
                }
                $("#refundDesc").html(refund.refundDesc);
                if (order.careType == 'HOSPITAL_CARE' || order.careType == 'HOME_CARE') {
                    $("#refundDurationTd").html(refund.refundDuration + "天");
                } else {
                    $("#refundDurationTd").html(refund.refundDuration + "次");
                }
                if (!isEmpty(refund.beginRefundPeriod) && !isEmpty(refund.endRefundPeriod)) {
                    var begin = parseInt(refund.beginRefundPeriod);
                    var end = parseInt(refund.endRefundPeriod);
                    $("#refundPeriod").html(getSmpFormatDateByLong(begin, true) + " 至 " + getSmpFormatDateByLong(end, true));
                }

                $("#bankAccountUserName").html(refund.bankAccountUserName);
                $("#bankName").html(refund.bankName);
                $("#bankAccountNumber").html(refund.bankAccountNumber);

                careType = order.careType;
                totalFee = order.totalFee;

                $("#id").val(refund.id);
                $("#refundDuration").val(refund.refundDuration);
                $("#refundFee").val(refund.refundFee);
                $("#orderId").val(order.id);

                if (refund.dealStatus != "APPLY") {
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