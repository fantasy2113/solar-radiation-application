jQuery(document).ready(function () {
    $('#radi_button').css("background-color", "whitesmoke");

    var calculation_button = jQuery('#calculation_button');
    calculation_button.bind('click', function () {
        $('#jsGrid').empty();
        $.ajax({
            method: 'GET',
            url: getPath() + 'calculation',
            dataType: "json",
            contentType: "application/json",
            data: {
                year: $('input[id=year]').val(),
                lat: $('input[id=lat]').val(),
                lon: $('input[id=lon]').val(),
                as: $('input[id=alignment]').val(),
                ys: $('input[id=tilt]').val()
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