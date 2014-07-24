'use strict';

// Declare app level module which depends on filters, and services
(function () {
    var app = angular.module('homies', ['homies-controller']);

    app.run(function ($rootScope) {
        $rootScope.baseUrl = 'http://localhost:7776/api';
//        $rootScope.baseUrl = 'http://stewie.griffin.lan:7070/teletask/api';
    });
})();
//    'homies.filters',
//    'homies.services',
//    'homies.directives'
