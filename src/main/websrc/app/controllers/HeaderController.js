module.exports = function(LimaApp){
    LimaApp.controller('HeaderController', function ($scope, $stateParams, $http, $rootScope, ENDPOINTS, STATUSES, LimaEntity) {

        $scope.init = function(){
            var ue = LimaEntity.all(ENDPOINTS.USER_REQUEST_PATH);
            ue.customGET('current').then(function(user){
                $scope.user = user?user:{name:'Anonymous', avatar:'default.png', id:'123456789'};
                $rootScope.currentUser = $scope.user;
            });
        };

    });
};