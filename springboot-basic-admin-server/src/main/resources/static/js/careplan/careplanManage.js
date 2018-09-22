$().ready(function () {
    initDictData("CARE_PLAN_TYPE");
    initDictData("CARE_PLAN_CASE");
    // 加载列表
    loadTable();
});

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "careplan/queryCarePlanForPage",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                size: params.limit,  //页面大小
                page: params.offset / params.limit,
                typeId: $("#typeId").val(),
                caseId: $("#caseId").val(),
                planDesc: $("#planDesc").val()
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
                field: 'planDesc',
                title: '照护明细项',
                align: 'center'
            },
            {
                field: 'typeId',
                title: '类型',
                align: 'center',
                formatter: function (value, row, index) {
                    var typeDict = row.typeDict;
                    if (typeDict === null) {
                        return "-";
                    }
                    return typeDict.codeValue;
                }
            },
            {
                field: 'caseId',
                title: '适应症',
                align: 'center',
                formatter: function (value, row, index) {
                    var caseDict = row.caseDict;
                    if (caseDict === null) {
                        return "-";
                    }
                    return caseDict.codeValue;
                }
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
    tip.openIframe("新增", contextPath + 'index/careplan/add');
}

/**
 * 编辑字典
 * @param menuId
 */
function editDict(planId) {
    tip.openIframe("编辑", contextPath + 'index/careplan/add?planId=' + planId);
}

function deleteDict(planId) {
    tip.confirm("确定要删除吗？删除后无法恢复！", function () {
        $.ajax({
            type: "POST",
            url: contextPath + "careplan/deleteCarePlan",
            data: {
                "planId": planId
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

function initDictData(dictName) {
    $.ajax({
        type: "GET",
        url: contextPath + "system/getSysDictByDictName",
        data: {
            "dictName": dictName
        },
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var rows = data.rows;
                if (!isEmpty(rows)) {
                    for (var i = 0; i < rows.length; i++) {
                        if (dictName == "CARE_PLAN_TYPE") {
                            $("#typeId").append('<option value="'+rows[i].id+'">'+rows[i].codeValue+'</option>');
                        } else if (dictName == "CARE_PLAN_CASE") {
                            $("#caseId").append('<option value="'+rows[i].id+'">'+rows[i].codeValue+'</option>');
                        }
                    }
                }
            } else {
                tip.alertError("加载信息失败");
            }
        },
        error: function () {
            tip.hideLoading();
            tip.alertError("加载信息失败");
        }
    });
}