var driverId = getQueryString("driverId");

$(function () {

    loadUserInfo(driverId);

});

function loadUserInfo(driverId) {
    $.ajax({
        type: "GET",
        url: contextPath + "/user/driverController/viewDriverInfo",
        data: {
            "driverId": driverId
        },
        beforeSend: function () {
            tip.showLoading();
        },
        error: function () {
            tip.hideLoading();
            tip.alertError("查询失败");
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == "200") {
                var info = data.info;
                $("#id").val(info.id);
                $("#platformRating").html(info.platformRating);
                $("#userRating").html(info.userRating);
                $("#driverName").html(info.driverName);
                $("#userAccount").html(info.account.account);
                $("#onLineStatus").html(info.onLineStatus);
                if (!isEmpty(info.headImg)) {
                    fillImageUpload(info.headImg);
                }
            } else {
                tip.alertError(data.message);
            }
        }
    });
}

function fillImageUpload(fileNames) {
    $("#headImg").val(fileNames);
    var content = "<span class=\"pic3\">";
    content += "<a data-lightbox=\"" + imgPath + fileNames + "\" href=\"" + imgPath + fileNames + "\">";
    content += "<img src=\"" + imgPath + fileNames + "\" >";
    content += "</a>";
    content += "</span>";
    $("#headerImgDiv").html(content);
}