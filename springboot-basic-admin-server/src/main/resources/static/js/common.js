/**
 * 判断是否为空
 * @param val
 */
function isEmpty(val) {
    if (val != null && val != undefined && val != '' && val != 'null') {
        return false;
    }
    return true;
}

/**
 * 判断是否为图片格式
 * @param type
 * @returns
 */
function isImage(type) {
    var isImage = false;
    var imgTypeArr = ["image/jpeg", "image/png", "image/gif", "image/bmp"];
    for (var index = 0; index < imgTypeArr.length; index++) {
        if (imgTypeArr[index] == type) {
            isImage = true;
            break;
        }
    }
    return isImage;
}

/**
 * 获取Get的请求参数
 * @param name
 * @returns {null}
 */
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null)return unescape(r[2]);
    return null;
}