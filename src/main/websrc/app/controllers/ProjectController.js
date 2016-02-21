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

    var ProjectController = function ($scope, $http, project, ENDPOINTS, LimaEntity) {

        //Init real
        $scope.members = [];
        $scope.tasks = [];
        $scope.newTask = {
            name: 'name here',
            description: 'descripion here'
        };

        //do what we must
        console.log("projectDetail loaded");
        var _populateTeam = function (team) {
            $scope.members = team.members;
            $("#taskList").toggleClass("loading");
        };

        $scope.project = project;
        $scope.tasks = _.isEmpty(project.tasks)?[]:project.tasks;


        //Starts loader
        $("#taskList").toggleClass("loading");
        var team = LimaEntity.one(ENDPOINTS.TEAM_REQUEST_PATH, project.teamId);
        team.get().then(_populateTeam);

        $scope.addTask = function(){
            $scope.newTask = {
                name: 'name here',
                description: 'descripion here'
            };
        };

        $scope.submitTask = function(){
            $scope.newTask.start = $('#txtTaskStart').val();
            $scope.newTask.end = $('#txtTaskEnd').val();

            var taskObject = _.extend(LimaEntity.one(ENDPOINTS.TASK_REQUEST_PATH), $scope.newTask);
            taskObject.save().then(function(response){
                var data = response.data;
                debugger;
            });

        };

        $('#addTaskModal')
            .modal({
                closable  : false,
                onDeny    : function(){
                    $scope.newTask = undefined;
                },
                onApprove : function() {
                    $scope.submitTask();
                }
            })
            .modal('attach events', '.add-task-btn', 'show')
        ;

        $('#taskStart').calendar({
            type: 'date',
            endCalendar: $('#taskEnd')
        });

        $('#taskEnd').calendar({
            type: 'date',
            startCalendar: $('#taskStart')
        });


    };

    LimaApp
        .controller('ProjectListController', ['$scope', '$http', '$state', "ENDPOINTS", "projects", ProjectListController])
        .controller('ProjectController', ['$scope', '$http', 'project', 'ENDPOINTS', 'LimaEntity', ProjectController]);
};