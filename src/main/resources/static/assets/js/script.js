(function ($){
    let $body;
    $(document).ready(function (){
        $body = $('body');
        $('#hf-menu-trigger').on('click', function(e){
            e.preventDefault();
            $body.toggleClass('hf-menu-active');
        });
        $('.hf-selectable-options li').on('click', function (e){
            e.preventDefault();
            $(this).closest('.hf-selectable-options').find('li').not(this).removeClass('hf-active');
            $(this).toggleClass('hf-active');
        });
        $('.hf-accordion-title').on('click', function (e){
            var $title = $(this);
            var $acc = $(this).closest('.hf-accordion');
            var $panel = $title.closest('li').find('.hf-accordion-panel');
            e.preventDefault();
            if(!$title.hasClass('hf-acc-active')) {
                $title.addClass('hf-acc-active');
                $panel[0].style.maxHeight = $panel[0].scrollHeight + "px";
            }else{
                $title.removeClass('hf-acc-active');
                $panel[0].style.maxHeight = null;
            }
        });
        $('.hf-graph-icon').on('click', function (e){
            e.preventDefault();
            $(this).toggleClass('hf-selected');
        });
    });
})(jQuery);