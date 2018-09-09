$(function () {

    initForm();

    if (!isEmpty(roleId)) {
        $("#roleId").val(roleId);
        loadRoleData(roleId);
    } else {
        loadMenuData();
    }

});

function initForm() {
    $("#form-menu-add").Validform({
        tiptype: 2,
        callback: function (form) {
            $.ajax({
                type: "POST",
                url: contextPath + "/system/saveRole",
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
 * 加载角色菜单信息
 * @param roleId
 */
function loadRoleData(roleId) {
    $.ajax({
        type: "GET",
        url: contextPath + "/system/getMenuByRoleId",
        data: {
            "roleId": roleId
        },
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var info = data.info;
                $("#roleName").val(info.roleName);
                $("#roleType").val(info.roleType);
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
 * 加载菜单信息
 */
function loadMenuData() {
    $.ajax({
        type: "POST",
        url: contextPath + "/system/getAllMenu",
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
    for (var i = 0; i < rows.length; i++) {
        var mainMenu = rows[i];
        content += '<dl class="permission-list">';
        content += '<dt>';
        if (mainMenu.checked == true) {
            content += '<label><input type="checkbox" value="' + mainMenu.id + '" id="menu_' + i + '" name="menuList[' + menuIndex + '].id" onclick="selectMainMenu(this)" checked="checked" />' + mainMenu.menuName + '</label>';
        } else {
            content += '<label><input type="checkbox" value="' + mainMenu.id + '" id="menu_' + i + '" name="menuList[' + menuIndex + '].id" onclick="selectMainMenu(this)" />' + mainMenu.menuName + '</label>';
        }
        content += '</dt>';
        menuIndex++;
        var subMenuList = mainMenu.subMenuList;
        if (!isEmpty(subMenuList)) {
            content += '<dd><dl class="cl permission-list2">';
            for (var j = 0; j < subMenuList.length; j++) {
                var subMenu = subMenuList[j];
                content += '<dt>';
                content += '<label class="">';
                if (subMenu.checked == true) {
                    content += '<input type="checkbox" value="' + subMenu.id + '" id="menu_' + i + '_' + j + '" name="menuList[' + menuIndex + '].id" onclick="selectSubMenu(this)" checked="checked" />' + subMenu.menuName + '</label>';
                } else {
                    content += '<input type="checkbox" value="' + subMenu.id + '" id="menu_' + i + '_' + j + '" name="menuList[' + menuIndex + '].id" onclick="selectSubMenu(this)" />' + subMenu.menuName + '</label>';
                }
                content += '</dt>';
                menuIndex++;
            }
            content += '</dl></dd>';
        }
        content += '</dl>';
    }
    $("#menuListDiv").html(content);
}

/**
 * 选择主菜单
 * @param obj
 */
function selectMainMenu(obj) {
    $(obj).closest("dl").find("dd input:checkbox").prop("checked", $(obj).prop("checked"));
}

/**
 * 选择子菜单
 * @param obj
 */
function selectSubMenu(obj) {
    var allChecked = true;
    if ($(obj).prop("checked") == false) {
        allChecked = false
    } else {
        var subMenuCheckboxs = $(obj).closest("dl").find("dt input:checkbox");
        for (var i = 0; i < subMenuCheckboxs.length; i++) {
            if ($(subMenuCheckboxs[i]).prop("checked") == false) {
                allChecked = false;
                break;
            }
        }
    }
    var mainMenuObj = $(obj).parents(".permission-list").children("dt").first().find("input:checkbox");
    $(mainMenuObj).prop("checked", allChecked);

}