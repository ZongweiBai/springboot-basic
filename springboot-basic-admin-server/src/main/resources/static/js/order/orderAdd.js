$(function () {

    initUserList();

    initQuestionList();

    initProductInfo();

    initHospital();

    initForm();

});

function initForm() {
    $("#form-menu-add").Validform({
        tiptype: 1,
        callback: function (form) {
            $.ajax({
                type: "POST",
                url: contextPath + "order/saveOrder",
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
                        tip.alertSuccess("下单成功", function () {
                            $("#form-menu-add")[0].reset();
                            window.location.reload();
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

var userMap = new Map();

function initUserList() {
    $.ajax({
        type: "GET",
        url: contextPath + "user/queryUserProfile",
        data: {},
        success: function (data) {
            if (data.result == 200) {
                var rows = data.rows;
                if (!isEmpty(rows)) {
                    for (var index = 0; index < rows.length; index++) {
                        userMap.put(rows[index].id, rows[index]);
                        $("#userSelect").append("<option value='" + rows[index].id + "'>" + rows[index].account + '-' + rows[index].nickName + "</option>");
                    }
                }
            }
        }
    });
}

function initQuestionList() {
    $.ajax({
        type: "GET",
        url: contextPath + "question/queryQuestions",
        data: {
            "careType": "HOSPITAL_CARE"
        },
        success: function (data) {
            if (data.result == 200) {
                var rows = data.rows;
                if (!isEmpty(rows)) {
                    var DISEASES = rows.DISEASES;
                    var SELF_CARE = rows.SELF_CARE;
                    var EATING = rows.EATING;
                    var CATHETER_CARE = rows.CATHETER_CARE;
                    var ASSIST_WITH_MEDICATION = rows.ASSIST_WITH_MEDICATION;

                    parseQuestion(DISEASES, "checkbox", "DISEASES");
                    parseQuestion(SELF_CARE, "radio", "SELF_CARE");
                    parseQuestion(EATING, "radio", "EATING");
                    parseQuestion(CATHETER_CARE, "checkbox", "CATHETER_CARE");
                    parseQuestion(ASSIST_WITH_MEDICATION, "checkbox", "ASSIST_WITH_MEDICATION");
                }
            }
        }
    });
}

var questionIndex = 0;

function parseQuestion(questions, type, divId) {
    if (!isEmpty(questions)) {
        var content = '';
        content += '<dl class="permission-list">';
        content += '<dd><dl class="cl permission-list2">';
        if (type == 'checkbox') {
            for (var i = 0; i < questions.length; i++) {
                var question = questions[i];
                content += '<dt>';
                content += '<label class="">';
                if (divId == 'CATHETER_CARE' || divId == 'ASSIST_WITH_MEDICATION') {
                    content += '<input onclick="clickQuestion(this, \''+divId+'\');" class="'+divId+'" questionName="'+question.itemName+'" type="checkbox" value="' + question.id + '" name="questions[' + questionIndex + '].id" />';
                } else {
                    content += '<input type="checkbox" value="' + question.id + '" name="questions[' + questionIndex + '].id" />';
                }
                content += question.itemName + '</label>';
                content += '</dt>';
                questionIndex++;
            }
        } else {
            questionIndex++;
            for (var i = 0; i < questions.length; i++) {
                var question = questions[i];
                content += '<dt>';
                content += '<label class="">';
                content += '<input type="radio" value="' + question.id + '" name="questions[' + questionIndex + '].id" />' + question.itemName + '</label>';
                content += '</dt>';
            }
        }

        content += '</dl></dd>';
        content += '</dl>';
        $("#" + divId + "_div").html(content);
    }
}

function clickQuestion(obj, type) {
    var checked = $(obj).prop("checked");
    var questionName = $(obj).attr("questionName");
    if (type == 'CATHETER_CARE') {
        if (checked && questionName == '无') {
            $('input:checkbox[class=CATHETER_CARE]').prop("checked", false);
            $(obj).prop("checked", true);
        } else {
            $('input:checkbox[class=CATHETER_CARE][questionName=无]').prop("checked", false);
        }
    }
    if (type == 'ASSIST_WITH_MEDICATION') {
        if (checked && questionName == '无') {
            $('input:checkbox[class=ASSIST_WITH_MEDICATION]').prop("checked", false);
            $(obj).prop("checked", true);
        } else {
            $('input:checkbox[class=ASSIST_WITH_MEDICATION][questionName=无]').prop("checked", false);
        }
    }
}

function selectUser(userId) {
    $("#orderUserId").val(userId);
    var userProfile = userMap.get(userId);
    $("#userName").html(userProfile.nickName);
    $("#userMobile").html(userProfile.account);
}

function initProductInfo() {
    $.ajax({
        type: "GET",
        url: contextPath + "item/getBasicProductByType",
        data: {
            "careType": "HOSPITAL_CARE"
        },
        success: function (data) {
            if (data.result == 200) {
                var rows = data.rows;
                if (!isEmpty(rows)) {
                    for (var index = 0; index < rows.length; index++) {
                        $("#productId").append("<option value='" + rows[index].id + "'>" + rows[index].productName + "</option>");
                    }
                }
            }
        }
    });
}

function initHospital() {
    $.ajax({
        type: "GET",
        url: contextPath + "hospital/getAllHospital",
        data: {},
        success: function (data) {
            if (data.result == 200) {
                var rows = data.rows;
                if (!isEmpty(rows)) {
                    for (var index = 0; index < rows.length; index++) {
                        $("#hospitalAddress").append("<option value='" + rows[index].hospitalName + "'>" + rows[index].hospitalName + "</option>");
                    }
                }
            }
        }
    });
}

var productFee = 0.0;

function selectProduct(productId) {
    $.ajax({
        type: "GET",
        url: contextPath + "item/getServiceProductById",
        data: {
            "productId": productId
        },
        success: function (data) {
            if (data.result == 200) {
                var rows = data.rows;
                if (!isEmpty(rows)) {
                    var BASIC = rows.BASIC;
                    var MEDICAL = rows.MEDICAL;
                    var LIFE = rows.LIFE;
                    var HOME = rows.HOME;

                    parseBasicItem(BASIC, "BASIC");
                    parseBasicItem(MEDICAL, "MEDICAL");
                    parseBasicItem(LIFE, "LIFE");
                    parseBasicItem(HOME, "HOME");
                }
                var info = data.info;
                productFee = info.productPrice;
                calculateTotalFee();
            }
        }
    });
}

var basicItemPrice = 0;
var itemIndex = 0;
var basicItemPriceMap = new Map();
function parseBasicItem(basicItems, divId) {
    if (!isEmpty(basicItems)) {
        var content = '';
        content += '<dl class="basic-item-list">';
        content += '<dd><dl class="cl permission-list2">';
        for (var i = 0; i < basicItems.length; i++) {
            var item = basicItems[i];
            content += '<dt>';
            content += '<label class="">';
            if (item.checked == true) {
                content += '<input type="checkbox" value="' + item.id + '" name="basicItems[' + itemIndex + '].id" checked="checked" readonly="readonly" disabled="disabled" />' + item.itemName + '</label>';
            } else {
                basicItemPriceMap.put(item.id, item.itemFee);
                content += '<input class="basic-item-input" onclick="calculateItemPrice();" type="checkbox" value="' + item.id + '" name="basicItems[' + itemIndex + '].id" />' + item.itemName + '</label>';
            }
            content += '</dt>';
            questionIndex++;
            itemIndex++;
        }
        content += '</dl></dd>';
        content += '</dl>';
        $("#" + divId + "_div").html(content);
        $("#" + divId + "_div_panel").show();
    }
}

var serviceDuration = 0;

function getTimeStamp(type) {
    if (type == '1') {
        var stringTime = $("#datemin").val();
        if (isEmpty(stringTime)) {
            $("#serviceStartDate").val("");
        } else {
            $("#serviceStartDate").val(datetime_to_unix(stringTime));
        }
    } else {
        var stringTime = $("#datemax").val();
        if (isEmpty(stringTime)) {
            $("#serviceEndDate").val("");
        } else {
            $("#serviceEndDate").val(datetime_to_unix(stringTime));
        }
    }
    var startDate = $("#serviceStartDate").val();
    var endDate = $("#serviceEndDate").val();
    if (!isEmpty(startDate) && !isEmpty(endDate)) {
        var durationHour = (parseInt(endDate) - parseInt(startDate)) / 1000 / 3600;
        var durationDays = durationHour / 24;
        var modHours = durationHour % 24;
        if (modHours > 12) {
            serviceDuration = Math.floor(durationDays) + 1;
        } else if (modHours < 1) {
            serviceDuration = Math.floor(durationDays);
        } else {
            serviceDuration = Math.floor(durationDays) + 0.5;
        }
    } else {
        serviceDuration = 0;
    }
    $("#serviceDuration").val(serviceDuration);
    $("#serviceDurationSpan").html(serviceDuration + " 天");

    calculateTotalFee();
}

function calculateItemPrice() {
    basicItemPrice = 0;
    $('input:checkbox[class=basic-item-input]:checked').each(function(i){
        basicItemPrice += basicItemPriceMap.get($(this).val());
    });
    calculateTotalFee();
}

function calculateTotalFee() {
    var fee = productFee + basicItemPrice;
    var totalFee = serviceDuration * fee;
    $("#totalFee").val(totalFee.toFixed(2));
}