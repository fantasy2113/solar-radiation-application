jQuery(document).ready(function () {
    var logout_button = jQuery('#logout_button');
    logout_button.bind('click', function () {
        document.cookie = 'token=;';
        $(location).attr('href', getPath());
    });


    var calc_button = jQuery('#calc_button');
    calc_button.bind('click', function () {
        $(location).attr('href', getPath() + 'calc');
    });

    jQuery(document).ready(function () {
        var app_button = jQuery('#app_button');
        app_button.bind('click', function () {
            $(location).attr('href', getPath() + 'app');
        });
    });
});


function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

String.prototype.includes = function (str) {
    var returnValue = false;

    if (this.indexOf(str) !== -1) {
        returnValue = true;
    }

    return returnValue;
};

function getPath() {
    var url = window.location.href;
    if (url.includes("localhost")) {
        return 'http://localhost:8080/';
    }
    return 'https://sonneneinstrahlung.herokuapp.com/';
}
