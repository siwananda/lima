var LimaApp = angular.module('LimaApp',
    [
        require('angular-ui-router'),
        'restangular',
        'ngSanitize',
        'ngAnimate',
        //Modules
        require('./projects/p-app'),
        require('./tasks/t-app')
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
                        template: require('./components/header.html')/*,
                         controller: 'HeaderController'*/
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
                resolve: {}
            })
            .state('projects', {
                parent: 'site',
                url: '/projects',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        template: require('./components/projects.html'),
                        controller: 'ProjectListController'
                    }
                },
                resolve: {}
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
                        if (_.isEqual($stateParams.id, "new")) {
                            return {};
                        } else {
                            var baseProject = LimaEntity.one(ENDPOINTS.PROJECT_REQUEST_PATH, $stateParams.id);
                            return baseProject.get();
                        }
                    }
                }
            })
            .state('tasks', {
                parent: 'site',
                url: '/project/:projectId/tasks',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        template: require('./components/tasks.html'),
                        controller: 'TaskListController'
                    }
                },
                resolve: {}
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
                        debugger;
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

    });

require("./common/app-constants")(LimaApp);
require("./common/app-entities")(LimaApp);


require("./directives/project-card")(LimaApp);
require("./directives/project-cards")(LimaApp);
require("./directives/task-card")(LimaApp);
require("./directives/task-cards")(LimaApp);


require("./controllers/SidebarController")(LimaApp);
require("./controllers/HomeController")(LimaApp);

module.exports = LimaApp;
