jQuery(document).ready(function () {
    $('#radi_button').css("background-color", "whitesmoke");

    var search_button = jQuery('#search_button');
    search_button.bind('click', function () {
        $('#jsGrid').empty();
        $.ajax({
            method: 'GET',
            url: getPath() + 'extractor',
            dataType: "json",
            contentType: "application/json",
            data: {
                startDate: $('input[id=year]').val(),
                lat: $('input[id=lat]').val(),
                lon: $('input[id=lon]').val()
            },
            success: function (json) {
                $("#jsGrid").jsGrid({
                    width: "620",
                    height: "700",

                    sorting: true,

                    data: json,

                    fields: [
                        {name: "date", title: "Datum", type: "text"},
                        {name: "lat", title: "Lat", type: "number"},
                        {name: "lon", title: "Lon", type: "number"},
                        {name: "value", title: "Wert", type: "number"},
                        {name: "unit", title: "Einheit", type: "text"},
                        {name: "dim", title: "Dim", type: "text"}
                    ]
                });
            }
        });
    });

});