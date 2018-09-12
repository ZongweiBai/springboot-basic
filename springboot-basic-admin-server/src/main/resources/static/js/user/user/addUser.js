$(function () {
    initForm();

    if (userId != null) {
        $("#password-div").remove();
        loadData(userId);
    }
});

function initForm() {
    $("#form-menu-add").Validform({
        tiptype: 2,
        callback: function (form) {
            $.ajax({
                type: "POST",
                url: contextPath + "/user/saveUser",
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
function loadData(userId) {
    $.ajax({
        type: "GET",
        url: contextPath + "/user/getUserById",
        data: {
            "userId": userId
        },
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var info = data.info;
                $("#userId").val(info.id);
                $("#nickName").val(info.nickName);
                $("#account").val(info.account);
                $("#birthdayStr").val(getSmpFormatDateByLong(info.birthday, "yyyy-MM-dd"));
                $("input[name='sex'][value='"+info.sex+"']").attr("checked",true);
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


