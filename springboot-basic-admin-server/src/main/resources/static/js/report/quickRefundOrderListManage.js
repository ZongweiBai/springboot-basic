$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "/order/queryQuickRefundOrder",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                size: params.limit,  //页面大小
                page: params.offset / params.limit,
                hospitalName: hospitalName,
                datemin: datemin,
                datemax: datemax,
                paydatemin: paydatemin,
                paydatemax: paydatemax
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
                field: 'orderRefund',
                title: '退款时间',
                align: 'center',
                formatter: function (value, row, index) {
                    if (isEmpty(value)) {
                        return "-";
                    }
                    return value.createTime;
                }
            },
            {
                field: 'hospitalName',
                title: '医院',
                align: 'center'
            },
            {
                field: 'id',
                title: '订单号',
                align: 'center'
            },
            {
                field: 'userProfile',
                title: '患者',
                align: 'center',
                formatter: function (value, row, index) {
                    if (!isEmpty(value)) {
                        return value.nickName + "(" + value.account + ")";
                    } else {
                        return "-";
                    }
                }
            },
            {
                field: 'orderRefund',
                title: '退款金额',
                align: 'center',
                formatter: function (value, row, index) {
                    return "￥" + value.refundFee
                }
            },
            {
                field: 'serviceScope',
                title: '服务地点',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == "INSIDE") {
                        return "院内";
                    } else if (value == "OUTSIDE") {
                        return "院外";
                    } else {
                        return "-"
                    }
                }
            },
            {
                field: 'id',
                title: '操作类型',
                align: 'center',
                formatter: function (value, row, index) {
                    return '退款';
                }
            }
        ]
    });
}

var refreshData = function () {
    $('#menuTable').bootstrapTable('refresh');
}
