module.exports = function ($) {
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

        //do what we must
        console.log("projectDetail loaded");
        var _populateTeam = function (team) {
            $scope.members = team.members;
            $("#taskList").toggleClass("loading");
        };

        $scope.project = project;
        $scope.tasks = project.tasks;


        //Starts loader
        $("#taskList").toggleClass("loading");
        var team = LimaEntity.one(ENDPOINTS.TEAM_REQUEST_PATH, project.teamId);
        team.get().then(_populateTeam);
    };

    return {
        ProjectListController: ['$scope', '$http', '$state', "ENDPOINTS", "projects", ProjectListController],
        ProjectController: ['$scope', '$http', 'project', 'ENDPOINTS', 'LimaEntity', ProjectController]
    };
};