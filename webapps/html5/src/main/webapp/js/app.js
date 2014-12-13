// define a namespace for storing global variables
$.tdsScope = {};

$(document).ready(function () {

    var full_url = window.location;
    $.tdsScope.baseUrl = full_url.protocol + "//" + full_url.host + "/" + full_url.pathname.split('/')[1];
    //$.tdsScope.baseUrl = "https://home.xhibit.be:8080/teletask";

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

        $.ajax({
            type: "GET", url: $.tdsScope.baseUrl +"/api/component/config", crossDomain:true, cache:false
        }).done(function(config) {
            $().initRoomDOM(config.rooms);
        });

        $.ajax({
            type: "GET", url: $.tdsScope.baseUrl +"/api/component/config/MOTOR", crossDomain:true, cache:false
        }).done(function(components) {
            $().initDOM(components, $('#page_screens .ui-body'));
        });

        $.ajax({
            type: "GET", url: $.tdsScope.baseUrl +"/api/component/config/LOCMOOD", crossDomain:true, cache:false
        }).done(function(components) {
            $().initDOM(components, $('#page_moods .ui-body'));
        });

        $.ajax({
            type: "GET", url: $.tdsScope.baseUrl +"/api/component/config/GENMOOD", crossDomain:true, cache:false
        }).done(function(components) {
            $().initDOM(components, $('#page_moods .ui-body'));
        });

        $.ajax({
            type: "GET", url: $.tdsScope.baseUrl +"/api/component/config/SENSOR", crossDomain:true, cache:false
        }).done(function(components) {
            $().initDOM(components, $('#page_sensors .ui-body'));
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
 * Init Room DOM.
 */
(function( $ ){
    $.fn.initRoomDOM = function(rooms) {

        var page = $('#rooms');
        $.each(rooms, function(i, room) {

            var placeholder = $("<div>")
                .attr("data-role", "collapsible")
                .attr("id", "ROOM_" +room.id)
                .append(
                    $("<h3>").text(room.name)
                );

            var components = room.relays;
            $().initDOM(components, placeholder);

            placeholder.appendTo(page);
        });

    };
})( jQuery );



/**
 * Init DOM for the components.
 */
(function( $ ){
    $.fn.initDOM = function(components, page) {

        $.each(components, function(i, component) {
            //console.log("component: " +JSON.stringify(component));
            var func = component.function;

            if (func === "RELAY" || func === "LOCMOOD" || func === "GENMOOD" || func === "MOTOR") {

                var componentID = func + '-SWITCH-' + component.number;
                var onTxt = func == 'MOTOR' ? 'down' : 'on';
                var offTxt = func == 'MOTOR' ? 'up' : 'off';
                var switchButton = $("<input>")
                    .attr("type", "checkbox")
                    .attr("id", componentID)
                    .attr("name", componentID)
                    .attr("class", "switchButton")
                    .attr("data-role", "flipswitch")
                    .attr("data-wrapper-class", "custom-label-flipswitch")
                    .attr("data-on-text", onTxt)
                    .attr("data-off-text", offTxt)
                    .attr("data-tds-number", component.number);

                $("<p>")
                    .append(
                        $("<label>").attr("for", componentID).text(component.description + ": ")
                    )
                    .append(switchButton)
                .appendTo(page);

                // set correct state of button
                if (component.state == 'ON' || component.state == 'DOWN') {
                    switchButton.attr('checked', true);
                }

                // bind change event triggering REST call
                switchButton.change(function () {
                    $().switchComponentState($(this));
                });

            } else if (func === "SENSOR") {

                $("<h3>")
                    .attr("class", "ui-bar ui-bar-a ui-corner-all")
                    .text(component.description)
                .appendTo(page);

                $("<div>")
                    .attr("class", "ui-body")
                    .append(
                        $("<p>").attr("id", "SENSOR"+component.type+"-"+component.number).text(component.state +" Lux")
                    )
                .appendTo(page);

            }

        });

    };
})( jQuery );

/**
 * Init state switches.
 */
(function( $ ){
    $.fn.switchComponentState = function(element) {

            var componentValue = element.data('tds-number');
            var componentType = element.attr('id').split('-')[0];
            var stateValue;
            if (componentType === "RELAY" || componentType === "LOCMOOD" || componentType === "GENMOOD") {
                stateValue = element.is(':checked') ? "on" : "off";
            } else if (componentType === "MOTOR") {
                stateValue = element.is(':checked') ? "down" : "up";
            }

            var url = $.tdsScope.baseUrl +'/api/component/' +componentType +"/" +componentValue +"/state/" +stateValue

            $.ajax({
                type: "GET"
                ,url: url
                ,crossDomain : true
                ,cache:false
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

                // made more robust: getting null events (need to find cause).
                if (components != null && components.length > 0 && components[0] != null) {
                    $.each(components, function (i, component) {
                        console.log('Changing state' + ': ' + component.function + ':' + component.number + ' to ' + component.state);

                        var func = component.function;

                        if (func === "RELAY" || func === "LOCMOOD" || func === "GENMOOD" || func === "MOTOR") {
                            var componentID = func +'-SWITCH-' +component.number;
                            var compEl = $("#"+componentID);

                            // unbind the change event to prevent it from being executed
                            compEl.unbind('change');

                            // set correct state of button
                            if (component.state=='ON' || component.state=='DOWN') {
                                compEl.attr('checked',true);
                                compEl.flipswitch();
                                compEl.flipswitch("refresh");
                                compEl.parent().addClass("ui-flipswitch-active");
                            } else {
                                compEl.attr('checked',false);
                                compEl.flipswitch();
                                compEl.flipswitch("refresh");
                                compEl.parent().removeClass("ui-flipswitch-active");
                            }

                            // unbind the change event again
                            compEl.change(function () {
                                $().switchComponentState($( this ));
                            });

                        } else if (func === "SENSOR") {
                            var componentID = 'SENSOR' +component.type+"-"+component.number;
                            var compEl = $("#"+componentID);
                            compEl.text(component.state +" Lux");
                        }
                    });
                }

            };
        }
    };
})( jQuery );