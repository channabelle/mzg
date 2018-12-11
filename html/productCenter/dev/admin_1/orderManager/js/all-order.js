$(function () {
    console.log('ready');


    var _vue = _fn_initVue();

    _fn_initWidget(
        REG_EXP.getURLParam('_orderName'),
        REG_EXP.getURLParam('_cTime_start'),
        REG_EXP.getURLParam('_cTime_end'));

    _fn_refreshDataTableList(
        _vue,
        REG_EXP.getURLParam('_orderName'),
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
var _fn_initWidget = function (_orderName, _cTime_start, _cTime_end) {
    $('.input_orderName').val(_orderName);

    $('.clickSearch').click(function () {
        var _orderName = $('.input_orderName').val();
        var _cTime_start = $('.input_dateRange_start').html();
        var _cTime_end = $('.input_dateRange_end').html();

        ROUTER.replaceAbsPath('?search=1'
            + ((null == _orderName || 0 == _orderName.length) ? "" : "&_orderName=" + _orderName)
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

var _fn_initVue = function () {
    return new Vue({
        el: '.listTable',
        data: {
            list: []
        },
        methods: {
            clickOrder: function (order_uuid) {
                console.log('clickOrder', order_uuid);
                ROUTER.goWindow('/orderManager/order-info.html?order_uuid=' + order_uuid);
            }
        }
    });
}

var _fn_refreshDataTableList = function (_v, _orderName, _cTime_start, _cTime_end) {
    AJAX.get(
        url = "/b/order/list?page=1&pagePerNumber=" + MY_CONFIG.NUMBER.MAX_INT_VALUE
        + ((null == _orderName) ? "" : "&orderName=" + _orderName)
        + ((null == _cTime_start) ? "" : "&cTime_start=" + _cTime_start)
        + ((null == _cTime_end) ? "" : "&cTime_end=" + _cTime_end),
        successCb = function (res) {
            if (2000 == res.status && res.data) {
                var _d = res.data;

                if (_d && _d.list) {
                    _v.list = [];
                    _d.list.forEach(function (item, index) {
                        _v.list.push(item);
                    });
                }
            }
        },
        alwaysCb = null,
        formValid = null,
        isBlockUI = true,
        animateSubmit = false,
        _async = false);
}