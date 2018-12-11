const CONFIG = require('../../utils/config');
const AJAX = require('../../utils/ajax');


Page({

  /**
   * 页面的初始数据
   */
  data: {
    //所有目录
    menus: null,


    //选定的目录及目录下的商品
    menuUuid: null,
    list: null,
    currentPage: 1,
    totalPage: 0,


    pagePerNumber: 20
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    console.log('=== onLoad ===');

    this._refreshPro(null, false);
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

    if (this.data.currentPage < this.data.totalPage) {
      this._refreshPro(this.data.menuUuid, true);
    }

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function(res) {
    console.log('onShareAppMessage', res);


    return {
      title: '自定义转发标题',
      path: 'pages/my/my?toPage=infoActivity'
    }
  },

  /*
  刷新商品列表
  */
  _refreshPro: function(_menuUuid, _append) {
    console.log('_refreshPro _menuUuid: ' + _menuUuid + ", _append: " + _append);

    if (true == _append) {
      this.data.currentPage++;
    } else {
      this.data.currentPage = 1;
    }

    var _self = this;
    AJAX.httpRequest(
      "/productCenterServer/g/product/proInfo/list?page=" + this.data.currentPage +
      "&pagePerNumber=" + this.data.pagePerNumber +
      "&miniProgram_appid=" + CONFIG.MINI_PROGRAM_APPID +
      ((null != _menuUuid) ? "&menuId=" + _menuUuid : ""),
      "GET",
      null,
      function success(result) {

        if (result.data && 2000 == result.data.status) {
          if (result.data.data && result.data.data) {
            var _data = result.data.data;
            var _proInfo = _data.proInfos;

            if (_proInfo) {
              var _list = _proInfo.list;
              if (true == _append) {
                _list = _self.data.list.concat(_list);
              }

              _self.setData({
                menus: _data.menus,
                menuUuid: _data.menuUuid,
                list: _list,

                currentPage: _proInfo.current_page,
                totalPage: _proInfo.total_page
              });
            } else {
              _self.setData({
                menus: _data.menus,
                menuUuid: _data.menuUuid,
                list: null,

                currentPage: 0,
                totalPage: 0
              });
            }
          }
        }
      }
    );
  },

  tapItem: function(event) {
    console.log('tapItem id: ' + event.currentTarget.dataset.id);
    wx.navigateTo({
      url: '/pages/product/product?id=' + event.currentTarget.dataset.id
    });
  },

  tapMenu: function(event) {
    var _menuUuid = event.currentTarget.dataset.id;
    console.log('tapMenu id: ' + _menuUuid);

    if (this.data.menuUuid != _menuUuid) {
      this._refreshPro(event.currentTarget.dataset.id, false);
    }
  }
})