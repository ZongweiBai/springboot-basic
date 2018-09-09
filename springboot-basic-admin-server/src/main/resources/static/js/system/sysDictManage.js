$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "/system/sysManageController/queryDictForPage",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                size: params.limit,  //页面大小
                page: params.offset/params.limit
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
                field: 'codeName',
                title: '字典名称',
                align: 'center'
            },
            {
                field: 'codeKey',
                title: '字典key',
                align: 'center'
            },
            {
                field: 'codeValue',
                title: '字典值',
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
                field: 'id',
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    var content = '<a href="javascript:void(0);" style="text-decoration:none;" onclick="editDict(\'' + value + '\')" title="编辑"><i style="font-size: 18px;" class="Hui-iconfont">&#xe6df;</i></a>&nbsp;';
                    return content;
                }
            }

        ]
    });
}

/**
 * 新增菜单
 */
function addDict() {
    tip.openIframe("新增字典", contextPath + '/system/sysDict/sysDictAdd.jsp');
}

/**
 * 编辑字典
 * @param menuId
 */
function editDict(dictId) {
    tip.openIframe("编辑字典", contextPath + '/system/sysDict/sysDictAdd.jsp?dictId=' + dictId);
}
