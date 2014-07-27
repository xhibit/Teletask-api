'use strict';

(function () {
    /* Controllers */
    var app = angular.module('homies-controller', ['toggle-switch']);
    app.controller('ConfigController', ['$rootScope', '$scope', '$http', function ($rootScope, $scope, $http) {
        $scope.config = [];

//        $scope.roomStates = [];

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
                $scope.config = angular.fromJson(evt.data);
            });
        };
    }]);
})();
