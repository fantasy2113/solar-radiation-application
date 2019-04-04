jQuery(document).ready(function () {

    var search_button = jQuery('#search_button')
        search_button.bind('click', function () {
            $.ajax({
            url: getPath() + 'find',
            contentType: "application/json",
              dataType: "json",

                method: 'GET',
                data: {
                        'startDate': $('input[id=start_date]').val(),
                        'endDate': $('input[id=end_date]').val(),
                        'lat': $('input[id=lat]').val(),
                        'lon': $('input[id=lon]').val(),
                        'typ': $('input[id=typ]').val()
                    },
                 success: function(data)
                    {
                        console.log(data)
                    }
            });
        })

    var logout_button = jQuery('#logout_button')
    logout_button.bind('click', function () {
        document.cookie = 'key=;';
        $(location).attr('href', getPath());
    })

    var export_button = jQuery('#export_button')
    export_button.bind('click', function () {

$(location).attr('href', getPath() + "export?" + getExportQuery());
    })
});

