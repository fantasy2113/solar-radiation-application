jQuery(document).ready(function () {
    var button = jQuery('#login_button')
    button.bind('click', function () {
        $.ajax({
            beforeSend: function (request) {
                request.setRequestHeader('login', $('input[name=username]').val());
                request.setRequestHeader('password', $('input[name=password]').val());
            },
            url: M_originPath + 'key',
            method: 'GET',
            success: function (data) {
                document.cookie = 'key=' + data + ';';
                $(location).attr('href', getPath() + 'app');
            }
        });
    })
});

