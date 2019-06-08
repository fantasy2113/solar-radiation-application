var ERROR_MSG_1 = 'Etwas ist schief gelaufen';
var ERROR_MSG_2 = 'Bitte Passwort UND Benutzername eingeben';
var ERROR_MSG_3 = 'Passwort oder Benutzername falsch';
var ERROR_MSG_4 = 'Benutzer existiert bereits';

function getToken(username, password, path) {
    $.ajax({
        beforeSend: function (request) {
            request.setRequestHeader('login', username);
            request.setRequestHeader('password', password);
        },
        url: getPath() + path,
        method: 'GET',
        dataType: "json",
        success: function (result) {
            if (path === 'create_user' && result.error) {
                $("#alert").append('<b>' + ERROR_MSG_1 + '</b>');
            } else if (path === 'create_user' && result.userError) {
                $("#alert").append('<b>' + ERROR_MSG_4 + '</b>');
            } else if (!result.authorized) {
                $("#alert").append('<b>' + ERROR_MSG_3 + '</b>');
            } else if (result.authorized) {
                document.cookie = 'token=' + result.token + ';app=' + 'rad' + ';';
                $(location).attr('href', getPath());
            } else {
                $("#alert").append('<b>' + ERROR_MSG_1 + '</b>');
            }
        }
    });
}

function login(path, username, password) {
    clear();
    if (username === '' || password === '' || username === undefined || password === undefined) {
        $("#alert").append('<b>' + ERROR_MSG_2 + '</b>');
    } else {
        getToken(username, password, path);
    }
}

function clear() {
    $("#alert").empty();
    $("#alert").append('<b>&nbsp;</b>');
}

jQuery(document).ready(function () {
    var login_button = jQuery('#login_button');
    login_button.bind('click', function () {
        login('token', $('input[id=username]').val(), $('input[id=password]').val());
    });

    var create_button = jQuery('#create_button');
    create_button.bind('click', function () {
        login('create_user', $('input[id=username]').val(), $('input[id=password]').val());
    });


    var username_input = jQuery('#username');
    username_input.bind('change', function () {
        clear();
    });

    var password_input = jQuery('#password');
    password_input.bind('change', function () {
        clear();
    });
});
