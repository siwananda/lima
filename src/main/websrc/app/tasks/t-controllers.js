module.exports = function ($) {
    'use strict';

    var TaskController = function ($scope, $http, task) {

        //do what we must
        console.log("Task detail loaded")
        $scope.task = task;

    };

    return {
        TaskController: ['$scope', '$http', 'task', TaskController]
    };
};