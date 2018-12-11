const AJAX = require('../../../utils/ajax');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    list: null,
    totalPage: 0,
    currentPage: 1,
    pagePerNumber: 20
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    console.log("=== onLoad ===");

    this._refreshData(false);
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {
    console.log("=== onReady ===");
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {
    console.log("=== onShow ===");

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {
    console.log("=== onHide ===");
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {
    console.log("=== onUnload ===");

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {
    console.log("=== onPullDownRefresh ===");


  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {
    console.log("=== onReachBottom ===");

    if (this.data.currentPage < this.data.totalPage) {
      this._refreshData(true);
    }

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  },

  _refreshData: function(_append = false) {
    if (true == _append) {
      this.data.currentPage++;
    }

    var _self = this;
    AJAX.httpRequest(
      "/productCenterServer/c/order/list?page=" + this.data.currentPage +
      "&pagePerNumber=" + this.data.pagePerNumber,
      "GET",
      null,
      function success(result) {
        if (result.data && 2000 == result.data.status) {
          if (result.data.data) {
            var _data = result.data.data;
            var _list = _data.list;

            if (true == _append) {
              _list = _self.data.list.concat(_list);
            }

            _self.setData({
              list: _list,
              currentPage: _data.current_page,
              totalPage: _data.total_page
            });
          }
        }
      }, null, true, '加载中'
    );
  }
})