module.exports = function (LimaApp) {
    LimaApp.directive('taskCard', function (LimaEntity, ENDPOINTS) {

        return {
            restrict: 'E',
            template: require('./task-card.html'),
            scope: {
                task: '='
            },
            link: function (scope, element, attrs) {

                var _saveTask = function (task, callback) {
                    var baseTask = _.extend(LimaEntity.one(ENDPOINTS.TASK_REQUEST_PATH), task);
                    baseTask.put().then(_.isFunction(callback)?callback:function (response) {
                        console.log("task saved %o", response)
                    })
                };

                //cheat cheat cheat - yeay!
                var _saveDate = function (date) {
                    scope.task.end = moment(date).format("YYYY-MM-DD");
                    _saveTask(scope.task);
                };

                var _saveStatus = function (value, text, $choice) {
                    scope.task.status = value;
                    _saveTask(scope.task, function (response) {
                        $(element).find(".task-status").removeClass("blue gray red green");
                        var newColor = "gray";
                        switch(value){
                            case "BACKLOG":
                                newColor = "gray";
                                break;
                            case "IN_PROGRESS":
                                newColor = "blue";
                                break;
                            case "DONE":
                                newColor = "green";
                                break;
                            case "OBSOLETE":
                            default:
                                newColor = "red";
                        }
                        $(element).find(".task-status").addClass(newColor);
                    });
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
                $(element).find(".task-status").dropdown({
                    onChange: _saveStatus,
                    preserveHtml: false
                })
            }
        }

    });
};