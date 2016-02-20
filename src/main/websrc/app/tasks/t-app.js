(function(window, angular, $){
    var p = angular.module('LimaApp.Task', []);

    var pControllers = require("./t-controllers")($);
    p.controller('TaskController', pControllers.TaskController);

    return p;
})(window, window.angular, window.$);

if (typeof module !== "undefined" && typeof exports !== "undefined" && module.exports === exports){
    module.exports = 'LimaApp.Task';
}