$(function() {
    console.log('ready');

    var _action = REG_EXP.getURLParam('action');
    var _pid = REG_EXP.getURLParam('pid');

    // 控件初始化
    _fn_widgetListener();

    // 初始化页面样式（多种场景，公用一个页面）
    _fn_init(_action, _pid);

    // 刷新分类菜单数据
    _fn_refreshMenus();

    // 刷新封面图片素材
    _fn_refreshImgs();

    // 刷新商品信息数据
    var _pro_info = _fn_refreshProInfo(_action, _pid);

    // 数据交互按键监听
    _fn_buttonListener(_action, _pid, _pro_info);
});

// 上传
var _fn_save_info = function(e) {
    console.log('.save-info click _action: ' + e.data._action + ", _pid: " + e.data._pid);

    var _action = e.data._action;
    var _pid = e.data._pid;

    var _url = null;

    // 分类
    var _jMenus = $('.select-fl').select2('data');
    var _arrayMenu = [];
    for(var m = 0; m < _jMenus.length; m++) {
        var _menu = {
            'p_uuid_pro_menu': _jMenus[m].id
        }
        _arrayMenu.push(_menu);
    }
    // 封面
    var _jCover = $('.select-fm').select2('data');
    var _coverImg = _jCover[0].id;

    var _data = {
        'pro_status': $('.select-zt').selectpicker('val'),
        'pro_price': $('.input-jg').val(),
        'pro_total_quantity_unlimited': (true == $('.checkbox-cpzl-bx').is(':checked')) ? 1 : 0,
        'pro_total_quantity': $('.input-cpzl').val(),
        'pro_title_short': $('.input-cpmc-short').val(),
        'pro_menus': _arrayMenu,
        'pro_cover_img_url': _coverImg,
        'valid_sTime_unlimited': (true == $('.checkbox-yxq-start-bx').is(':checked')) ? 1 : 0,
        'valid_eTime_unlimited': (true == $('.checkbox-yxq-end-bx').is(':checked')) ? 1 : 0,
        'valid_sTime': $(".input-yxq-start").datepicker("getDate"),
        'valid_eTime': $(".input-yxq-end").datepicker("getDate")
    };

    if('u' == _action && _pid) {
        _url = "/b/product/proInfo/patchUpdate";
        _data.p_uuid_pro_info = _pid;
    } else if('c' == _action) {
        _url = "/b/product/proInfo/create";
        _data.uuid_shop_pro = $('.input-cpbh').val();
    }

    if(_url) {
        AJAX.post(
            url = _url,
            data = _data,
            successCb = function(response) {
                if(2000 == response.status && response.data) {
                    toastr.success(response.message.content, response.message.title);

                    // 如果是新增成功，跳转到查看页面
                    if('c' == _action) {
                        setTimeout(function() {
                            ROUTER.goAbsPath(ROUTER.getCurrentHostAndPath() + '?pid=' + response.data.p_uuid_pro_info + '&action=u');
                        }, 2000);
                    }
                } else if(response.message) {
                    toastr.error(response.message.content, response.message.title);
                }
            },
            alwaysCb = null,
            formValid = $(this).parents('form'),
            isBlockUI = true,
            animateSubmit = this);

        return true;
    }

    return false;
}

var _fn_save_detail = function(e) {
    console.log('.save-detail click _action: ' + e.data._action + ", _pid: " + e.data._pid);
    var _action = e.data._action;
    var _pid = e.data._pid;
    var _pro_info = e.data._pro_info;
    //var _markupStr = $('#summernote-prodetail').summernote('code');
    var _pro_detail_img_url_list = _fn_get_detail_img();
    var _data = {
        //"pro_detail_full_h5": _markupStr
        "pro_detail_img_url_list": _pro_detail_img_url_list
    }

    if(_pid) {
        _data.uuid_pro_info = _pid;
        if(_pro_info && _pro_info.pro_detail) {
            _data.p_uuid_pro_detail = _pro_info.pro_detail.p_uuid_pro_detail;
        }

        AJAX.post(
            url = "/b/product/proDetail/createOrUpdate",
            data = _data,
            successCb = function(response) {
                if(2000 == response.status && response.data) {
                    toastr.success(response.message.content, response.message.title);

                } else if(response.message) {
                    toastr.error(response.message.content, response.message.title);
                }
            },
            alwaysCb = null,
            formValid = $(this).parents('form'),
            isBlockUI = true,
            animateSubmit = this);

        return true;
    } else {
        toastr.info("请先保存基本信息", "提示");
    }
    return false;
}

var _fn_widgetListener = function() {
    console.log('_fn_widgetListener');

    // 只读与编辑
    $('.checkbox-editable').on('switchChange.bootstrapSwitch', function(event, state) {
        console.log('switchChange.bootstrapSwitch state: ' + state);

        if(false == state) {
            $('.form-actions-area').slideDown('normal', 'swing');
            $('input').removeAttr("disabled");
            $('select').removeAttr("disabled");
            $('button').removeAttr("disabled");
            $('.summernote-prodetail').summernote('enable');
        } else {
            $('.form-actions-area').slideUp('normal', 'swing');
            $('input').attr("disabled", "disabled");
            $('select').attr("disabled", "disabled");
            $('button').attr("disabled", "disabled");
            $('.summernote-prodetail').summernote('disable');
        }
    });

    // 产品总量不限
    $('.checkbox-cpzl-bx').on('ifChecked ifUnchecked', function(event) {
        console.log('.checkbox-cpzl-bx: ' + event.type);

        if('ifChecked' == event.type) {
            $('.input-cpzl').val("");
            $('.input-cpzl').attr("readonly", "readonly");
        } else {
            $('.input-cpzl').removeAttr("readonly");
        }
    });

    // 有效期开始
    $('.checkbox-yxq-start-bx').on('ifChecked ifUnchecked', function(event) {
        console.log('.checkbox-yxq-start-bx: ' + event.type);

        if('ifChecked' == event.type) {
            $('.input-yxq-start').val("");
            $('.input-yxq-start').attr("readonly", "readonly");
            $('.input-yxq-start').attr("disabled", "disabled");
        } else {
            $('.input-yxq-start').removeAttr("readonly");
            if(false == $('.checkbox-editable').bootstrapSwitch('state')) {
                $('.input-yxq-start').removeAttr("disabled");
            }
        }
    });

    // 有效期结束
    $('.checkbox-yxq-end-bx').on('ifChecked ifUnchecked', function(event) {
        console.log('.checkbox-yxq-end-bx: ' + event.type);

        if('ifChecked' == event.type) {
            $('.input-yxq-end').val("");
            $('.input-yxq-end').attr("readonly", "readonly");
            $('.input-yxq-end').attr("disabled", "disabled");
        } else {
            $('.input-yxq-end').removeAttr("readonly");
            if(false == $('.checkbox-editable').bootstrapSwitch('state')) {
                $('.input-yxq-end').removeAttr("disabled");
            }
        }
    });

    // 图文详情图片选择
    $('.select-twxq').on('select2:select', function(e) {
        // Do something
        console.log('select2:select');
        var _data = e.params.data;
        console.log(_data);

        _fn_add_detail_img(_data.id, _data.text);
    });
    $('.select-twxq').on('select2:unselect', function(e) {
        // Do something
        console.log('select2:unselect');
        var _data = e.params.data;
        console.log(_data);

        _fn_remove_detail_img(_data.id);
    });
}

var _fn_add_detail_img = function(_img_id, _img_url) {
    var _dom = '<li class="dd-item" img_id="' +
        _img_id + '"><div class="dd-handle"><img src="' +
        _img_url + '"/></div></li>';
    $('.pro_imgs_nestable_list ol').append(_dom);
}

var _fn_remove_detail_img = function(_img_id) {
    var _i = '[img_id="' + _img_id + '"]';
    var _dom = $('.pro_imgs_nestable_list ol').find(_i);
    $(_dom).remove();
}

var _fn_get_detail_img = function() {
    var _list = '';
    var _doms = $('.pro_imgs_nestable_list ol img');
    for(var m = 0; m < _doms.length; m++) {
        if(0 == m) {
            _list = $(_doms[m]).attr('src');
        } else {
            _list = _list + ',' + $(_doms[m]).attr('src');
        }
    }

    return _list;
}

var _fn_buttonListener = function(_action, _pid, _pro_info) {
    console.log('_fn_buttonListener');

    $('#save-info').click({
        '_action': _action,
        '_pid': _pid
    }, _fn_save_info);
    $('#save-detail').click({
        '_action': _action,
        '_pid': _pid,
        '_pro_info': _pro_info
    }, _fn_save_detail);

    $('.btn-add-fl').click(function() {
        console.log('.btn-add-fl click');
        ROUTER.goWindow('/productManager/productManage-proMenu.html');
    });
}

/*
 * 初始化数据
 */
var _fn_init = function(_action, _pid) {
    $('#summernote-prodetail').summernote({
        minHeight: 550
    });

    if('c' == _action && null == _pid) {
        $('.checkbox-editable').bootstrapSwitch('state', false);
        $('.checkbox-editable').trigger('switchChange.bootstrapSwitch', false);
    } else if('u' == _action && _pid) {
        $('.checkbox-editable').bootstrapSwitch('state', true);
        $('.checkbox-editable').trigger('switchChange.bootstrapSwitch', true);
    } else {
        toastr.error('页面参数错误', '');
    }

    $('.checkbox-cpzl-bx').trigger('ifChecked');
    $('.checkbox-yxq-start-bx').trigger('ifChecked');
    $('.checkbox-yxq-end-bx').trigger('ifChecked');

    // 图文详情排序
    $('.pro_imgs_nestable_list').nestable({
        'maxDepth': 1
    });
}

/*
 * 获取商铺下所有产品的分类
 */
var _fn_refreshMenus = function() {
    // 初始化
    AJAX.get(
        url = "/b/product/proMenu/list",
        successCb = function(res) {
            if(2000 == res.status && res.data) {
                var _d = res.data;

                for(var m = 0; m < _d.length; m++) {
                    var newOption = new Option(_d[m].value_path, _d[m].value.p_uuid_pro_menu, false, false);
                    $('.select-fl').append(newOption);
                }
            }
        },
        alwaysCb = null,
        formValid = null,
        isBlockUI = true,
        animateSubmit = false,
        _async = false);
    // 实时刷新
    $('.select-fl').select2({
        ajax: {
            url: window.MY_CONFIG.SERVER_URL + '/b/product/proMenu/list',
            processResults: function(res) {
                var _array = [];
                if(2000 == res.status && res.data) {
                    var _d = res.data;

                    for(var m = 0; m < _d.length; m++) {
                        _array.push({
                            'id': _d[m].value.p_uuid_pro_menu,
                            'text': _d[m].value_path
                        })
                    }
                }
                // Tranforms the top-level key of the response object from 'items' to 'results'
                return {
                    results: _array
                };
            }
        }
    });
}

/*
 * 获取商铺下所有图片，初始化封面和图文详情
 */
var _fn_refreshImgs = function() {
    AJAX.get(
        url = "/b/product/proImg/list",
        successCb = function(res) {
            if(2000 == res.status && res.data) {
                var _d = res.data;

                for(var m = 0; m < _d.length; m++) {
                    if(_d[m].fileSize > 0) {
                        $('.select-fm').append(new Option(_d[m].path, _d[m].path, false, false));
                        $('.select-twxq').append(new Option(_d[m].path, _d[m].path, false, false));
                    }
                }
            }
        },
        alwaysCb = null,
        formValid = null,
        isBlockUI = true,
        animateSubmit = false,
        _async = false);
    // 实时刷新
    var _select_config = {
        ajax: {
            url: window.MY_CONFIG.SERVER_URL + '/b/product/proImg/list',
            processResults: function(res) {
                var _array = [];
                if(2000 == res.status && res.data) {
                    var _d = res.data;

                    for(var m = 0; m < _d.length; m++) {
                        if(_d[m].fileSize > 0) {
                            _array.push({
                                'id': _d[m].path,
                                'text': _d[m].path
                            })
                        }
                    }
                }
                // Tranforms the top-level key of the response object from 'items' to 'results'
                return {
                    results: _array
                };
            }
        },
        placeholder: "请选择"
    }
    $('.select-fm').select2(_select_config);
    $('.select-twxq').select2(_select_config);
    $('.select-twxq + .select2').css('width', '100%');
}

/*
 * 获取商品基本信息
 */
var _fn_refreshProInfo = function(_action, _pid) {
    var _d = null;

    if('u' == _action && _pid) {
        // 产品编号只读
        $('.input-cpbh').attr("readonly", "readonly");

        AJAX.get(
            url = "/b/product/proInfo?pid=" + _pid,
            successCb = function(res) {
                if(2000 == res.status && res.data) {
                    _d = res.data;
                    $('.input-cpbh').val(_d.uuid_shop_pro);
                    $('.select-zt').selectpicker('val', _d.pro_status);

                    if(_d.pro_price > 0) {
                        $('.input-jg').val(_d.pro_price);
                    }
                    if(_d.pro_total_quantity > 0) {
                        $('.input-cpzl').val(_d.pro_total_quantity);
                    }
                    $('.input-cpmc-short').val(_d.pro_title_short);

                    // 分类
                    if(_d.pro_menus && 0 < _d.pro_menus.length) {
                        var _flArray = [];
                        for(var n = 0; n < _d.pro_menus.length; n++) {
                            var id = _d.pro_menus[n].p_uuid_pro_menu;
                            _flArray.push(id);
                        }
                        $('.select-fl').val(_flArray);
                        $('.select-fl').trigger('change');
                    }

                    // 封面
                    if(_d.pro_cover_img_url) {
                        $('.select-fm').val(_d.pro_cover_img_url);
                        $('.select-fm').trigger('change');
                    }

                    if(1 == _d.pro_total_quantity_unlimited) {
                        $('.checkbox-cpzl-bx').iCheck('check');
                    } else {
                        $('.input-cpzl').val(_d.pro_total_quantity);
                        $('.checkbox-cpzl-bx').iCheck('uncheck');
                    }

                    if(1 == _d.valid_sTime_unlimited) {
                        $('.checkbox-yxq-start-bx').iCheck('check');
                        $(".input-yxq-start").datepicker("setDate", "");
                    } else {
                        if(_d.valid_sTime) {
                            $(".input-yxq-start").datepicker("setDate", new Date(_d.valid_sTime).Format("yyyy/MM/dd"));
                        }
                        $('.checkbox-yxq-start-bx').iCheck('uncheck');
                    }

                    if(1 == _d.valid_eTime_unlimited) {
                        $('.checkbox-yxq-end-bx').iCheck('check');
                        $(".input-yxq-end").datepicker("setDate", "");
                    } else {
                        if(_d.valid_eTime) {
                            $(".input-yxq-end").datepicker("setDate", new Date(_d.valid_eTime).Format("yyyy/MM/dd"));
                            // 结束时间会把开始时间也一并改变，需要复位
                            if(1 == _d.valid_sTime_unlimited || null == _d.valid_sTime) {
                                $(".input-yxq-start").datepicker("setDate", "");
                            }
                        }
                        $('.checkbox-yxq-end-bx').iCheck('uncheck');
                    }

                    // 产品详情
                    if(_d.pro_detail && _d.pro_detail.pro_detail_full_h5) {
                        $('#summernote-prodetail').summernote('code', _d.pro_detail.pro_detail_full_h5);
                    }
                    // 产品详情图文资源
                    if(_d.pro_detail && _d.pro_detail.pro_detail_img_url_list && 0 < _d.pro_detail.pro_detail_img_url_list.length) {
                        var _twxqArray = _d.pro_detail.pro_detail_img_url_list.split(',');

                        $('.select-twxq').val(_twxqArray);
                        $('.select-twxq').trigger('change');

                        for(var m = 0; m < _twxqArray.length; m++) {
                            _fn_add_detail_img(_twxqArray[m], _twxqArray[m]);
                        }
                    }
                }
            },
            alwaysCb = null,
            formValid = null,
            isBlockUI = true,
            animateSubmit = false,
            _async = false);
    }

    return _d;
}