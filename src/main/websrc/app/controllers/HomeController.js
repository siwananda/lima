module.exports = function(LimaApp){
    LimaApp.controller('HomeController', function ($scope, $stateParams, projects, users, ENDPOINTS, STATUSES, LimaEntity) {

        $scope.projects = projects;
        $scope.users = users;

        $scope.projectSubmitLoader = $('#projectSubmitLoader').hide();

        $scope.addProject = function(){
            $scope.newProject = {};
        };

        $scope.submitProject = function(){
            $scope.newProject.start = $('#txtProjectStart').val();
            $scope.newProject.end = $('#txtProjectEnd').val();

            var projectObject = _.extend(LimaEntity.one(ENDPOINTS.PROJECT_REQUEST_PATH), $scope.newProject);
            projectObject.save().then(function(project){
                $scope.projects.push(project);
                $('#addProjectModal').modal('hide');

                $scope.projectSubmitLoader.hide();
            });

        };

        $('#addProjectModal')
            .modal({
                closable  : false,
                onDeny    : function(){
                    $scope.newProject = undefined;
                },
                onApprove : function() {
                    $scope.projectSubmitLoader.show();
                    $scope.submitProject();
                    return false;
                }
            })
            .modal('attach events', '.add-project-btn', 'show')
        ;

        $('#projectStart').calendar({
            type: 'date',
            endCalendar: $('#projectEnd'),
            formatter: {
                date: function (date, settings) {
                    if (!date) return '';
                    return moment(date).format("YYYY-MM-DD");
                }
            }
        });

        $('#projectEnd').calendar({
            type: 'date',
            startCalendar: $('#projectStart'),
            formatter: {
                date: function (date, settings) {
                    if (!date) return '';
                    return moment(date).format("YYYY-MM-DD");
                }
            }
        });

    });
};