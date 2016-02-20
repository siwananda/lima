module.exports = function (LimaApp) {
    LimaApp.directive('projectCard', function ($state, LimaEntity, ENDPOINTS) {

        return {
            restrict: 'E',
            template: require('./project-card.html'),
            scope: {
                project: '='
            },
            link: function (scope, element, attrs) {

                var _populateTeam = function (team) {
                    scope.members = _.take(team.members, 10);
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