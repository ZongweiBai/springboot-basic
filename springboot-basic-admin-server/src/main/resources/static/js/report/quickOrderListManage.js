$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "/order/queryQuickOrder",
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
                serviceScope: serviceScope,
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
                field: 'payTime',
                title: '支付时间',
                align: 'center',
                formatter: function (value, row, index) {
                    if (isEmpty(value)) {
                        return "-";
                    }
                    return value;
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
                field: 'totalFee',
                title: '订单金额',
                align: 'center',
                formatter: function (value, row, index) {
                    return "￥" + value
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
                        if (value == "PAY_OFFLINE_CASH") {
                            return "线下支付-现金";
                        } else if (value == "PAY_OFFLINE_POS") {
                            return "线下支付-POS";
                        } else if (value == "PAY_OFFLINE_ALI") {
                            return "线下支付-支付宝";
                        } else if (value == "PAY_OFFLINE_WECHAT") {
                            return "线下支付-微信";
                        }
                        return "线下支付";
                    }
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
                field: 'admin',
                title: '操作人',
                align: 'center',
                formatter: function (value, row, index) {
                    var adminStaff = row.adminStaff
                    if (isEmpty(value)) {
                        if (isEmpty(adminStaff)) {
                            return "-"
                        } else {
                            return adminStaff.userName;
                        }
                    }
                    return value.adminName;
                }
            }
        ]
    });
}

var refreshData = function () {
    $('#menuTable').bootstrapTable('refresh');
}
