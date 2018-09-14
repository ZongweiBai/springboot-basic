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
                url: contextPath + "/order/assignOrderStaff",
                data: {
                    "orderId": $("#orderId").html(),
                    "staffId": $("#serviceStaffId").val()
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
    });
}

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
                $("#id").val(order.id);
                $("#orderId").html(order.id);
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

function selectStaff(staffType) {
    if (staffType == null || staffType == "") {
        $("#serviceStaffId").empty();
        $("#serviceStaffId").append('<option value="">筛选</option>');
    } else {
        $.ajax({
            type: "GET",
            url: contextPath + "/staff/queryStaffByType",
            data: {
                "serviceStaffType": staffType
            },
            beforeSend: function () {
                tip.showLoading();
            },
            success: function (data) {
                tip.hideLoading();
                if (data.result == 200) {
                    $("#serviceStaffId").empty();
                    var rows = data.rows;
                    for (var i = 0; i < rows.length; i++) {
                        var staff = rows[i];
                        $("#serviceStaffId").append('<option value="' + staff.id + '">' + staff.userName + '-' + staff.mobile + '</option>');
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


