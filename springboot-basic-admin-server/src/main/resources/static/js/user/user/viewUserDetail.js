var userId = getQueryString("userId");

$(function () {

    loadUserInfo(userId);

});

function loadUserInfo(userId) {
    $.ajax({
        type: "GET",
        url: contextPath + "/user/userController/viewUserInfo",
        data: {
            "userId": userId
        },
        beforeSend: function () {
            tip.showLoading();
        },
        error: function () {
            tip.hideLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == "200") {
                var info = data.info;
                $("#nickName").html(info.account.nickName);
                $("#userAccount").html(info.account.account);
                $("#data").html(info.data);
                $("#balance").html(info.balance);
                $("#commission").html(info.commission);
                $("#redPackage").html(info.redPackage);
                $("#bonusPoint").html(info.bonusPoint);
                $("#registerDate").html(getSmpFormatDateByLong(info.registerDate, true));
            } else {
                tip.alertError(data.message);
            }
        }
    });
}
function resetPsd(){
    $.ajax({
        type: "GET",
        url: contextPath + "/user/userController/resetPwd",
        data: {
            "userId": userId
        },
        beforeSend: function () {
            tip.showLoading();
        },
        error: function () {
            tip.hideLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == "200") {
                var info = data.info;
                tip.alertSuccess("重置成功，新密码为："+info, function () {
                    tip.closeIframe();
                });
            } else {
                tip.alertError(data.message);
            }
        }
    });
}