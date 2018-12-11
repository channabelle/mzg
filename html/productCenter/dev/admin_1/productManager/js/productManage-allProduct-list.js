$(function () {
    console.log('ready');

    var _vue = _fn_initVue();

    _fn_initWidget(
        REG_EXP.getURLParam('_proName'),
        REG_EXP.getURLParam('_cTime_start'),
        REG_EXP.getURLParam('_cTime_end'));

    _fn_refreshProductList(
        _vue,
        REG_EXP.getURLParam('_proName'),
        REG_EXP.getURLParam('_cTime_start'),
        REG_EXP.getURLParam('_cTime_end'));

    setTimeout(() => {
        _fn_initDataTable();
    }, 50);

    console.log('end');
});

/*
dateRange空间无法和VUE同时使用
*/
var _fn_initWidget = function (_proName, _cTime_start, _cTime_end) {
    $('.input_proName').val(_proName);

    $('.clickSearch').click(function () {
        var _proName = $('.input_proName').val();
        var _cTime_start = $('.input_dateRange_start').html();
        var _cTime_end = $('.input_dateRange_end').html();

        ROUTER.replaceAbsPath('?search=1'
            + ((null == _proName || 0 == _proName.length) ? "" : "&_proName=" + _proName)
            + ((null == _cTime_start || 0 == _cTime_start.length) ? "" : "&_cTime_start=" + _cTime_start)
            + ((null == _cTime_end || 0 == _cTime_end.length) ? "" : "&_cTime_end=" + _cTime_end));
    });

    $('.clickReset').click(function () {
        ROUTER.replaceAbsPath('?search=1');
    });

    $('.pro_add').click(function () {
        ROUTER.goWindow("/productManager/productManage-edit-proInfo.html?action=c");
    });
}


var _fn_refreshProductList = function (_v, _proName, _cTime_start, _cTime_end) {
    AJAX.get(
        url = "/b/product/proInfo/list?page=1&pagePerNumber=" + MY_CONFIG.NUMBER.MAX_INT_VALUE
        + ((null == _proName) ? "" : "&proName=" + _proName)
        + ((null == _cTime_start) ? "" : "&cTime_start=" + _cTime_start)
        + ((null == _cTime_end) ? "" : "&cTime_end=" + _cTime_end),
        successCb = function (res) {
            if (2000 == res.status && res.data) {
                var _d = res.data;

                if (_d && _d.list) {
                    _v.list = [];
                    _d.list.forEach(element => {
                        element.cTime = new Date(element.cTime).toLocaleString('chinese', { hour12: false });
                        element.uTime = new Date(element.uTime).toLocaleString('chinese', { hour12: false });
                        _v.list.push(element);
                    });

                }
            }
        },
        alwaysCb = null,
        formValid = null,
        isBlockUI = true,
        animateSubmit = false,
        _async = false);
};

var _fn_initDataTable = function () {
    $('.dataTable').DataTable({
        order: dataTableConfig.order,
        language: dataTableConfig.language,
        destroy: true
    });
};

var _fn_initVue = function (_userName) {
    return new Vue({
        el: '.vueData',
        data: {
            list: []
        },
        methods: {
            clickProInfo: function (_uuid) {
                console.log('clickProInfo', _uuid);
                ROUTER.goWindow("/productManager/productManage-edit-proInfo.html?action=u&pid=" + _uuid);
            }
        }
    });
};
