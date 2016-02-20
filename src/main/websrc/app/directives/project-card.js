module.exports = function(LimaApp){
    LimaApp.directive('projectCard', function($http) {

        return {
            restrict: 'E',
            templateUrl: 'directives/project-card.html',
            scope: {
                project: '='
            },
            link: function(scope, element, attrs){

            }
        }

    });
};