var _vue = null;

$(function () {
    console.log('ready');

    _vue = _fn_initVue();

    _fn_initWidget(
        REG_EXP.getURLParam('_userName'),
        REG_EXP.getURLParam('_phone'),
        REG_EXP.getURLParam('_cTime_start'),
        REG_EXP.getURLParam('_cTime_end'));

    _fn_refreshDataTableList(
        _vue,
        REG_EXP.getURLParam('_userName'),
        REG_EXP.getURLParam('_phone'),
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
var _fn_initWidget = function (_userName, _phone, _cTime_start, _cTime_end) {
    $('.input_userName').val(_userName);
    $('.input_phone').val(_phone);

    $('.clickSearch').click(function () {
        var _userName = $('.input_userName').val();
        var _phone = $('.input_phone').val();
        var _cTime_start = $('.input_dateRange_start').html();
        var _cTime_end = $('.input_dateRange_end').html();

        ROUTER.replaceAbsPath('?search=1'
            + ((null == _userName || 0 == _userName.length) ? "" : "&_userName=" + _userName)
            + ((null == _phone || 0 == _phone.length) ? "" : "&_phone=" + _phone)
            + ((null == _cTime_start || 0 == _cTime_start.length) ? "" : "&_cTime_start=" + _cTime_start)
            + ((null == _cTime_end || 0 == _cTime_end.length) ? "" : "&_cTime_end=" + _cTime_end));
    });
    $('.clickReset').click(function () {
        ROUTER.replaceAbsPath('?search=1');
    });

}


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
    });
};

var _fn_refreshDataTableList = function (
    _v, _userName = null, _phone = null, _cTime_start = null, _cTime_end = null) {
    AJAX.get(
        url = "/b/user/list?page=1&pagePerNumber=" + MY_CONFIG.NUMBER.MAX_INT_VALUE
        + ((null == _userName) ? "" : "&uName=" + _userName)
        + ((null == _phone) ? "" : "&phone=" + _phone)
        + ((null == _cTime_start) ? "" : "&cTime_start=" + _cTime_start)
        + ((null == _cTime_end) ? "" : "&cTime_end=" + _cTime_end),
        successCb = function (res) {
            if (2000 == res.status && res.data) {
                var _d = res.data;

                if (_d && _d.list) {
                    _v.list = [];
                    _d.list.forEach(element => {
                        element.cTime = new Date(element.cTime).toLocaleString('chinese', { hour12: false });
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

