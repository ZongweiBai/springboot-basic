$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "/item/queryServiceTypeForPage",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                size: params.limit,  //页面大小
                page: params.offset/params.limit,
                sort: params.sort,
                order: params.order,
                basicItemType: $("#basicItemType").val()
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
                field: 'serviceName',
                title: '分组名称',
                align: 'center'
            },
            {
                field: 'keyWord',
                title: '关键词',
                align: 'center'
            },
            {
                field: 'serviceDesc',
                title: '分组描述',
                align: 'center'
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
                field: 'id',
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    var content = '<a href="javascript:void(0);" style="text-decoration:none;" onclick="editServiceType(\'' + value + '\')" title="编辑"><i style="font-size: 18px;" class="Hui-iconfont">&#xe6df;</i></a>&nbsp;';
                    return content;
                }
            }

        ]
    });
}

/**
 * 新增
 */
function addServiceType() {
    tip.openIframe("新增服务分类", contextPath + 'index/type/basic/add');
}

/**
 * 编辑
 * @param serviceId
 */
function editServiceType(typeId) {
    tip.openIframe("编辑服务分类", contextPath + 'index/type/basic/add?typeId=' + typeId);
}
