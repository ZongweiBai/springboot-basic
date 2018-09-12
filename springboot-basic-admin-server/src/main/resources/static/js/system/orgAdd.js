$(function () {

    loadRoleData();

    initForm();

    if (!isEmpty(userId)) {
        $("#userId").val(userId);
        loadUserData(userId);
    }

});

function initForm() {
    $("#form-menu-add").Validform({
        tiptype: 2,
        callback: function (form) {
            $.ajax({
                type: "POST",
                url: contextPath + "/system/saveAdmin",
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
 * 加载角色信息
 * @param roleId
 */
function loadRoleData() {
    $.ajax({
        type: "GET",
        url: contextPath + "/system/getAllRoleList",
        data: {},
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var rows = data.rows;
                var content = '<option value="">请选择所属角色</option>';
                for (var i = 0; i < rows.length; i++) {
                    var roleObj = rows[i];
                    content += '<option value="' + roleObj.id + '">' + roleObj.roleName + '</option>';
                }
                $("#roleId").html(content);
            } else {
                tip.alertError("加载角色信息失败");
            }
        },
        error: function () {
            tip.hideLoading();
            tip.alertError("加载角色信息失败");
        }
    });
}

/**
 * 加载用户信息
 */
function loadUserData(userId) {
    $.ajax({
        type: "GET",
        url: contextPath + "/system/getAdminById",
        data: {
            "userId": userId,
        },
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var info = data.info;
                if (!isEmpty(info)) {
                    $("#account").val(info.account);
                    $("#mobile").val(info.mobile);
                    $("#roleId").val(info.roleId);
                }
            } else {
                tip.alertError("加载角色信息失败");
            }
        },
        error: function () {
            tip.hideLoading();
            tip.alertError("加载角色信息失败");
        }
    });
}

