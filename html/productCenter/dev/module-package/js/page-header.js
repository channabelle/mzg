$(function () {
    var _vue = _fn_initVueCookie();

    // 登出操作
    $('.btn-logout').click(function () {
        var _logoutUrl = "/user/logout";

        AJAX.post(
            url = _logoutUrl,
            data = {
                "account": $('.input-account').val(),
                "password": $('.input-pwd').val()
            },
            successCb = function (data) {
                if (data) {
                    if (2000 == data.status) {
                        MY_CONFIG.clearCookie('shopManager');

                        ROUTER.go('/page/user-login.html');
                    }
                }
            },
            alwaysCb = null,
            formValid = null,
            isBlockUI = false,
            animateSubmit = false);
    });
});

var _fn_initVueCookie = function () {
    var _smStr = MY_CONFIG.getCookie('shopManager');
    var _sm = _smStr ? JSON.parse(_smStr) : {};

    return new Vue({
        el: '.vueCookie',
        data: {
            shopManager: _sm
        }
    });
}