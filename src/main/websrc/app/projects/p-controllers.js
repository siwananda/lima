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

    var ProjectController = function ($scope, $http, project) {

        //do what we must
        console.log("projectDetail loaded")

    };

    return {
        ProjectListController: ['$scope', '$http', '$state', "ENDPOINTS", "projects", ProjectListController],
        ProjectController: ['$scope', '$http', 'project', ProjectController]
    };
};