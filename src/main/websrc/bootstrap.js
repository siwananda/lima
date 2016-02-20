require('./style/main.scss');
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