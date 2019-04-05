jQuery(document).ready(function () {

    var search_button = jQuery('#search_button')
        search_button.bind('click', function () {
            $.ajax({
                method: 'GET',
                url: getPath() + 'find',
                dataType: "json",
                contentType: "application/json",
                data: {
                    startDate : $('input[id=start_date]').val(),
                    endDate : $('input[id=end_date]').val(),
                    lat : $('input[id=lat]').val(),
                    lon : $('input[id=lon]').val(),
                    typ : $('input[id=typ]').val()
                },
                success: function (data) {
                    console.log(data);
                    $("#records_table tr").remove();

                    $.each(data, function(i, item) {
                        $('<tr>').html(
                            '<td>' + data[i].date + '</td>'
                            + '<td>' + data[i].lat + '</td>'
                            + '<td>' + data[i].lon + '</td>'
                            + '<td>' + data[i].typ + '</td>'
                            + '<td>' + data[i].value + '</td>'
                            + '</tr>')
                            .appendTo('#records_table');
                    });

                },
                error: function(jqXhr, textStatus, errorMessage){
                    console.log(jqXhr + textStatus + errorMessage);
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
        $(location).attr('href', getPath() + 'export?' + getExportQuery());
    })
});

