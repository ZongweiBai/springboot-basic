$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "/order/queryHospitalOrder",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                size: params.limit,  //页面大小
                page: params.offset / params.limit,
                orderTime: orderTime,
                staffId: staffId
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
                field: 'orderTime',
                title: '时间',
                align: 'center'
            },
            {
                field: 'id',
                title: '关联订单',
                align: 'center'
            },
            {
                field: 'serviceStaff',
                title: '服务人员',
                align: 'center',
                formatter: function (value, row, index) {
                    if (isEmpty(value)) {
                        return "-";
                    }
                    return value.userName;
                }
            },
            {
                field: 'userProfile',
                title: '被服务人员',
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
                field: 'orderExt',
                title: '服务天数',
                align: 'center',
                formatter: function (value, row, index) {
                    if (!isEmpty(value)) {
                        return value.serviceDuration;
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
            }
        ]
    });
}

var refreshData = function () {
    $('#menuTable').bootstrapTable('refresh');
}
