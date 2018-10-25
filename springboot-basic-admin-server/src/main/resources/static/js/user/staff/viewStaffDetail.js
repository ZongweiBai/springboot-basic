$(function () {

    loadUserInfo(staffId);

});

function loadUserInfo(staffId) {
    $.ajax({
        type: "GET",
        url: contextPath + "/staff/viewStaffDetail",
        data: {
            "staffId": staffId
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
                var staff = info.staff;

                $("#userName").html(staff.userName);
                if (staff.sex == "M") {
                    $("#sex").html("女");
                } else {
                    $("#sex").html("男");
                }
                $("#experience").html(staff.experience);
                $("#height").html(staff.height);
                $("#weight").html(staff.weight);
                $("#nationality").html(staff.nationality);
                $("#birthplace").html(staff.birthplace);
                $("#localism").html(staff.localism);
                $("#mandarin").html(staff.mandarin);
                $("#localtion").html(staff.localtion);
                $("#firstSkill").html(staff.firstSkill);
                $("#specialty").html(staff.specialty);
                $("#idCard").html(staff.idCard);
                $("#healthCardId").html(staff.healthCardId);
                $("#pensionCardId").html(staff.pensionCardId);
                $("#healthCareCardId").html(staff.healthCareCardId);

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
                        orderTBody += '<td id="viewOrder"><a href="javascript:void(0);" onclick="viewOrderDetail(\''+order.id+'\')">查看详情</a></td>';
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

function fillImageUpload(fileNames) {
    $("#headImg").val(fileNames);
    var content = "<span class=\"pic3\">";
    content += "<a data-lightbox=\"" + imgPath + fileNames + "\" href=\"" + imgPath + fileNames + "\">";
    content += "<img src=\"" + imgPath + fileNames + "\" >";
    content += "</a>";
    content += "</span>";
    $("#headerImgDiv").html(content);
}

function viewOrderDetail(orderId) {
    tip.openIframe("订单详情", contextPath + 'index/order/detail?orderId=' + orderId);
}