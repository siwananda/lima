module.exports = function (LimaApp) {
    'use strict';

    var TaskController = function ($scope, $http, task) {

        var originalTask = task;

        //do what we must
        console.log("Task detail loaded");

        $scope.task = _.extend(task, {endDate: moment(task.end).format("MMMM DD, YYYY")})

    };

    LimaApp.controller('TaskController', ['$scope', '$http', 'task', TaskController]);
};