$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "system/queryDictForPage",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                size: params.limit,  //页面大小
                page: params.offset / params.limit,
                dictName: $("#dictName").val(),
                codeValue: $("#codeValue").val()
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
                field: 'codeValue',
                title: '名称',
                align: 'center'
            },
            {
                field: 'createTime',
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
                    var content = '<a href="javascript:void(0);" style="text-decoration:none;" onclick="editDict(\'' + value + '\')" title="编辑"><i style="font-size: 18px;" class="Hui-iconfont">&#xe6df;</i></a>&nbsp;';
                    content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="deleteDict(\'' + value + '\')" title="删除"><i style="font-size: 18px;" class="Hui-iconfont">&#xe609;</i></a>&nbsp;';
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
    tip.openIframe("新增", contextPath + 'index/careplan/dict/add?dictName=' + dictName, 350, 250, refreshData);
}

/**
 * 编辑字典
 * @param menuId
 */
function editDict(dictId) {
    tip.openIframe("编辑", contextPath + 'index/careplan/dict/add?dictId=' + dictId + '&dictName=' + dictName, 350, 250, refreshData);
}

function deleteDict(dictId) {
    tip.confirm("确定要删除吗？删除后无法恢复！", function () {
        $.ajax({
            type: "GET",
            url: contextPath + "system/deleteSysDict",
            data: {
                "dictId": dictId
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

var refreshData = function () {
    $('#menuTable').bootstrapTable('refresh');
}
