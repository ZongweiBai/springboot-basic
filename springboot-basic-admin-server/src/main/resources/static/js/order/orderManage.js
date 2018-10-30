$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "/order/queryOrderForPage",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                size: params.limit,  //页面大小
                page: params.offset / params.limit,
                careType: $("#careType").val(),
                orderId: $("#orderId").val(),
                status: $("#status").val(),
                payStatus: $("#payStatus").val(),
                orderSource: $("#orderSource").val(),
                datemin: $("#datemin").val(),
                datemax: $("#datemax").val(),
                account: $("#account").val(),
                address: $("#address").val()
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
                field: 'id',
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
                field: 'totalFee',
                title: '订单金额',
                align: 'center',
                formatter: function (value, row, index) {
                    return "￥ " + value;
                }
            },
            {
                field: 'orderTime',
                title: '下单时间',
                align: 'center',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value, "yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                field: 'orderSource',
                title: '下单类型',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == "WECHAT") {
                        return "自主下单";
                    } else {
                        return "代下单";
                    }
                }
            },
            {
                field: 'userProfile',
                title: '下单用户',
                align: 'center',
                formatter: function (value, row, index) {
                    if (!isEmpty(value)) {
                        return value.account;
                    } else {
                        return "-";
                    }
                }
            },
            {
                field: 'orderExt',
                title: '服务地址',
                align: 'center',
                formatter: function (value, row, index) {
                    if (!isEmpty(value)) {
                        return value.serviceAddress;
                    } else {
                        return "-";
                    }
                }
            },
            {
                field: 'payWay',
                title: '付款方式',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == "PAY_ONLINE_WITH_WECHAT") {
                        return "微信支付";
                    } else {
                        return "线下支付";
                    }
                }
            },
            {
                field: 'payTime',
                title: '付款时间',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == "" || value == null) {
                        return "-";
                    }
                    return getFormatDateByLong(value, "yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                field: 'serviceStaff',
                title: '指派护工',
                align: 'center',
                formatter: function (value, row, index) {
                    if (isEmpty(value)) {
                        return "-";
                    }
                    return value.userName;
                }
            },
            {
                field: 'admin',
                title: '监督人员',
                align: 'center',
                formatter: function (value, row, index) {
                    if (isEmpty(value)) {
                        return "-";
                    }
                    return value.adminName;
                }
            },
            {
                field: 'status',
                title: '订单状态',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == "ORDER_UN_PAY") {
                        return "已下单待付款";
                    } else if (value == "ORDER_PAYED") {
                        return "已付款待指派";
                    } else if (value == "ORDER_ASSIGN") {
                        return "已指派待服务";
                    } else if (value == "ORDER_PROCESSING") {
                        return "服务中";
                    } else if (value == "ORDER_FINISH") {
                        return "已完成";
                    } else if (value == "ORDER_FULL_REFUND") {
                        return "已全额退款";
                    }
                    return "";
                }
            },
            {
                field: 'refundStatus',
                title: '退款状态',
                align: 'center',
                formatter: function (value, row, index) {
                    var fullRecord = (row.fullRecord == true ? "全额退款" : "部分退款")
                    if (value == "APPLY") {
                        return "退款待处理 | " + fullRecord;
                    } else if (value == "AGREE") {
                        return "退款审核通过 | " + fullRecord;
                    } else if (value == "COMPLETED") {
                        return "退款已拨款 | " + fullRecord;
                    } else if (value == "REJECT") {
                        return "退款申请驳回";
                    }
                    return "";
                }
            },
            {
                field: 'id',
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    var payWay = row.payWay;
                    var status = row.status;
                    var refundEd = true;
                    if (isEmpty(row.refundStatus)) {
                        refundEd = false;
                    }
                    var payTime = row.payTime;
                    var staffChanged = true;
                    if (isEmpty(row.staffChangeStatus)) {
                        staffChanged = false;
                    }

                    var content = '';
                    content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="viewOrderInfo(\'' + value + '\')" title="查看详情"><i style="font-size: 18px;" class="Hui-iconfont">&#xe695;</i></a>&nbsp;';

                    if (payWay != 'PAY_ONLINE_WITH_WECHAT' && (status == "ORDER_UN_PAY" || isEmpty(payTime))) {
                        content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="offlinePay(\'' + value + '\')" title="收款"><i style="font-size: 18px;" class="Hui-iconfont">&#xe63a;</i></a>&nbsp;';
                    }

                    if (status == "ORDER_PAYED") {
                        content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="orderAssign(\'' + value + '\')" title="指派"><i style="font-size: 18px;" class="Hui-iconfont">&#xe645;</i></a>&nbsp;';
                    }

                    if (status != "ORDER_FINISH" && !refundEd && !isEmpty(payTime)) {
                        content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="orderRefund(\'' + value + '\')" title="退款申请"><i style="font-size: 18px;" class="Hui-iconfont">&#xe628;</i></a>&nbsp;';
                    }

                    if (status == "ORDER_PROCESSING" && !refundEd && !staffChanged) {
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
    tip.openIframe("订单指派", contextPath + 'index/order/assign?orderId=' + orderId, 550, 350, refreshData, "no");
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
