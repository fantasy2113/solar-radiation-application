jQuery(document).ready(function () {
    var login_button = jQuery('#login_button');
    login_button.bind('click', function () {
        document.cookie = 'token=';
        $.ajax({
            beforeSend: function (request) {
                request.setRequestHeader('login', $('input[id=username]').val());
                request.setRequestHeader('password', $('input[id=password]').val());
            },
            url: getPath() + 'token',
            method: 'GET',
            dataType: "text",
            success: function (token) {
                document.cookie = 'token=' + token + ';';
                $(location).attr('href', getPath() + 'app');
            }
        });
    });

    var save_button = jQuery('#save_button');
    save_button.bind('click', function () {
        document.cookie = 'token=';
        $.ajax({
            beforeSend: function (request) {
                request.setRequestHeader('login', $('input[id=username]').val());
                request.setRequestHeader('password', $('input[id=password]').val());
            },
            url: getPath() + 'save_user',
            method: 'GET',
            dataType: "text",
            success: function (token) {
                if (token.includes('Fehler: ')) {
                    alert(token);
                } else {
                    document.cookie = 'token=' + token + ';';
                    $(location).attr('href', getPath() + 'app');
                }

            }
        });
    })
});

