var G_THEME = (function (mod) {
    var panel = $('.theme-panel');
    var sidebarOption = $('.sidebar-option', panel).val();
    var footerOption = $('.page-footer-option', panel).val();

    mod.setBoxedLayout = function () {
        $("body").addClass("page-boxed");

        // set header
        $('.page-header > .page-header-inner').addClass("container");
        var cont = $('body > .clearfix').after('<div class="container"></div>');

        // set content
        $('.page-container').appendTo('body > .container');

        // set footer
        if (footerOption === 'fixed') {
            $('.page-footer').html('<div class="container">' + $('.page-footer').html() + '</div>');
        } else {
            $('.page-footer').appendTo('body > .container');
        }
    };

    mod.setColorLayout = function (color_) {
        $('#style_color').attr("href", Layout.getLayoutCssPath() + 'themes/' + color_ + ".min.css");
    };

    mod.setSidebarMenuStyle = function (sidebarMenuOption) {
        if (sidebarMenuOption === 'hover') {
            if (sidebarOption == 'fixed') {
                $('.sidebar-menu-option', panel).val("accordion");
                alert("Hover Sidebar Menu is not compatible with Fixed Sidebar Mode. Select Default Sidebar Mode Instead.");
            } else {
                $(".page-sidebar-menu").addClass("page-sidebar-menu-hover-submenu");
            }
        } else {
            $(".page-sidebar-menu").removeClass("page-sidebar-menu-hover-submenu");
        }
    }

    return mod;
})(window.G_THEME || {});

// 默认样式
//G_THEME.setBoxedLayout();
G_THEME.setColorLayout('light2');
G_THEME.setSidebarMenuStyle('hover');