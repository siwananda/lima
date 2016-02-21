module.exports = function (LimaApp) {
    LimaApp.directive('projectCard', function ($state, LimaEntity, ENDPOINTS) {

        return {
            restrict: 'E',
            template: require('./project-card.html'),
            scope: {
                project: '='
            },
            link: function (scope, element, attrs) {

                scope.members = [];
                scope.tasks = [];

                scope.tasks = _.isEmpty(scope.project.taskIds)?[]:scope.project.taskIds;

                var _populateTeam = function (team) {
                    scope.members = team.members;
                };

                var team = LimaEntity.one(ENDPOINTS.TEAM_REQUEST_PATH, scope.project.teamId);
                team.get().then(_populateTeam);

                $(element).find('.image').dimmer({
                    on: 'hover'
                });

                scope.projectDetail = function () {
                    $state.go('project', {projectId: scope.project.id});
                }
            }
        }

    });
};