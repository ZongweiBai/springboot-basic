$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "/user/withdrawController/userWithdrawRecords",
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
                field: 'account',
                title: '手机号',
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
                field: 'money',
                title: '提现金额',
                align: 'center'
            },
            {
                field: 'result',
                title: '提现结果',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == 'UN_DEAL') {
                        return "未处理"
                    } else if (value == 'SUCCESS') {
                        return "已拨款";
                    } else if (value == 'DENY') {
                        return "拒绝提现";
                    }
                    return "";
                }
            },
            {
                field: 'bankName',
                title: '银行',
                align: 'center'
            },
            {
                field: 'bankSubName',
                title: '银行支行',
                align: 'center'
            },
            {
                field: 'openAccountUserNme',
                title: '开户人姓名',
                align: 'center'
            },
            {
                field: 'bankAccount',
                title: '银行卡号',
                align: 'center'
            },
            {
                field: 'date',
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
            },
            {
                field: 'id',
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    var result = row.result;
                    if (result == 'UN_DEAL') {
                        var content = '<a href="javascript:void(0);" style="text-decoration:none;" onclick="dealApply(\'' + value + '\', \'SUCCESS\')" title="已拨款"><i style="font-size: 18px;" class="Hui-iconfont">&#xe6e1;</i></a>&nbsp;';
                        content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="dealApply(\'' + value + '\', \'DENY\')" title="拒绝提现"><i style="font-size: 18px;" class="Hui-iconfont">&#xe6dd;</i></a>&nbsp;';
                        return content;
                    }
                    return "";
                }
            }

        ]
    });
}

/**
 * 审核提现
 * @param userId
 */
function dealApply(id, result) {
    $.ajax({
        type: "POST",
        url: contextPath + "/user/withdrawController/dealWithdraw",
        contentType: "application/x-www-form-urlencoded",
        data: {
            "id": id,
            "result": result
        },
        beforeSend: function () {
            tip.showLoading();
        },
        error: function () {
            tip.hideLoading();
            tip.alertError("处理失败，请稍后重试");
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                tip.alertSuccess("处理成功", refreshData)
            } else {
                tip.alertError("处理失败")
            }
        }
    });
}

var refreshData = function () {
    $('#menuTable').bootstrapTable('refresh');
}
