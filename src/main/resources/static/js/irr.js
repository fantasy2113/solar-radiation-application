jQuery(document).ready(function () {
    autocomplete(document.getElementById("year"), getYears());

    $('#rad_button').css("background-color", "whitesmoke");

    var alignment = jQuery('#alignment');
    alignment.bind('change', function () {
        if ($('input[id=alignment]').val() < 0) {
            $('#alignment').val(0);
        }
        if ($('input[id=alignment]').val() > 360) {
            $('#alignment').val(360);
        }
    });

    var tilt = jQuery('#tilt');
    tilt.bind('change', function () {
        if ($('input[id=tilt]').val() < 0) {
            $('#tilt').val(0);
        }
        if ($('input[id=tilt]').val() > 90) {
            $('#tilt').val(90);
        }
    });

    var calc_irr_button = jQuery('#calc_irr_button');
    calc_irr_button.bind('click', function () {
        $('html,body').css('cursor', 'wait');
        $('#jsGrid').empty();
        $('#jsGrid').append('<b>Berechnung läuft ...</b>');
        $.ajax({
            method: 'GET',
            url: getPath() + 'irr',
            dataType: "json",
            contentType: "application/json",
            data: {
                year: $('input[id=year]').val(),
                lat: $('input[id=lat]').val(),
                lon: $('input[id=lon]').val(),
                ae: $('input[id=alignment]').val(),
                ye: $('input[id=tilt]').val()
            },
            success: function (json) {
                $('#jsGrid').empty();
                $("#jsGrid").jsGrid({
                    width: "620",
                    height: "700",
                    //sorting: true,
                    data: json,
                    fields: [
                        {name: "date", title: "Datum", type: "text"},
                        {name: "eGlobHor", title: "EGlobHor"},
                        {name: "eGlobGen", title: "EGlobGen"},
                        {name: "unit", title: "Einh.", type: "text"},
                        {name: "dim", title: "Dim.", type: "text"}
                    ]
                });
                $('html,body').css('cursor', 'default');
            },
            error: function (request, status, error) {
                $('#jsGrid').empty();
                $('html,body').css('cursor', 'default');
            }
        });
    });

    var calc_irr_export_button = jQuery('#calc_irr_export_button');
    calc_irr_export_button.bind('click', function () {
        $('#jsGrid').empty();
        $('#jsGrid').append('<b>Export läuft ...</b>');
        $(location).attr('href', getPath() + 'export_irr?' + getExportIrrQuery());
    });
});

function getExportIrrQuery() {
    return 'year=' + $('input[id=year]').val()
        + '&lat=' + $('input[id=lat]').val() + '&lon=' + $('input[id=lon]').val()
        + '&ae=' + $('input[id=alignment]').val() + '&ye=' + $('input[id=tilt]').val();
}

function getYears() {
    var date = new Date();
    var years = [];
    for (var year = 1991; year <= date.getFullYear(); year++) {
        years.push('' + year);
    }
    return years;
}