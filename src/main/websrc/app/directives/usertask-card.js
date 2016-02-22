module.exports = function (LimaApp) {
    LimaApp.directive('userTaskCard', function (LimaEntity, ENDPOINTS, STATUSES) {

        return {
            restrict: 'E',
            template: require('./usertask-card.html'),
            scope: {
                task: '='
            },
            link: function (scope, element, attrs) {
                var _grabLabel = function(value){
                    return STATUSES[value].label;
                };

                var _grabLabelColor = function(value){
                    return STATUSES[value].color;
                };

                scope.task.statusLabel = _grabLabel(scope.task.status);
                scope.task.colorLabel = _grabLabelColor(scope.task.status);
            }
        }

    });
};