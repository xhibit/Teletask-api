'use strict';

(function () {
    /* Controllers */
    var app = angular.module('homies-controller', ['toggle-switch']);
    app.controller('ConfigController', ['$rootScope', '$scope', '$http', function ($rootScope, $scope, $http) {
        $scope.config = [];

        $http.get($rootScope.baseUrl + '/config').success(function (data, status, headers, config) {
            $scope.config = data;
        });

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
                var components = angular.fromJson(evt.data);
                angular.forEach($scope.config.rooms, function (room, key) {
                    function changeComponentState(comps) {
                        angular.forEach(comps, function (comp, key) {
                            angular.forEach(components, function (component, key) {
                                if (comp.number == component.number && comp.function == component.function) {
                                    console.log('Changing state in room ' + room.name + ': ' + component.function + ':' + component.number + ' to ' + component.state);
                                    comp.state = component.state;
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
                    changeComponentState(room.sensors);
                });
            });
        };
    }]);
})();
