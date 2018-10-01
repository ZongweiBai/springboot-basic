$().ready(function () {
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "/question/queryQuestionForPage",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                size: params.limit,  //页面大小
                page: params.offset / params.limit,
                sort: params.sort,
                order: params.order,
                careType: $("#careType").val(),
                questionType: $("#questionType").val()
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
                field: 'careType',
                title: '陪护类型',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == "HOSPITAL_CARE") {
                        return "医院陪护";
                    } else if (value == "HOME_CARE") {
                        return "居家照护";
                    } else if (value == "REHABILITATION") {
                        return "康复护理";
                    }
                    return "";
                }
            },
            {
                field: 'questionType',
                title: '问题类型',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == "DISEASES") {
                        return "问题1";
                    } else if (value == "SELF_CARE") {
                        return "问题2";
                    } else if (value == "EATING") {
                        return "问题3";
                    } else if (value == "CATHETER_CARE") {
                        return "问题4";
                    } else if (value == "ASSIST_WITH_MEDICATION") {
                        return "问题5";
                    }
                    return "";
                }
            },
            {
                field: 'itemName',
                title: '问题选项',
                align: 'center'
            },
            {
                field: 'itemDesc',
                title: '选项描述',
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
function addDict() {
    tip.openIframe("新增问题", contextPath + 'index/question/add');
}

/**
 * 编辑
 * @param serviceId
 */
function editServiceType(questionId) {
    tip.openIframe("编辑问题", contextPath + 'index/question/add?questionId=' + questionId);
}
