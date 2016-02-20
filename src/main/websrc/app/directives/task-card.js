module.exports = function(LimaApp){
    LimaApp.directive('taskCard', function($http) {

        return {
            restrict: 'E',
            templateUrl: 'directives/task-card.html',
            scope: {
                task: '='
            },
            link: function(scope, element, attrs){

            }
        }

    });
};