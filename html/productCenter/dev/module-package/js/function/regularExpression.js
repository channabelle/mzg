var REG_EXP = (function(mod) {
    mod.catchString = function(str, reg) {
        var group = str.match(reg);

        if(null != group && 2 <= group.length) {
            return group[1];
        } else {
            return null;
        }
    };

    mod.getURLParam = function(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var _paramStr = decodeURI(window.location.search.substr(1));
        var r = _paramStr.match(reg); //匹配目标参数
        if(r != null) {
            return unescape(r[2])
        };
        return null; //返回参数值
    }

    return mod;
})(window.REG_EXP || {});

Date.prototype.Format = function(fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if(/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}