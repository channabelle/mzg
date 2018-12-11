var _vue = null;
var _tree = null;

$(function () {
    console.log('ready');

    _vue = _fn_initVue();
    _tree = $("#tree_menu");

    _fn_refreshMenuTree(_tree);
    _fn_addMenuTreeEvent(_tree);

    $('.dd').nestable();
});

var _fn_initVue = function () {
    return new Vue({
        el: '.vueData',
        data: {
            menu: {},
            prolist: [],
            proUuidToChangeOrder: null,
        },
        methods: {
            goToProInfo: function (_uuid) {
                ROUTER.goWindow("/productManager/productManage-edit-proInfo.html?action=u&pid=" + _uuid);
            },
            changeMenuStatus: function (_uuid, _status) {
                _menu_statusModify(_uuid, _status, _tree);
            },
            changeProStatus: function (_uuid, _status) {
                _pro_statusModify(_uuid, _status);
            },
            changeProOrder: function (_uuid) {
                this.proUuidToChangeOrder = _uuid;

                $('.input_order').val(null);
            },
            changeProOrderConfirm: function (_uuid) {
                console.log('=== changeProOrderConfirm ===', _uuid);

                _pro_orderModify(_uuid, $('.input_order').val());
            }
        }
    });
};

var _printJSTreeEvent = function (event, nData) {
    console.log('event.type: ' + event.type);
    console.log('nData.parent: ' + nData.parent);
    console.log('nData.position: ' + nData.position);
    console.log('nData.node.id: ' + nData.node.id);
    console.log('nData.node.text: ' + nData.node.text);
};

var _pro_orderModify = function (_uuid, _order) {
    AJAX.get(
        url = "/b/product/proInfo/orderUpdate/" + _uuid + "/" + _order,
        successCb = function (result) {
            if (2000 == result.status && result.data) {
                var _data = result.data;

                // 重新加载选定目录的菜单页
                _fn_getMenuInfo(_vue, _vue.menu.p_uuid_pro_menu);
            } else {
                _tree.jstree().refresh();
                toastr.error(result.message.content, result.message.title);
            }
        },
        alwaysCb = function () {

        },
        formValid = null,
        isBlockUI = true,
        animateSubmit = false);
};

var _menu_statusModify = function (_uuid, _status, _tree) {
    AJAX.post(
        url = "/b/product/proMenu/patchUpdate",
        data = {
            "p_uuid_pro_menu": _uuid,
            "menu_status": _status,
            "order_number": -1
        },
        successCb = function (result) {
            if (2000 == result.status && result.data) {
                var _data = result.data;

                // 还需要更新图标
                _vue.menu.menu_status = _data.menu_status;
                // 重新加载当前页面
                ROUTER.replaceAbsPath('?refresh=1');
            } else {
                _tree.jstree().refresh();
                toastr.error(result.message.content, result.message.title);
            }
        },
        alwaysCb = function () {

        },
        formValid = null,
        isBlockUI = true,
        animateSubmit = false);
};

var _pro_statusModify = function (_uuid, _status) {
    AJAX.get(
        url = "/b/product/proInfo/statusUpdate/" + _uuid + "/" + _status,
        successCb = function (result) {
            if (2000 == result.status && result.data) {
                var _data = result.data;

                // 更新图标
                for (var m = 0; m < _vue.prolist.length; m++) {
                    if (_vue.prolist[m].p_uuid_pro_info == _data.p_uuid_pro_info) {
                        _vue.prolist[m].pro_status = _data.pro_status;
                        break;
                    }
                }
            } else {
                _tree.jstree().refresh();
                toastr.error(result.message.content, result.message.title);
            }
        },
        alwaysCb = function () {

        },
        formValid = null,
        isBlockUI = true,
        animateSubmit = false);
};

var _fn_refreshMenuTree = function (_tree) {
    _tree.jstree({
        "core": {
            "themes": {
                "responsive": false
            },
            // so that create works
            "check_callback": true,
            'data': function (obj, callback) {
                AJAX.get(
                    url = "/b/product/proMenu/tree",
                    successCb = function (data) {
                        var _array = data.data;
                        var _tree_array = [];
                        for (var k = 0; k < _array.length; k++) {
                            var _node = getTreeNode(_array[k]);

                            _tree_array.push(_node);
                        }
                        callback.call(this, _tree_array);
                    });
            }
        },
        "types": {
            "#": {
                "max_depth": 1
            },
            "default": {
                "icon": "fa fa-folder icon-state-warning icon-lg"
            },
            "file": {
                "icon": "fa fa-file icon-state-warning icon-lg"
            }
        },
        "state": {
            "key": "demo2"
        },
        "plugins": ["contextmenu", "dnd", "state", "types", "wholerow"]
    });
};

var _fn_addMenuTreeEvent = function (_tree) {
    console.log('=== _fn_addMenuTreeEvent ===');

    $('.menu_add').click(function () {
        _tree.jstree().create_node(null, {}, 'last', null, false);
    });

    _tree.on('activate_node.jstree', function (event, nData) {
        _printJSTreeEvent(event, nData);

        _fn_getMenuInfo(_vue, nData.node.id);
    });

    _tree.on('create_node.jstree', function (event, nData) {
        _printJSTreeEvent(event, nData);

        var _menu_father_uuid = null;
        if (nData.parent && nData.parent != '#') {
            _menu_father_uuid = nData.parent;
        }
        AJAX.post(
            url = "/b/product/proMenu/create",
            data = {
                "menu_father_uuid": _menu_father_uuid,
                "menu_name": nData.node.text
            },
            successCb = function (result) {
                if (2000 == result.status && result.data) {
                    var rData = result.data;
                    _tree.jstree().set_id(nData.node, rData.p_uuid_pro_menu);
                    _tree.jstree().set_icon(nData.node, "icon-check icon-state-success");
                    toastr.success(result.message.content, result.message.title);
                } else {
                    _tree.jstree().refresh();
                    toastr.error(result.message.content, result.message.title);
                }
            },
            alwaysCb = function () {

            },
            formValid = null,
            isBlockUI = true,
            animateSubmit = false);
    });

    _tree.on('rename_node.jstree', function (event, nData) {
        _printJSTreeEvent(event, nData);

        AJAX.post(
            url = "/b/product/proMenu/patchUpdate",
            data = {
                "p_uuid_pro_menu": nData.node.id,
                "menu_name": nData.node.text,
                "order_number": -1,
                "menu_status": -1
            },
            successCb = function (result) {
                if (2000 == result.status && result.data) {
                    var rData = result.data;
                    toastr.success(result.message.content, result.message.title);
                } else {
                    _tree.jstree().refresh();
                    toastr.error(result.message.content, result.message.title);
                }
            },
            alwaysCb = function () {

            },
            formValid = null,
            isBlockUI = true,
            animateSubmit = false);
    });
    _tree.on('delete_node.jstree', function (event, nData) {
        _printJSTreeEvent(event, nData);

        AJAX.post(
            url = "/b/product/proMenu/delete",
            data = {
                "p_uuid_pro_menu": nData.node.id
            },
            successCb = function (result) {
                if (2000 == result.status) {
                    var rData = result.data;
                    toastr.success(result.message.content, result.message.title);
                } else {
                    // 删除失败暂时不处理，刷新后重置
                    _tree.jstree().refresh();
                    toastr.error(result.message.content, result.message.title);
                }
            },
            alwaysCb = function () {

            },
            formValid = null,
            isBlockUI = true,
            animateSubmit = false);

    });
    _tree.on('move_node.jstree', function (event, nData) {
        _printJSTreeEvent(event, nData);

        var _menu_father_uuid = "";
        if (nData.parent && nData.parent != '#') {
            _menu_father_uuid = nData.parent;
        }

        AJAX.post(
            url = "/b/product/proMenu/patchUpdate",
            data = {
                "p_uuid_pro_menu": nData.node.id,
                "menu_father_uuid": _menu_father_uuid,
                "menu_status": -1
            },
            successCb = function (result) {
                if (2000 == result.status && result.data) {
                    var rData = result.data;
                    toastr.success(result.message.content, result.message.title);
                } else {
                    // 移动失败暂时不处理，刷新后重置
                    _tree.jstree().refresh();
                    toastr.error(result.message.content, result.message.title);
                }
            },
            alwaysCb = function () {

            },
            formValid = null,
            isBlockUI = true,
            animateSubmit = false);

        setTimeout(() => {
            _fn_updateMenuOrder();
        }, 500);

    });
};

var _fn_updateMenuOrder = function () {
    var _uuids = _tree.jstree().get_node(_tree).children;

    AJAX.post(
        url = "/b/product/proMenu/updateOrder",
        data = _uuids,
        successCb = function (result) {
            if (2000 == result.status && result.data) {
                var rData = result.data;

            } else {
                // 移动失败暂时不处理，刷新后重置
                _tree.jstree().refresh();
                toastr.error(result.message.content, result.message.title);
            }
        },
        alwaysCb = function () {

        },
        formValid = null,
        isBlockUI = true,
        animateSubmit = false);
};

var _fn_getMenuInfo = function (_v, _menu_Uuid) {
    AJAX.get(
        url = "/b/product/proMenu/info?menu_uuid=" + _menu_Uuid,
        successCb = function (res) {
            if (2000 == res.status && res.data) {
                var _d = res.data;

                _d.menu.cTime = new Date(_d.menu.cTime).toLocaleString('chinese', { hour12: false });
                _d.menu.uTime = new Date(_d.menu.uTime).toLocaleString('chinese', { hour12: false });
                _v.menu = _d.menu;


                _v.prolist = [];
                if (null != _d.proinfo_list) {
                    // CC:https://segmentfault.com/a/1190000007581722

                    _d.proinfo_list.forEach(element => {
                        element.cTime = new Date(element.cTime).toLocaleDateString();
                        element.uTime = new Date(element.uTime).toLocaleDateString();
                        _v.prolist.push(element);
                    });
                }

            }
        },
        alwaysCb = null,
        formValid = null,
        isBlockUI = true,
        animateSubmit = false,
        _async = false);
};

var getTreeNode = function (_json) {
    var _node = {
        'id': _json.p_uuid_pro_menu,
        'text': _json.menu_name,
        'icon': (1 == _json.menu_status) ? "icon-close icon-state-danger" : "icon-check icon-state-success",
        'state': {
            'opened': true
        },
        'children': getTreeNodeChildren(_json)
    }

    return _node;
};


var getTreeNodeChildren = function (_json) {
    var _children = [];
    if (_json && _json.children && _json.children.length > 0) {
        for (var k = 0; k < _json.children.length; k++) {
            var _j = _json.children[k];
            var _j_children = [];

            if (_j.children && _j.children.length > 0) {
                _j_children = getTreeNodeChildren(_j);
            }
            var _node = {
                'id': _j.p_uuid_pro_menu,
                'text': _j.menu_name,
                'icon': (1 == _j.menu_status) ? "icon-close icon-state-danger" : "icon-check icon-state-success",
                'state': {
                    'opened': true
                },
                'children': _j_children
            }
            _children.push(_node);
        }
    }
    return _children;
};