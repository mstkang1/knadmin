$(function() {
    // 관리자 사이트 메뉴
    $('.admin-menu li').on('click', function() {
        $(this).children('ul').is(':hidden')
        $(this).children('ul').slideDown(300, 'swing');
        $(this).siblings().children('ul').slideUp(300, 'swing')
    });
    $('.admin-menu li>ul>li').on('click', function() {
        $('.admin-menu li>ul>li').removeClass('on');
        $(this).addClass('on');
    });
});


function loadingOpen(modalname) {
    document.get
    $("." + modalname).fadeIn(300);
    $('body').css("pointer-events","none");
    $('.lodaing').addClass('on');
}

function lodaingClose(modalname) {
    document.get
    $("." + modalname).fadeOut(300);
    $('body').css("pointer-events","auto");
    $('.lodaing').removeClass('on');
}