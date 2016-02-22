module.exports = function(LimaApp){
    LimaApp.controller('UserTasksController', function ($scope, $stateParams, $http, $rootScope, ENDPOINTS, STATUSES, LimaEntity) {

        $scope.init = function(){
            $rootScope.currentUser.all(ENDPOINTS.TASK_REQUEST_PATH).getList().then(function(tasks){
                $scope.tasks = tasks;
            });
        };

    });
};