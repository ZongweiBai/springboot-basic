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
                title: '审核结果',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == "APPLY") {
                        return "审核中";
                    } else if (value == "AGREE") {
                        return "审核通过";
                    } else if (value == "REJECT") {
                        return "审核不通过";
                    }
                    return "";
                }
            },
            {
                field: 'auditUser',
                title: '审核人',
                align: 'center'
            },
            {
                field: 'auditTime',
                title: '审核时间',
                align: 'center'
            },
            {
                field: 'reply',
                title: '回复内容',
                align: 'center'
            },
            {
                field: 'id',
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    var reply = row.reply;
                    var auditStatus = row.auditStatus;
                    var content = '';
                    if (auditStatus === 'APPLY') {
                        content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="audit(\'' + value + '\')" title="审核"><i style="font-size: 18px;" class="Hui-iconfont">&#xe6df;</i></a>&nbsp;';
                    }

                    if (isEmpty(reply)) {
                        content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="reply(\'' + value + '\')" title="回复"><i style="font-size: 18px;" class="Hui-iconfont">&#xe692;</i></a>&nbsp;';
                    }
                    return content;
                }
            }
        ]
    });
}

var refreshData = function () {
    $('#menuTable').bootstrapTable('refresh');
}

/**
 * 导出excel
 */
function downloadExcel() {
    var orderId = $("#orderId").val();
    var grade = $("#grade").val();
    var datemin = $("#datemin").val();
    var datemax = $("#datemax").val();
    var url = contextPath + "report/downloadEvaluate?orderId=" + orderId +
        "&grade=" + grade + "&datemin=" + datemin + "&datemax=" + datemax;
    window.open(url);
}

/**
 * 评论审核
 * @param evaluateId
 */
function audit(evaluateId) {
    tip.openIframe("评论审核", contextPath + 'index/aftersales/evaluate/deal?evaluateId=' + evaluateId, 550, 450, refreshData, "no");
}

/**
 * 评论回复
 * @param evaluateId
 */
function reply(evaluateId) {
    tip.openIframe("评论回复", contextPath + 'index/aftersales/evaluate/reply?evaluateId=' + evaluateId, 550, 350, refreshData, "no");
}
