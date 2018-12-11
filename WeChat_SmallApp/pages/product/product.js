const AJAX = require('../../utils/ajax');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    shopCar_totalNum: 0,
    pro: null,
    pro_detail_imgs: null,
    shopCar_totalNum: 0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    console.log('=== onLoad ===', options);

    this.setData({
      pid: options.id
    });

    this._refreshProInfo();
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {
    console.log('=== onReady ===');


  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {
    console.log('=== onShow ===');


    this._refreshShoppingCar();
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {
    console.log('=== onHide ===');
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {
    console.log('=== onUnload ===');
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {
    console.log('=== onPullDownRefresh ===');
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {
    console.log('=== onReachBottom ===');
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {
    console.log('=== onShareAppMessage ===');
  },

  _refreshProInfo: function() {
    var _self = this;
    // 获取商品详情
    AJAX.httpRequest(
      "/productCenterServer/g/product/proInfo?pid=" + this.data.pid,
      "GET",
      null,
      function success(result) {
        if (result.data && 2000 == result.data.status) {
          if (result.data.data) {
            var _pro = result.data.data;
            var _pro_detail_imgs = [];

            if (_pro.pro_detail && _pro.pro_detail.pro_detail_img_url_list) {
              _pro_detail_imgs = _pro.pro_detail.pro_detail_img_url_list.split(',');
            }

            _self.setData({
              'pro': _pro,
              'pro_detail_imgs': _pro_detail_imgs
            });
          }
        }
      }
    );
  },

  _refreshShoppingCar: function() {
    var _self = this;
    // 更新购物车商品数量
    AJAX.httpRequest(
      "/productCenterServer/c/shoppingCar/count",
      "GET",
      null,
      function success(result) {
        if (result.data && 2000 == result.data.status) {
          if (result.data.data) {
            var _date = result.data.data;

            _self.setData({
              shopCar_totalNum: _date.totalNum
            });
          }
        }
      }
    );
  },

  putToShoppingCar: function(event) {
    console.log('putToShoppingCar uuid: ' + event.currentTarget.dataset.uuid);

    var _self = this;
    AJAX.httpRequest(
      "/productCenterServer/c/shoppingCar/addOrUpdate",
      "POST", {
        "uuid_pro_info": event.currentTarget.dataset.uuid,
        "checked": "checked",
        "amountChangeNum": 1
      },
      function success(result) {
        if (result.data && 2000 == result.data.status && result.data.data) {
          var _data = result.data.data.summary;

          _self.setData({
            shopCar_totalNum: _data.totalNum
          });

        }
      }
    );
  },
  // end putToShoppingCar

  tapToShoppingCar: function(event) {
    console.log('tapToShoppingCar event: ', event);

    wx.navigateTo({
      url: '/pages/shoppingCar/shoppingCar',
    });
  }
})