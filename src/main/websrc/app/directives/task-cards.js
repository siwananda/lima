module.exports = function(LimaApp){
    LimaApp.directive('taskCards', function($http) {

        return {
            restrict: 'E',
            template: require('./task-cards.html'),
            scope: {
                tasks: '='
            },
            link: function(scope, element, attrs){

            }
        }

    });
};