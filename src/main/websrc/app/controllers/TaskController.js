module.exports = function (LimaApp) {
    'use strict';

    var TaskController = function ($scope, $http, $state, $stateParams, LimaEntity, ENDPOINTS, task) {

        var originalTask = task;

        //do what we must
        console.log("Task detail loaded");

        $scope.task = _.extend(task, {
            endDate: moment(task.end).format("MMMM DD, YYYY"),
            startDate: moment(task.start).format("MMMM DD, YYYY")
        });

        LimaEntity.one(ENDPOINTS.USER_REQUEST_PATH, task.assigneeId).get().then(function (user) {
            $scope.task.assignee = user;
        });

        console.log("state %o", $state);

        if (_.isEqual($state.current.name, "task")) {
            $state.transitionTo("task.description", {"id": task.id});
        }

    };

    var TaskDescriptionController = function ($scope, $state, $stateParams, task) {
        console.log("Description detail loaded here");

    };

    var TaskHistoryController = function ($scope, $state, $stateParams, LimaEntity, ENDPOINTS, task) {
        //Take history on open tab
        var baseEvent = LimaEntity.one(ENDPOINTS.EVENT_REQUEST_PATH, $stateParams.id);
        baseEvent.get().then(function (event) {
            $scope.events = event;
        })
    };

    var TaskTimeTrackController = function ($scope, $state, $stateParams, task) {

    };

    LimaApp.controller('TaskController', ['$scope', '$http', '$state', '$stateParams', 'LimaEntity', 'ENDPOINTS', 'task', TaskController])
        .controller('TaskDescriptionController', ['$scope', '$state', '$stateParams', 'task', TaskDescriptionController])
        .controller('TaskHistoryController', ['$scope', '$state', '$stateParams', 'LimaEntity', 'ENDPOINTS', 'task', TaskHistoryController])
        .controller('TaskTimeTrackController', ['$scope', '$state', '$stateParams', 'task', TaskTimeTrackController]);
};