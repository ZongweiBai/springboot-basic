$().ready(function () {
    loadServiceTypeData();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "item/queryServiceProductForPage",
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
                field: 'id',
                title: '产品编号',
                align: 'center'
            },
            {
                field: 'serviceTypeId',
                title: '服务分类',
                align: 'center',
                formatter: function (value, row, index) {
                    return serviceTypeMap.get(value);
                }
            },
            {
                field: 'productName',
                title: '产品名称',
                align: 'center'
            },
            {
                field: 'productPrice',
                title: '价格',
                align: 'center',
                formatter: function (value, row, index) {
                    return "￥ " + value;
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
    tip.openIframe("新增产品", contextPath + 'index/product/basic/add');
}

/**
 * 编辑
 * @param serviceId
 */
function editServiceType(productId) {
    tip.openIframe("编辑产品", contextPath + 'index/product/basic/add?productId=' + productId);
}

var serviceTypeMap = new Map();

function loadServiceTypeData() {
    $.ajax({
        type: "GET",
        url: contextPath + "item/getAllServiceType",
        data: {},
        success: function (data) {
            tip.hideLoading();
            if (data.result === 200) {
                var rows = data.rows;
                if (!isEmpty(rows)) {
                    rows.forEach(function (value, index, arr) {
                        var typeId = value.id;
                        serviceTypeMap.put(typeId, value.serviceName);
                    });
                }

                // 加载列表
                loadTable();
            } else {
                tip.alertError("加载服务分类信息失败");
            }
        },
        error: function () {
            tip.hideLoading();
            tip.alertError("加载服务分类信息失败");
        }
    });
}
