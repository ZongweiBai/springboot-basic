$(function () {

    initForm();

    if (!isEmpty(orgId)) {
        $("#orgId").val(orgId);
        loadUserData(orgId);
    }

});

function initForm() {
    $("#form-menu-add").Validform({
        tiptype: 2,
        callback: function (form) {
            $.ajax({
                type: "POST",
                url: contextPath + "/system/saveOrg",
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
function loadUserData(orgId) {
    $.ajax({
        type: "GET",
        url: contextPath + "/system/getOrgById",
        data: {
            "orgId": orgId,
        },
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var info = data.info;
                if (!isEmpty(info)) {
                    $("#orgName").val(info.orgName);
                    $("#orgDesc").html(info.orgDesc);
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

