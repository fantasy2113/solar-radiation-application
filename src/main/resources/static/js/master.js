
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

// https://stackoverflow.com/questions/32995666/spring-rest-and-jquery-ajax-file-download
// https://blog.exxeta.com/2016/08/03/simple-excel-export-spring-based-web-application/
