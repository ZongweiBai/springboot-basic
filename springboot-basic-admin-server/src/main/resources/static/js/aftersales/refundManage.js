$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "afterSales/queryRefundForPage",
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
                align: 'center',
                formatter: function (value, row, index) {
                    var careType = row.careType;
                    if (careType == 'HOSPITAL_CARE' || careType == 'HOME_CARE') {
                        return value + " 天";
                    } else {
                        return value + " 次";
                    }
                }
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
                    var content = ''
                    content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="viewRefundInfo(\'' + value + '\')" title="查看详情"><i style="font-size: 18px;" class="Hui-iconfont">&#xe695;</i></a>&nbsp;';
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
function viewRefundInfo(refundId) {
    tip.openIframe("退款详情", contextPath + 'index/aftersales/refund/detail?refundId=' + refundId);
}

var refreshData = function () {
    $('#menuTable').bootstrapTable('refresh');
}
