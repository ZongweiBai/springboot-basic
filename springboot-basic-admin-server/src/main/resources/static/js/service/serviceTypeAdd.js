$(function () {

    initForm();

    if (!isEmpty(typeId)) {
        $("#typeId").val(typeId);
        loadRoleData(typeId);
    }

});

function initForm() {
    $("#form-menu-add").Validform({
        tiptype: 2,
        callback: function (form) {
            $.ajax({
                type: "POST",
                url: contextPath + "/item/saveServiceType",
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
function loadRoleData(typeId) {
    $.ajax({
        type: "GET",
        url: contextPath + "/item/getServiceTypeById",
        data: {
            "typeId": typeId
        },
        beforeSend: function () {
            tip.showLoading();
        },
        success: function (data) {
            tip.hideLoading();
            if (data.result == 200) {
                var info = data.info;
                $("#serviceName").val(info.serviceName);
                $("#keyWord").val(info.keyWord);
                $("#serviceDesc").html(info.serviceDesc);

                fillImageUpload(info.serviceIcon, '1');
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

    // 计算是否超过了3个附件
    // var fileArr;
    // if (uploadImgType == 2) {
    //     fileArr = $("#productBannerImgs").val().split(",");
    // }
    // if (!isEmpty(fileArr)) {
    //     if (fileArr.length >= 3) {
    //         $("#uploadBannerImageSpan").hide();
    //         tip.hideLoading();
    //         tip.toast('最多上传三张图片');
    //         return;
    //     }
    // }

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
        $("#serviceIcon").val(fileNames);
        var content = "<span class=\"pic3\">";
        content += "<a data-lightbox=\"" + imgPath + fileNames + "\" href=\"" + imgPath + fileNames + "\">";
        content += "<img src=\"" + imgPath + fileNames + "\" >";
        content += "</a>";
        content += "<img class=\"guanbi\" onclick=\"removeFileImg(this, '" + fileNames + "', '" + uploadImgType + "');\" >";
        content += "</span>";
        $("#imgDiv").html(content);
    }
    // else if (uploadImgType == 2) {
    //     if ($("#productBannerImgs").val() == "") {
    //         $("#productBannerImgs").val(fileNames);
    //     } else {
    //         $("#productBannerImgs").val($("#productBannerImgs").val() + "," + fileNames);
    //     }
    //     var content = "<span class=\"pic3\">";
    //     content += "<a data-lightbox=\"" + imgPath + fileNames + "\" href=\"" + imgPath + fileNames + "\">";
    //     content += "<img src=\"" + imgPath + fileNames + "\" >";
    //     content += "</a>";
    //     content += "<img class=\"guanbi\" onclick=\"removeFileImg(this, '" + fileNames + "', '" + uploadImgType + "');\" >";
    //     content += "</span>";
    //     $("#productBannerImgDiv").prepend(content);
    //     var fileArr = $("#productBannerImgs").val().split(",");
    //     if (!isEmpty(fileArr)) {
    //         if (fileArr.length >= 3) {
    //             $("#uploadBannerImageSpan").hide();
    //         }
    //     }
    // }else if (uploadImgType == 3) {
    //     $("#indexImg").val(fileNames);
    //     var content = "<span class=\"pic3\">";
    //     content += "<a data-lightbox=\"" + imgPath + fileNames + "\" href=\"" + imgPath + fileNames + "\">";
    //     content += "<img src=\"" + imgPath + fileNames + "\" >";
    //     content += "</a>";
    //     content += "<img class=\"guanbi\" onclick=\"removeFileImg(this, '" + fileNames + "', '" + uploadImgType + "');\" >";
    //     content += "</span>";
    //     $("#indexImgDiv").html(content);
    // }
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
        if ($("#serviceIcon").length > 0) {
            arr = $("#serviceIcon").val().split(",");
        }
    }
    // else if (type == 2) {
    //     if ($("#productBannerImgs").length > 0) {
    //         arr = $("#productBannerImgs").val().split(",");
    //     }
    // } else if (type == 3) {
    //     if ($("#indexImg").length > 0) {
    //         arr = $("#indexImg").val().split(",");
    //     }
    // }
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
        $("#serviceIcon").val(newStr);
    }
    // else if (type == 2) {
    //     $("#productBannerImgs").val(newStr);
    //     var fileArr = $("#productBannerImgs").val().split(",");
    //     if (!isEmpty(fileArr)) {
    //         if (fileArr.length < 3) {
    //             $("#uploadBannerImageSpan").show();
    //         }
    //     }
    // }else if (type == 3) {
    //     $("#indexImg").val(newStr);
    // }
    $(obj).parent("span").remove();
}
