$(function() {
    console.log('ready');

    // 加载列表
    _refreshList();
    // 滚动监听
    _initScrollListener();



    console.log('end');
});




function _refreshList() {
    var itemModel = $('.row-item').clone().css('display', 'block');

    for (var m = 0; m < 20; m++) {
        var item = $(itemModel).clone();
        $('.list-item').append(item);
    }
}

function _initScrollListener() {
    $(window).scroll(function(event) {
        var _d = $(window).scrollTop();
        var _opacity = 1;
        if (_d < 500) {
            _opacity = _d / 500;
        }

        $('.opacity-mask').css('background-color', 'rgba(255, 255, 255, ' + _opacity + ')');
    });
}