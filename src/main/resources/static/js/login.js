var ERROR_MSG_1 = 'Etwas ist schief gelaufen';
var ERROR_MSG_2 = 'Bitte Passwort UND Benutzername eingeben';
var ERROR_MSG_3 = 'Passwort oder Benutzername falsch';
var ERROR_MSG_4 = 'Benutzer existiert bereits oder beinhaltet ungültige Zeichen';
var CREATE_USER_PATH = 'create_user';

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
            if (path === CREATE_USER_PATH && result.error) {
                $("#alert").append(ERROR_MSG_1);
            } else if (path === CREATE_USER_PATH && result.userError) {
                $("#alert").append(ERROR_MSG_4);
            } else if (!result.authorized) {
                $("#alert").append(ERROR_MSG_3);
            } else if (result.authorized) {
                document.cookie = 'token=' + result.token + ';app=' + 'rad' + ';';
                $(location).attr('href', getPath());
            } else {
                $("#alert").append(ERROR_MSG_1);
            }
        },
        error: function (request, status, error) {
            showError('#alert', request, status, error);
        }
    });
}

function isLogin(path, username, password) {
    return username === '' || password === ''
        || username === undefined || password === undefined;
}

function login(path, username, password) {
    clear();
    if (isLogin(path, username, password)) {
        $("#alert").append(ERROR_MSG_2);
    } else {
        getToken(username, password, path);
    }
}

function clear() {
    $("#alert").empty();
    $("#alert").append('&nbsp;');
}

jQuery(document).ready(function () {
    var login_button = jQuery('#login_button');
    login_button.bind('click', function () {
        login('token', $('input[id=username]').val(), $('input[id=password]').val());
    });

    var create_button = jQuery('#create_button');
    create_button.bind('click', function () {
        login(CREATE_USER_PATH, $('input[id=username]').val(), $('input[id=password]').val());
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
