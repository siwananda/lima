var LimaApp = angular.module('LimaApp',
    [
        require('angular-ui-router'),
        'restangular',
        'ngSanitize',
        'ngAnimate',
        //Modules
        require('./projects/p-app')
    ])
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider) {

        //$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

        $urlRouterProvider.when('', '/');
        $urlRouterProvider.otherwise('/404');

        $stateProvider.state('site', {
                'abstract': true,
                views: {
                    'sidebar@': {
                        templateUrl: 'components/sidebar.html'/*,
                         controller: 'SidebarController'*/
                    },
                    'header@': {
                        templateUrl: 'components/header.html'/*,
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
                        templateUrl: 'components/home.html',
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
                        templateUrl: 'components/projects.html',
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
                        templateUrl: 'components/project.html',
                        controller: 'ProjectController'
                    }
                },
                resolve: {}
            })
            .state('tasks', {
                parent: 'site',
                url: '/project/:projectId/tasks',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'components/tasks.html',
                        controller: 'TaskListController'
                    }
                },
                resolve: {}
            })
            .state('task', {
                parent: 'site',
                url: '/task/:taskId',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'components/task.html',
                        controller: 'TaskController'
                    }
                },
                resolve: {}
            })
            .state('404', {
                parent: 'site',
                url: '/404',
                views: {
                    'content@': {
                        templateUrl: '404.html'
                    }
                }
            });

    });

require("./common/app-constants")(LimaApp);
require("./common/app-entities")(LimaApp);
require("./controllers/HomeController")(LimaApp);

module.exports = LimaApp;
