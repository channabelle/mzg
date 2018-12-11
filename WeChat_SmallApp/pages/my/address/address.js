const AJAX = require('../../../utils/ajax');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    /*
    是否是选择地址
    */
    choose: 0,
    list: null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log('=== onLoad ===', options);

    if (1 == options.choose) {
      wx.setNavigationBarTitle({
        title: '选择地址',
      });
    } else if (0 == options.choose) {
      wx.setNavigationBarTitle({
        title: '收货地址',
      });
    }

    this.setData({
      choose: options.choose
    });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

    var _self = this;
    AJAX.httpRequest(
      "/productCenterServer/c/user/address/list",
      "GET",
      null,
      function success(result) {

        if (result.data && 2000 == result.data.status) {
          if (result.data.data) {
            console.log(result.data.data);

            _self.setData({
              list: result.data.data
            });

          }
        }
      }
    );
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  tapAddAddress: function (event) {
    wx.navigateTo({
      url: '/pages/my/address/edit'
    })
  },

  chooseAddress: function (event) {
    console.log("chooseAddress event.detail.value: " + event.detail.value);

    var _pages = getCurrentPages();
    var _prePage = _pages[_pages.length - 2];
    _prePage.setData({
      userAddress: this.data.list[event.detail.value]
    });

    setTimeout(function() {
      wx.navigateBack({
        delta: 1
      });
    }, 150);
  }
})