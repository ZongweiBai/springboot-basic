var menuId = getQueryString("menuId");

$(function () {

    initForm();

    loadParentMenu();

    if (!isEmpty(menuId)) {
        $("#menuId").val(menuId);
        loadMenuData(menuId);
    }
});

function initForm() {
    $("#form-menu-add").Validform({
        tiptype: 2,
        callback: function (form) {
            $.ajax({
                type: "POST",
                url: contextPath + "/system/sysManageController/saveMenu",
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

function loadParentMenu() {
    $.ajax({
        type: "POST",
        url: contextPath + "/system/sysManageController/getMainMenuList",
        data: {},
        success: function (data) {
            if (data.result == 200) {
                var rows = data.rows;
                var content = '<option value="">选择上级菜单</option>';
                for (var i = 0; i < rows.length; i++) {
                    var menuObj = rows[i];
                    content += '<option value="' + menuObj.menuId + '">' + menuObj.menuName + '</option>';
                }
                $("#parentId").html(content);
            }
        }
    });
}

/**
 * 选择菜单等级
 * @param obj
 */
function selectLevel(obj) {
    var level = $(obj).val();
    if (level == 1) {
        $("#parentMenuDiv").hide();
        $("#parentId").removeAttr("datatype");

        $("#menuUrl").val("/");

        $("#menuIconDiv").show();
        $("#menuIcon").attr("datatype", "*");
    } else if (level == 2) {
        $("#parentMenuDiv").show();
        $("#parentId").attr("datatype", "*");

        $("#menuUrl").val("/");

        $("#menuIconDiv").hide();
        $("#menuIcon").removeAttr("datatype");
    }
}

/**
 * 加载菜单信息
 * @param menuId
 */
function loadMenuData(menuId) {
    $.ajax({
        type: "POST",
        url: contextPath + "/system/sysManageController/getMenuById",
        data: {
            "menuId": menuId
        },
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var info = data.info;
                if (!isEmpty(info)) {
                    $("#menuName").val(info.menuName);
                    if (info.level == 1) {
                        $("#level").val("1");
                    } else if (info.level == 2) {
                        $("#level").val("2");
                        $("#parentId").val(info.parentId);
                    }
                    selectLevel($("#level"));
                    $("#menuUrl").val(info.menuUrl);
                    $("#menuIcon").val(info.menuIcon);
                }
            } else {
                tip.alertError("加载菜单信息失败");
            }
        },
        error: function () {
            tip.hideLoading();
            tip.alertError("加载菜单信息失败");
        }
    });
}