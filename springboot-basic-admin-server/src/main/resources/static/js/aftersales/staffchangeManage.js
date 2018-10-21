$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "afterSales/queryStaffchangeForPage",
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
                field: 'orderId',
                title: '订单号',
                align: 'center'
            },
            {
                field: 'orderId',
                title: '客户ID',
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
                field: 'changeDesc',
                title: '申请原因',
                align: 'center'
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
                field: 'oldStaffId',
                title: '更换前护工',
                align: 'center'
            },
            {
                field: 'newStaffId',
                title: '更换后护工',
                align: 'center'
            },
            {
                field: 'id',
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    var content = ''
                    content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="viewStaffInfo(\'' + value + '\')" title="查看详情"><i style="font-size: 18px;" class="Hui-iconfont">&#xe695;</i></a>&nbsp;';
                    return content;
                }
            }

        ]
    });
}

/**
 * 查看详情
 * @param changeId
 */
function viewStaffInfo(changeId) {
    tip.openIframe("更换详情", contextPath + 'index/aftersales/staffchange/detail?changeId=' + changeId);
}

var refreshData = function () {
    $('#menuTable').bootstrapTable('refresh');
}
