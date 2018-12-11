const CONFIG = require('../../utils/config');
const AJAX = require('../../utils/ajax');


Page({

  /**
   * 页面的初始数据
   */
  data: {
    list: null,
    summary: null,
    allChecked: null
  },


  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    console.log('=== onLoad ===');

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

    var _self = this;
    AJAX.httpRequest(
      "/productCenterServer/c/shoppingCar/list",
      "GET",
      null,
      function success(result) {

        if (result.data && 2000 == result.data.status) {
          if (result.data.data) {
            var _check = _self.isAllChecked(result.data.data.list);
            _self.setData({
              allChecked: _check,
              list: result.data.data.list,
              summary: result.data.data.summary
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
    console.log('onShareAppMessage');
  },

  tapItem: function(event) {
    console.log('tapItem id: ' + event.currentTarget.dataset.id);

    if (true == this.handleItemMoveBack()) {
      return;
    }

    wx.navigateTo({
      url: '/pages/product/product?id=' + event.currentTarget.dataset.id,
    })
  },

  tapAdd: function(event) {
    console.log('tapAdd id: ' + event.currentTarget.dataset.id + ', num: ' + event.currentTarget.dataset.num);

    if (true == this.handleItemMoveBack()) {
      return;
    }

    this._modifyProductNum(event.currentTarget.dataset.id, 1);
  },
  tapDelete: function(event) {
    console.log('tapDelete id: ' + event.currentTarget.dataset.id + ', num: ' + event.currentTarget.dataset.num);

    if (true == this.handleItemMoveBack()) {
      return;
    }

    if (2 <= event.currentTarget.dataset.num) {
      this._modifyProductNum(event.currentTarget.dataset.id, -1);
    }
  },
  tapDeleteAll: function(event) {
    console.log('tapDeleteAll id: ' + event.currentTarget.dataset.id);

    this._modifyProductNum(event.currentTarget.dataset.id, 0, 0);
  },

  itemCheckboxChange: function(event) {
    console.log('itemCheckboxChange id：' + event.currentTarget.dataset.id + ', num: ' + event.currentTarget.dataset.num);
    var _check = (0 == event.detail.value.length) ? "" : "checked";
    this._modifyProductNum(event.currentTarget.dataset.id, 0, event.currentTarget.dataset.num, _check);
  },

  itemCheckboxAllChange: function(event) {
    var _action = (0 == event.detail.value.length) ? "uncheck" : "check";
    console.log('itemCheckboxAllChange _action: ' + _action);

    var _self = this;
    AJAX.httpRequest(
      "/productCenterServer/c/shoppingCar/checkOrUncheck/" + _action,
      "POST",
      null,
      function success(result) {
        if (result.data && 2000 == result.data.status) {
          if (result.data.data) {
            var _check = _self.isAllChecked(result.data.data.list);
            _self.setData({
              allChecked: _check,
              list: result.data.data.list,
              summary: result.data.data.summary
            });
          }
        }
      }
    );
  },

  _modifyProductNum: function(uuid_pro_info, amountChangeNum = 0, amount = 0, checked = "nochange") {
    var _self = this;
    var _postData = {
      "uuid_pro_info": uuid_pro_info,
      "amountChangeNum": amountChangeNum,
      "amount": amount
    };
    if ("nochange" != checked) {
      _postData.checked = checked;
    }

    AJAX.httpRequest(
      "/productCenterServer/c/shoppingCar/addOrUpdate",
      "POST",
      _postData,
      function success(result) {
        if (result.data && 2000 == result.data.status) {
          console.log(result.data.data);

          if (result.data.data) {
            var _check = _self.isAllChecked(result.data.data.list);
            _self.setData({
              allChecked: _check,
              list: result.data.data.list,
              summary: result.data.data.summary
            });
          }
        }
      }
    );
  },

  isAllChecked: function(_list) {
    var _checked = "checked";
    for (var m = 0; m < _list.length; m++) {
      if ("checked" != _list[m].checked) {
        _checked = "";
        break;
      }
    }
    return _checked;
  },

  itemMoveChange: function(event) {
    var _moveX = event.detail.x * CONFIG.DEVICE_RPX_RATE;
    var _moveIndex = event.currentTarget.dataset.index;

    this.data.list[_moveIndex].moveX = _moveX;
  },

  itemTouchStart: function(event) {
    // console.log("=== itemTouchStart ===");

  },

  handleItemMoveBack: function(exceptIndex = -1) {
    var _needMove = false;

    var _list = this.data.list;
    for (var m = 0; m < _list.length; m++) {
      if (_list[m].moveX < 0 && m != exceptIndex) {
        _list[m].moveX = 0;
        _needMove = true;
      }
    }
    if (true == _needMove) {
      this.setData({
        list: _list
      });
    }
    return _needMove;
  },
  toOrder: function(event) {
    console.log("=== toOrder ===", event);

    var _list = this.data.list;
    var _checkedNum = 0;
    var _checkedList = "";
    var _uuid_shop = null;

    for (var m = 0; m < _list.length; m++) {
      console.log("p_uuid_shopping_car: " + _list[m].p_uuid_shopping_car + ", checked: " + _list[m].checked);

      if ('checked' == _list[m].checked) {
        if (0 == _checkedNum) {
          _checkedList = _list[m].p_uuid_shopping_car;
          _uuid_shop = _list[m].proInfo.uuid_shop;
        } else {
          _checkedList += ("," + _list[m].p_uuid_shopping_car);

          if (_uuid_shop != _list[m].proInfo.uuid_shop) {
            wx.showToast({
              'title': '商品属于不同商铺，请分别创建订单',
              'icon': 'none',
              'duration': 2000
            });
            return;
          }
        }

        _checkedNum++;
      }
    }

    if (0 < _checkedNum) {
      console.log("_checkedNum: " + _checkedNum + ", _checkedList: " + _checkedList);

      wx.navigateTo({
        url: '/pages/order/orderEdit/orderEdit?shppingCarUuids=' + _checkedList + '&uuid_shop=' + _uuid_shop + '&edit=1'
      });
    }
  }
});