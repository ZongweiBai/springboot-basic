$(function () {

    loadDataInfo(orderId);

});

function loadDataInfo(orderId) {
    $.ajax({
        type: "GET",
        url: contextPath + "/order/viewOrderDetail",
        data: {
            "orderId": orderId
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
                var order = info.order;
                var orderExt = info.orderExt;
                var userProfile = info.user;

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
                var status = "";
                if (order.status == "ORDER_UN_PAY") {
                    status = "已下单待付款";
                } else if (order.status() == "ORDER_PAYED") {
                    status = "已付款待指派";
                } else if (order.status() == "ORDER_ASSIGN") {
                    status = "已指派待服务";
                } else if (order.status() == "ORDER_PROCESSING") {
                    status = "服务中";
                } else if (order.status() == "ORDER_FINISH") {
                    status = "已完成";
                }
                $("#orderId").html(order.id);
                $("#careType").html(careType);
                $("#userName").html(userProfile.nickName);
                $("#userMobile").html(userProfile.account);
                $("#orderTime").html(order.orderTime);
                $("#totalFee").html(order.totalFee);
                $("#status").html(status);
                $("#payWay").html(payWay);
                $("#payTime").html(order.payTime);
                $("#serviceAddress").html(orderExt.serviceAddress);

                var invoice = info.invoice;
                if (invoice != null) {
                    if (invoice.invoiceType == "E") {
                        $("#invoiceType").html("电子");
                    } else {
                        $("#invoiceType").html("纸质");
                    }
                    $("#invoiceHeader").html(invoice.invoiceHeader);
                    $("#taxNo").html(invoice.taxNo);
                    $("#invoiceFee").html(invoice.invoiceFee);
                    $("#Recipient").html(invoice.Recipient);
                    $("#recipientMobile").html(invoice.recipientMobile);
                    $("#recipientAddress").html(invoice.recipientAddress);
                }
            } else {
                tip.alertError(data.message);
            }
        }
    });
}