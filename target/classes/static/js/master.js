function getExportQuery() {
    return 'startDate=' + $('input[id=start_date]').val()
    + '&endDate=' + $('input[id=end_date]').val()
    + '&lat=' + $('input[id=lat]').val()
    + '&lon=' + $('input[id=lon]').val()
    + '&typ=' + $('input[id=typ]').val();
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function getPath() {
	var url = window.location.href;
	if (url.includes("localhost")) {
		return 'http://localhost:8080/';
	}
	return 'https://solar-radiation-application.herokuapp.com/';
}
