//app.js
const CONFIG = require('./utils/config');
const AJAX = require('./utils/ajax');

App({
  onLaunch: function () {
    // 登录
    AJAX.autoLogin();

    // 获取用户信息
    console.log('<=== wx.getSetting');
    wx.getSetting({
      success: res => {
        console.log('===> wx.getSetting', JSON.stringify(res));

        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          console.log('<=== wx.getUserInfo');
          wx.getUserInfo({
            success: res => {
              console.log('===> wx.getUserInfo', JSON.stringify(res));

              // 可以将 res 发送给后台解码出 unionId
              this.globalData.userInfo = res.userInfo

              // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
              // 所以此处加入 callback 以防止这种情况
              if (this.userInfoReadyCallback) {
                this.userInfoReadyCallback(res)
              }
            }
          })
        }
      }
    });

    wx.getSystemInfo({
      success: function (res) {
        CONFIG.DEVICE_RPX_RATE = 750 / res.windowWidth;
        console.log('DEVICE_RPX_RATE: ' + CONFIG.DEVICE_RPX_RATE);
      }
    });
  },
  globalData: {
    userInfo: null
  }
})