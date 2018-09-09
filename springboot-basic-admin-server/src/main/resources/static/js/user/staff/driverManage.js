$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "/user/driverController/queryDriverForPage",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                limit: params.limit,  //页面大小
                offset: params.offset,
                sort: params.sort,
                order: params.order,
                driverName: $("#driverName").val(),
                statusType: $("#statusType").val()
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
                field: 'driverName',
                title: '姓名',
                align: 'center'
            },
            {
                field: 'account',
                title: '手机号',
                align: 'center',
                formatter: function (value, row, index) {
                    return value.account;
                }
            },
            {
                field: 'platformRating',
                title: '等级',
                align: 'center'
            },
            {
                field: 'userRating',
                title: '用户评分',
                align: 'center'
            },
            {
                field: 'statusType',
                title: '状态',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == 'NORMAL') {
                        return "正常";
                    } else if (value == 'FORBIDDEN') {
                        return "禁用";
                    } else {
                        return "-";
                    }
                }

            },
            {
                field: 'onLineStatus',
                title: '在线状态',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == 'ONLINE') {
                        return "在线";
                    } else if (value == 'OFFLINE') {
                        return "离线";
                    } else {
                        return "-";
                    }
                }

            },
            {
                field: 'registerDate',
                title: '创建时间',
                align: 'center',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value, "yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                field: 'id',
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    var content = '<a href="javascript:void(0);" style="text-decoration:none;" onclick="viewDriverInfo(\'' + value + '\')" title="详情"><i style="font-size: 18px;" class="Hui-iconfont">&#xe695;</i></a>&nbsp;';
                    content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="editDriverInfo(\'' + value + '\')" title="编辑"><i style="font-size: 18px;" class="Hui-iconfont">&#xe6df;</i></a>&nbsp;';
                    var statusType = row.statusType;
                    if (statusType == 'NORMAL') {
                        content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="toggleDriverStatus(\'' + value + '\', \'1\')" title="禁用"><i style="font-size: 18px;" class="Hui-iconfont">&#xe631;</i></a>&nbsp;';
                    } else {
                        content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="toggleDriverStatus(\'' + value + '\', \'0\')" title="启用"><i style="font-size: 18px;" class="Hui-iconfont">&#xe615;</i></a>&nbsp;';
                    }
                    content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="modifyPassword(\'' + value + '\')" title="修改密码"><i style="font-size: 18px;" class="Hui-iconfont">&#xe63f;</i></a>&nbsp;';
                    // content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="deleteDriverInfo(\'' + value + '\')" title="删除"><i style="font-size: 18px;" class="Hui-iconfont">&#xe609;</i></a>&nbsp;';
                    return content;
                }
            }

        ]
    });
}

/**
 * 查看详情
 * @param driverId
 */
function viewDriverInfo(driverId) {
    tip.openIframe("速递员详情", contextPath + '/user/staff/viewDriverDetail.html?driverId=' + driverId);
}

/**
 * 编辑
 */
function editDriverInfo(driverId) {
    tip.openIframe("编辑", contextPath + '/user/staff/driverAdd.html?driverId=' + driverId);
}

/**
 * 删除
 */
function deleteDriverInfo(driverId) {
    tip.confirm("确定要删除快递员吗？删除后无法恢复！",function () {
        $.ajax({
            type: "POST",
            url: contextPath + "/user/driverController/deleteDriver",
            data: {
                "driverId": driverId
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

/**
 * 新增
 */
function addDriver() {
    tip.openIframe("新增", contextPath + '/user/staff/driverAdd.html');
}

var refreshData = function () {
    $('#menuTable').bootstrapTable('refresh');
};

/**
 * 修改状态
 * @param driverId
 * @param status
 */
function toggleDriverStatus(driverId, status) {
    $.ajax({
        type: "POST",
        url: contextPath + "/user/driverController/toggleDriverStatus",
        data: {
            "driverId": driverId,
            "statusType": status
        },
        success: function (data) {
            if (data.result == 200) {
                tip.alertSuccess("修改成功", refreshData);
            } else {
                tip.alertError("修改失败");
            }
        }
    });
}

/**
 * 修改密码
 */
function modifyPassword(driverId) {
    layer.prompt({
        title: '请输入新密码',
        formType: 0 //prompt风格，支持0-2
    }, function (password) {
        $.ajax({
            type: "POST",
            url: contextPath + "/user/driverController/modifyPassword",
            data: {
                "driverId": driverId,
                "password": password
            },
            success: function (data) {
                if (data.result == 200) {
                    tip.alertSuccess("修改成功");
                } else {
                    tip.alertError("修改失败");
                }
            }
        });
    });
}
