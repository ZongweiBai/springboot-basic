$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "/system/sysManageController/queryMenuForPage",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                limit: params.limit,  //页面大小
                offset: params.offset,
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
                field: 'menuName',
                title: '菜单名称',
                align: 'center'
            },
            {
                field: 'menuUrl',
                title: '菜单URL',
                align: 'center'
            },
            {
                field: 'level',
                title: '菜单等级',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == 1) {
                        return "一级菜单";
                    } else {
                        return "二级菜单";
                    }
                }
            },
            {
                field: 'parentName',
                title: '上级菜单',
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
                field: 'createBy',
                title: '创建人',
                align: 'center'
            },
            {
                field: 'menuId',
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    var content = '<a href="javascript:void(0);" style="text-decoration:none;" onclick="editMenu(\'' + value + '\')" title="编辑"><i style="font-size: 18px;" class="Hui-iconfont">&#xe6df;</i></a>&nbsp;';
                    return content;
                }
            }

        ]
    });
}

/**
 * 新增菜单
 */
function addMenu() {
    tip.openIframe("新增菜单", contextPath + '/system/sysMenu/sysMenuAdd.jsp');
}

/**
 * 编辑菜单
 * @param menuId
 */
function editMenu(menuId) {
    tip.openIframe("编辑菜单", contextPath + '/system/sysMenu/sysMenuAdd.jsp?menuId=' + menuId);
}
