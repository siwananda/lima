module.exports = function (LimaApp) {
    LimaApp.directive('taskCard', function (LimaEntity, ENDPOINTS) {

        return {
            restrict: 'E',
            template: require('./task-card.html'),
            scope: {
                task: '='
            },
            link: function (scope, element, attrs) {

                //cheat cheat cheat - yeay!
                var _saveDate = function (date) {
                    scope.task.end = moment(date).format("YYYY-MM-DD");
                    var baseTask = _.extend(LimaEntity.one(ENDPOINTS.TASK_REQUEST_PATH), scope.task);
                    baseTask.put().then(function (response) {
                        console.log("task saved %o", response)
                    })
                };

                //Init calendar and formats it
                $(element).find("#dueDate").calendar({
                    type: 'date',
                    formatter: {
                        date: function (date, settings) {
                            if (!date) return '';
                            return moment(date).format("MMMM DD, YYYY");
                        }
                    },
                    onChange: _saveDate
                });

            }
        }

    });
};