$().ready(function () {

    selectStaff('WORKER');

    // 订单统计
    loadAllTable();

});

function selectStaff(staffType) {
    $.ajax({
        type: "GET",
        url: contextPath + "/staff/queryStaffByType",
        data: {
            "serviceStaffType": staffType
        },
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var rows = data.rows;
                for (var i = 0; i < rows.length; i++) {
                    var staff = rows[i];
                    $("#serviceStaffId").append('<option value="' + staff.id + '">' + staff.userName + '-' + staff.mobile + '</option>');
                }
            } else {
                tip.alertError("加载护工信息失败");
            }
        },
        error: function () {
            tip.hideLoading();
            tip.alertError("加载护工信息失败");
        }
    });
}

function loadAllTable() {
    $('#allOrderTable').bootstrapTable({
        url: contextPath + "report/queryHospitalBizReport",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                size: params.limit,  //页面大小
                page: params.offset / params.limit,
                datemin: $("#allDatemin").val(),
                datemax: $("#allDatemax").val(),
                serviceStaffId: $("#serviceStaffId").val()
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
                title: '日期',
                align: 'center'
            },
            {
                field: 'staffName',
                title: '服务人员',
                align: 'center'
            },
            {
                field: 'personFee',
                title: '服务人员实收',
                align: 'center',
                formatter: function (value, row, index) {
                    return "￥ " + value;
                }
            },
            {
                field: 'totalFee',
                title: '业绩',
                align: 'center',
                formatter: function (value, row, index) {
                    return "￥ " + value;
                }
            },
            {
                field: 'companyFee',
                title: '公司收入',
                align: 'center',
                formatter: function (value, row, index) {
                    var orderTime = row.orderTime
                    var staffId = row.staffId
                    content = '<a href="javascript:void(0);" style="color: #0a6999;" onclick="showDetail(\'' + orderTime + '\', \'' + staffId + '\')" title="订单详情">￥'+ value +'</a>&nbsp;';
                    return content;
                }
            },
            {
                field: 'orderCount',
                title: '订单量',
                align: 'center'
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

function showDetail(orderTime, staffId) {
    tip.openIframe("订单详情", contextPath + 'index/report/orderlist/manage?orderTime=' + orderTime + "&staffId=" + staffId);
}
