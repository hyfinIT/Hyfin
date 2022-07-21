var custom;
(function($) {
  "use strict";
  custom = (function() {
    return {
      init: function() {
        this.radioInputs();
      },

      radioInputs: function() {
        $(".trade-currency__currency").click(function () {
          $(".trade-currency__currency").find("input").attr("checked", false)
          $(this).find("input").attr("checked", true)
        });
        $(".spot-trader__item").click(function () {
          $(".spot-trader__item").find("input").attr("checked", false)
          $(this).find("input").attr("checked", true)
        });
        $(".spot-trader__amount-item").click(function () {
          $(".spot-trader__amount-item").find("input").attr("checked", false)
          $(this).find("input").attr("checked", true)
        });
        $(".select-price__item").click(function () {
          $(".select-price__item").find("input").attr("checked", false)
          $(this).find("input").attr("checked", true)
        });
      }
    }
  })();
})(jQuery);

$(document).ready(function() {
  custom.init();
});
