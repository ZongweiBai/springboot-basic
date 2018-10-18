$(function () {
    initForm();

    loadData(orderId);
});

function initForm() {
    $("#form-menu-add").Validform({
        tiptype: 2,
        callback: function (form) {
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

                careType = order.careType;
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


