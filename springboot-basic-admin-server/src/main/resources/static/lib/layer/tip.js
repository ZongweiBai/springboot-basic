var tip = {

    /**
     * 版本号
     */
    version: 1.0
};

tip.toast = function (message) {
    layer.msg(message, {
        time: 2000
    });
};

tip.alert = function (message) {
    layer.alert(message, {title: '温馨提示', skin: 'layui-layer-molv', closeBtn: 0, icon: 0});
};

tip.alertSuccess = function (message, callBack) {
    if (callBack == null) {
        layer.alert(message, {title: '温馨提示', skin: 'layui-layer-molv', closeBtn: 0, icon: 1});
    } else {
        layer.alert(message, {title: '温馨提示', skin: 'layui-layer-molv', closeBtn: 0, icon: 1}, function (index) {
            layer.close(index);
            callBack();
        });
    }
};

tip.alertError = function (message, callBack) {
    if (callBack == null) {
        layer.alert(message, {title: '温馨提示', skin: 'layui-layer-molv', closeBtn: 0, icon: 2});
    } else {
        layer.alert(message, {title: '温馨提示', skin: 'layui-layer-molv', closeBtn: 0, icon: 2}, function (index) {
            layer.close(index);
            callBack();
        });
    }
};

tip.tips = function (message, id) {
    layer.tips(message, '#' + id, {
        tips: [1, '#24b292'],
        time: 2000
    });
};

tip.confirm = function (message, callBack) {
    layer.confirm(message, {icon: 3, closeBtn: 0, title: '温馨提示', skin: 'layui-layer-molv'}, function (index) {
        layer.close(index);
        callBack();
    });
};

tip.showLoading = function () {
    layer.load(2, {
        shade: [0.3, '#fff'] //0.3透明度的白色背景
    });
};

tip.hideLoading = function () {
    layer.closeAll('loading'); //关闭所有加载层
};

/**
 * 参数解释：
 * title    标题
 * url        请求的url
 * w        弹出层宽度（缺省调默认值）
 * h        弹出层高度（缺省调默认值）
 */
tip.openIframe = function (title, url, w, h, end) {
    if (title == null || title == '') {
        title = false;
    }
    if (url == null || url == '') {
        url = contextPath + "/assets/page/404.jsp";
    }
    var layerIfreme = layer.open({
        type: 2,
        area: [w + 'px', h + 'px'],
        fix: false, //不固定
        maxmin: true,
        shade: 0.4,
        title: title,
        content: url,
        end: end
    });
    if (isEmpty(w) || isEmpty(h)) {
        layer.full(layerIfreme);
    }
};

/**
 * 关闭弹出框口
 */
tip.closeIframe = function () {
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
};