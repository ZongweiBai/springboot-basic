$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "/item/queryItemForPage",
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
                field: 'basicItemType',
                title: '服务分类',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == 'BASIC') {
                        return "基础护理";
                    } else if (value == 'MEDICAL') {
                        return "医疗照护";
                    } else if (value == 'LIFE') {
                        return "活动照护";
                    } else if (value == 'HOME') {
                        return "生活服务";
                    } else {
                        return "";
                    }
                }
            },
            {
                field: 'itemName',
                title: '服务项目',
                align: 'center'
            },
            {
                field: 'status',
                title: '状态',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == 'UPCART') {
                        return "上架";
                    } else if (value == 'DOWNCART') {
                        return "下架";
                    } else {
                        return "";
                    }
                }
            },
            {
                field: 'itemFee',
                title: '服务价格',
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
                    var content = '<a href="javascript:void(0);" style="text-decoration:none;" onclick="editRole(\'' + value + '\')" title="编辑"><i style="font-size: 18px;" class="Hui-iconfont">&#xe6df;</i></a>&nbsp;';
                    return content;
                }
            }

        ]
    });
}

/**
 * 新增
 */
function addRole() {
    tip.openIframe("新增服务项目", contextPath + 'index/item/basic/add');
}

/**
 * 编辑
 * @param serviceId
 */
function editRole(itemId) {
    tip.openIframe("编辑服务项目", contextPath + 'index/item/basic/add?itemId=' + itemId);
}
