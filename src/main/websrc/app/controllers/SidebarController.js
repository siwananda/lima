module.exports = function(LimaApp){
    LimaApp.controller('SidebarController', function ($scope, $stateParams, $http, $rootScope) {

        $scope.init = function(){
            $('.ui.sidebar')
                .sidebar('setting', 'transition', 'overlay')
                .sidebar('attach events', '.toc.item')
            ;
        };

    });
};