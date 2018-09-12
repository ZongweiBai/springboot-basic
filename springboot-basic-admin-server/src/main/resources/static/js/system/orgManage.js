$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "/system/queryAdminForPage",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                size: params.limit,  //页面大小
                page: params.offset/params.limit,
                sort: params.sort,
                order: params.order
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
                field: 'account',
                title: '用户账号',
                align: 'center'
            },
            {
                field: 'grade',
                title: '用户等级',
                align: 'center',
                formatter: function (value, row, index) {
                    //1、超级管理员 2、普通管理员
                    var gradeName = "";
                    switch (value) {
                        case 1:
                            gradeName = "超级管理员";
                            break;
                        case 2:
                            gradeName = "普通管理员";
                            break;
                    }
                    return gradeName;
                }
            },
            {
                field: 'mobile',
                title: '手机号码',
                align: 'center'
            },
            {
                field: 'roleName',
                title: '所属角色',
                align: 'center'
            },
            {
                field: 'createTime',
                title: '创建时间',
                align: 'center',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value, "yyyy-MM-dd hh:mm");
                }
            },
            {
                field: 'creator',
                title: '创建人',
                align: 'center'
            },
            {
                field: 'id',
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    if (adminGrade == 1) {
                        if (row.grade == 2) {
                            var content = '<a href="javascript:void(0);" style="text-decoration:none;" onclick="editRole(\'' + value + '\')" title="编辑"><i style="font-size: 18px;" class="Hui-iconfont">&#xe6df;</i></a>&nbsp;';
                            var content = '<a href="javascript:void(0);" style="text-decoration:none;" onclick="resetPsd(\'' + row.account + '\')" title="重置密码"><i style="font-size: 18px;" class="Hui-iconfont">&#xe68f;</i></a>&nbsp;';
                            return content;
                        } else {
                            return "";
                        }
                    } else {
                        return "";
                    }
                }
            }

        ]
    });
}

/**
 * 新增用户
 */
function addRole() {
    tip.openIframe("新增用户", contextPath + 'index/sysuser/add');
}

/**
 * 编辑用户
 * @param roleId
 */
function editRole(userId) {
    tip.openIframe("编辑用户", contextPath + 'index/sysuser/add?userId=' + userId);
}


function resetPsd(account){
    $.ajax({
        type: "POST",
        url: contextPath + "/system/resetPsd",
        data: {
            "account": account
        },
        beforeSend: function () {
            tip.showLoading();
        },
        error: function () {
            tip.hideLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == "200") {
                var info = data.info;
                tip.alertSuccess("重置成功，新密码为："+info, function () {
                    tip.closeIframe();
                });
            } else {
                tip.alertError(data.message);
            }
        }
    });
}
