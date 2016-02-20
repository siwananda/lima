module.exports = function(LimaApp){
    LimaApp.directive('taskCard', function($http) {

        return {
            restrict: 'E',
            template: require('./task-card.html'),
            scope: {
                task: '='
            },
            link: function(scope, element, attrs){
                $(element).find("#dueDate").calendar({type : 'date'})
            }
        }

    });
};