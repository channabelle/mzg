$(function () {
    var htmlFileName = REG_EXP.catchString(window.location.href, /\/([^\/]+\.html)/);
    console.log(htmlFileName);

    var currentMenu = $("a[href $= '" + htmlFileName + "']");
    if (1 == currentMenu.length) {
        var menuItem = null;

        menuItem = $(currentMenu).find('span:eq(1)');
        if (false == menuItem.hasClass('selected')) {
            menuItem.addClass('selected');
        }

        menuItem = $(currentMenu).parent('li');
        if (false == menuItem.hasClass('active')) {
            menuItem.addClass('active');
        }
        if (false == menuItem.hasClass('open')) {
            menuItem.addClass('open');
        }

        menuItem = $(currentMenu).parent('li').parents('li');
        if (false == menuItem.hasClass('active')) {
            menuItem.addClass('active');
        }
        if (false == menuItem.hasClass('open')) {
            menuItem.addClass('open');
        }


        menuItem = $(menuItem).find('a .arrow');
        if (false == menuItem.hasClass('open')) {
            menuItem.addClass('open');
        }
    }
});

