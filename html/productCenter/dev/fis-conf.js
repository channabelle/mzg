fis.match('::packager', {
    spriter: fis.plugin('csssprites')
});

fis.match('*', {
    useHash: false
});

fis.match('*.js', {
    optimizer: fis.plugin('uglify-js')
});

fis.match('*.css', {
    useSprite: true,
    optimizer: fis.plugin('clean-css')
});

fis.match('*.png', {
    optimizer: fis.plugin('png-compressor')
});

fis.match('*.{html,js,css,png,jpg,eot,svg,ttf,woff,woff2,otf,gif}', {
    release: 'productCenter/dist/$0'
});