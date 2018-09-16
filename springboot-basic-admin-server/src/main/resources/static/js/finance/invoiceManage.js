$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "finance/queryInvoiceForPage",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                limit: params.limit,  //页面大小
                offset: params.offset / params.limit,
                sort: params.sort,
                order: params.order,
                orderId: $("#orderId").val(),
                dealStatus: $("#dealStatus").val(),
                datemin: $("#datemin").val(),
                datemax: $("#datemax").val()
            };
            return paramsMap;
        },
        pageNumber: 1,
        pageSize: 10,//单页记录数
        pageList: [10, 20, 30, 50],//分页步进值
        pagination: true, //分页
        sidePagination: "server", //服务端处理分页
        columns: [
            {
                field: 'orderIds',
                title: '订单号',
                align: 'center',
                formatter: function (value, row, index) {
                    // var orderIds = value.split(",");
                    // var html = '';
                    // for (var i = 0; i < orderIds; i++) {
                    //     html += orderIds[i] + '&nbsp;';
                    // }
                    return value;
                }
            },
            {
                field: 'invoiceType',
                title: '发票类型',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == "E") {
                        return "电子发票";
                    } else if (value == "P") {
                        return "纸质发票";
                    }
                    return "";
                }
            },
            {
                field: 'headerType',
                title: '抬头类型',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == "C") {
                        return "企业单位";
                    } else if (value == "P") {
                        return "个人";
                    }
                    return "";
                }
            },
            {
                field: 'invoiceHeader',
                title: '发票抬头',
                align: 'center'
            },
            {
                field: 'taxNo',
                title: '税号',
                align: 'center'
            },
            {
                field: 'invoiceFee',
                title: '开票金额',
                align: 'center'
            },
            {
                field: 'recipient',
                title: '收货人',
                align: 'center'
            },
            {
                field: 'recipientMobile',
                title: '收货人手机',
                align: 'center'
            },
            {
                field: 'recipientAddress',
                title: '收货地址',
                align: 'center'
            },
            {
                field: 'createTime',
                title: '申请时间',
                align: 'center',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value, "yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                field: 'dealTime',
                title: '处理时间',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == null || value == "") {
                        return "-";
                    }
                    return getFormatDateByLong(value, "yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                field: 'dealStatus',
                title: '申请状态',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == "AGREE") {
                        return "已开票";
                    } else if (value == "APPLY") {
                        return "未开票";
                    }
                    return "";
                }
            },
            {
                field: 'id',
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    var content = '';
                    var dealStatus = row.dealStatus;
                    if (dealStatus == "APPLY") {
                        content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="finishRefund(\'' + value + '\')" title="完成开票"><i style="font-size: 18px;" class="Hui-iconfont">&#xe6e1;</i></a>&nbsp';
                    }
                    return content;
                }
            }

        ]
    });
}

function finishRefund(invoiceId) {
    tip.confirm("该发票是否已开?", function () {
        $.ajax({
            type: "POST",
            url: contextPath + "/finance/finishInvoice",
            data: {
                "invoiceId": invoiceId
            },
            success: function (data) {
                if (data.result == 200) {
                    tip.alertSuccess("操作成功", refreshData);
                } else {
                    tip.alertError("操作失败");
                }
            }
        });
    })
}

/**
 * 查看详情
 * @param orderId
 */
function viewOrderInfo(orderId) {
    tip.openIframe("订单详情", contextPath + 'index/order/detail?orderId=' + orderId);
}

var refreshData = function () {
    $('#menuTable').bootstrapTable('refresh');
}
