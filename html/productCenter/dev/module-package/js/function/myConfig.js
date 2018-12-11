var MY_CONFIG = (function (mod) {
    mod.SERVER_URL = "http://localhost:8088/productCenterServer";

    mod.getAllCookie = function () {
        var _cookie = $.cookie();
        // console.log('getAllCookie: ', _cookie);

        return _cookie;
    };

    mod.getCookie = function (name) {
        var _value = $.cookie(name);
        // console.log('getCookie ' + name + ": ", _value);

        return _value;
    };

    mod.setCookie = function (name, value, timeSec) {
        // console.log('setCookie name: ' + name + ", value: " + value + ", second: " + timeSec);

        var _date = new Date();
        _date.setTime(_date.getTime() + timeSec * 1000);

        $.cookie(name, value, {
            path: "/",
            expires: _date
        });
    };

    mod.clearCookie = function (name) {
        $.removeCookie(name, {
            path: "/"
        });
    };

    mod.NUMBER = {
        MAX_INT_VALUE: 2147483647
    };

    return mod;
})(window.MY_CONFIG || {});