var ERROR_MSG = 'Etwas ist schief gelaufen';

function getToken(path, username, password) {
    clear();
    $.ajax({
        beforeSend: function (request) {
            request.setRequestHeader('login', username);
            request.setRequestHeader('password', password);
        },
        url: getPath() + path,
        method: 'GET',
        dataType: "text",
        success: function (result) {
            if (path === 'create_user' && result === 'create_failed') {
                $("#alert").append('<b>' + ERROR_MSG + '</b>');
            } else {
                document.cookie = 'token=' + result + ';app=' + 'rad' + ';';
                $(location).attr('href', getPath());
            }
        }
    });
}

function clear() {
    $("#alert").empty();
    $("#alert").append('<b>&nbsp;</b>');
}

jQuery(document).ready(function () {
    var login_button = jQuery('#login_button');
    login_button.bind('click', function () {
        clear();
        var password = $('input[id=password]').val();
        var username = $('input[id=username]').val();
        if (username === '' || password === '') {
            $("#alert").append('<b>' + 'Bitte Passwort UND Benutzername eingeben' + '</b>');
        } else {
            $.ajax({
                beforeSend: function (request) {
                    request.setRequestHeader('login', $('input[id=username]').val());
                    request.setRequestHeader('password', $('input[id=password]').val());
                },
                url: getPath() + 'check',
                method: 'GET',
                dataType: "text",
                success: function (result) {
                    if (result === 'unauthorized') {
                        $("#alert").append('<b>' + 'Passwort oder Benutzername falsch' + '</b>');
                    } else if (result === 'authorized') {
                        getToken('token', username, password);
                    } else {
                        $("#alert").append('<b>' + ERROR_MSG + '</b>');
                    }
                }
            });
        }
    })
    ;

    var create_button = jQuery('#create_button');
    create_button.bind('click', function () {
        clear();
        var password = $('input[id=password]').val();
        var username = $('input[id=username]').val();
        getToken('create_user', username, password);
    });


    var username_input = jQuery('#username');
    username_input.bind('change', function () {
        clear();
    });

    var password_input = jQuery('#password');
    password_input.bind('change', function () {
        clear();
    });
})
;
