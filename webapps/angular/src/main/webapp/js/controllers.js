'use strict';

(function () {
    /* Controllers */
    var app = angular.module('homies-controller', ['toggle-switch']);
    app.controller('ConfigController', ['$rootScope', '$scope', '$http', function ($rootScope, $scope, $http) {
        $scope.config = [];

//        $scope.roomStates = [];

        $http.get($rootScope.baseUrl + '/config').success(function (data, status, headers, config) {
            changeConfig(data);
        });

        this.changeConfig = function (data) {
            $scope.config = data;
//            angular.forEach(data.rooms, function(room, key) {
//                angular.forEach(room.relays, function(relay, key) {
//                    $scope.$watch(
//                        // This function returns the value being watched. It is called for each turn of the $digest loop
//                        function() { return relay.state.value; },
//                        // This is the change listener, called when the value returned from the above function changes
//                        function(newValue, oldValue) {
//                            if ( newValue !== oldValue ) {
//                                changeState(relay, relay.state.value);
//                            }
//                        }
//                    );
//                });
//            });
        };

//        $scope.roomState = function(roomId) {
//            angular.forEach($scope.roomStates, function(room, key) {
//                if(room.id == roomId) {
//                    return room.state;
//                }
//            });
//        };

        this.changeState = function (component, newState) {
            $http.get($rootScope.baseUrl + '/component/' + component.function + '/' + component.number + '/state/' + newState);
        };

        this.groupGet = function () {
            $http.post($rootScope.baseUrl + '/group/relay/for', {numbers: [1, 2, 6]}).error(function (data, status, headers, config) {
                window.alert("Error");
            }).success(function (data, status, headers, config) {
                window.alert(JSON.stringify(data));
            });
        };

        var wsocket = new WebSocket($rootScope.baseWsUrl + '/state-changes');
        wsocket.onmessage = function (evt) {
            $scope.$apply(function () {
                changeConfig(angular.fromJson(evt.data));
            });
        };
    }]);
})();
