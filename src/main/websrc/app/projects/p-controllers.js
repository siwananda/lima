module.exports = function ($) {
    'use strict';

    var ProjectListCtrl = function ($scope, $http, $state, ENDPOINTS, projects) {
        //do what we must
        $scope.projects = projects;

        //Opens detail of clicked Project
        $scope.openDetail = function (projectId) {
            $state.go("projects.detail", {id: projectId});
        };
    };

    var ProjectDetailCtrl = function ($scope, $http, project) {

        //do what we must
        console.log("projectDetail loaded")

    };

    return {
        ProjectListCtrl: ['$scope', '$http', '$state', "ENDPOINTS", "projects", ProjectListCtrl],
        ProjectDetailCtrl: ['$scope', '$http', 'project', ProjectDetailCtrl]
    };
};