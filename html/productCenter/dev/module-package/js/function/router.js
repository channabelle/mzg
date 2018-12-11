var ROUTER = (function (mod) {
    mod.getCurrentDomain = function () {
        return window.location.href.substr(0, window.location.href.indexOf('admin_1/') + 'admin_1/'.length - 1);
    }
    mod.getCurrentURL = function () {
        return window.location.href;
    }
    mod.getCurrentHostAndPath = function () {
        return window.location.protocol + '//' + window.location.host + window.location.pathname;
    }

    mod.replaceAbsPath = function (path) {
        console.log('replaceAbsPath: ' + path);
        window.location.replace(path);
    }

    mod.go = function (path) {
        var _u = this.getCurrentDomain() + path;
        console.log('go: ' + _u);
        window.location.href = _u;
    }

    mod.goAbsPath = function (path) {
        console.log('goAbsPath: ' + path);
        window.location.href = path;
    }

    mod.goWindow = function (path) {
        var _u = this.getCurrentDomain() + path;
        console.log('goWindow: ' + _u);
        window.open(_u);
    }

    return mod;
})(window.ROUTER || {});