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
                var product = info.product;

                var careType = "";
                if (order.careType == "HOSPITAL_CARE") {
                    careType = "医院陪护";
                } else if (order.careType == "HOME_CARE") {
                    careType = "居家照护";
                } else if (order.careType == "REHABILITATION") {
                    careType = "康复护理";
                    if (!isEmpty(product)) {
                        careType += "/" + product.productName;
                    }
                }
                var payWay = "";
                if (order.payWay == "PAY_ONLINE_WITH_WECHAT") {
                    payWay = "微信支付";
                } else {
                    if (order.payWay == "PAY_OFFLINE_CASH") {
                        payWay = "线下支付-现金";
                    } else if (order.payWay == "PAY_OFFLINE_POS") {
                        payWay = "线下支付-POS";
                    } else if (order.payWay == "PAY_OFFLINE_ALI") {
                        payWay = "线下支付-支付宝";
                    } else if (order.payWay == "PAY_OFFLINE_WECHAT") {
                        payWay = "线下支付-微信";
                    } else {
                        payWay = "线下支付";
                    }
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
                } else if (order.status == "ORDER_FULL_REFUND") {
                    status = "已全额退款";
                }
                $("#orderId").html(order.id);
                $("#careType").html(careType);
                if (!isEmpty(userProfile)) {
                    $("#userName").html(userProfile.nickName);
                    $("#actualName").html(userProfile.actualName);
                    $("#userMobile").html(userProfile.account);
                }
                $("#orderTime").html(order.orderTime);
                $("#totalFee").html(order.totalFee);
                $("#status").html(status);
                $("#payWay").html(payWay);
                $("#payTime").html(order.payTime);
                $("#remark").html(order.remark);
                $("#serviceAddress").html(orderExt.serviceAddress);

                $("#serviceStartTime").html(orderExt.serviceStartTime);
                $("#serviceEndDate").html(orderExt.serviceEndDate);
                if (!isEmpty(orderExt.serviceDuration)) {
                    $("#serviceDuration").html(orderExt.serviceDuration + " 天");
                }
                if (!isEmpty(orderExt.serviceNumber)) {
                    $("#serviceDuration").html(orderExt.serviceNumber + " 次");
                }
                $("#contact").html(orderExt.contact);
                $("#contactMobile").html(orderExt.contactMobile);
                $("#hospitalAddress").html(orderExt.hospitalAddress);
                var patientInfo = orderExt.patientInfo;
                if (!isEmpty(patientInfo)) {
                    var DISEASES = patientInfo.DISEASES;
                    if (!isEmpty(DISEASES)) {
                        var DISEASES_TD = '';
                        DISEASES.forEach(function(item,index) {
                            DISEASES_TD += item.itemName + "<br>";
                        })
                        $("#DISEASES").html(DISEASES_TD);
                    }

                    var SELF_CARE = patientInfo.SELF_CARE;
                    if (!isEmpty(SELF_CARE)) {
                        var SELF_CARE_TD = '';
                        SELF_CARE.forEach(function(item,index) {
                            SELF_CARE_TD += item.itemName + "<br>";
                        })
                        $("#SELF_CARE").html(SELF_CARE_TD);
                    }

                    var EATING = patientInfo.EATING;
                    if (!isEmpty(EATING)) {
                        var EATING_TD = '';
                        EATING.forEach(function(item,index) {
                            EATING_TD += item.itemName + "<br>";
                        })
                        $("#EATING").html(EATING_TD);
                    }

                    var CATHETER_CARE = patientInfo.CATHETER_CARE;
                    if (!isEmpty(CATHETER_CARE)) {
                        var CATHETER_CARE_TD = '';
                        CATHETER_CARE.forEach(function(item,index) {
                            CATHETER_CARE_TD += item.itemName + "<br>";
                        })
                        $("#CATHETER_CARE").html(CATHETER_CARE_TD);
                    }

                    var ASSIST_WITH_MEDICATION = patientInfo.ASSIST_WITH_MEDICATION;
                    if (!isEmpty(ASSIST_WITH_MEDICATION)) {
                        var ASSIST_WITH_MEDICATION_TD = '';
                        ASSIST_WITH_MEDICATION.forEach(function(item,index) {
                            ASSIST_WITH_MEDICATION_TD += item.itemName + "<br>";
                        })
                        $("#ASSIST_WITH_MEDICATION").html(ASSIST_WITH_MEDICATION_TD);
                    }
                }


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