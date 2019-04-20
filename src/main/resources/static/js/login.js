function getToken(path) {
    $("#alert").empty();
    $("#alert").append('<b>&nbsp;</b>');
    $.ajax({
        beforeSend: function (request) {
            request.setRequestHeader('login', $('input[id=username]').val());
            request.setRequestHeader('password', $('input[id=password]').val());
        },
        url: getPath() + path,
        method: 'GET',
        dataType: "text",
        success: function (token) {
            if (token.includes('!')) {
                $("#alert").append('<b>' + token + '</b>');
            } else {
                document.cookie = 'token=' + token + ';app=' + 'radiapp' + ';';
                $(location).attr('href', getPath());
            }
        }
    });
}

jQuery(document).ready(function () {
    var login_button = jQuery('#login_button');
    login_button.bind('click', function () {
        getToken('token');
    });

    var save_button = jQuery('#save_button');
    save_button.bind('click', function () {
        getToken('saveuser');
    });
});
