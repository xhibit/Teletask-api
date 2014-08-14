// define a namespace for storing global variables
$.tdsScope = {};

$(document).ready(function () {

    $( "[data-role='navbar']" ).navbar();
    $( "[data-role='header'], [data-role='footer']" ).toolbar();

    $().initComponents();
    $().initWebSocket();

});



/**
 * Init config, build DOM.
 * TODO: better look into Backbone.js: http://demos.jquerymobile.com/1.4.3/backbone-requirejs/
 */
(function( $ ){
    $.fn.initComponents = function() {

        var full_url = window.location;
        $.tdsScope.baseUrl = full_url.protocol + "//" + full_url.host + "/" + full_url.pathname.split('/')[1];

        $.getJSON( $.tdsScope.baseUrl +"/api/config/MOTOR")
            .done(function(components) {
                $().initDOM(components);
            });

        $.getJSON( $.tdsScope.baseUrl +"/api/config/LOCMOOD")
            .done(function(components) {
                $().initDOM(components);
            });

        $.getJSON( $.tdsScope.baseUrl +"/api/config/GENMOOD")
            .done(function(components) {
                $().initDOM(components);
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
 * Init DOM for the components.
 */
(function( $ ){
    $.fn.initDOM = function(components) {

        var pages= {
            'MOTOR': '#page_screens .ui-body',
            'LOCMOOD': '#page_moods .ui-body',
            'GENMOOD': '#page_moods .ui-body'
        }

        $.each(components, function(i, component) {
            //console.log("component: " +JSON.stringify(component));
            //console.log("rendering DOM for component type: " +type);
            var func = component.function;
            var componentID = func +'-SWITCH-' +component.number;
            var onTxt = func=='MOTOR' ? 'down' : 'on';
            var offTxt = func=='MOTOR' ? 'up' : 'off';
            var page = $(pages[func]);

            $("<p>")
                .append(
                    $("<label>")
                    .attr("for", componentID)
                    .text(component.description +": ")
                )
                .append(
                    $("<input>")
                    .attr("type", "checkbox")
                    .attr("id", componentID)
                    .attr("name", componentID)
                    .attr("class", "switchButton")
                    .attr("data-role", "flipswitch")
                    .attr("data-wrapper-class", "custom-label-flipswitch")
                    .attr("data-on-text", onTxt)
                    .attr("data-off-text", offTxt)
                    .attr("data-tds-number", component.number)
                    //.attr("data-tds-type", component.function)
                )
            .appendTo(page);

            // set correct state of button
            var compEl = $("#"+componentID);
            if (component.state=='ON' || component.state=='DOWN') {
                //compEl.attr('checked','checked').flipswitch("refresh");
                compEl.attr('checked','checked');
            }

            // bind change event triggering REST call
            compEl.change(function () {
                $().switchComponentState($( this ));
            });

        });

    };
})( jQuery );

/**
 * Init state switches.
 */
(function( $ ){
    $.fn.switchComponentState = function(element) {

            var componentValue = element.data('tds-number');
            //var componentType = $(this).data('tds-type');
            var componentType = element.attr('id').split('-')[0];
            var stateValue;
            if (componentType === "RELAY" || componentType === "LOCMOOD" || componentType === "GENMOOD") {
                stateValue = $(this).is(':checked') ? "on" : "off";
            } else if (componentType === "MOTOR") {
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

                //TODO: switch the components button's state also triggers the "change" event again!!! Solve.
                $.each(components, function (i, component) {
                    console.log('Changing state' + ': ' + component.function + ':' + component.number + ' to ' + component.state);

                    // set correct state of button
                    var func = component.function;
                    var componentID = func +'-SWITCH-' +component.number;
                    var compEl = $("#"+componentID);
                    if (component.state=='ON' || component.state=='DOWN') {
                        //compEl.attr('checked','checked').flipswitch("refresh");
                        compEl.attr('checked','checked').flipswitch("refresh");
                    } else {
                        compEl.removeAttr('checked').flipswitch("refresh");
                    }

                    // get elements which starts with
                    //var element = $("input[id^=" + component.function + "]")
                });

            };
        }
    };
})( jQuery );