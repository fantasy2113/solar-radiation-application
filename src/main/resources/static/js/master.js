
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
function download(id) {
    var id = $('#file').attr('id')
    var xhr = new XMLHttpRequest();
    xhr.open('GET', 'url here' + id, true);
    xhr.responseType = 'arraybuffer';
    xhr.onload = function() {
        if(this.status == '200') {
           var filename = '';
           //get the filename from the header.
           var disposition = xhr.getResposeHeader('Content-Disposition');
           if (disposition && disposition.indexOf('attachment') !== -1) {
               var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
               var matches = filenameRegex.exec(disposition);
               if (matches !== null && matches[1])
                   filename = matches[1].replace(/['"]/g, '');
           }
           var type = xhr.getResponseHeader('Content-Type');
           var blob = new Blob([this.response],  {type: type});
           //workaround for IE
           if(typeof window.navigator.msSaveBlob != 'undefined') {
               window.navigator.msSaveBlob(blob, filename);
           }
           else {
               var URL = window.URL || window.webkitURL;
               var download_URL = URL.createObjectURL(blob);
               if(filename) {
                   var a_link = document.createElement('a');
                   if(typeof a.download == 'undefined') {
                       window.location = download_URL;
                   }else {
                       a_link.href = download_URL;
                       a_link.download = filename;
                       document.body.appendChild(a_link);
                       a_link.click();
                   }
               }else {
                   window.location = download_URL;
               }
               setTimeout(function() {
                   URL.revokeObjectURL(download_URL);
               }, 10000);
           }
        }else {
            alert('error')';//do something...
        }
    }; 
    xhr.setRequestHeader('Content-type', 'application/*');
    xhr.send();
}
