module.exports = function(LimaApp){
    LimaApp.controller('HomeController', function ($scope, $stateParams, projects, users) {

        $scope.projects = projects;
        $scope.users = users;

    });
};