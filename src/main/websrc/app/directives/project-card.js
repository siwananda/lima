module.exports = function(LimaApp){
    LimaApp.directive('projectCard', function($state, $http) {

        return {
            restrict: 'E',
            template: require('./project-card.html'),
            scope: {
                project: '='
            },
            link: function(scope, element, attrs){
                $(element).find('.image').dimmer({
                    on: 'hover'
                });

                scope.projectDetail = function(){
                    $state.go('project', {projectId: scope.project.id});
                }
            }
        }

    });
};