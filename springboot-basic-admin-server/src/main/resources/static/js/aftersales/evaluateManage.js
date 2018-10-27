$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "afterSales/queryEvaluateForPage",
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
                auditStatus: $("#auditStatus").val(),
                grade: $("#grade").val(),
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
                field: 'userProfile',
                title: '用户账号',
                align: 'center',
                formatter: function (value, row, index) {
                    if (!isEmpty(value)) {
                        return value.account;
                    }
                    return "";
                }
            },
            {
                field: 'createTime',
                title: '评价时间',
                align: 'center',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value, "yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                field: 'description',
                title: '评价内容',
                align: 'center'
            },
            {
                field: 'grade',
                title: '评价星级',
                align: 'center',
                formatter: function (value, row, index) {
                    return value + "星";
                }
            },
            {
                field: 'auditStatus',
                title: '审核状态',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == "APPLY") {
                        return "审核中";
                    } else if (value == "AGREE") {
                        return "通过";
                    } else if (value == "REJECT") {
                        return "不通过";
                    }
                    return "";
                }
            },
            {
                field: 'auditTime',
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
                field: 'id',
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    var orderId = row.orderId;
                    var status = row.auditStatus;
                    var content = ''
                    content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="viewOrderInfo(\'' + orderId + '\')" title="查看订单详情"><i style="font-size: 18px;" class="Hui-iconfont">&#xe695;</i></a>&nbsp;';
                    if (status == 'APPLY' || isEmpty(status)) {
                        content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="dealInfo(\'' + value + '\')" title="审核"><i style="font-size: 18px;" class="Hui-iconfont">&#xe6df;</i></a>&nbsp;';
                    }
                    content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="deleteInfo(\'' + value + '\')" title="删除"><i style="font-size: 18px;" class="Hui-iconfont">&#xe609;</i></a>&nbsp;';
                    return content;
                }
            }

        ]
    });
}

/**
 * 查看订单详情
 * @param orderId
 */
function viewOrderInfo(orderId) {
    tip.openIframe("订单详情", contextPath + 'index/order/detail?orderId=' + orderId);
}

function dealInfo(evaluateId) {
    tip.openIframe("评价审核", contextPath + 'index/aftersales/evaluate/deal?evaluateId=' + evaluateId, 550, 350, refreshData, "no");
}

function deleteInfo(evaluateId) {
    tip.confirm("确定要删除吗？删除后无法恢复！", function () {
        $.ajax({
            type: "GET",
            url: contextPath + "/afterSales/deleteEvaluate",
            data: {
                "evaluateId": evaluateId
            },
            success: function (data) {
                if (data.result == 200) {
                    tip.alertSuccess("删除成功", refreshData);
                } else {
                    tip.alertError("删除失败");
                }
            }
        });
    });
}

var refreshData = function () {
    $('#menuTable').bootstrapTable('refresh');
}
