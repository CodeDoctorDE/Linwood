function toggleDrawer(drawer) {
  var x = document.getElementById(drawer);
  if (x.className === "topnav") {
    x.className += " responsive";
  } else {
    x.className = "topnav";
  }
}
$('#nav .item').click(function () {
  $('#nav .item.active').removeClass('active');
  $(this).addClass('active');
});
$('.menuitem').on('click', function (e) {
  e.preventDefault();
  //  $(document).off("scroll");
  var athis = this;
  var target = this.hash,
    menu = target;
  $target = $(target);
  $('html, body').stop().animate({
    'scrollTop': $target.offset().top + 2
  }, 800, 'swing', function () {
    window.location.hash = target;
    $('.menuitem').removeClass('active');
    $(athis).addClass('active');

  });
});

$(window).on("scroll", function (event) {
  var scrollPos = $(document).scrollTop();
  $('#nav .item').each(function () {
    var currLink = $("a", this);
    var refElement = $(currLink.attr("href"));
    if (refElement.length) {
      // refElement.position().top <= scrollPos && refElement.position().top + refElement.height() > scrollPos
      if (refElement.position().top - 500 <= scrollPos && refElement.position().top + refElement.height() > scrollPos) {
        $('#nav .item').removeClass("active");
        $(this).addClass("active");
      } else {
        $(this).removeClass("active");
      }
    }
  });
});