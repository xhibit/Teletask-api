'use strict';

(function () {
    /* Controllers */
    var app = angular.module('homies-controller', []);
    app.controller('ConfigController', ['$http', function ($http) {
        var url = 'http://localhost:7776';
//        var url = 'http://stewie.griffin.lan:7070/teletask';

        var controller = this;

        var config = [];

        $http.get(url + '/api/config').success(function (data, status, headers, config) {
            controller.config = data;
        });
    }])
    ;
})();
