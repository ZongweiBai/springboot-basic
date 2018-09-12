$(function () {

    initForm();

    if (!isEmpty(staffId)) {
        loadStaffById(staffId)
    }

});

function initForm() {
    $("#form-menu-add").Validform({
        tiptype: 2,
        callback: function (form) {
            $.ajax({
                type: "POST",
                url: contextPath + "/staff/saveStaff",
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
 * 上传照片
 */
function fileSelected(type) {
    tip.showLoading();
    uploadImgType = type;
    var oFile = document.getElementById('image_header_file').files[0];
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
            var quality = 1;
            if (naturalWidth > 1024) {
                naturalWidth = 1024;
                quality = 1
            }
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
        url: contextPath + '/system/sysManageController/uploadWap',
        contentType: "application/x-www-form-urlencoded",
        data: {
            "base64": basestr,
            "size": basestr.length // 校验用，防止未完整接收
        },
        error: function (request, error) {
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
    $("#headImg").val(fileNames);
    var content = "<span class=\"pic3\">";
    content += "<a data-lightbox=\"" + imgPath + fileNames + "\" href=\"" + imgPath + fileNames + "\">";
    content += "<img src=\"" + imgPath + fileNames + "\" >";
    content += "</a>";
    content += "<img class=\"guanbi\" onclick=\"removeFileImg(this, '" + fileNames + "', '" + uploadImgType + "');\" >";
    content += "</span>";
    $("#headerImgDiv").html(content);
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
        if ($("#headerImg").length > 0) {
            arr = $("#headerImg").val().split(",");
        }
    } else {
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
    $("#headerImg").val(newStr);
    $(obj).parent("span").remove();
}

function loadStaffById(staffId) {
    $.ajax({
        type: "GET",
        url: contextPath + "/staff/getStaffById",
        data: {
            "staffId": staffId
        },
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var info = data.info;
                $("#id").val(info.id);
                $("#userName").val(info.userName);
                $("#mobile").val(info.mobile);
                $("input[name='sex'][value='"+info.sex+"']").attr("checked",true);
                $("#experience").val(info.experience);
                $("#height").val(info.height);
                $("#weight").val(info.weight);
                $("#nationality").val(info.nationality);
                $("#birthplace").val(info.birthplace);
                $("#localism").val(info.localism);
                $("#mandarin").val(info.mandarin);
                $("#localtion").val(info.localtion);
                $("#firstSkill").val(info.firstSkill);
                $("#specialty").val(info.specialty);
                $("#idCard").val(info.idCard);
                $("#healthCardId").val(info.healthCardId);
                $("#pensionCardId").val(info.pensionCardId);
                $("#healthCareCardId").val(info.healthCareCardId);
            }
        },
        error: function () {
            tip.hideLoading();
            tip.alertError("加载信息失败");
        }
    });
}
