'use strict';

(function () {
    /* Controllers */
    var app = angular.module('homies-rooms', []);
    app.controller('RoomsController', ['$http', function ($http) {
        var controller = this;

        var rooms = [];

        $http.get('http://stewie.griffin.lan:7070/teletask/api/config').success(function (data, status, headers, config) {
            controller.rooms = data.rooms;
        });
    }])
    ;
})();
