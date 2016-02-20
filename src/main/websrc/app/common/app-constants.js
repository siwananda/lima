module.exports = function (LimaApp) {

    var ENDPOINTS = {
        BASE_URL: "rest",
        USER_REQUEST_PATH: "users",
        TASK_REQUEST_PATH: "tasks",
        PROJECT_REQUEST_PATH: "projects"
    };

    LimaApp
        .constant('ENDPOINTS', ENDPOINTS)
    ;

    return LimaApp;
};