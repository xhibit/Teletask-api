'use strict';

(function () {
    /* Controllers */
    var app = angular.module('homies-controller', []);
    app.controller('ConfigController', ['$rootScope', '$http', function ($rootScope, $http) {
        var controller = this;

        var config = [];

        $http.get($rootScope.baseUrl + '/config').success(function (data, status, headers, config) {
            controller.config = data;
        });

        this.changeState = function(component, newState){
            $http.get($rootScope.baseUrl + '/component/' + component.function + '/' + component.number + '/state/' + newState);
        };
    }])
    ;
})();
