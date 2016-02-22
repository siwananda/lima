module.exports = function (LimaApp) {
    'use strict';

    var TaskController = function ($scope, $element, $state, $stateParams, LimaEntity, ENDPOINTS, STATUSES, task) {

        var originalTask = task;

        //do what we must
        console.log("Task detail loaded");

        $scope.task = _.extend(task, {
            endDate: moment(task.end).format("MMM DD, YYYY"),
            startDate: moment(task.start).format("MMM DD, YYYY"),
            statusLabel: STATUSES[task.status].label
        });

        LimaEntity.one(ENDPOINTS.USER_REQUEST_PATH, task.assigneeId).get().then(function (user) {
            $scope.task.assignee = user;
        });

        console.log("state %o", $state);

        if (_.isEqual($state.current.name, "task")) {
            $state.transitionTo("task.description", {"id": task.id});
        }

        //last few miles, copy pasting is valid now :D
        var _grabMildColor = function (value) {
            return STATUSES[value].color;
        };

        var _saveTask = function (task, callback) {
            var baseTask = _.extend(LimaEntity.one(ENDPOINTS.TASK_REQUEST_PATH), task);
            baseTask.put().then(_.isFunction(callback) ? callback : function (response) {
                console.log("task saved %o", response)
            })
        };

        var _saveDate = function (type, date) {
            if (_.isEqual(type, "end")) {
                $scope.task.end = moment(date).format("YYYY-MM-DD");
            } else {
                $scope.task.start = moment(date).format("YYYY-MM-DD");
            }

            _saveTask($scope.task, function(response){
                $($element).find(".task-"+type+"-date").text(moment(date).format("MMM DD, YYYY"));
            });
        };

        var _saveStatus = function (value, text, $choice) {
            $($element).find(".task-status").addClass("loading");
            $scope.task.status = value;
            _saveTask($scope.task, function (response) {
                var newColor = $scope.buttonColor = _grabMildColor(response.status);
                $($element).find(".task-status").removeClass("blue gray red green loading");
                $($element).find(".task-status").addClass(newColor);
            });
        };

        $scope.buttonColor = _grabMildColor($scope.task.status);

        var _formatter = {
            date: function (date, settings) {
                if (!date) return '';
                return moment(date).format("MMMM DD, YYYY");
            }
        };

        //Init calendar and formats it
        $($element).find("#endDate").calendar({
            type: 'date',
            formatter: _formatter,
            onChange: function(date){_saveDate("end", date)},
            initialDate: new Date($scope.task.end)
        });
        $($element).find("#startDate").calendar({
            type: 'date',
            formatter: _formatter,
            onChange: function(date){_saveDate("start", date)},
            initialDate: new Date($scope.task.start)
        });
        $($element).find(".task-status").dropdown({
            onChange: _saveStatus,
            preserveHtml: false
        })

    };

    var TaskDescriptionController = function ($scope, $state, $stateParams, LimaEntity, ENDPOINTS, task) {
        console.log("Description detail loaded here");

        $scope.edit = false;

        $scope.startEdit = function(){
            $scope.edit = true;
        };

        var _saveTask = function (task, callback) {
            var baseTask = _.extend(LimaEntity.one(ENDPOINTS.TASK_REQUEST_PATH), task);
            baseTask.put().then(_.isFunction(callback) ? callback : function (response) {
                console.log("task saved %o", response)
            })
        };

        $scope.saveDesc = function(task){
            _saveTask(task, function(){
                $scope.edit=false;
            })
        }
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

    LimaApp.controller('TaskController', ['$scope', '$element', '$state', '$stateParams', 'LimaEntity', 'ENDPOINTS',
        'STATUSES', 'task', TaskController])
        .controller('TaskDescriptionController', ['$scope', '$state', '$stateParams', 'LimaEntity', 'ENDPOINTS', 'task', TaskDescriptionController])
        .controller('TaskHistoryController', ['$scope', '$state', '$stateParams', 'LimaEntity', 'ENDPOINTS', 'task', TaskHistoryController])
        .controller('TaskTimeTrackController', ['$scope', '$state', '$stateParams', 'task', TaskTimeTrackController]);
};