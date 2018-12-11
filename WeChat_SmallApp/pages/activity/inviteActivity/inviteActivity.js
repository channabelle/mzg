// pages/activity/inviteActivity/inviteActivity.js
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  },

  acceptInvite: function(res) {
    console.log('=== acceptInvite ===', res);

    var _pages = getCurrentPages();
    for (var m = 0; m < _pages.length; m++) {
      console.log('m: '+ m + ', route: ', _pages[m].route);

      if (0 <= _pages[m].route.indexOf('pages/my/my')) {
        wx.redirectTo({
          url: '/pages/activity/infoActivity/infoActivity',
        });
        return;
      }
    }

    wx.redirectTo({
      url: '/pages/my/my?toPage=infoActivity',
    });
  }
})