jQuery(document).ready(function () {
    var button = jQuery('#back_button')
    button.bind('click', function () {
        $(location).attr('href', M_originPath);
    })
});

