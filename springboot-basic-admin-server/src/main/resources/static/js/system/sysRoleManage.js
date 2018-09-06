$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "/system/sysManageController/queryRoleForPage",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                limit: params.limit,  //页面大小
                offset: params.offset,
                sort: params.sort,
                order: params.order,
                roleName: $("#roleName").val(),
                roleType: $("#roleType").val(),
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
                field: 'roleName',
                title: '角色名称',
                align: 'center'
            },
            {
                field: 'roleType',
                title: '角色类型',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == 'A') {
                        return "系统管理员";
                    } else {
                        return "普通管理员";
                    }
                }
            },
            {
                field: 'createTime',
                title: '创建时间',
                align: 'center',
                sortable: true,
                order: 'desc',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value, "yyyy-MM-dd hh:mm");
                }
            },
            {
                field: 'createBy',
                title: '创建人',
                align: 'center'
            },
            {
                field: 'roleId',
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    var content = '<a href="javascript:void(0);" style="text-decoration:none;" onclick="editRole(\'' + value + '\')" title="编辑"><i style="font-size: 18px;" class="Hui-iconfont">&#xe6df;</i></a>&nbsp;';
                    return content;
                }
            }

        ]
    });
}

/**
 * 新增角色
 */
function addRole() {
    tip.openIframe("新增角色", contextPath + '/system/sysRole/sysRoleAdd.jsp');
}

/**
 * 编辑角色
 * @param roleId
 */
function editRole(roleId) {
    tip.openIframe("编辑角色", contextPath + '/system/sysRole/sysRoleAdd.jsp?roleId=' + roleId);
}
