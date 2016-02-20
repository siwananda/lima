module.exports = function(LimaApp){
    LimaApp.controller('HomeController', function ($scope, $stateParams, $http, $rootScope) {

        $scope.init = function(){

            $http.get('rest/projects').then(
                function(response){
                    var data = response.data;
                    if(data){
                        $scope.projects = data;
                    }
                },
                function(response){

                }
            );

        };

    });
};