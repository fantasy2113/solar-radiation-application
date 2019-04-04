jQuery(document).ready(function () {
    var click_button = jQuery('#click_button')
    click_button.bind('click', function () {
        console.log(getCookie('key'));
    })

    var logout_button = jQuery('#logout_button')
    logout_button.bind('click', function () {
        document.cookie = 'key=';
        $(location).attr('href', getPath());
    })
    
    var export_button = jQuery('#export_button')
    export_button.bind('click', function () {
        console.log(getPath() + 'Report0.pdf');
        $.fileDownload(getPath() + 'Report0.pdf').done(function () { alert('File download a success!'); }).fail(function () { alert('File download failed!'); });
    })
});

