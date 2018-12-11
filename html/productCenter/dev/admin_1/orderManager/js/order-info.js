$(function () {
    console.log('======= js ready =======');


    var _order_uuid = REG_EXP.getURLParam('order_uuid');
    var _vue = _fn_initVue();
    _fn_refreshVueList(_vue, _order_uuid);


    console.log('======= js  end  =======');
});

var _fn_initVue = function () {
    return new Vue({
        el: '.vueList',
        data: {
            data: null
        }
    });
}

var _fn_refreshVueList = function (_v, _order_uuid) {
    AJAX.get(
        url = "/b/order/info/" + _order_uuid,
        successCb = function (res) {
            if (2000 == res.status && res.data) {
                var _d = res.data;

                _v.data = _d;
            }
        },
        alwaysCb = null,
        formValid = null,
        isBlockUI = true,
        animateSubmit = false,
        _async = false);
}