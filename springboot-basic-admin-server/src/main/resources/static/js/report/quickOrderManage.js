$().ready(function () {

    initHospital();

    // 结算统计表
    loadJSTable()

});

function queryTable() {
    var groupType = $("#groupType").val()
    var hospitalAddress = $("#allHospitalAddress").val()
    if (groupType === 'department' && isEmpty(hospitalAddress)) {
        tip.alertError("请先选择医院")
        return
    }
    $('#allOrderTable').bootstrapTable('refresh');
}

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
                paydatemin: $("#payDatemin").val(),
                paydatemax: $("#payDatemax").val(),
                groupType: $("#groupType").val(),
                hospitalAddress: $("#allHospitalAddress").val(),
                hospitalDepartment: $("#hospitalDepartment").val()
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
                    field: 'title',
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
                        var groupType = $("#groupType").val()
                        var title = row.title
                        if (groupType === 'hospital') {
                            content = '<a href="javascript:void(0);" style="color: #0a6999;" onclick="showDetail(\'' + title + '\', \'\')" title="订单详情">￥' + value + '</a>&nbsp;';
                        } else {
                            var hospitalName = $("#allHospitalAddress").val()
                            content = '<a href="javascript:void(0);" style="color: #0a6999;" onclick="showDetail(\'' + hospitalName + '\', \'' + title + '\')" title="订单详情">￥' + value + '</a>&nbsp;';
                        }
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
                        var groupType = $("#groupType").val()
                        var title = row.title
                        if (groupType === 'hospital') {
                            content = '<a href="javascript:void(0);" style="color: #0a6999;" onclick="showRefundDetail(\'' + title + '\', \'\')" title="退款订单详情">￥' + value + '</a>&nbsp;';
                        } else {
                            var hospitalName = $("#allHospitalAddress").val()
                            content = '<a href="javascript:void(0);" style="color: #0a6999;" onclick="showRefundDetail(\'' + hospitalName + '\', \'' + title + '\')" title="退款订单详情">￥'+ value +'</a>&nbsp;';
                        }
                        return content;
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
                        var groupType = $("#groupType").val()
                        var title = row.title
                        if (groupType === 'hospital') {
                            content = '<a href="javascript:void(0);" style="color: #0a6999;" onclick="showOtherDetail(\'' + title + '\', \'INSIDE\', \'\')" title="订单详情">￥'+ value +'</a>&nbsp;';
                        } else {
                            var hospitalName = $("#allHospitalAddress").val()
                            content = '<a href="javascript:void(0);" style="color: #0a6999;" onclick="showOtherDetail(\'' + hospitalName + '\', \'INSIDE\', \'' + title + '\')" title="订单详情">￥'+ value +'</a>&nbsp;';
                        }
                        return content;
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
                        var groupType = $("#groupType").val()
                        var title = row.title
                        if (groupType === 'hospital') {
                            content = '<a href="javascript:void(0);" style="color: #0a6999;" onclick="showOtherDetail(\'' + title + '\', \'OUTSIDE\', \'\')" title="订单详情">￥' + value + '</a>&nbsp;';
                        } else {
                            var hospitalName = $("#allHospitalAddress").val()
                            content = '<a href="javascript:void(0);" style="color: #0a6999;" onclick="showOtherDetail(\'' + hospitalName + '\', \'OUTSIDE\', \'' + title + '\')" title="订单详情">￥'+ value +'</a>&nbsp;';
                        }
                        return content;
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
        ],
        onLoadSuccess: function (data) {
            var thinnerobjs = document.getElementsByClassName("th-inner");  //通过类名找到标签
            var groupType = $("#groupType").val()
            if (groupType === 'department') {
                thinnerobjs[0].textContent = "科室"
            } else {
                thinnerobjs[0].textContent = "医院"
            }
        }
    });
}

/**
 * 导出excel
 */
function downloadExcel() {
    var datemin = $("#allDatemin").val()
    var datemax = $("#allDatemax").val()
    var paydatemin = $("#payDatemin").val()
    var paydatemax = $("#payDatemax").val()
    var hospitalAddress = $("#allHospitalAddress").val()
    var hospitalDepartment = $("#hospitalDepartment").val()
    var groupType = $("#groupType").val()
    var url = contextPath + "report/downloadQuickRefundOrder?datemin=" + datemin +
        "&datemax=" + datemax + "&paydatemin=" + paydatemin +
        "&paydatemax=" + paydatemax + "&hospitalAddress=" + hospitalAddress +
        "&hospitalDepartment=" + hospitalDepartment + "&groupType=" + groupType;
    window.open(url);
}

function initHospital() {
    $.ajax({
        type: "GET",
        url: contextPath + "hospital/getUserHospital",
        async: false,
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

function selectHospitalDepartment() {
    var allHospitalAddress = $("#allHospitalAddress").val()
    $("#hospitalDepartment").html('<option value="">选择科室</option>')
    if (isEmpty(allHospitalAddress)) {
        return
    }

    $.ajax({
        type: "GET",
        url: contextPath + "hospital/getDepartmentByHospital",
        async: false,
        data: {
            "hospitalName": allHospitalAddress
        },
        success: function (data) {
            if (data.result == 200) {
                var rows = data.rows;
                if (!isEmpty(rows)) {
                    for (var index = 0; index < rows.length; index++) {
                        $("#hospitalDepartment").append("<option value='" + rows[index].departmentName + "'>" + rows[index].departmentName + "</option>");
                    }
                }
            }
        }
    });
}

function showDetail(hospitalName, department) {
    var datemin = $("#allDatemin").val()
    var datemax = $("#allDatemax").val()
    var paydatemin = $("#payDatemin").val()
    var paydatemax = $("#payDatemax").val()
    hospitalName = encodeURI(hospitalName)
    if (!isEmpty(department)) {
        department = encodeURI(department)
    }
    console.log(hospitalName)
    tip.openIframe("订单详情", contextPath + 'index/report/quickOrderlist/manage?hospitalName=' + hospitalName + "&datemin=" + datemin + "&datemax=" + datemax + "&paydatemin=" + paydatemin + "&paydatemax=" + paydatemax + "&department=" + department);
}

function showOtherDetail(hospitalName, serviceScope, department) {
    var datemin = $("#allDatemin").val()
    var datemax = $("#allDatemax").val()
    var paydatemin = $("#payDatemin").val()
    var paydatemax = $("#payDatemax").val()
    hospitalName = encodeURI(hospitalName)
    if (!isEmpty(department)) {
        department = encodeURI(department)
    }
    console.log(hospitalName)
    tip.openIframe("订单详情", contextPath + 'index/report/quickOrderlist/manage?hospitalName=' + hospitalName + "&datemin=" + datemin + "&datemax=" + datemax + "&paydatemin=" + paydatemin + "&paydatemax=" + paydatemax + "&serviceScope=" + serviceScope + "&department=" + department);
}

function showRefundDetail(hospitalName, department) {
    var datemin = $("#allDatemin").val()
    var datemax = $("#allDatemax").val()
    var paydatemin = $("#payDatemin").val()
    var paydatemax = $("#payDatemax").val()
    hospitalName = escape(hospitalName)
    if (!isEmpty(department)) {
        department = encodeURI(department)
    }
    tip.openIframe("订单详情", contextPath + 'index/report/quickRefundOrderlist/manage?hospitalName=' + hospitalName + "&datemin=" + datemin + "&datemax=" + datemax + "&paydatemin=" + paydatemin + "&paydatemax=" + paydatemax + "&department=" + department);
}
