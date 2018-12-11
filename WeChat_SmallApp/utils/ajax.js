const CONFIG = require('./config');

var ajax = {
  systemErrorText: '系统繁忙，请稍后...',
  _showLoading: false,
  session_token: '',
  httpRequest: function(_requestUrl, _method = "GET", _data = null, _success, _fail, _autoLogin = true, _loadingTitle = null) {
    var _trace_id = Math.random();
    var _url = CONFIG.HTTP_HEADER + _requestUrl;
    console.log("<=== [" + _trace_id + "] httpRequest _requestUrl: " + _url + ", _data: ", _data);

    //默认所有请求有loading
    if (null != _loadingTitle) {
      ajax._showLoading = true;
    } else {
      ajax._showLoading = false;
    }
    
    if (true == ajax._showLoading) {
      wx.showLoading({
        title: _loadingTitle,
        mask: true
      });
    }


    var _self = this;
    wx.request({
      url: _url,
      data: _data,
      header: {
        'Content-Type': 'application/json',
        'token': this.session_token
      },
      method: _method,
      success: function(result) {
        console.log('===> [' + _trace_id + '] success');

        if (result.data && result.data.message && 2103 == result.data.message.code && true == _autoLogin) {
          console.log('登录过期或未登录，开始自动登录...');
          _self.autoLogin(function() {
            _self.httpRequest(_requestUrl, _method, _data, _success, _fail, false);
          });
          return;
        }

        if (_success) {
          _success(result);
        }
      },

      fail: function(errMsg) {
        console.log('===> [' + _trace_id + '] fail', errMsg);

        wx.showToast({
          'title': _self.systemErrorText,
          'icon': 'none',
          'duration': 2000
        });

        if (_fail) {
          _fail(errMsg);
        }
      },

      complete: function(result) {
        console.log('===> [' + _trace_id + '] complete');
        if (ajax._showLoading) {
          wx.hideLoading();
        }
      }
    })
  },
  autoLogin: function(_successCallback) {
    console.log('<=== wx.login');
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        console.log('===> wx.login', JSON.stringify(res));

        var _self = this;
        _self.httpRequest(
          "/productCenterServer/wechat/miniProgram/login?appid=" + CONFIG.MINI_PROGRAM_APPID + "&code=" + res.code,
          "GET",
          null,
          function success(result) {
            if (result.data && 2000 == result.data.status) {
              if (result.data.data) {
                var _data = result.data.data;
                if (_data.uuid_user && _data.token) {
                  console.log('登录成功 _data: ' + JSON.stringify(_data));
                  _self.session_token = _data.token;
                }


                if (_successCallback) {
                  _successCallback();
                }
              }
            }
          }
        );
      }
    })
  }
}

module.exports = ajax;