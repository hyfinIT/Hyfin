var hyfin;
(function($) {
  "use strict";
  hyfin = (function() {
    return {
      init: function() {
        // Header
        this.header();

        // First section
        this.firstSection();

        // Second section
        this.secondSection();
        this.secondSectionTitle();

        // Third section
        this.thirdSection();
        this.thirdSectionTitle();

        // Fourth section
        this.fourthSectionTitle();

        // Fifth section
        this.fifthSection();
        this.fifthSectionTitle();

        // Sixth section
        this.sixthSectionTitle();

        // Contact section
        this.contactSectionTitle();

        // Utilities
        this.video();
      },

      // Header
      header: function() {
        $(window).scroll(function() {
          var scrollTop = $(window).scrollTop();
          if(scrollTop > 50) { 
            $(".js-header").addClass("active");
          } else {
            $(".js-header").removeClass("active");
          }
        });
        $(".js-go-to-section").click(function () {
          $(".js-go-to-section").removeClass("active");
          $('html, body').scrollTop($($(this).attr("data-section")).offset().top);
          $(this).addClass("active");
          $(".js-header").removeClass("active-mobile-menu");
          return false;
        });
        $(".js-open-mobile-menu").click(function () {
          $(".js-header").addClass("active-mobile-menu");
          return false;
        });
        $(".js-close-mobile-menu").click(function () {
          $(".js-header").removeClass("active-mobile-menu");
          return false;
        });
        var currentSection = 0;
        $(window).scroll(function() {
          $(".js-go-to-section").each(function () {
            var section = $(this).attr("data-section");
            var index = $(this).parent().index();
            if($(section).visible(true)) {
              currentSection = index;
            }
          });
          $(".js-go-to-section").removeClass("active");
          $(".js-go-to-section").parent().eq(currentSection).find(".js-go-to-section").addClass("active");          
        });
      },

      // First section
      firstSection: function() {
        $(".js-first-section-title").addClass("active");
        var data = $('.js-first-section-title').text();
        var arr = data.split('');
        var text = '';
        var className = 'light';
        $(arr).each(function(index) {
          if(arr[index] == '/') {
            text = text + '<br>';
            className = '';
          } else {
            if(arr[index] == ' ') {
              text = text + ' ';
            } else {
              text = text + '<span class="' + className + '"><i>' + arr[index] + '</i></span>'; 
            }
          }
        });
        $(".js-first-section-title").html(text);
        var i = 0;
        setInterval(function () {
          $(".js-first-section-title span").eq(i).addClass("active");
          i++;
        }, 75);
      },

      // Second section
      secondSection: function() {
        $(window).scroll(function() {
          var scrollTop = $(window).scrollTop();
          if(scrollTop > $(".js-second-section").offset().top) { 
            $(".js-second-section-content").addClass("active");
          } else {
            $(".js-second-section-content").removeClass("active");
          }
          if(scrollTop > $(".js-parallax-second-section").offset().top - $(window).outerHeight()) {
            var difference = scrollTop - $(".js-parallax-second-section").offset().top + $(window).outerHeight();
            if(difference/$(".js-parallax-second-section").outerHeight() < 1) {
              var transform = (difference/$(".js-parallax-second-section").outerHeight()) * ($(".js-second-section-item").length - 1) * 100;
            } else {
              var transform = ($(".js-second-section-item").length - 1) * 100;
            }
            $(".js-second-section-item").css("willChange", "transform");
            $(".js-second-section-item").css("transform", "translateX(-" + transform + "%)");
          } else {
            $(".js-second-section-item").css("transform", "none");
          }
          if(scrollTop > $(".js-parallax-second-section").offset().top + $(".js-parallax-second-section").outerHeight() - $(window).outerHeight()) { 
            $(".js-second-section-content").addClass("active2");
          } else {
            $(".js-second-section-content").removeClass("active2");
          }
        });
      },

      secondSectionTitle: function() {
        $(".js-second-section-item-title").each(function(eq) {
          $(".js-second-section-item-title").eq(eq).addClass("active");
          var data = $('.js-second-section-item-title').eq(eq).text();
          var arr = data.split('');
          var text = '';
          $(arr).each(function(index) {
            if(arr[index] == '/') {
              text = text + '<br>';
            } else {
              if(arr[index] == ' ') {
                text = text + ' ';
              } else {
                text = text + '<span><i>' + arr[index] + '</i></span>'; 
              }
            }
          });
          $(".js-second-section-item-title").eq(eq).html(text);
          $(window).scroll(function() {
            if($(".js-second-section-item-title").eq(eq).visible(true)) {
              var i = 0;
              setInterval(function () {
                $(".js-second-section-item-title").eq(eq).find("span").eq(i).addClass("active");
                i++;
              }, 75);
            }
          });
        })
      },

      // Third section
      thirdSection: function() {
        if($(window).outerWidth() > 991) {
          var images = document.querySelectorAll('.js-third-section-parallax-item');
          new simpleParallax(images, {
            overflow: true,
            scale: 1.5,
            orientation: "left"
          });
        }
      },

      thirdSectionTitle: function() {
        $(".js-third-section-left-title").each(function(eq) {
          $(".js-third-section-left-title").eq(eq).addClass("active");
          var data = $('.js-third-section-left-title').eq(eq).text();
          var arr = data.split('');
          var text = '';
          var className = 'light';
          $(arr).each(function(index) {
            if(arr[index] == '/') {
              text = text + '<br>';
              className = '';
            } else {
              if(arr[index] == ' ') {
                text = text + ' ';
              } else {
                text = text + '<span class="' + className + '"><i>' + arr[index] + '</i></span>'; 
              }
            }
          });
          $(".js-third-section-left-title").eq(eq).html(text);
          $(window).scroll(function() {
            if($(".js-third-section-left-title").eq(eq).visible(true)) {
              var i = 0;
              setInterval(function () {
                $(".js-third-section-left-title").eq(eq).find("span").eq(i).addClass("active");
                i++;
              }, 75);
            }
          });
        })
      },

      // Fourth section
      fourthSectionTitle: function() {
        $(".js-fourth-section-title").each(function(eq) {
          $(".js-fourth-section-title").eq(eq).addClass("active");
          var data = $('.js-fourth-section-title').eq(eq).text();
          var arr = data.split('');
          var text = '';
          $(arr).each(function(index) {
            if(arr[index] == '/') {
              text = text + '<br>';
            } else {
              if(arr[index] == ' ') {
                text = text + ' ';
              } else {
                text = text + '<span><i>' + arr[index] + '</i></span>'; 
              }
            }
          });
          $(".js-fourth-section-title").eq(eq).html(text);
          $(window).scroll(function() {
            if($(".js-fourth-section-title").eq(eq).visible(true)) {
              var i = 0;
              setInterval(function () {
                $(".js-fourth-section-title").eq(eq).find("span").eq(i).addClass("active");
                i++;
              }, 75);
            }
          });
        })
      },

      // Fifth section
      fifthSection: function() {
        $(window).scroll(function() {
          var scrollTop = $(window).scrollTop();
          if(scrollTop > $(".js-fifth-section").offset().top) { 
            $(".js-fifth-section-content").addClass("active");
          } else {
            $(".js-fifth-section-content").removeClass("active");
          }
          if(scrollTop > $(".js-parallax-fifth-section").offset().top - $(window).outerHeight()) {
            var difference = scrollTop - $(".js-parallax-fifth-section").offset().top + $(window).outerHeight();
            if(difference/$(".js-parallax-fifth-section").outerHeight() < 1) {
              var transform = (difference/$(".js-parallax-fifth-section").outerHeight()) * ($(".js-fifth-section-item").length) * 100;
            } else {
              var transform = ($(".js-fifth-section-item").length) * 100;
            }
            $(".js-fifth-section-item").css("willChange", "transform");
            $(".js-fifth-section-item").css("transform", "translateX(-" + transform + "%)");
          } else {
            $(".js-fifth-section-item").css("transform", "none");
          }
          if(scrollTop > $(".js-parallax-fifth-section").offset().top + $(".js-parallax-fifth-section").outerHeight() - $(window).outerHeight()) { 
            $(".js-fifth-section-content").addClass("active2");
          } else {
            $(".js-fifth-section-content").removeClass("active2");
          }
        });
      },

      fifthSectionTitle: function() {
        $(".js-fifth-section-title").each(function(eq) {
          $(".js-fifth-section-title").eq(eq).addClass("active");
          var data = $('.js-fifth-section-title').eq(eq).text();
          var arr = data.split('');
          var text = '';
          var className = 'light';
          $(arr).each(function(index) {
            if(arr[index] == '/') {
              text = text + '<br>';
              className = '';
            } else {
              if(arr[index] == ' ') {
                text = text + ' ';
              } else {
                text = text + '<span class="' + className + '"><i>' + arr[index] + '</i></span>'; 
              }
            }
          });
          $(".js-fifth-section-title").eq(eq).html(text);
          $(window).scroll(function() {
            if($(".js-fifth-section-title").eq(eq).visible(true)) {
              var i = 0;
              setInterval(function () {
                $(".js-fifth-section-title").eq(eq).find("span").eq(i).addClass("active");
                i++;
              }, 75);
            }
          });
        })
      },

      // Sixth section
      sixthSectionTitle: function() {
        $(".js-sixth-section-title").addClass("active");
        var data = $('.js-sixth-section-title').text();
        var arr = data.split('');
        var text = '';
        var className = 'light';
        $(arr).each(function(index) {
          if(arr[index] == '/') {
            text = text + '<br>';
            className = '';
          } else {
            if(arr[index] == ' ') {
              text = text + ' ';
            } else {
              text = text + '<span class="' + className + '"><i>' + arr[index] + '</i></span>'; 
            }
          }
        });
        $(".js-sixth-section-title").html(text);
        var i = 0;
        var eq = 0;
        $(window).scroll(function() {
          if($(".js-sixth-section-title").eq(eq).visible(true)) {
            var i = 0;
            setInterval(function () {
              $(".js-sixth-section-title").eq(eq).find("span").eq(i).addClass("active");
              i++;
            }, 75);
          }
        });
      },

      // Contact section
      contactSectionTitle: function() {
        $(".js-contact-title").addClass("active");
        var data = $('.js-contact-title').text();
        var arr = data.split('');
        var text = '';
        var className = 'light';
        $(arr).each(function(index) {
          if(arr[index] == '/') {
            text = text + '<br>';
            className = '';
          } else {
            if(arr[index] == ' ') {
              text = text + ' ';
            } else {
              text = text + '<span class="' + className + '"><i>' + arr[index] + '</i></span>'; 
            }
          }
        });
        $(".js-contact-title").html(text);
        var i = 0;
        var eq = 0;
        $(window).scroll(function() {
          if($(".js-contact-title").eq(eq).visible(true)) {
            var i = 0;
            setInterval(function () {
              $(".js-contact-title").eq(eq).find("span").eq(i).addClass("active");
              i++;
            }, 75);
          }
        });
      },

      // Utilities
      video: function() {
        $('.js-vimeo-video').vimeo_player();
      }
    }
  })();
})(jQuery);

$(document).ready(function() {
  hyfin.init();
  $("body").niceScroll({
    cursorcolor:    "#FF6900",
    cursorwidth:    "12px",
    cursorborder:     "0px solid #000",
    scrollspeed:    60,
    autohidemode:     true,
    background:     '#ddd',
    hidecursordelay:  400,
    cursorfixedheight:  false,
    cursorminheight:  20,
    enablekeyboard:   true,
    horizrailenabled:   true,
    bouncescroll:     false,
    smoothscroll:     true,
    iframeautoresize:   true,
    touchbehavior:    false,
    zindex: 999
  });
});