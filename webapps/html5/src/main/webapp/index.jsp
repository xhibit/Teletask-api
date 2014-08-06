<%@ page import="be.xhibit.teletask.client.TeletaskClient" %>
<%@ page import="be.xhibit.teletask.config.model.json.TDSClientConfig" %>
<%@ page import="be.xhibit.teletask.model.spec.Function" %>
<%@ page import="be.xhibit.teletask.webapp.ClientHolder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Teletask UI</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />

    <link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.3/jquery.mobile-1.4.3.min.css" />
    <link rel="stylesheet" href="css/style.css" />
    <link rel="apple-touch-icon" href="apple-touch-icon.png"/>

    <script src="http://code.jquery.com/jquery-1.10.2.min.js"></script>
    <script>
        $(document).ready(function () {
            var base_url = '${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/';

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

                var url = base_url +'api/component/' +componentType +"/" +componentValue +"/state/" +stateValue

                $.ajax({
                     type: "PUT"
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


        });
    </script>
    <script src="http://code.jquery.com/mobile/1.4.3/jquery.mobile-1.4.3.min.js"></script>
</head>
<body>

    <%
        TeletaskClient client = ClientHolder.getClient();
        TDSClientConfig tdsConfig = (TDSClientConfig) client.getConfig();

        request.setAttribute("tds_relays", tdsConfig.getComponentsTypes().get(Function.RELAY));
        request.setAttribute("tds_locmoods", tdsConfig.getComponentsTypes().get(Function.LOCMOOD));
        request.setAttribute("tds_genmoods", tdsConfig.getComponentsTypes().get(Function.GENMOOD));
        request.setAttribute("tds_screens", tdsConfig.getComponentsTypes().get(Function.MOTOR));
        request.setAttribute("tds_rooms", tdsConfig.getRooms());
    %>

    <div data-role="header" data-position="fixed" data-theme="b">
        <h1>Teletask UI</h1>
        <!--<a href="#fav-panel" data-icon="bars" data-iconpos="notext">Menu</a>-->
    </div>

    <%--
    <div data-role="panel" id="fav-panel">
        <ul data-role="listview" data-theme="a" class="nav-search">
            <li data-icon="delete"><a href="#" data-rel="close">Close menu</a></li>
            <li><a href="#page_v_gelijkvloers">Gelijkvloers</a></li>
            <li><a href="#page_v_verdiep1">Verdiep 1</a></li>
            <li><a href="#page_v_verdiep2">Verdiep 2</a></li>
            <li><a href="#page_v_buiten">Buiten</a></li>
        </ul>
    </div>
    --%>

    <!-- ####################### page:start ####################### -->
    <div data-role="page" data-theme="a" id="page_start">

        <div data-role="content">
            <p><a href="#page_lights" data-role="button" data-icon="arrow-r" data-iconpos="bottom" class="ui-nodisc-icon ui-btn-icon-bottom icon-brightness-contrast">Lights</a></p>
            <p><a href="#page_moods" data-role="button" data-icon="arrow-r" data-iconpos="bottom" class="ui-nodisc-icon ui-btn-icon-bottom icon-equalizer">Moods</a></p>
            <p><a href="#page_screens" data-role="button" data-icon="arrow-r" data-iconpos="bottom" class="ui-nodisc-icon ui-btn-icon-bottom icon-menu">Screens</a></p>
            <!--<p><a href="#page_rooms" data-role="button" data-icon="arrow-r" data-iconpos="bottom" class="ui-nodisc-icon ui-btn-icon-bottom icon-home">Rooms</a></p>-->
        </div>

    </div>
    <!-- ####################### /page:start ####################### -->

    <!-- ####################### page:screens ####################### -->
    <div data-role="page" data-theme="a" id="page_screens" data-add-back-btn="true">

        <div data-role="content">
            <h3 class="ui-bar ui-bar-a ui-corner-all">Screens:</h3>

            <div class="ui-body ui-body-a ui-corner-all">
            <c:forEach items="${requestScope.tds_screens}" var="screen" varStatus="status">
                <p>
                    <label for="screen-flip-<c:out value="${status.index}" />"><c:out value="${screen.description}" />:</label>
                    <input type="checkbox" data-role="flipswitch" name="screen-flip-<c:out value="${status.index}" />" class="stateSwitch" data-on-text="Down" data-off-text="Up" data-wrapper-class="custom-label-flipswitch" id="screen-flip-<c:out value="${status.index}" />" data-tds-type="motor" data-tds-number="<c:out value="${screen.number}" />">
                </p>
                <%--TODO: check state: <c:out value="${screen.state}"></c:out> --%>
            </c:forEach>
            </div>

        </div>

    </div>
    <!-- ####################### /page:screens ####################### -->

    <!-- ####################### page:moods ####################### -->
    <div data-role="page" data-theme="a" id="page_moods" data-add-back-btn="true">

        <div data-role="content">
            <h3 class="ui-bar ui-bar-a ui-corner-all">Local Moods:</h3>

            <div class="ui-body ui-body-a ui-corner-all">
            <c:forEach items="${requestScope.tds_locmoods}" var="locmoods" varStatus="status">
                <p>
                    <label for="locmood-flip-<c:out value="${status.index}" />"><c:out value="${locmoods.description}" />:</label>
                    <input type="checkbox" data-role="flipswitch" name="locmood-flip-<c:out value="${status.index}" />" class="stateSwitch" id="locmood-flip-<c:out value="${status.index}" />" data-tds-type="locmood" data-tds-number="<c:out value="${locmoods.number}" />">
                </p>
                <%--TODO: check state: <c:out value="${screen.state}"></c:out> --%>
            </c:forEach>
            </div>

            <h3 class="ui-bar ui-bar-a ui-corner-all">General Moods:</h3>

            <div class="ui-body ui-body-a ui-corner-all">
            <c:forEach items="${requestScope.tds_genmoods}" var="genmoods" varStatus="status">
                <p>
                    <label for="genmood-flip-<c:out value="${status.index}" />"><c:out value="${genmoods.description}" />:</label>
                    <input type="checkbox" data-role="flipswitch" name="genmood-flip-<c:out value="${status.index}" />" class="stateSwitch" id="genmood-flip-<c:out value="${status.index}" />" data-tds-type="genmood" data-tds-number="<c:out value="${genmoods.number}" />">
                </p>
                <%--TODO: check state: <c:out value="${screen.state}"></c:out> --%>
            </c:forEach>
            </div>

        </div>

    </div>
    <!-- ####################### /page:moods ####################### -->

    <!-- ####################### page:verlichting ####################### -->
    <div data-role="page" data-theme="a" id="page_lights" data-add-back-btn="true">

        <div data-role="content">

            <div data-role="collapsibleset" data-collapsed-icon="carat-d" data-expanded-icon="carat-u">
            <c:forEach items="${requestScope.tds_rooms}" var="room">
                <div data-role="collapsible">
                    <h3><c:out value="${room.name}" /></h3>

                    <!--<div class="ui-body ui-body-a ui-corner-all">-->
                        <c:forEach items="${room.relays}" var="relay" varStatus="status">
                        <p>
                            <label for="flip-room-<c:out value="${room.id}" />-<c:out value="${room.level}" />-<c:out value="${status.index}" />"><c:out value="${relay.description}" /></label>
                            <input type="checkbox" data-role="flipswitch" name="flip-room-<c:out value="${room.id}" />-<c:out value="${room.level}" />-<c:out value="${status.index}" />" class="stateSwitch" id="flip-room-<c:out value="${room.id}" />-<c:out value="${room.level}" />-<c:out value="${status.index}" />" data-tds-type="relay" data-tds-number="<c:out value="${relay.number}" />">
                        </p>
                        </c:forEach>
                    <!--</div>-->
                </div>
            </c:forEach>
            </div>
        </div>

    </div>
    <!-- ####################### /page:verlichting ####################### -->

    <div data-role="footer" data-position="fixed" data-theme="b">
        <div data-role="navbar">
            <ul>
                <li><a href="#page_lights" id="navbar_verlichting" class="ui-nodisc-icon ui-btn-icon-bottom icon-brightness-contrast"></a></li>
                <li><a href="#page_moods" id="navbar_sfeer" class="ui-nodisc-icon ui-btn-icon-bottom icon-equalizer"></a></li>
                <li><a href="#page_screens" id="navbar_screens" class="ui-nodisc-icon ui-btn-icon-bottom icon-menu"></a></li>
                <!--<li><a href="#page_rooms" id="navbar_allesuit" class="ui-nodisc-icon ui-btn-icon-bottom icon-home"></a></li>-->
            </ul>
        </div>
    </div>

</body>
</html>