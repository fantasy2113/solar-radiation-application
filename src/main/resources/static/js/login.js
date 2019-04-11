jQuery(document).ready(function () {
    var button = jQuery('#login_button');
    button.bind('click', function () {
        document.cookie = 'token=';
        $.ajax({
            beforeSend: function (request) {
                request.setRequestHeader('login', $('input[id=username]').val());
                request.setRequestHeader('password', $('input[id=password]').val());
            },
            url: getPath() + 'token',
            method: 'GET',
            dataType: "text",
            success: function (key) {
                document.cookie = 'token=' + key + ';';
                $(location).attr('href', getPath() + 'app');
            }
        });
    })
});

