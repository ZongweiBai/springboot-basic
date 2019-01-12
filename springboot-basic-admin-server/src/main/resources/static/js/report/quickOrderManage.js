$().ready(function () {

    initHospital();

    // 结算统计表
    loadJSTable()

});

function loadJSTable() {
    $('#allOrderTable').bootstrapTable({
        url: contextPath + "report/queryQuickOrderReport",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                size: params.limit,  //页面大小
                page: params.offset / params.limit,
                datemin: $("#allDatemin").val(),
                datemax: $("#allDatemax").val(),
                hospitalAddress: $("#allHospitalAddress").val()
            };
            return paramsMap;
        },
        pageNumber: 1,
        pageSize: 10,//单页记录数
        pageList: [10, 20, 30, 50],//分页步进值
        pagination: true, //分页
        sidePagination: "server", //服务端处理分页
        columns: [
            [
                {
                    title: '医院',
                    field: 'hospitalName',
                    rowspan: 2,
                    align: 'center',
                    valign: 'middle',
                },
                {
                    title: '总收入',
                    field: 'totalFee',
                    rowspan: 2,
                    align: 'center',
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        var hospitalName = row.hospitalName
                        content = '<a href="javascript:void(0);" style="color: #0a6999;" onclick="showDetail(\'' + hospitalName + '\')" title="订单详情">￥'+ value +'</a>&nbsp;';
                        return content;
                    }
                },
                {
                    title: '退款',
                    field: 'refundFee',
                    rowspan: 2,
                    align: 'center',
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        return "￥ " + value;
                    }
                },
                {
                    title: '实收',
                    field: 'actualIncome',
                    rowspan: 2,
                    align: 'center',
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        return "￥ " + value;
                    }
                },
                {
                    title: '院内',
                    colspan: 4,
                    align: 'center'
                },
                {
                    title: '院外',
                    colspan: 3,
                    align: 'center'
                }
            ],
            [
                {
                    field: 'totalInFee',
                    title: '合计',
                    align: 'center',
                    formatter: function (value, row, index) {
                        return "￥ " + value;
                    }
                },
                {
                    field: 'inOneToOne',
                    title: '一对一',
                    align: 'center',
                    formatter: function (value, row, index) {
                        return "￥ " + value;
                    }
                },
                {
                    field: 'inOneToMany',
                    title: '一对多',
                    align: 'center',
                    formatter: function (value, row, index) {
                        return "￥ " + value;
                    }
                },
                {
                    field: 'inManyToOne',
                    title: '多对一',
                    align: 'center',
                    formatter: function (value, row, index) {
                        return "￥ " + value;
                    }
                },
                {
                    field: 'totalOutFee',
                    title: '合计',
                    align: 'center',
                    formatter: function (value, row, index) {
                        return "￥ " + value;
                    }
                },
                {
                    field: 'outOneToOne',
                    title: '一对一',
                    align: 'center',
                    formatter: function (value, row, index) {
                        return "￥ " + value;
                    }
                },
                {
                    field: 'outManyToOne',
                    title: '多对一',
                    align: 'center',
                    formatter: function (value, row, index) {
                        return "￥ " + value;
                    }
                },
            ]
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

function initHospital() {
    $.ajax({
        type: "GET",
        url: contextPath + "hospital/getAllHospital",
        data: {},
        success: function (data) {
            if (data.result == 200) {
                var rows = data.rows;
                if (!isEmpty(rows)) {
                    for (var index = 0; index < rows.length; index++) {
                        $("#hospitalAddress").append("<option value='" + rows[index].hospitalName + "'>" + rows[index].hospitalName + "</option>");
                        $("#allHospitalAddress").append("<option value='" + rows[index].hospitalName + "'>" + rows[index].hospitalName + "</option>");
                    }
                }
            }
        }
    });
}

function showDetail(hospitalName) {
    var datemin = $("#allDatemin").val()
    var datemax = $("#allDatemax").val()
    tip.openIframe("订单详情", contextPath + 'index/report/quickOrderlist/manage?hospitalName=' + hospitalName + "&datemin=" + datemin + "&datemax=" + datemax);
}
