$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "/user/withdrawController/dataWithdrawRecords",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                limit: params.limit,  //页面大小
                offset: params.offset,
                sort: params.sort,
                order: params.order,
                userAccount: $("#userAccount").val(),
                result: $("#result").val(),
                datemin: $("#datemin").val(),
                datemax: $("#datemax").val(),
                mobilePhone: $("#mobilePhone").val()
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
                field: 'account',
                title: '提现人手机号码',
                align: 'center',
                formatter: function (value, row, index) {
                    return value.account;
                }
            },
            {
                field: 'account.nickName',
                title: '昵称',
                align: 'center'
            },
            {
                field: 'mobilePhone',
                title: '提现号码',
                align: 'center'
            },
            {
                field: 'data',
                title: '提现流量',
                align: 'center',
                formatter: function (value, row, index) {
                    return value + "M";
                }
            },
            {
                field: 'result',
                title: '提现结果',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == true) {
                        return "提现成功"
                    } else {
                        return "提现失败";
                    }
                    return "";
                }
            },
            {
                field: 'resultDesc',
                title: '结果详情',
                align: 'center'
            },
            {
                field: 'createDate',
                title: '申请时间',
                align: 'center',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value, "yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                field: 'dealDate',
                title: '处理时间',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == null || value == 0) {
                        return "";
                    }
                    return getFormatDateByLong(value, "yyyy-MM-dd hh:mm:ss");
                }
            }
        ]
    });
}

var refreshData = function () {
    $('#menuTable').bootstrapTable('refresh');
}
