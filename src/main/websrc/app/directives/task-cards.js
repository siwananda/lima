module.exports = function(LimaApp){
    LimaApp.directive('taskCards', function($http) {

        return {
            restrict: 'E',
            templateUrl: 'directives/task-cards.html',
            scope: {
                tasks: '='
            },
            link: function(scope, element, attrs){

            }
        }

    });
};