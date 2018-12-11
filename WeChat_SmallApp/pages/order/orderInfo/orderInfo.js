const AJAX = require('../../../utils/ajax');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    /*
    订单详情
    */
    userOrderUuid: null,
    userOrder: null,

    /*
    子订单明细
    */
    list: null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    console.log('=== onLoad ===', options);

    this.setData({
      userOrderUuid: options.userOrderUuid
    });
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
    console.log('=== onShow ===');

    var _self = this;
    AJAX.httpRequest(
      "/productCenterServer/c/order/info/" + this.data.userOrderUuid,
      "GET",
      null,
      function success(result) {
        if (result.data && 2000 == result.data.status) {
          if (result.data.data) {
            var _data = result.data.data;
            var _list = _data.userSubOrder;

            for (var m = 0; m < _list.length; m++) {
              _list[m].index_uuid = _list[m].p_uuid_user_order;

              _list[m].user_order_price = parseFloat(_list[m].user_order_price).toFixed(2);
              _list[m].user_order_price_with_discount = parseFloat(_list[m].user_order_price_with_discount).toFixed(2);
              _list[m].user_order_amount_price_with_discount = parseFloat(_list[m].user_order_price_with_discount * _list[m].user_order_num).toFixed(2);
            }
            _data.userOrder.order_amount = parseFloat(_data.userOrder.order_amount).toFixed(2);

            _self.setData({
              userOrder: _data.userOrder,
              list: _list
            });

          }
        }
      }
    );

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

  }
})