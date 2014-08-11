$(document).ready(function () {

    var full_url = window.location;
    var base_url = full_url.protocol + "//" + full_url.host + "/" + full_url.pathname.split('/')[1];

    $(document).bind("mobileinit", function() {
        $.mobile.page.prototype.options.addBackBtn = true;
    });

    //TODO: make API call to get all components (JSON), and init all toggle objects accordingly
    //TODO: better to work with websockets (node.js): this way component state changes can be pushed to the app
    /*var configURL = base_url +"config";
     var jqxhr = $.getJSON( configURL)
     .done(function(json) {
     console.log( "process json: " +json);
     //TODO: parse JSON and compose HTML DOM
     //TODO: better look into Backbone.js: http://demos.jquerymobile.com/1.4.3/backbone-requirejs/
     })
     .fail(function() {
     console.log( "init error" );
     })
     .always(function() {
     console.log( "init complete" );
     });*/

    /**
     * SWITCH RELAY STATE
     * URI: PUT <base_url>/api/relay/{number}/state/{off/on}
     *
     * SWITCH MOTOR STATE
     * URI: PUT <base_url>/api/motor/{number}/state/{down/up}
     */
    $( ".stateSwitch" ).bind( "change", function(event) {

        var componentValue = $(this).data('tds-number');
        var componentType = $(this).data('tds-type');
        var stateValue;
        if (componentType === "relay" || componentType === "locmood" || componentType === "genmood") {
            // relay needs to send 1 for "on"
            stateValue = $(this).is(':checked') ? "on" : "off";
        } else if (componentType === "motor") {
            // motor needs to send 0 for "down"
            stateValue = $(this).is(':checked') ? "down" : "up";
        }

        var url = base_url +'/api/component/' +componentType +"/" +componentValue +"/state/" +stateValue

        $.ajax({
            type: "GET"
            ,url: url
        })
            .done(function (data, textStatus, jqXHR) {
                //sample response: {"response":{"success": "true","status": "1","relay": "41"}}
                if ( console && console.log ) {
                    console.log( "Data returned:", data );
                }
            })
            .fail(function (jqXHR, textStatus, errorThrown) {
                if ( console && console.log ) {
                    console.log( "Component switch call failed. Error:", errorThrown );
                }
            })
            .always(function(data, textStatus) {
                if ( console && console.log ) {
                    console.log( "Component switch call success. Data returned:", data );
                }
            });

        return false;
    });


    $(function() {
        $( "[data-role='navbar']" ).navbar();
        $( "[data-role='header'], [data-role='footer']" ).toolbar();
    });


    // Update the contents of the toolbars
    $( document ).on( "pageshow", "[data-role='page']", function() {
        // Each of the four pages in this demo has a data-title attribute
        // which value is equal to the text of the nav button
        // For example, on first page: <div data-role="page" data-title="Info">
        //TODO: implement
        // var current = $( this ).jqmData( "title" );
        // Change the heading
        //$( "[data-role='header'] h1" ).text( current );
        // Remove active class from nav buttons
        $( "[data-role='navbar'] a.ui-btn-active" ).removeClass( "ui-btn-active" );
        // Add active class to current nav button
        /* TODO: implement
         $( "[data-role='navbar'] a" ).each(function() {
         if ( $( this ).text() === current ) {
         $( this ).addClass( "ui-btn-active" );
         }
         });*/
    });

    if(!("WebSocket" in window)){
        $('<p>Oh no, you need a browser that supports WebSockets.</p>').appendTo('#page_start');
    }else {
        //The user has WebSockets
        $().openWebSocket();
    }

});

(function( $ ){
    $.fn.openWebSocket = function() {
        if (document.URL.indexOf('http:') == 0) {
            var baseWsUrl = document.URL.replace("http://", "ws://");
        } else {
            var baseWsUrl = document.URL.replace("https://", "wss://");
        }
        var wsocket = new WebSocket(baseWsUrl + 'state-changes');
        console.log("Opening WebSocket connection: " +wsocket);

        wsocket.onmessage = function (evt) {

            console.log("WebSocket msg: " +evt.data);

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
        //return this;
    };
})( jQuery );