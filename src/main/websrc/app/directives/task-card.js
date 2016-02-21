module.exports = function(LimaApp){
    LimaApp.directive('taskCard', function($http) {

        return {
            restrict: 'E',
            template: require('./task-card.html'),
            scope: {
                task: '='
            },
            link: function(scope, element, attrs){
                //Init calendar and formats it
                $(element).find("#dueDate").calendar({
                    type : 'date',
                    formatter: {
                        date: function (date, settings) {
                            if (!date) return '';
                            return moment(date).format("MMMM DD, YYYY");
                        }
                    }
                });
            }
        }

    });
};