$(function () {

    loadData(orderId);

    initForm();

});

function initForm() {
    $("#form-menu-add").Validform({
        tiptype: 1,
        callback: function (form) {
            $.ajax({
                type: "POST",
                url: contextPath + "order/editOrder",
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
                        tip.alertSuccess("编辑成功", function () {
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
            "orderId": orderId,
            "type": "advance"
        },
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var info = data.info;
                var order = info.order;
                $("#id").val(order.id);
                $("#orderId").html(order.id);

                careType = order.careType;
                if (order.careType == 'HOSPITAL_CARE') {
                    var content = '<option value="">选择护工</option>';
                    content += '<option value="WORKER">护工</option>';
                    $("#serviceStaffType").html(content);
                    $("#serviceStaffType").val("WORKER");
                    $("#staffSelectDiv").hide();
                    $("#staffSelectSpan").html("选择护工");
                    selectStaff('WORKER');
                } else if (order.careType == 'HOME_CARE') {
                    $("#serviceStaffType").val("WORKER");
                    $("#staffSelectDiv").hide();
                    selectStaff('WORKER');

                    $("#staffSelectSpan").html("选择护工");
                    $("#nurseSelectDiv").show();
                    selectNurse();
                }
            } else {
                tip.alertError("加载订单信息失败");
            }
        },
        error: function () {
            tip.hideLoading();
            tip.alertError("加载订单信息失败");
        }
    });
}

var productFee = 0.0;
var serviceDuration = 0;

function getTimeStamp(type) {
    if (type == '1') {
        var stringTime = $("#datemin").val();
        if (isEmpty(stringTime)) {
            $("#serviceStartDate").val("");
        } else {
            $("#serviceStartDate").val(datetime_to_unix(stringTime));
        }
    } else {
        var stringTime = $("#datemax").val();
        if (isEmpty(stringTime)) {
            $("#serviceEndDate").val("");
        } else {
            $("#serviceEndDate").val(datetime_to_unix(stringTime));
        }
    }
    var startDate = $("#serviceStartDate").val();
    var endDate = $("#serviceEndDate").val();
    if (!isEmpty(startDate) && !isEmpty(endDate)) {
        var durationHour = (parseInt(endDate) - parseInt(startDate)) / 1000 / 3600;
        var durationDays = durationHour / 24;
        var modHours = durationHour % 24;
        if (modHours > 12) {
            serviceDuration = Math.floor(durationDays) + 1;
        } else if (modHours < 1) {
            serviceDuration = Math.floor(durationDays);
        } else {
            serviceDuration = Math.floor(durationDays) + 0.5;
        }
    } else {
        serviceDuration = 0;
    }
    $("#serviceDuration").val(serviceDuration);
    $("#serviceDurationSpan").html(serviceDuration + " 天");

    calculateTotalFee();
}

function calculateTotalFee() {
    var fee = productFee + basicItemPrice;
    var totalFee = serviceDuration * fee;
    $("#totalFee").val(totalFee.toFixed(2));
}