jQuery(document).ready(function () {
    var button = jQuery('#login_button')
    button.bind('click', function () {
        document.cookie = 'key=';
        $.ajax({
            beforeSend: function (request) {
                request.setRequestHeader('login', $('input[name=username]').val());
                request.setRequestHeader('password', $('input[name=password]').val());
            },
            url: getPath() + 'key',
            method: 'GET',
            success: function (data) {
                document.cookie = 'key=' + data + ';';
                $(location).attr('href', getPath() + 'app');
            }
        });
    })
});

