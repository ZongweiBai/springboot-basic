
$(function () {

    initDictData("CARE_PLAN_TYPE");
    initDictData("CARE_PLAN_CASE");

    initForm();

    if (!isEmpty(planId)) {
        $("#planId").val(planId);
        loadDictData(planId);
    }
});

function initForm() {
    $("#form-menu-add").Validform({
        tiptype: 2,
        callback: function (form) {
            $.ajax({
                type: "POST",
                url: contextPath + "careplan/saveCarePlan",
                data: $('#form-menu-add').serialize(),
                beforeSend: function () {
                    tip.showLoading();
                },
                error: function () {
                    tip.hideLoading();
                    tip.alertError("保存失败");
                },
                success: function (data) {
                    tip.hideLoading();
                    if (data.result == "200") {
                        tip.alertSuccess("保存成功", function () {
                            tip.closeIframe();
                        });
                    } else {
                        tip.alertError(data.message);
                    }
                }
            });
            return false;
        }
    });
}

/**
 * 加载字典信息
 * @param dictId
 */
function loadDictData(planId) {
    $.ajax({
        type: "GET",
        url: contextPath + "careplan/getCarePlanById",
        data: {
            "planId": planId
        },
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var info = data.info;
                if (!isEmpty(info)) {
                    $("#typeId").val(info.typeId);
                    $("#caseId").val(info.caseId);
                    $("#planDesc").html(info.planDesc);
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

function initDictData(dictName) {
    $.ajax({
        type: "GET",
        url: contextPath + "system/getSysDictByDictName",
        data: {
            "dictName": dictName
        },
        async: false,
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