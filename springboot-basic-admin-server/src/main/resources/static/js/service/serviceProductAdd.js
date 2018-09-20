$(function () {

    initForm();
    loadServiceTypeData();

    if (!isEmpty(productId)) {
        $("#productId").val(productId);
        loadRoleData(productId);
    } else {
        loadMenuData();
    }

});

function initForm() {
    $("#form-menu-add").Validform({
        tiptype: 2,
        callback: function (form) {
            $.ajax({
                type: "POST",
                url: contextPath + "item/saveServiceProduct",
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
 * 加载信息
 */
function loadRoleData(productId) {
    $.ajax({
        type: "GET",
        url: contextPath + "item/getServiceProductById",
        data: {
            "productId": productId
        },
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var info = data.info;
                $("#serviceTypeId").val(info.serviceTypeId);

                $("#productName").val(info.productName);
                $("#productPrice").val(info.productPrice);
                $("#serviceTips").html(info.serviceTips);
                $("#appointmentNotice").html(info.appointmentNotice);

                fillImageUpload(info.productIcon, '1');

                var rows = data.rows;
                if (!isEmpty(rows)) {
                    parseMenuList(rows);
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

var uploadImgType;

/**
 * 上传照片
 */
function fileSelected(type) {
    tip.showLoading();
    uploadImgType = type;
    var oFile;
    if (type == 1) {
        oFile = document.getElementById('image_index_file').files[0];
    } else {
        tip.hideLoading();
        tip.toast("图片类型异常");
        return;
    }
    var reader = new FileReader();
    reader.onload = function () {
        var result = this.result;

        if (!isImage(oFile.type)) {
            tip.hideLoading();
            tip.toast("请上传图片");
            return;
        }
        var oImage = document.getElementById("preview");
        oImage.src = result;
        oImage.onload = function () {
            var naturalWidth = oImage.naturalWidth;
            var naturalHeight = oImage.naturalHeight;
            var quality = 1;
            // if (naturalWidth != 72 || naturalHeight != 72) {
            //     tip.hideLoading();
            //     tip.toast("图片大小不是72*72");
            //     return;
            // }
            lrz(oFile, {width: naturalWidth, quality: quality}).then(function (rst) {
                startUploading(rst.base64);
            })
        };
    };

    reader.readAsDataURL(oFile);
}

/**
 * 开始上传
 */
function startUploading(basestr) {
    $.ajax({
        type: "POST",
        url: contextPath + '/system/uploadWap',
        contentType: "application/x-www-form-urlencoded",
        data: {
            "base64": basestr,
            "size": basestr.length, // 校验用，防止未完整接收
            "timestamp": new Date()
        },
        error: function () {
            tip.hideLoading();
            tip.alertError("上传失败，请稍后重试");
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var fileNames = data.info;
                fillImageUpload(fileNames, uploadImgType);
            } else {
                tip.alertError("上传失败，请稍后重试");
            }
        }
    });
}

function fillImageUpload(fileNames, uploadImgType) {
    if (uploadImgType == 1) {
        $("#productIcon").val(fileNames);
        var content = "<span class=\"pic3\">";
        content += "<a data-lightbox=\"" + imgPath + fileNames + "\" href=\"" + imgPath + fileNames + "\">";
        content += "<img src=\"" + imgPath + fileNames + "\" >";
        content += "</a>";
        content += "<img class=\"guanbi\" onclick=\"removeFileImg(this, '" + fileNames + "', '" + uploadImgType + "');\" >";
        content += "</span>";
        $("#imgDiv").html(content);
    }
}

/**
 * 删除上传的图片
 * @param obj
 * @param fileName
 * @param type
 */
function removeFileImg(obj, fileName, type) {
    var arr = [];

    if (type == 1) {
        if ($("#productIcon").length > 0) {
            arr = $("#productIcon").val().split(",");
        }
    }
    else {
        return;
    }

    var newStr = "";
    for (var i = 0; i < arr.length; i++) {
        if (fileName != arr[i]) {
            if (newStr == "") {
                newStr += arr[i];
            } else {
                newStr += "," + arr[i];
            }
        }
    }
    if (type == 1) {
        $("#productIcon").val(newStr);
    }
    $(obj).parent("span").remove();
}

function loadMenuData() {
    $.ajax({
        type: "GET",
        url: contextPath + "item/getAllUpcartItems",
        data: {},
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var rows = data.rows;
                if (!isEmpty(rows)) {
                    parseMenuList(rows);
                }
            } else {
                tip.alertError("加载收费项目库失败");
            }
        },
        error: function () {
            tip.hideLoading();
            tip.alertError("加载收费项目库失败");
        }
    });
}

var itemTypeMap = new Map();
itemTypeMap.put("BASIC", "基础护理");
itemTypeMap.put("MEDICAL", "医疗照护");
itemTypeMap.put("LIFE", "活动照护");
itemTypeMap.put("HOME", "生活服务");

var itemPrice = new Map();

function parseMenuList(rows) {
    var itemIndex = 0;
    Object.keys(rows).forEach(function (key) {
        var rowName = itemTypeMap.get(key);
        var itemArr = rows[key];
        var html = '';
        html += '<div class="row cl">';
        html += '<label class="form-label col-3"><span class="c-red">*</span>' + rowName + '：</label>';
        html += '<div class="formControls col-5">';
        if (!isEmpty(itemArr)) {
            html += '<dd><dl class="cl permission-list2">';
            for (var j = 0; j < itemArr.length; j++) {
                var item = itemArr[j];
                itemPrice.put(item.id, item.itemFee);
                html += '<dt>';
                html += '<label class="">';
                if (item.checked == true) {
                    html += '<input type="checkbox" value="' + item.id + '" checkboxType="basicItem" name="itemList[' + itemIndex + '].id" onclick="selectItem(this)" checked="checked" />' + item.itemName + '</label>';
                } else {
                    html += '<input type="checkbox" value="' + item.id + '" checkboxType="basicItem" name="itemList[' + itemIndex + '].id" onclick="selectItem(this)" />' + item.itemName + '</label>';
                }
                html += '</dt>';
                itemIndex++;
            }
            html += '</dl></dd>';
        }
        html += '</div>';
        html += '<div class="col-2"><span class="c-red"></span></div>';
        html += '</div>';
        $("#basicItemDiv").append(html);
    });

}

function selectItem(obj) {
    var totalFee = 0.0;
    $("input[type='checkbox']").attr("checkboxType","basicItem").each(
        function(){
            if ($(this).prop("checked") == true) {
                totalFee += itemPrice.get($(this).val());
            }
        }
    );
    $("#productPrice").val(totalFee);
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
                        serviceTypeMap.put(typeId, value.careType);
                        if (value.careType != "LTC_INSURANCE") {
                            $("#serviceTypeId").append('<option value="' + typeId + '">' + value.serviceName + '</option>');
                        }
                    });
                }
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

function selectServiceType(typeId) {
    var careType = serviceTypeMap.get(typeId);
    if (careType === null || careType === undefined || careType === "") {
        $("#basicItemDiv").show();
        $("#serviceTipsDiv").show();
        $("#appointmentNoticeDiv").show();
    } else if (careType === "HOSPITAL_CARE" || careType === "HOME_CARE") {
        $("#basicItemDiv").show();
        $("#serviceTipsDiv").hide();
        $("#appointmentNoticeDiv").hide();
    } else if (careType === "REHABILITATION") {
        $("#basicItemDiv").hide();
        $("#serviceTipsDiv").show();
        $("#appointmentNoticeDiv").show();
    }
}