$().ready(function () {

    initHospital();

    // 订单统计
    loadAllTable();

    // 结算统计表
    loadJSTable()

});

function loadAllTable() {
    $('#allOrderTable').bootstrapTable({
        url: contextPath + "report/queryOrderStatisticReport",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var careTypes = '';
            $('input:checkbox[name=allCareType]:checked').each(function(i){
                careTypes += $(this).val() + ",";
            });
            var paramsMap = {
                size: params.limit,  //页面大小
                page: params.offset / params.limit,
                datemin: $("#allDatemin").val(),
                datemax: $("#allDatemax").val(),
                hospitalAddress: $("#allHospitalAddress").val(),
                careTypes: careTypes
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
                field: 'id',
                title: '订单编号',
                align: 'center'
            },
            {
                field: 'userProfile',
                title: '患者',
                align: 'center',
                formatter: function (value, row, index) {
                    if (isEmpty(value)) {
                        return "-";
                    }
                    return value.nickName;
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

function loadJSTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "report/queryOrderJSReport",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var careTypes = '';
            $('input:checkbox[name=careType]:checked').each(function(i){
                careTypes += $(this).val() + ",";
            });
            var paramsMap = {
                size: params.limit,  //页面大小
                page: params.offset / params.limit,
                datemin: $("#datemin").val(),
                datemax: $("#datemax").val(),
                hospitalAddress: $("#hospitalAddress").val(),
                careTypes: careTypes
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
                    title: '日期',
                    field: 'orderTime',
                    rowspan: 2,
                    align: 'center',
                    valign: 'middle',
                },
                {
                    title: '服务项目',
                    field: 'careType',
                    rowspan: 2,
                    align: 'center',
                    valign: 'middle',
                },
                {
                    title: '已结算订单量',
                    field: 'orderCount',
                    rowspan: 2,
                    align: 'center',
                    valign: 'middle',
                },
                {
                    title: '客单价',
                    field: 'price',
                    rowspan: 2,
                    align: 'center',
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        return "￥ " + value;
                    }
                },
                {
                    title: '销售额',
                    field: 'totalFee',
                    rowspan: 2,
                    align: 'center',
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        return "￥ " + value;
                    }
                },
                {
                    title: '支付方式（订单数/百分比）',
                    colspan: 5,
                    align: 'center'
                }
            ],
            [
                {
                    field: 'wechatPayCount',
                    title: '微信(在线)',
                    align: 'center',
                    formatter: function (value, row, index) {
                        return value+"%";
                    }
                },
                {
                    field: 'cashPayCount',
                    title: '现金(线下)',
                    align: 'center',
                    formatter: function (value, row, index) {
                        return value+"%";
                    }
                },
                {
                    field: 'posPayCount',
                    title: 'POS(线下)',
                    align: 'center',
                    formatter: function (value, row, index) {
                        return value+"%";
                    }
                },
                {
                    field: 'alipayPayCount',
                    title: '支付宝(线下)',
                    align: 'center',
                    formatter: function (value, row, index) {
                        return value+"%";
                    }
                },
                {
                    field: 'offlineWechatPayCount',
                    title: '微信(线下)',
                    align: 'center',
                    formatter: function (value, row, index) {
                        return value+"%";
                    }
                }
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
