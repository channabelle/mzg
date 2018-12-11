var FILTER = (function (mod) {
    mod.loginFilter = function (response) {

        if (response && 2001 == response.status && response.message && 21 == parseInt(response.message.code / 100)) {
            MY_CONFIG.clearCookie('shopManager');

            var _u = '/page/user-login.html?returnUrl=' + encodeURIComponent(ROUTER.getCurrentURL());
            ROUTER.go(_u);
            return false;
        }
        return true;
    };

    mod.filter = function (response) {
        return this.loginFilter(response);
    };
    return mod;
})(window.FILTER || {});