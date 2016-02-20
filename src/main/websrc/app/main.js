var LimaApp = angular.module('LimaApp',
    [
        require('angular-ui-router'),
        'restangular',
        'ngSanitize',
        'ngAnimate'
    ]);
require("./common/app-constants")(LimaApp);
require("./common/app-entities")(LimaApp);
module.exports = LimaApp;