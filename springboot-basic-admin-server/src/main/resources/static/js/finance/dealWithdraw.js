$(function () {
    $("#withdrawId").val(withdrawId);

    initForm();

});

function initForm() {
    $("#form-menu-add").Validform({
        tiptype: 2,
        callback: function (form) {
            $.ajax({
                type: "POST",
                url: contextPath + "finance/dealWithdraw",
                data: $('#form-menu-add').serialize(),
                beforeSend: function () {
                    tip.showLoading();
                },
                error: function () {
                    tip.hideLoading();
                    tip.alertError("处理失败");
                },
                success: function (data) {
                    tip.hideLoading();
                    if (data.result == "200") {
                        tip.alertSuccess("处理成功", function () {
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

