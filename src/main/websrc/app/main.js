var LimaApp = angular.module('LimaApp',
    [
        require('angular-ui-router'),
        'restangular',
        'ngAnimate',
        //require('textangular/dist/textAngular-rangy.min'),
        //require('textangular/dist/textAngular-sanitize.min'),
        'textAngular'
    ])
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, ENDPOINTS) {


        //$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

        $urlRouterProvider.when('', '/');
        $urlRouterProvider.otherwise('/404');

        $stateProvider.state('site', {
                'abstract': true,
                views: {
                    'sidebar@': {
                        template: require('./components/sidebar.html'),
                        controller: 'SidebarController'
                    },
                    'header@': {
                        template: require('./components/header.html'),
                         controller: 'HeaderController'
                    },
                    'mobilemenu@': {
                        template: require('./components/mobilemenu.html')
                    }
                }
            })
            .state('home', {
                parent: 'site',
                url: '/',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        template: require('./components/home.html'),
                        controller: 'HomeController'
                    }
                },
                resolve: {
                    projects: function (LimaEntity) {
                        var baseProjects = LimaEntity.all(ENDPOINTS.PROJECT_REQUEST_PATH);
                        return baseProjects.getList();
                    },
                    users: function (LimaEntity) {
                        var baseUsers = LimaEntity.all(ENDPOINTS.USER_REQUEST_PATH);
                        return baseUsers.getList();
                    }
                }
            })
            .state('project', {
                parent: 'site',
                url: '/project/:projectId',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        template: require('./components/project.html'),
                        controller: 'ProjectController'
                    }
                },
                resolve: {
                    project: function (LimaEntity, $stateParams) {
                        if (_.isEqual($stateParams.projectId, "new")) {
                            return {};
                        } else {
                            var baseProject = LimaEntity.one(ENDPOINTS.PROJECT_REQUEST_PATH, $stateParams.projectId);
                            return baseProject.get();
                        }
                    }
                }
            })
            .state('user', {
                parent: 'site',
                abstract: true,
                template: "<ui-view></ui-view>"
            })
            .state('user.tasks', {
                parent: 'site',
                url: '/user/:id/tasks',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        template: require('./components/usertasks.html'),
                        controller: 'UserTasksController'
                    }
                },
                resolve: {

                }
            })
            .state('task', {
                parent: 'site',
                url: '/task/:id',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        template: require('./components/task.html'),
                        controller: 'TaskController'
                    }
                },
                resolve: {
                    task: function (LimaEntity, $stateParams) {
                        if (_.isEqual($stateParams.id, "new")) {
                            return {};
                        } else {
                            var baseTask = LimaEntity.one(ENDPOINTS.TASK_REQUEST_PATH, $stateParams.id);
                            return baseTask.get();
                        }
                    }
                }
            })
            .state('task.description', {
                url: '/description',
                data: {
                    authorities: []
                },
                template: require('./components/task-description.html'),
                controller: 'TaskDescriptionController',
                resolve: {
                    task: function (LimaEntity, $stateParams) {
                        if (_.isEqual($stateParams.id, "new")) {
                            return {};
                        } else {
                            var baseTask = LimaEntity.one(ENDPOINTS.TASK_REQUEST_PATH, $stateParams.id);
                            return baseTask.get();
                        }
                    }
                }
            })
            .state('task.history', {
                url: '/history',
                data: {
                    authorities: []
                },
                template: require('./components/task-history.html'),
                controller: 'TaskHistoryController',
                resolve: {
                    task: function (LimaEntity, $stateParams) {
                        if (_.isEqual($stateParams.id, "new")) {
                            return {};
                        } else {
                            var baseTask = LimaEntity.one(ENDPOINTS.TASK_REQUEST_PATH, $stateParams.id);
                            return baseTask.get();
                        }
                    }
                }
            })
            .state('task.timetrack', {
                url: '/timetrack',
                data: {
                    authorities: []
                },
                template: require('./components/task-timetrack.html'),
                controller: 'TaskTimeTrackController',
                resolve: {
                    task: function (LimaEntity, $stateParams) {
                        if (_.isEqual($stateParams.id, "new")) {
                            return {};
                        } else {
                            var baseTask = LimaEntity.one(ENDPOINTS.TASK_REQUEST_PATH, $stateParams.id);
                            return baseTask.get();
                        }
                    }
                }
            })
            .state('404', {
                parent: 'site',
                url: '/404',
                views: {
                    'content@': {
                        template: require('./components/error404.html')
                    }
                }
            });

    })
    .run(["$rootScope", "$state", "$location", function ($rootScope, $state, $location) {
        $rootScope.$state = $state; // state to be accessed from view
        $rootScope.$location = $location; // location to be accessed from view
        $rootScope.loadingScreen = jQuery('#mainLoader');
        $rootScope
            .$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
                    NProgress.start();
                    console.debug("START!! -> event(%o) | toState(%o) | fromState(%o)", event, toState, fromState);
                    $rootScope.loadingScreen.addClass("active");
                }
            );

        $rootScope
            .$on('$stateChangeSuccess',
                function (event, toState, toParams, fromState, fromParams) {
                    NProgress.done();
                    $rootScope.loadingScreen.removeClass("active");
                    console.debug("FINISH!! -> event(%o) | toState(%o) | fromState(%o)", event, toState, fromState);
                });
    }]);


require("./common/app-constants")(LimaApp);
require("./common/app-entities")(LimaApp);


require("./directives/project-card")(LimaApp);
require("./directives/project-cards")(LimaApp);
require("./directives/task-card")(LimaApp);
require("./directives/task-cards")(LimaApp);
require("./directives/usertask-card")(LimaApp);
require("./directives/submission-modal")(LimaApp);


require("./controllers/HeaderController")(LimaApp);
require("./controllers/SidebarController")(LimaApp);
require("./controllers/HomeController")(LimaApp);
require("./controllers/ProjectController")(LimaApp);
require("./controllers/TaskController")(LimaApp);
require("./controllers/UserTasksController")(LimaApp);


module.exports = LimaApp;
