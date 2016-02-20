module.exports = function (LimaApp) {

    var LimaEntity = function (Restangular, ENDPOINTS) {

        return Restangular.withConfig(function (RestangularConfigurer) {
            RestangularConfigurer.setBaseUrl(ENDPOINTS.BASE_URL);
        });

    };

    LimaApp
        .factory('LimaEntity', ["Restangular", "ENDPOINTS", LimaEntity])
    ;

    return LimaApp;
};