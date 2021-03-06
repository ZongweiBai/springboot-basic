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
                url: contextPath + "/order/staffChange",
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

/**
 * 加载信息
 */
function loadData(orderId) {
    $.ajax({
        type: "GET",
        url: contextPath + "/order/getOrderBasicWithUserInfo",
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
                var staff = info.staff;
                var admin = info.admin;
                if (order != null) {
                    $("#id").val(order.id);
                    $("#orderId").html(order.id);
                }
                if (staff != null) {
                    $("#currentStaffId").html(staff.userName);
                    $("#oldStaffId").val(staff.id);
                }
                if (admin != null) {
                    $("#currentAdminId").html(admin.adminName);
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


