// define a namespace for storing global variables
$.tdsScope = {};

$(document).ready(function () {

    $().initComponents();
    $().initStateSwitches();
    $().initWebSocket();

});



/**
 * Init config, build DOM.
 */
(function( $ ){
    $.fn.initComponents = function() {

        var full_url = window.location;
        $.tdsScope.baseUrl = full_url.protocol + "//" + full_url.host + "/" + full_url.pathname.split('/')[1];

        //make API call to get all components (JSON), and init all objects accordingly
        var configURL = $.tdsScope.baseUrl +"/api/config";
        $.getJSON( configURL)
            .done(function(json) {
                console.log( "Loaded JSON for: " +json.centralUnitType);
                $.tdsScope.config = json;
                //TODO: parse JSON and compose HTML DOM
                //TODO: better look into Backbone.js: http://demos.jquerymobile.com/1.4.3/backbone-requirejs/

                $.each($.tdsScope.config.rooms, function(i, room) {
                    //console.log(room.name);
                });
            })
            .fail(function() {
                console.log( "init error" );
            })
            .always(function() {
                console.log( "init complete" );
            });




        $(function() {
            $( "[data-role='navbar']" ).navbar();
            $( "[data-role='header'], [data-role='footer']" ).toolbar();
        });


        // Update the contents of the toolbars
        /*$( document ).on( "pageshow", "[data-role='page']", function() {
         // Each of the four pages in this demo has a data-title attribute which value is equal to the text of the nav button
         var current = $( this ).jqmData( "title" );
         // Remove active class from nav buttons
         //$( "[data-role='navbar'] a.ui-btn-active" ).removeClass( "ui-btn-active" );
         // Add active class to current nav button
         $( "[data-role='navbar'] a" ).each(function() {
         if ( $( this ).text() === current ) {
         $( this ).addClass( "ui-btn-active" );
         }
         });
         });*/

    };
})( jQuery );


/**
 * Init state switches.
 */
(function( $ ){
    $.fn.initStateSwitches = function() {

        $( ".stateSwitch" ).bind( "change", function(event) {

            var componentValue = $(this).data('tds-number');
            var componentType = $(this).data('tds-type');
            var stateValue;
            if (componentType === "relay" || componentType === "locmood" || componentType === "genmood") {
                stateValue = $(this).is(':checked') ? "on" : "off";
            } else if (componentType === "motor") {
                stateValue = $(this).is(':checked') ? "down" : "up";
            }

            var url = $.tdsScope.baseUrl +'/api/component/' +componentType +"/" +componentValue +"/state/" +stateValue

            $.ajax({
                type: "GET"
                ,url: url
            })
                .fail(function (jqXHR, textStatus, errorThrown) {
                    if ( console && console.log ) {
                        console.log( "Component switch call failed. Error:", errorThrown );
                    }
                })
                .always(function(data, textStatus) {
                    if ( console && console.log ) {
                        console.log( "Component switch call success. Data returned:", JSON.stringify(data) );
                    }
                });

            return false;
        });

    };
})( jQuery );



/**
 * Websocket function.
 */
(function( $ ){
    $.fn.initWebSocket = function() {

        if(!("WebSocket" in window)){
            $('<p>Oh no, you need a browser that supports WebSockets.</p>').appendTo('#page_start');
        }else {
            if (document.URL.indexOf('http:') == 0) {
                var baseWsUrl = $.tdsScope.baseUrl.replace("http://", "ws://");
            } else {
                var baseWsUrl = $.tdsScope.baseUrl.replace("https://", "wss://");
            }
            var wsocket = new WebSocket(baseWsUrl + '/state-changes');
            console.log("Opening WebSocket connection: " + wsocket.url);

            wsocket.onmessage = function (evt) {

                console.log("WebSocket msg: " + evt.data);
                var components = $.parseJSON(evt.data);

                //TODO: switch the components state
                $.each(components, function (i, component) {
                    console.log("RECIEVED: component: " + component.description + ", state: " + component.state);
                });

                /*$scope.$apply(function () {
                 var components = angular.fromJson(evt.data);
                 angular.forEach($scope.config.rooms, function (room, key) {
                 function changeComponentState(comps) {
                 angular.forEach(comps, function (comp, key) {
                 angular.forEach(components, function (component, key) {
                 if (comp.number == component.number && comp.function == component.function) {
                 console.log('Changing state in room ' + room.name + ': ' + component.function + ':' + component.number + ' to ' + component.state.value);
                 comp.state.value = component.state.value;
                 comp.state.state = component.state.state;
                 }
                 });
                 });
                 }
                 changeComponentState(room.relays);
                 changeComponentState(room.localMoods);
                 changeComponentState(room.motors);
                 changeComponentState(room.generalMoods);
                 changeComponentState(room.dimmers);
                 changeComponentState(room.conditions);
                 });
                 });*/
            };
        }
    };
})( jQuery );