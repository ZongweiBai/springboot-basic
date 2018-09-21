
$(function () {

    initForm();

    if (!isEmpty(dictId)) {
        $("#dictId").val(dictId);
        loadDictData(dictId);
    }
});

function initForm() {
    $("#form-menu-add").Validform({
        tiptype: 2,
        callback: function (form) {
            $.ajax({
                type: "POST",
                url: contextPath + "/system/saveSysDict",
                data: {
                    "id": $("#dictId").val(),
                    "dictName": $("#dictName").val(),
                    "codeValue": $("#codeValue").val(),
                    "codeKey": $("#codeValue").val()
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
 * 加载字典信息
 * @param dictId
 */
function loadDictData(dictId) {
    $.ajax({
        type: "GET",
        url: contextPath + "system/getDictById",
        data: {
            "dictId": dictId
        },
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var info = data.info;
                if (!isEmpty(info)) {
                    $("#dictName").val(info.dictName);
                    $("#codeValue").val(info.codeValue);
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