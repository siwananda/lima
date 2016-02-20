require('./style/main.scss');
require('nprogress-npm/nprogress.css');
var LimaApp = require('./app/main');

$(document).ready(function () {

    angular.element().ready(function() {
        // bootstrap the app manually
        angular.bootstrap(document, ['LimaApp']);
        $("#loader").fadeOut(function () {
            $("#loader").remove();
        });

    });
});