<%@ page import="be.xhibit.teletask.client.TeletaskClient" %>
<%@ page import="be.xhibit.teletask.config.model.json.TDSClientConfig" %>
<%@ page import="be.xhibit.teletask.model.spec.Function" %>
<%@ page import="be.xhibit.teletask.webapp.ClientHolder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--
  Created by IntelliJ IDEA.
  User: bruno
  Date: 26/05/14
  Time: 11:53
--%>
<html>
<head>
    <title>Teletask UI</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />

    <link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.2/jquery.mobile-1.4.2.min.css" />
    <link rel="stylesheet" href="css/style.css" />
    <link rel="apple-touch-icon" href="apple-touch-icon.png"/>

    <script src="http://code.jquery.com/jquery-1.10.2.min.js"></script>
    <script>
        $(document).ready(function () {
            //TODO: define URL globally: must match the SERVER's IP in order to work for AJAX call!!!
            //var base_url = 'http://192.168.1.10:8181/teletask/api/';
            var base_url = 'http://localhost:8181/teletask/api/';

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
                //TODO: better look into Backbone.js: http://demos.jquerymobile.com/1.4.2/backbone-requirejs/
            })
            .fail(function() {
                console.log( "init error" );
            })
            .always(function() {
                console.log( "init complete" );
            });*/

            /**
             * SWITCH RELAY STATE
             * URI: PUT <base_url>/relay/{number}/state/{off/on}
             *
             * SWITCH MOTOR STATE
             * URI: PUT <base_url>/motor/{number}/state/{down/up}
             */
            $( ".stateSwitch" ).bind( "change", function(event) {

                var componentValue = $(this).data('tds-number');
                var componentType = $(this).data('tds-type');
                var stateValue;
                if (componentType === "relay") {
                    // relay needs to send 1 for "on"
                    stateValue = $(this).is(':checked') ? "on" : "off";
                } else if (componentType === "motor") {
                    // motor needs to send 0 for "down"
                    stateValue = $(this).is(':checked') ? "up" : "down";
                }

                var url = base_url +componentType +"/" +componentValue +"/state/" +stateValue
                console.log("TDS REST call: " +url);
                $.ajax({
                     type: "PUT"
                    ,url: url
                    ,done: function (data) {
                        //sample response: {"response":{"success": "true","status": "1","relay": "41"}}
                        //alert('success: ' +data.response.success);
                        console.log(data);
                    }
                    ,fail: function (data) {
                        //alert('data: ' +data);
                        console.log( "component switch call failed" );
                    }
                    ,always: function() {
                        console.log( "component switch call complete" );
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
    <script src="http://code.jquery.com/mobile/1.4.0/jquery.mobile-1.4.0.min.js"></script>
</head>
<body>

    <%

        //TDSClientConfig tdsConfig = TDSClientConfig.read(TDSClientConfig.class.getClassLoader().getResourceAsStream("tds-config.json"));
        //TDSClient client = TDSClient.getInstance(tdsConfig);

        TeletaskClient client = ClientHolder.getClient();
        TDSClientConfig tdsConfig = (TDSClientConfig) client.getConfig();

        request.setAttribute("tds_relays", tdsConfig.getComponentsTypes().get(Function.RELAY));
        request.setAttribute("tds_locmoods", tdsConfig.getComponentsTypes().get(Function.LOCMOOD));
        request.setAttribute("tds_genmoods", tdsConfig.getComponentsTypes().get(Function.GENMOOD));
        request.setAttribute("tds_screens", tdsConfig.getComponentsTypes().get(Function.MOTOR));
        request.setAttribute("tds_rooms", tdsConfig.getRooms());
    %>

    <%--<c:out value="${requestScope.tds_rooms}"></c:out>--%>

    <%--
    <c:forEach items="${requestScope.tds_relays}" var="relay">
        <c:out value="${relay.description}" />
        <c:out value="${relay.number}"></c:out>
        <c:out value="${relay.state}"></c:out>
    </c:forEach>
    --%>

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
            <p><a href="#page_rooms" data-role="button" data-icon="arrow-r" data-iconpos="bottom" class="ui-nodisc-icon ui-btn-icon-bottom icon-home">Rooms</a></p>
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
                    <input type="checkbox" data-role="flipswitch" name="locmood-flip-<c:out value="${status.index}" />" class="stateSwitch" id="locmood-flip-<c:out value="${status.index}" />" data-tds-type="relay" data-tds-number="<c:out value="${locmoods.number}" />">
                </p>
                <%--TODO: check state: <c:out value="${screen.state}"></c:out> --%>
            </c:forEach>
            </div>

            <h3 class="ui-bar ui-bar-a ui-corner-all">General Moods:</h3>

            <div class="ui-body ui-body-a ui-corner-all">
            <c:forEach items="${requestScope.tds_genmoods}" var="genmoods" varStatus="status">
                <p>
                    <label for="genmood-flip-<c:out value="${status.index}" />"><c:out value="${genmoods.description}" />:</label>
                    <input type="checkbox" data-role="flipswitch" name="genmood-flip-<c:out value="${status.index}" />" class="stateSwitch" id="genmood-flip-<c:out value="${status.index}" />" data-tds-type="relay" data-tds-number="<c:out value="${genmoods.number}" />">
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
            <p>Verlichting: overzicht</p>
            <ul data-role="listview" data-inset="true" data-filter="false">
                <li><a href="#page_v_gelijkvloers">Gelijkvloers</a></li>
                <li><a href="#page_v_verdiep1">Verdiep 1</a></li>
                <li><a href="#page_v_verdiep2">Verdiep 2</a></li>
                <li><a href="#page_v_buiten">Buiten</a></li>
            </ul>
        </div>

    </div>
    <!-- ####################### /page:verlichting ####################### -->

    <!-- ####################### page:verlichting:gelijkvloers ####################### -->
    <div data-role="page" data-theme="a" id="page_v_gelijkvloers" data-add-back-btn="true">

        <div data-role="content">
            <p>Verlichting Gelijkvloers</p>
            <ul data-role="listview" data-inset="true" data-filter="false">
                <li><a href="#page_v_g_living">Living</a></li>
                <li><a href="#page_v_g_zithoek">Zithoek</a></li>
                <li><a href="#page_v_g_keuken">Keuken</a></li>
                <li><a href="#page_v_g_berging">Berging</a></li>
                <li><a href="#page_v_g_hal">Hal</a></li>
                <li><a href="#page_v_g_wc">WC</a></li>
            </ul>
        </div>

    </div>
    <!-- ####################### /page:verlichting:gelijkvloers ####################### -->

    <!-- ####################### page:verlichting:verdiep1 ####################### -->
    <div data-role="page" data-theme="a" id="page_v_verdiep1" data-add-back-btn="true">

        <div data-role="content">
            <p>Verlichting Gelijkvloers</p>
            <ul data-role="listview" data-inset="true" data-filter="false">
                <li><a href="#page_v_v1_bureau">Bureau</a></li>
            </ul>
        </div>

    </div>
    <!-- ####################### /page:verlichting:verdiep1 ####################### -->

    <!-- ####################### page:verlichting:gelijkvloers:zithoek ####################### -->
    <div data-role="page" data-theme="a" id="page_v_g_zithoek" data-add-back-btn="true">


        <div data-role="content">
            <p>Verlichting: gelijkvloers: zithoek</p>

            <p>
                <label for="zithoek-flip-1">Spots zijde oprit</label>
                <input type="checkbox" data-role="flipswitch" name="zithoek-flip-1" class="stateSwitch" id="zithoek-flip-1" data-tds-type="relay" data-tds-number="41">
            </p>

            <p>
                <label for="zithoek-flip-2">Spots zijde berging</label>
                <input type="checkbox" data-role="flipswitch" name="zithoek-flip-2" class="stateSwitch" id="zithoek-flip-2" data-tds-type="relay" data-tds-number="42">
            </p>

            <p>
                <label for="zithoek-flip-3">LED's</label>
                <input type="checkbox" data-role="flipswitch" name="zithoek-flip-3" class="stateSwitch" id="zithoek-flip-3" data-tds-type="relay" data-tds-number="16">
            </p>

            <p>
                <label for="zithoek-flip-3">Screen cinema</label>
                <input type="checkbox" data-role="flipswitch" name="zithoek-flip-4" class="stateSwitch" data-on-text="Down" data-off-text="Up" data-wrapper-class="custom-label-flipswitch" id="zithoek-flip-4"  data-tds-type="motor" data-tds-number="0">
            </p>

        </div>

    </div>
    <!-- ####################### /page:verlichting:gelijkvloers:zithoek ####################### -->

    <!-- ####################### page:verlichting:gelijkvloers:keuken ####################### -->
    <div data-role="page" data-theme="a" id="page_v_g_keuken" data-add-back-btn="true">

        <div data-role="content">
            <p>Verlichting: gelijkvloers: keuken</p>

            <p>
                <label for="flip-1">Spots kasten</label>
                <input type="checkbox" data-role="flipswitch" name="flip-checkbox" class="stateSwitch" id="flip-1" data-tds-type="relay" data-tds-number="38">
            </p>

            <p>
                <label for="flip-2">Kookeiland</label>
                <input type="checkbox" data-role="flipswitch" name="flip-checkbox" class="stateSwitch" id="flip-2" data-tds-type="relay" data-tds-number="31">
            </p>

            <p>
                <label for="flip-3">LED's</label>
                <input type="checkbox" data-role="flipswitch" name="flip-checkbox" class="stateSwitch" id="flip-3" data-tds-type="relay" data-tds-number="20">
            </p>

            <p>
                <label for="flip-4">Tafel</label>
                <input type="checkbox" data-role="flipswitch" name="flip-checkbox" class="stateSwitch" id="flip-4" data-tds-type="relay" data-tds-number="23">
            </p>

        </div>


    </div>
    <!-- ####################### /page:verlichting:gelijkvloers:keuken ####################### -->

    <!-- ####################### page:verlichting:gelijkvloers:bureau ####################### -->
    <div data-role="page" data-theme="a" id="page_v_v1_bureau" data-add-back-btn="true">


        <div data-role="content">
            <p>Verlichting: gelijkvloers: bureau</p>

            <p>
                <label for="bureau-flip-1">Spots zijde muur</label>
                <input type="checkbox" data-role="flipswitch" name="bureau-flip-1" class="stateSwitch" id="bureau-flip-1" data-tds-type="relay" data-tds-number="39">
            </p>

            <p>
                <label for="bureau-flip-2">Spots zijde vide</label>
                <input type="checkbox" data-role="flipswitch" name="bureau-flip-2" class="stateSwitch" id="bureau-flip-2" data-tds-type="relay" data-tds-number="40">
            </p>

            <p>
                <label for="bureau-flip-3">Centrale lamp</label>
                <input type="checkbox" data-role="flipswitch" name="bureau-flip-3" class="stateSwitch" id="bureau-flip-3" data-tds-type="relay" data-tds-number="19">
            </p>

        </div>


    </div>
    <!-- ####################### /page:verlichting:gelijkvloers:bureau ####################### -->


    <div data-role="footer" data-position="fixed" data-theme="b">
        <div data-role="navbar">
            <ul>
                <li><a href="#page_lights" id="navbar_verlichting" class="ui-nodisc-icon ui-btn-icon-bottom icon-brightness-contrast"></a></li>
                <li><a href="#page_moods" id="navbar_sfeer" class="ui-nodisc-icon ui-btn-icon-bottom icon-equalizer"></a></li>
                <li><a href="#page_screens" id="navbar_screens" class="ui-nodisc-icon ui-btn-icon-bottom icon-menu"></a></li>
                <li><a href="#page_rooms" id="navbar_allesuit" class="ui-nodisc-icon ui-btn-icon-bottom icon-home"></a></li>
            </ul>
        </div>
    </div>

</body>
</html>