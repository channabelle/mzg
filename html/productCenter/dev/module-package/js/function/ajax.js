var AJAX = (function (mod) {
    mod.validForm = function (_form) {
        if (_form) {
            var _valid = $(_form).valid();
            console.log('_valid: ' + _valid);
            if (false == _valid) {
                // e.preventDefault();
                setTimeout(function () {
                    $('.alert').slideUp('normal', 'swing');
                }, 3500);
                return false;
            }
        }
        return true;
    };
    mod.blockUI = function (isBlockUI) {
        if (true == isBlockUI) {
            App.blockUI({
                message: '请稍后...',
                boxed: true
            });
        }
    };
    mod.animateSubmit = function (animateSubmit) {
        var l = null;
        if (animateSubmit) {
            l = Ladda.create(animateSubmit);
            l.start();
        }
        return l;
    };

    mod.get = function (_url, successCb, alwaysCb = null, formValid = null, isBlockUI = false, animateSubmit = false, _async = true) {
        var url = window.MY_CONFIG.SERVER_URL + _url;
        if (false == this.validForm(formValid)) {
            return false;
        }
        this.blockUI(isBlockUI);
        var l = this.animateSubmit(animateSubmit);

        console.log('[ajax] get ==> ' + url);
        var _ajax = $.ajax({
            url: url,
            type: "GET",
            datType: "JSON",
            contentType: "application/json",
            async: _async,
            success: function (response) {
                console.log('[ajax] success <==');
                // console.log(JSON.stringify(response));
                if (false == FILTER.filter(response)) {
                    return;
                }

                if (successCb) {
                    successCb(response);
                }
            },
            error: function (response, status) {
                console.log('[ajax] error <==');
                if ('undefined' !== typeof toastr) {
                    toastr.error(response.statusText, response.status);
                }
            }
        });
        _ajax.always(function () {
            console.log('[ajax] finish <==');
            if (true == isBlockUI) {
                App.unblockUI();
            }
            if (l) {
                l.stop();
            }
            if (alwaysCb) {
                alwaysCb();
            }
        });
    };

    mod.post = function (_url, data, successCb, alwaysCb = null, formValid = null, isBlockUI = false, animateSubmit = false, _async = true) {
        var url = window.MY_CONFIG.SERVER_URL + _url;
        if (false == this.validForm(formValid)) {
            return false;
        }
        this.blockUI(isBlockUI);
        var l = this.animateSubmit(animateSubmit);

        console.log('[ajax] post ==> ' + url);
        var _ajax = $.ajax({
            url: url,
            type: "POST",
            datType: "JSON",
            contentType: "application/json",
            data: JSON.stringify(data),
            async: _async,
            success: function (response) {
                console.log('[ajax] success <==');
                console.log(JSON.stringify(response));
                if (false == FILTER.filter(response)) {
                    return;
                }

                if (successCb) {
                    successCb(response);
                }
            },
            error: function (response, status) {
                console.log('[ajax] error <==');
                if ('undefined' !== typeof toastr) {
                    toastr.error(response.statusText, response.status);
                }
            }
        });
        _ajax.always(function () {
            console.log('[ajax] finish <==');
            if (true == isBlockUI) {
                App.unblockUI();
            }
            if (l) {
                l.stop();
            }
            if (alwaysCb) {
                alwaysCb();
            }
        });
    };
    return mod;
})(window.AJAX || {});