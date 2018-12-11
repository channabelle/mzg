const AJAX = require('../../../utils/ajax');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    /*
    填写订单
    */
    shppingCarUuids: null,
    uuid_shop: null,
    userAddress: null,

    /*
    子订单明细
    */
    summary: null,
    list: null,

    remark: null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    console.log('=== onLoad ===', options);

    this.setData({
      shppingCarUuids: options.shppingCarUuids,
      uuid_shop: options.uuid_shop
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
    var _listArray = this.data.shppingCarUuids.split(',');
    AJAX.httpRequest(
      "/productCenterServer/c/shoppingCar/list",
      "POST",
      _listArray,
      function success(result) {
        if (result.data && 2000 == result.data.status) {
          if (result.data.data) {
            var _summary = result.data.data.summary;
            var _list = result.data.data.list;

            _summary.totalMoney_withDiscount = parseFloat(_summary.totalMoney_withDiscount).toFixed(2);
            _summary.totalMoney = parseFloat(_summary.totalMoney).toFixed(2);

            for (var m = 0; m < _list.length; m++) {
              _list[m].proInfo.pro_price = parseFloat(_list[m].proInfo.pro_price).toFixed(2);
              _list[m].proInfo.pro_price_with_discount = parseFloat(_list[m].proInfo.pro_price_with_discount).toFixed(2);
              _list[m].amount_pro_price_with_discount = parseFloat(_list[m].amount_pro_price_with_discount).toFixed(2);
            }

            _self.setData({
              summary: _summary,
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

  },

  tapChooseAddress: function(event) {
    console.log("tapChooseAddress: ", event);

    wx.navigateTo({
      url: '/pages/my/address/address?id=' + event.currentTarget.dataset.id + '&choose=1'
    });
  },

  remarkInput: function(event) {
    this.setData({
      remark: event.detail.value
    });
  },

  submitOrder: function(event) {
    console.log("submitOrder: ", event);

    if (false == this._checkOrder()) {
      return;
    }

    var _self = this;
    var _shoppingCarUuids = this.data.shppingCarUuids.split(',');

    AJAX.httpRequest(
      "/productCenterServer/c/order/create",
      "POST", {
        'userAddress': this.data.userAddress,
        'shoppingCarUuids': _shoppingCarUuids,
        'uuid_shop': this.data.uuid_shop,
        'remark': this.data.remark
      },
      function success(result) {
        if (result.data && 2000 == result.data.status) {
          if (result.data.data) {
            var _orderUuid = result.data.data;

            console.log('submitOrder success, _orderUuid: ' + _orderUuid);

            //关闭当前页面，再跳至订单详情页面
            wx.redirectTo({
              url: '/pages/order/orderInfo/orderInfo?userOrderUuid=' + _orderUuid,
            });
          }
        } else if (result.data && 2001 == result.data.status) {
          wx.showToast({
            'title': result.data.message.content,
            'icon': 'none',
            'duration': 2000
          });
        }
      },
      null, true, '提交中，请稍后...'
    );
  },

  _checkOrder: function() {
    if (null == this.data.userAddress) {
      wx.showToast({
        'title': '请选择收货地址',
        'icon': 'none',
        'duration': 2000
      });
      return false;
    }

    return true;
  },

})