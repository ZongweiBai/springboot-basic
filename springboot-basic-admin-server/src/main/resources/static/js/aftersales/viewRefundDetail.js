$(function () {

    loadDataInfo(refundId);

});

function applyFund() {
    $("#form-menu-add").Validform({
        tiptype: 2,
        callback: function (form) {
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
    if (dealDesc == "") {
        tip.alertError("处理备注不能为空")
        return;
    }
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

                $("#refundId").html(refund.id);
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
                $("#totalFee").html(order.totalFee);
                $("#createTime").html(refund.createTime);
                if (order.careType == 'HOSPITAL_CARE' || order.careType == 'HOME_CARE') {
                    $("#refundFeeTd").html(refund.refundFee + " 天");
                } else {
                    $("#refundFeeTd").html(refund.refundFee + " 次");
                }
                $("#refundDesc").html(refund.refundDesc);
                $("#serviceDuration").html(orderExt.serviceDuration);

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