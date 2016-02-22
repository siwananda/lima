module.exports = function (LimaApp) {
    'use strict';

    var ProjectListController = function ($scope, $http, $state, ENDPOINTS, projects) {
        //do what we must
        $scope.projects = projects;

        //Opens detail of clicked Project
        $scope.openDetail = function (projectId) {
            $state.go("projects.detail", {id: projectId});
        };
    };

    var ProjectController = function ($scope, $http, project, ENDPOINTS, STATUSES, LimaEntity) {

        //Init real
        $scope.members = [];
        $scope.tasks = [];
        $scope.newTask = {
            name: 'name here',
            description: 'description here'
        };
        $scope.taskSubmitLoader = $('#taskSubmitLoader').hide();

        //do what we must
        console.log("projectDetail loaded");
        var _populateTeam = function (team) {
            $scope.members = team.members;
            debugger;
            _.each(project.tasks, function(task){
                task.assignee = _.find($scope.members, {"id": task.assigneeId});
            });
            $("#taskList").toggleClass("loading");
        };

        $scope.project = project;
        $scope.tasks = _.isEmpty(project.tasks)?[]: _.map(project.tasks, function(task){
            return _.extend(task, {
                endDate: moment(task.end).format("MMMM DD, YYYY"),
                statusLabel: STATUSES[task.status].label
            })
        });


        //Starts loader
        $("#taskList").toggleClass("loading");

        var team = LimaEntity.one(ENDPOINTS.TEAM_REQUEST_PATH, project.teamId);
        team.get().then(_populateTeam);

        $scope.addTask = function(){
            $scope.newTask = {};
        };

        $scope.submitTask = function(){
            $scope.newTask.start = $('#txtTaskStart').val();
            $scope.newTask.end = $('#txtTaskEnd').val();

            var taskObject = _.extend(LimaEntity.one(ENDPOINTS.TASK_REQUEST_PATH), $scope.newTask);
            taskObject.save().then(function(task){
                $scope.newTask = task;

                $scope.project.one(ENDPOINTS.TASK_REQUEST_PATH, task.id).put().then(function(project){
                    $scope.tasks = project.tasks;
                    $('#addTaskModal').modal('hide');

                    $scope.taskSubmitLoader.hide();
                });
            });

        };

        $('#addTaskModal')
            .modal({
                closable  : false,
                onDeny    : function(){
                    $scope.newTask = undefined;
                },
                onApprove : function() {
                    if($('#addTaskForm').form('is valid')){
                        $scope.taskSubmitLoader.show();
                        $scope.submitTask();
                    }else {
                        $('#addTaskForm').form('validate form')
                    }
                    return false;
                }
            })
            .modal('attach events', '.add-task-btn', 'show')
        ;

        $('#taskStart').calendar({
            type: 'date',
            endCalendar: $('#taskEnd'),
            formatter: {
                date: function (date, settings) {
                    if (!date) return '';
                    return moment(date).format("YYYY-MM-DD");
                }
            }
        });

        $('#taskEnd').calendar({
            type: 'date',
            startCalendar: $('#taskStart'),
            formatter: {
                date: function (date, settings) {
                    if (!date) return '';
                    return moment(date).format("YYYY-MM-DD");
                }
            }
        });

        $('#addTaskForm')
            .form(
                {
                    fields: {
                        name: {
                            identifier: 'name',
                            rules: [
                                {
                                    type   : 'empty',
                                    prompt : 'Please enter task name'
                                }
                            ]
                        }
                    }
                }
            )
        ;

    };

    LimaApp
        .controller('ProjectListController', ['$scope', '$http', '$state', "ENDPOINTS", "projects", ProjectListController])
        .controller('ProjectController', ['$scope', '$http', 'project', 'ENDPOINTS', 'STATUSES', 'LimaEntity', ProjectController]);
};