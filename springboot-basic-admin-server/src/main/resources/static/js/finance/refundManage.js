$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "finance/queryRefundForPage",
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
                field: 'orderId',
                title: '订单号',
                align: 'center'
            },
            {
                field: 'careType',
                title: '产品名称',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == "HOSPITAL_CARE") {
                        return "医院陪护";
                    } else if (value == "HOME_CARE") {
                        return "居家照护";
                    } else if (value == "REHABILITATION") {
                        return "康复护理";
                    }
                    return "";
                }
            },
            {
                field: 'refundTime',
                title: '申请时间',
                align: 'center',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value, "yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                field: 'refundDuration',
                title: '退款数量',
                align: 'center'
            },
            {
                field: 'refundFee',
                title: '退款金额',
                align: 'center',
                formatter: function (value, row, index) {
                    return "￥ " + value;
                }
            },
            {
                field: 'bankAccountUserName',
                title: '退款人帐户',
                align: 'center'
            },
            {
                field: 'bankName',
                title: '退款人开户行',
                align: 'center'
            },
            {
                field: 'bankAccountNumber',
                title: '退款人银行帐号',
                align: 'center'
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
                    if (value == "COMPLETED") {
                        return "已退款";
                    } else if (value == "AGREE") {
                        return "未退款";
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
                    var orderId = row.orderId;
                    if (dealStatus == "AGREE") {
                        content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="finishRefund(\'' + value + '\')" title="完成退款"><i style="font-size: 18px;" class="Hui-iconfont">&#xe6e1;</i></a>&nbsp';
                    }
                    content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="viewOrderInfo(\'' + orderId + '\')" title="查看详情"><i style="font-size: 18px;" class="Hui-iconfont">&#xe695;</i></a>&nbsp;';
                    return content;
                }
            }

        ]
    });
}

function finishRefund(refundId) {
    tip.confirm("该退款是否已拨款?", function () {
        $.ajax({
            type: "POST",
            url: contextPath + "/finance/finishRefund",
            data: {
                "refundId": refundId
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
