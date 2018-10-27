$().ready(function () {
    // 订单统计
    loadAllTable();
    
    // 用户统计
    loadTable();

    // 照护师统计
    loadMallTable();
});

function loadAllTable() {
    $('#allOrderTable').bootstrapTable({
        url: contextPath + "report/queryPlatformOrderReport",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                limit: params.limit,  //页面大小
                offset: params.offset,
                datemin: $("#allDatemin").val(),
                datemax: $("#allDatemax").val(),
                queryType: 3
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
                field: 'orderTotalFee',
                title: '服务订单总额',
                align: 'center'
            },
            {
                field: 'validOrderCount',
                title: '有效订单总数',
                align: 'center'
            },
            {
                field: 'validOrderTotalFee',
                title: '有效订单总额',
                align: 'center'
            },
            {
                field: 'invalidOrderCount',
                title: '无效订单总数(关闭或取消)',
                align: 'center'
            },
            {
                field: 'invalidOrderTotalFee',
                title: '无效订单总额',
                align: 'center'
            },
            {
                field: 'refundOrderCount',
                title: '退款订单总数',
                align: 'center'
            },
            {
                field: 'refundOrderTotalFee',
                title: '退款订单总额',
                align: 'center'
            }
        ]
    });
}

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "report/queryUserInfoReport",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                limit: params.limit,  //页面大小
                offset: params.offset,
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
                field: 'userCount',
                title: '用户总数',
                align: 'center'
            },
            {
                field: 'userHaveOrderCount',
                title: '有订单用户数',
                align: 'center'
            }
        ]
    });
}

function loadMallTable() {
    $('#mallOrderTable').bootstrapTable({
        url: contextPath + "report/queryServiceStaffReport",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                limit: params.limit,  //页面大小
                offset: params.offset,
                datemin: $("#malldatemin").val(),
                datemax: $("#malldatemax").val()
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
                field: 'staffCount',
                title: '照护师总数',
                align: 'center'
            },
            {
                field: 'staffHaveOrderCount',
                title: '服务订单数',
                align: 'center'
            }
        ]
    });
}
