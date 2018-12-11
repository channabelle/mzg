const AJAX = require('../../../utils/ajax');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    address_uuid: null,
    address_type: ['住宅', '公司', '学校', '其他'],
    index: 0,
    address: null
  },

  bindPickerChange: function(e) {
    this.setData({
      index: e.detail.value
    });
  },

  tapDelete: function(e) {
    var _self = this;
    wx.showModal({
      title: '提示',
      content: '确认删除此收货地址么？',
      success: function(res) {
        if (res.confirm) {
          // 删除地址
          AJAX.httpRequest(
            "/productCenterServer/c/user/address/delete",
            "POST", {
              'p_uuid_user_address': _self.data.address_uuid
            },
            function success(result) {
              if (result.data && 2000 == result.data.status) {
                _self._showActionResultAndClosePage('删除成功');
              }
            }
          );
        } 
      }
    })
  },

  formSubmit: function(e) {
    console.log('formSubmit: ', e.detail.value);
    var v = e.detail.value;
    var _action = "";

    if ("" == v.contact_name) {
      this._showToast('请输入收货人姓名');
    } else if ("" == v.contact_phone) {
      this._showToast('请输入联系电话');
    } else if ("" == v.address) {
      this._showToast('请输入收货地址');
    } else {
      if (this.data.address_uuid) {
        console.log('update address');
        _action = 'update';
      } else {
        console.log('create address');
        _action = 'create';
      }

      // 获取创建/更新地址
      var _self = this;
      AJAX.httpRequest(
        "/productCenterServer/c/user/address/" + _action,
        "POST", {
          'p_uuid_user_address': this.data.address_uuid,
          'address_type': v.address_type,
          'address_full': v.address,
          'contact_name': v.contact_name,
          'contact_phone': v.contact_phone
        },
        function success(result) {
          if (result.data && 2000 == result.data.status) {

            _self._showActionResultAndClosePage('保存成功');
          }
        }
      );

    }
  },

  _showActionResultAndClosePage(text) {
    wx.showToast({
      'title': text,
      'icon': 'none',
      'duration': 1500
    });
    setTimeout(function () {
      wx.navigateBack({
        delta: 1
      });
    }, 1500);
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    console.log('=== onLoad ===', options);

    if (options.id) {
      this.setData({
        address_uuid: options.id
      });
    }
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

    var _self = this;
    if (this.data.address_uuid) {
      AJAX.httpRequest(
        "/productCenterServer/c/user/address/info/" + this.data.address_uuid,
        "GET",
        null,
        function success(result) {
          if (result.data && 2000 == result.data.status) {
            if (result.data.data) {
              var _address = result.data.data;
              var _index = 0;

              for (var _i in _self.data.address_type) {
                if (_address.address_type == _self.data.address_type[_i]) {
                  _index = _i;
                  break;
                }
              }

              _self.setData({
                address: _address,
                index: _index
              });

            }
          }
        }
      );
    }
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