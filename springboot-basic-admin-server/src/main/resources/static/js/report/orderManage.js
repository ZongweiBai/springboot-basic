$().ready(function () {
    // 订单统计
    loadAllTable();

});

function loadAllTable() {
    $('#allOrderTable').bootstrapTable({
        url: contextPath + "report/queryOrderReport",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                size: params.limit,  //页面大小
                page: params.offset / params.limit,
                datemin: $("#allDatemin").val(),
                datemax: $("#allDatemax").val(),
                careType: $("#careType").val()
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
                title: '订单号',
                align: 'center'
            },
            {
                field: 'orderTime',
                title: '日期',
                align: 'center'
            },
            {
                field: 'careType',
                title: '服务项目',
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
                field: 'status',
                title: '订单状态',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == "ORDER_UN_PAY") {
                        return "已下单待付款";
                    } else if (value == "ORDER_PAYED") {
                        return "已付款待指派";
                    } else if (value == "ORDER_ASSIGN") {
                        return "已指派待服务";
                    } else if (value == "ORDER_PROCESSING") {
                        return "服务中";
                    } else if (value == "ORDER_FINISH") {
                        return "已完成";
                    }
                    return "";
                }
            },
            {
                field: 'totalFee',
                title: '订单金额',
                align: 'center',
                formatter: function (value, row, index) {
                    return "￥ " + value;
                }
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
            }
        ]
    });
}

/**
 * 导出excel
 */
function downloadExcel() {
    var careType = $("#careType").val();
    var datemin = $("#datemin").val();
    var datemax = $("#datemax").val();
    var url = contextPath + "report/downloadOrder?careType=" + careType +
        "&datemin=" + datemin + "&datemax=" + datemax;
    window.open(url);
}
