$(function () {

    loadRoleData();

    loadOrgData();

    initForm();

    if (!isEmpty(userId)) {
        $("#userId").val(userId);
        loadUserData(userId);
    } else {
        loadHistoryData();
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
        async: true,
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
 * 加载部门信息
 * @param roleId
 */
function loadOrgData() {
    $.ajax({
        type: "GET",
        url: contextPath + "/system/getAllOrg",
        data: {},
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var rows = data.rows;
                var content = '<option value="">请选择所属部门</option>';
                for (var i = 0; i < rows.length; i++) {
                    var roleObj = rows[i];
                    content += '<option value="' + roleObj.id + '">' + roleObj.orgName + '</option>';
                }
                $("#orgId").html(content);
            } else {
                tip.alertError("加载部门信息失败");
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
                    $("#orgId").val(info.orgId);
                    $("#adminName").val(info.adminName);
                    $("#email").val(info.email);
                    $("#adminNote").text(info.adminNote);

                    parseMenuList(info.hospitalList)
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

/**
 * 加载医院信息
 */
function loadHistoryData() {
    $.ajax({
        type: "GET",
        url: contextPath + "/hospital/getAllHospital",
        data: {},
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var rows = data.rows;
                if (!isEmpty(rows)) {
                    parseMenuList(rows);
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

/**
 * 解析并填充菜单
 * @param rows
 */
function parseMenuList(rows) {
    var menuIndex = 0;
    var content = '';

    content += '<dl class="permission-list">';
    content += '<dt>';
    content += '<label style="margin-right: 10px;"><input type="checkbox" onclick="selectHospitalAll(this)" />全选/全不选</label>'
    // content += '<label><input type="checkbox" onclick="unSelectHospitalAll(this)" />全不选</label>'
    content += '</dt>';
    content += '<dd><dl class="cl permission-list2">';
    for (var i = 0; i < rows.length; i++) {
        var subMenu = rows[i];
        content += '<dt style="width: 50%;">';
        content += '<label class="">';
        if (subMenu.checked == true || isEmpty(userId)) {
            content += '<input type="checkbox" value="' + subMenu.id + '" id="menu_' + i + '" name="hospitalList[' + menuIndex + '].id" checked="checked" />' + subMenu.hospitalName + '</label>';
        } else {
            content += '<input type="checkbox" value="' + subMenu.id + '" id="menu_' + i + '" name="hospitalList[' + menuIndex + '].id" />' + subMenu.hospitalName + '</label>';
        }
        content += '</dt>';
        menuIndex++;
    }
    content += '</dl></dd>';
    content += '</dl>';
    $("#menuListDiv").html(content);
}

function selectHospitalAll(obj) {
    $(obj).closest("dl").find("dd input:checkbox").prop("checked", $(obj).prop("checked"));
}

function unSelectHospitalAll(obj) {
    if ($(obj).prop("checked")) {
        $(obj).closest("dl").find("dd input:checkbox").prop("checked", false);
    }
}