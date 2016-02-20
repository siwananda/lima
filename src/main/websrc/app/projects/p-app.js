(function(window, angular, $){
    var p = angular.module('LimaApp.Project', []);

    var pControllers = require("./p-controllers")($);
    p.controller('ProjectListCtrl', pControllers.ProjectListCtrl);
    p.controller('ProjectDetailCtrl', pControllers.ProjectDetailCtrl);

    return p;
})(window, window.angular, window.$);

if (typeof module !== "undefined" && typeof exports !== "undefined" && module.exports === exports){
    module.exports = 'LimaApp.Project';
}