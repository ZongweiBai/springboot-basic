$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "/hospital/queryHospitalForPage",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                size: params.limit,  //页面大小
                page: params.offset / params.limit,
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
                field: 'hospitalName',
                title: '医院名称',
                align: 'center'
            },
            {
                field: 'departmentNames',
                title: '医院所属科室',
                align: 'center'
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
function addDict() {
    tip.openIframe("新增医院", contextPath + 'index/hospital/add');
}

/**
 * 编辑
 * @param serviceId
 */
function editServiceType(hospitalId) {
    tip.openIframe("编辑医院", contextPath + 'index/hospital/add?hospitalId=' + hospitalId);
}
