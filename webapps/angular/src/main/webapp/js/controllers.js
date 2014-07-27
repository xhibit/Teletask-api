'use strict';

(function () {
    /* Controllers */
    var app = angular.module('homies-controller', []);
    app.controller('ConfigController', ['$rootScope', '$scope', '$http', function ($rootScope, $scope, $http) {
        $scope.config = [];

//        $scope.roomStates = [];

        $http.get($rootScope.baseUrl + '/config').success(function (data, status, headers, config) {
            $scope.config = data;
//            angular.forEach(data.rooms, function(room, key) {
//                this.push({id: room.id, state: 'collapse'});
//            }, $scope.roomStates);
        });

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
                $scope.config = angular.fromJson(evt.data);
            });
        };
    }]);
})();
