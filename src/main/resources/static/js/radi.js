jQuery(document).ready(function () {
    $('#calc_button').css("background-color", "whitesmoke");
    initDateInputs(1991);

    $('#src').empty();
    $.ajax({
        beforeSend: function (request) {
            request.setRequestHeader('login', $('input[id=username]').val());
            request.setRequestHeader('password', $('input[id=password]').val());
        },
        url: getPath() + 'count',
        method: 'GET',
        dataType: "text",
        success: function (rows) {
            $('#rows').append('<b>Datenanzahl: </b>' + rows);
        }
    });

    var type_select = jQuery('#type_select');
    type_select.bind('change', function () {
        $('#jsGrid').empty();
        var selected_value = $('#type_select option:selected').text();
        if (selected_value === 'GLOBAL') {
            initDateInputs(1991);
        } else {
            initDateInputs(2015);
        }
    });

    var start_date_select = jQuery('#start_date');
    start_date_select.bind('change', function () {
        $('#jsGrid').empty();
    });

    var end_date_select = jQuery('#end_date');
    end_date_select.bind('change', function () {
        $('#jsGrid').empty();
    });

    var lon_input = jQuery('#lon');
    lon_input.bind('change', function () {
        $('#jsGrid').empty();
    });

    var lat_input = jQuery('#lat');
    lat_input.bind('change', function () {
        $('#jsGrid').empty();
    });

    var search_button = jQuery('#search_button');
    search_button.bind('click', function () {
        $('#jsGrid').empty();
        $.ajax({
            method: 'GET',
            url: getPath() + 'radiation',
            dataType: "json",
            contentType: "application/json",
            data: {
                startDate: $('input[id=start_date]').val(),
                endDate: $('input[id=end_date]').val(),
                lat: $('input[id=lat]').val(),
                lon: $('input[id=lon]').val(),
                type: $('#type_select option:selected').text()
            },
            success: function (json) {
                $("#jsGrid").jsGrid({
                    width: "620",
                    height: "700",

                    //sorting: true,

                    data: json,

                    fields: [
                        {name: "date", title: "Datum", type: "text"},
                        {name: "value", title: "EHor", type: "number"},
                        {name: "unit", title: "Einh.", type: "text"},
                        {name: "dim", title: "Dim.", type: "text"}
                    ]
                });
            }
        });
    });

    var export_button = jQuery('#export_button');
    export_button.bind('click', function () {
        $(location).attr('href', getPath() + 'exportradi?' + getExportRadiQuery());
    });
});

function getExportRadiQuery() {
    return 'startDate=' + $('input[id=start_date]').val() + '&endDate=' + $('input[id=end_date]').val()
            + '&lat=' + $('input[id=lat]').val() + '&lon=' + $('input[id=lon]').val()
            + '&type=' + $('#type_select option:selected').text().replace("-", "#");
}

function initDateInputs(startYear) {
    var date = new Date();
    var dates = [];
    for (var year = startYear; year <= date.getFullYear(); year++) {
        if (year !== date.getFullYear()) {
            for (var month = 1; month <= 12; month++) {
                pushToDates(dates, year, month);
            }
        } else {
            if (date.getDay() < 15) {
                date.setMonth(date.getMonth() - 1);
            }
            var end = date.getMonth() + 1;
            for (var currentMonth = 1; currentMonth <= end; currentMonth++) {
                pushToDates(dates, year, currentMonth);
            }
        }
    }

    $('#start_date').val(dates[0]);
    $('#end_date').val(dates[dates.length - 1]);
    autocomplete(document.getElementById("start_date"), dates);
    autocomplete(document.getElementById("end_date"), dates);
}

function pushToDates(dates, year, month) {
    if (month.toString().length === 1) {
        dates.push(year.toString() + '-0' + month.toString());
    } else {
        dates.push(year.toString() + '-' + month.toString());
    }
}

