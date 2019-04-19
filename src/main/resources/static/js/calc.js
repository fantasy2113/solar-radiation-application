jQuery(document).ready(function () {
    $('#radi_button').css("background-color", "whitesmoke");

    var calculation_button = jQuery('#calculation_button');
    calculation_button.bind('click', function () {
        $('html,body').css('cursor', 'wait');
        $('#jsGrid').empty();
        $('#jsGrid').append('<b>Berechnung läuft ...</b>');
        $.ajax({
            method: 'GET',
            url: getPath() + 'calculation',
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

    var calculation_export_button = jQuery('#calculation_export_button');
    calculation_export_button.bind('click', function () {
        $('html,body').css('cursor', 'wait');
        $(location).attr('href', getPath() + 'export_calc?' + getExportCalcQuery());
        $('html,body').css('cursor', 'default');
    });
});

function getExportCalcQuery() {
    return 'year=' + $('input[id=year]').val()
        + '&lat=' + $('input[id=lat]').val() + '&lon=' + $('input[id=lon]').val()
        + '&ae=' + $('input[id=alignment]').val() + '&ye=' + $('input[id=tilt]').val();
}