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
            url: getPath() + 'find',
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

    var export_button = jQuery('#export_button');
    export_button.bind('click', function () {
        $(location).attr('href', getPath() + 'export?' + getExportQuery());
    })
});

function getExportQuery() {
    return 'startDate=' + $('input[id=start_date]').val() + '&endDate=' + $('input[id=end_date]').val()
        + '&lat=' + $('input[id=lat]').val() + '&lon=' + $('input[id=lon]').val()
        + '&type=' + $('#type_select option:selected').text().replace("-", "#");
}

function initDateInputs(startYear) {
    var date = new Date();
    var dates = [];
    for (var year = startYear; year <= date.getFullYear(); year++) {
        if (year != date.getFullYear()) {
            for (var month = 1; month <= 12; month++) {
                pushToDates(dates, year, month);
            }
        } else {
            if (date.getDay() < 15) {
                date.setMonth(date.getMonth() - 1);
            }
            var end = date.getMonth() + 1;
            console.log(end);
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

function autocomplete(inp, arr) {
    /*the autocomplete function takes two arguments,
     the text field element and an array of possible autocompleted values:*/
    var currentFocus;
    /*execute a function when someone writes in the text field:*/
    inp.addEventListener("input", function (e) {
        var a, b, i, val = this.value;
        /*close any already open lists of autocompleted values*/
        closeAllLists();
        if (!val) {
            return false;
        }
        currentFocus = -1;
        /*create a DIV element that will contain the items (values):*/
        a = document.createElement("DIV");
        a.setAttribute("id", this.id + "autocomplete-list");
        a.setAttribute("class", "autocomplete-items");
        /*append the DIV element as a child of the autocomplete container:*/
        this.parentNode.appendChild(a);
        /*for each item in the array...*/
        for (i = 0; i < arr.length; i++) {
            /*check if the item starts with the same letters as the text field value:*/
            if (arr[i].substr(0, val.length).toUpperCase() == val.toUpperCase()) {
                /*create a DIV element for each matching element:*/
                b = document.createElement("DIV");
                /*make the matching letters bold:*/
                b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
                b.innerHTML += arr[i].substr(val.length);
                /*insert a input field that will hold the current array item's value:*/
                b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
                /*execute a function when someone clicks on the item value (DIV element):*/
                b.addEventListener("click", function (e) {
                    /*insert the value for the autocomplete text field:*/
                    inp.value = this.getElementsByTagName("input")[0].value;
                    /*close the list of autocompleted values,
                     (or any other open lists of autocompleted values:*/
                    closeAllLists();
                });
                a.appendChild(b);
            }
        }
    });
    /*execute a function presses a key on the keyboard:*/
    inp.addEventListener("keydown", function (e) {
        var x = document.getElementById(this.id + "autocomplete-list");
        if (x)
            x = x.getElementsByTagName("div");
        if (e.keyCode == 40) {
            /*If the arrow DOWN key is pressed,
             increase the currentFocus variable:*/
            currentFocus++;
            /*and and make the current item more visible:*/
            addActive(x);
        } else if (e.keyCode == 38) { //up
            /*If the arrow UP key is pressed,
             decrease the currentFocus variable:*/
            currentFocus--;
            /*and and make the current item more visible:*/
            addActive(x);
        } else if (e.keyCode == 13) {
            /*If the ENTER key is pressed, prevent the form from being submitted,*/
            e.preventDefault();
            if (currentFocus > -1) {
                /*and simulate a click on the "active" item:*/
                if (x)
                    x[currentFocus].click();
            }
        }
    });

    function addActive(x) {
        /*a function to classify an item as "active":*/
        if (!x)
            return false;
        /*start by removing the "active" class on all items:*/
        removeActive(x);
        if (currentFocus >= x.length)
            currentFocus = 0;
        if (currentFocus < 0)
            currentFocus = (x.length - 1);
        /*add class "autocomplete-active":*/
        x[currentFocus].classList.add("autocomplete-active");
    }

    function removeActive(x) {
        /*a function to remove the "active" class from all autocomplete items:*/
        for (var i = 0; i < x.length; i++) {
            x[i].classList.remove("autocomplete-active");
        }
    }

    function closeAllLists(elmnt) {
        /*close all autocomplete lists in the document,
         except the one passed as an argument:*/
        var x = document.getElementsByClassName("autocomplete-items");
        for (var i = 0; i < x.length; i++) {
            if (elmnt != x[i] && elmnt != inp) {
                x[i].parentNode.removeChild(x[i]);
            }
        }
    }

    /*execute a function when someone clicks in the document:*/
    document.addEventListener("click", function (e) {
        closeAllLists(e.target);
    });
}

