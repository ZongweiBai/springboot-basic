$().ready(function () {
    // 加载列表
    loadTable();
});

function curRoleType() {
    return $(".layui-tab-title li[class='layui-this']").data('roletype');
}

function loadTable() {
    $('#menuTable').bootstrapTable({
        url: contextPath + "/user/queryUserForPage",
        dataType: "json",
        method: "POST",
        contentType: "application/x-www-form-urlencoded",
        queryParams: function (params) {
            var paramsMap = {
                size: params.limit,  //页面大小
                page: params.offset / params.limit,
                sort: params.sort,
                order: params.order,
                account: $("#userAccount").val(),
                nickName: $("#nickName").val(),
                sex: $("#sex").val(),
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
                field: 'id',
                title: '编号',
                align: 'center'
            },
            {
                field: 'nickName',
                title: '昵称',
                align: 'center'
            },
            {
                field: 'account',
                title: '手机号',
                align: 'center'
            },
            {
                field: 'idpNickName',
                title: '微信昵称',
                align: 'center'
            },
            {
                field: 'sex',
                title: '性别',
                align: 'center',
                formatter: function (value, row, index) {
                    if (value == "M") {
                        return "男";
                    } else {
                        return "女";
                    }
                }
            },
            {
                field: 'orderCount',
                title: '下单次数',
                align: 'center'
            },
            {
                field: 'registerTime',
                title: '注册时间',
                align: 'center',
                formatter: function (value, row, index) {
                    return getFormatDateByLong(value, "yyyy-MM-dd hh:mm:ss");
                }
            },
            {
                field: 'idpId',
                title: '是否绑定微信',
                align: 'center',
                formatter: function (value, row, index) {
                    if (isEmpty(value)) {
                        return "未绑定";
                    }
                    return "已绑定";
                }
            },
            {
                field: 'id',
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    var content = ''
                    content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="viewUserInfo(\'' + value + '\')" title="查看详情"><i style="font-size: 18px;" class="Hui-iconfont">&#xe695;</i></a>&nbsp;';
                    content += '<a href="javascript:void(0);" style="text-decoration:none;" onclick="editUserInfo(\'' + value + '\')" title="编辑"><i style="font-size: 18px;" class="Hui-iconfont">&#xe6df;</i></a>&nbsp;';
                    return content;
                }
            }

        ]
    });
}

/**
 * 查看详情
 * @param userId
 */
function viewUserInfo(userId) {
    tip.openIframe("用户详情", contextPath + 'index/userprofile/detail?userId=' + userId);
}

/**
 * 编辑
 * @param userId
 */
function editUserInfo(userId) {
    tip.openIframe("编辑用户", contextPath + 'index/userprofile/add?userId=' + userId, null, null, refreshData);
}

/**
 * 新增
 * @param userId
 */
function addUser() {
    tip.openIframe("新增用户", contextPath + 'index/userprofile/add', null, null, refreshData);
}

var refreshData = function () {
    $('#menuTable').bootstrapTable('refresh');
}
