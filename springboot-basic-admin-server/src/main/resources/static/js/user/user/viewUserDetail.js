$(function () {

    loadUserInfo(userId);

});

function loadUserInfo(userId) {
    $.ajax({
        type: "GET",
        url: contextPath + "/user/viewUserDetail",
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
                var userProfile = info.userProfile;
                $("#nickName").html(userProfile.nickName);
                $("#account").html(userProfile.account);
                if (userProfile.sex == "M") {
                    $("#sex").html("男");
                } else {
                    $("#sex").html("女");
                }
                if (userProfile.birthday != null) {
                    $("#birthday").html(getFormatDateByLong(userProfile.birthday, "yyyy年MM月dd日"));
                }
                $("#registerTime").html(userProfile.registerTime);

                var addressList = info.addressList;
                if (addressList != null) {
                    var addressTBody = '';
                    for (var i = 0; i < addressList.length; i++) {
                        var address = addressList[i];
                        var addressType = "医院";
                        var addressDetail = "";
                        if (address.addressType == "M") {
                            addressType = "居家";
                            addressDetail = address.community + address.communityDetail;
                        } else {
                            addressDetail = address.hospital + address.department + address.hospitalDetail;
                        }
                        addressTBody += '<tr class="text-c">';
                        addressTBody += '<td id="addressType">'+addressType+'</td>';
                        addressTBody += '<td id="createTime">'+getFormatDateByLong(address.createTime, true)+'</td>';
                        addressTBody += '<td id="addressDetail">'+addressDetail+'</td>';
                        addressTBody += '</tr>';
                    }
                    $("#addressTBody").html(addressTBody);
                }

                var orderList = info.orderList;
                if (orderList != null) {
                    var orderTBody = '';
                    for (var i = 0; i < orderList.length; i++) {
                        var order = orderList[i];
                        var careType = "";
                        if (order.careType == "HOSPITAL_CARE") {
                            careType = "医院陪护";
                        } else if (order.careType == "HOME_CARE") {
                            careType = "居家照护";
                        } else if (order.careType == "REHABILITATION") {
                            careType = "康复护理";
                        }
                        var payWay = "";
                        if (order.payWay == "PAY_ONLINE_WITH_WECHAT") {
                            payWay = "微信支付";
                        } else {
                            payWay = "线下支付";
                        }
                        var orderSource = "";
                        if (order.orderSource == "WECHAT") {
                            orderSource = "微信";
                        } else {
                            orderSource = "PC";
                        }
                        var status = "";
                        if (order.status == "ORDER_UN_PAY") {
                            status = "已下单待付款";
                        } else if (order.status == "ORDER_PAYED") {
                            status = "已付款待指派";
                        } else if (order.status == "ORDER_ASSIGN") {
                            status = "已指派待服务";
                        } else if (order.status == "ORDER_PROCESSING") {
                            status = "服务中";
                        } else if (order.status == "ORDER_FINISH") {
                            status = "已完成";
                        }
                        orderTBody += '<tr class="text-c">';
                        orderTBody += '<td id="orderId">'+order.id+'</td>';
                        orderTBody += '<td id="careType">'+careType+'</td>';
                        orderTBody += '<td id="orderTime">'+order.orderTime+'</td>';
                        orderTBody += '<td id="totalFee">'+order.totalFee+'</td>';
                        orderTBody += '<td id="payWay">'+payWay+'</td>';
                        orderTBody += '<td id="status">'+status+'</td>';
                        orderTBody += '<td id="orderSource">'+orderSource+'</td>';
                        orderTBody += '</tr>';
                    }
                    $("#orderTBody").html(orderTBody);
                }
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