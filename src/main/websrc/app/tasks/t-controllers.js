module.exports = function ($) {
    'use strict';

    var TaskController = function ($scope, $http, task) {

        //do what we must
        console.log("Task detail loaded")

    };

    return {
        TaskController: ['$scope', '$http', 'task', TaskController]
    };
};