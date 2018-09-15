$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "afterSales/queryStaffchangeForPage",
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
                field: 'refundTime',
                title: '退款金额',
                align: 'center',
                formatter: function (value, row, index) {
                    return "￥ " + value;
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
                field: 'status',
                title: '申请状态',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == "APPLY") {
                        return "已申请";
                    } else if (value == "AGREE") {
                        return "已同意";
                    } else if (value == "REJECT") {
                        return "已驳回";
                    } else if (value == "COMPLETED") {
                        return "已完成";
                    }
                    return "";
                }
            },
            {
                field: 'id',
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    var status = row.status;
                    var content = ''
                    content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="viewOrderInfo(\'' + value + '\')" title="查看详情"><i style="font-size: 18px;" class="Hui-iconfont">&#xe695;</i></a>&nbsp;';
                    if (status == "ORDER_UN_PAY") {
                        content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="offlinePay(\'' + value + '\')" title="收款"><i style="font-size: 18px;" class="Hui-iconfont">&#xe63a;</i></a>&nbsp;';
                    } else if (status == "ORDER_PAYED") {
                        content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="orderAssign(\'' + value + '\')" title="指派"><i style="font-size: 18px;" class="Hui-iconfont">&#xe645;</i></a>&nbsp;';
                    }
                    if (status != "ORDER_UN_PAY" && status != "ORDER_FINISH") {
                        content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="orderRefund(\'' + value + '\')" title="退款申请"><i style="font-size: 18px;" class="Hui-iconfont">&#xe628;</i></a>&nbsp;';
                    }
                    if (status == "ORDER_ASSIGN" || status == "ORDER_PROCESSING") {
                        content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="staffChange(\'' + value + '\')" title="换人"><i style="font-size: 18px;" class="Hui-iconfont">&#xe68f;</i></a>&nbsp;';
                    }
                    return content;
                }
            }

        ]
    });
}

/**
 * 查看详情
 * @param orderId
 */
function viewOrderInfo(orderId) {
    tip.openIframe("订单详情", contextPath + 'index/order/detail?orderId=' + orderId);
}

/**
 * 订单指派
 * @param orderId
 */
function orderAssign(orderId) {
    tip.openIframe("订单指派", contextPath + 'index/order/assign?orderId=' + orderId, 550, 350, refreshData);
}

/**
 * 退款申请
 * @param orderId
 */
function orderRefund(orderId) {
    tip.openIframe("退款申请", contextPath + 'index/order/refund?orderId=' + orderId, 600, 350, refreshData);
}

/**
 * 换人
 * @param orderId
 */
function staffChange(orderId) {
    tip.openIframe("换人", contextPath + 'index/order/staffchange?orderId=' + orderId, 550, 350, refreshData);
}

/**
 * 代收款
 * @param orderId
 */
function offlinePay(orderId) {
    tip.openIframe("代收款", contextPath + 'index/order/offlinePay?orderId=' + orderId, 600, 350, refreshData);
}


var refreshData = function () {
    $('#menuTable').bootstrapTable('refresh');
}
