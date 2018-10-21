$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "finance/queryWithdrawForPage",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                size: params.limit,  //页面大小
                page: params.offset / params.limit,
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
                field: 'id',
                title: '编号',
                align: 'center'
            },
            {
                field: 'userId',
                title: '用户ID',
                align: 'center'
            },
            {
                field: 'withdrawAmount',
                title: '提现金额',
                align: 'center'
            },
            {
                field: 'result',
                title: '提现结果',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == "UN_DEAL") {
                        return "处理中";
                    } else if (value == "SUCCESS") {
                        return "已到账";
                    } else if (value == "DENY") {
                        return "已拒绝";
                    }
                    return "";
                }
            },
            {
                field: 'bankAccountUserName',
                title: '银行账户',
                align: 'center'
            },
            {
                field: 'bankAccountNumber',
                title: '银行账号',
                align: 'center'
            },
            {
                field: 'bankName',
                title: '开户行名称',
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
                field: 'dealDesc',
                title: '处理备注',
                align: 'center'
            },
            {
                field: 'id',
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    var content = '';
                    var dealStatus = row.result;
                    if (dealStatus == "UN_DEAL") {
                        content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="dealWithdraw(\'' + value + '\')" title="处理申请"><i style="font-size: 18px;" class="Hui-iconfont">&#xe6df;</i></a>&nbsp';
                    }
                    return content;
                }
            }

        ]
    });
}

/**
 * 处理申请
 * @param orderId
 */
function dealWithdraw(withdrawId) {
    tip.openIframe("处理申请", contextPath + 'index/finance/withdraw/dealWithdraw?withdrawId=' + withdrawId, 550, 350, refreshData, "no");
}

var refreshData = function () {
    $('#menuTable').bootstrapTable('refresh');
}
