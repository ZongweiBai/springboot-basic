$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "/staff/queryStaffForPage",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                size: params.limit,  //页面大小
                page: params.offset / params.limit,
                sort: params.sort,
                order: params.order,
                userName: $("#userName").val(),
                mobile: $("#mobile").val(),
                sex: $("#sex").val()
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
                title: '编号',
                align: 'center'
            },
            {
                field: 'serviceStaffType',
                title: '服务人员类型',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == 'NURSE') {
                        return "护士";
                    }
                    return "护工";
                }
            },
            {
                field: 'userName',
                title: '姓名',
                align: 'center'
            },
            {
                field: 'mobile',
                title: '手机号码',
                align: 'center'
            },
            {
                field: 'serviceCount',
                title: '服务次数',
                align: 'center'
            },
            {
                field: 'serviceStatus',
                title: '服务状态',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == 'FREE') {
                        return "空闲中";
                    } else if (value == 'ASSIGNED') {
                        return "已指派订单";
                    } else if (value == 'IN_SERVICE') {
                        return "服务中";
                    }
                    return "-";
                }
            },
            {
                field: 'experience',
                title: '护理经验',
                align: 'center',
                formatter: function (value, row, index) {
                    if (isEmpty(value)) {
                        return "-";
                    }
                    return value + "年";
                }
            },
            {
                field: 'firstSkill',
                title: '优先服务类型',
                align: 'center'
            },
            {
                field: 'createTime',
                title: '注册时间',
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
                    var content = '<a href="javascript:void(0);" style="text-decoration:none;" onclick="viewDetailInfo(\'' + value + '\')" title="详情"><i style="font-size: 18px;" class="Hui-iconfont">&#xe695;</i></a>&nbsp;';
                    content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="editStaffInfo(\'' + value + '\')" title="编辑"><i style="font-size: 18px;" class="Hui-iconfont">&#xe6df;</i></a>&nbsp;';
                    content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="deleteStaffInfo(\'' + value + '\')" title="删除"><i style="font-size: 18px;" class="Hui-iconfont">&#xe609;</i></a>&nbsp;';
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
function viewDetailInfo(staffId) {
    tip.openIframe("护工详情", contextPath + 'index/staff/detail?staffId=' + staffId);
}

/**
 * 编辑
 */
function editStaffInfo(staffId) {
    tip.openIframe("编辑", contextPath + 'index/staff/add?staffId=' + staffId, null, null, refreshData);
}

/**
 * 删除
 */
function deleteStaffInfo(staffId) {
    tip.confirm("确定要删除吗？删除后无法恢复！", function () {
        $.ajax({
            type: "GET",
            url: contextPath + "/staff/deleteStaff",
            data: {
                "staffId": staffId
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
    tip.openIframe("新增", contextPath + 'index/staff/add', null, null, refreshData);
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
