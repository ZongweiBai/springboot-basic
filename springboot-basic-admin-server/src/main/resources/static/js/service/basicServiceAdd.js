$(function () {

    initForm();

    if (!isEmpty(itemId)) {
        $("#itemId").val(itemId);
        loadRoleData(itemId);
    }

});

function initForm() {
    $("#form-menu-add").Validform({
        tiptype: 2,
        callback: function (form) {
            $.ajax({
                type: "POST",
                url: contextPath + "/item/saveItem",
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
function loadRoleData(itemId) {
    $.ajax({
        type: "GET",
        url: contextPath + "/item/getItemById",
        data: {
            "itemId": itemId
        },
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var info = data.info;
                $("#basicItemType").val(info.basicItemType);
                $("#itemName").val(info.itemName);
                $("#itemFee").val(info.itemFee);
                $("#status").val(info.status);
                $("#description").html(info.description);
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
