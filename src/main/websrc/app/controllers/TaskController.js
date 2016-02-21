module.exports = function (LimaApp) {
    'use strict';

    var TaskController = function ($scope, $http, task) {

        //do what we must
        console.log("Task detail loaded");
        $scope.task = task;

    };

    LimaApp.controller('TaskController', ['$scope', '$http', 'task', TaskController]);
};