'use strict';

/* Controllers */
angular.module('homies.controllers', [])
    .controller('LinkController', function ($scope, $http) {
        $scope.linkGroups = [];
        $http.get('links.json').success(function (data, status, headers, config) {
            $scope.linkGroups = data;
        });
    })
;
