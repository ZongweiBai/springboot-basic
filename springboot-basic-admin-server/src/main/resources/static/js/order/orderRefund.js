$(function () {
    initForm();

    loadData(orderId);
});

function initForm() {
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
                url: contextPath + "/order/orderRefund",
                data: $('#form-menu-add').serialize(),
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
    });
}

var careType;
var serviceDuration;
var totalFee;
/**
 * 加载信息
 */
function loadData(orderId) {
    $.ajax({
        type: "GET",
        url: contextPath + "/order/getOrderBasic",
        data: {
            "orderId": orderId
        },
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var info = data.info;
                var order = info.order;

                if (order.careType == 'HOSPITAL_CARE' || order.careType == 'HOME_CARE') {
                    $("#serviceDurationLabel").html("购买天数：");
                    $("#refundDurationLabel").html("退款天数：");
                }

                var orderExt = info.orderExt;
                $("#id").val(order.id);
                $("#orderId").html(order.id);
                $("#orderTime").html(order.orderTime);
                $("#orderFeeDisplay").html(order.totalFee);
                $("#serviceDuration").html(orderExt.serviceDuration);

                careType = order.careType;
                serviceDuration = orderExt.serviceDuration;
                totalFee = order.totalFee;
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


