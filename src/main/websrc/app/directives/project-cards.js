module.exports = function(LimaApp){
    LimaApp.directive('projectCards', function($http) {

        return {
            restrict: 'E',
            template: require('./project-cards.html'),
            scope: {
                projects: '='
            },
            link: function(scope, element, attrs){

            }
        }

    });
};