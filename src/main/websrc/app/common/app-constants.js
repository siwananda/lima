module.exports = function (LimaApp) {

    var ENDPOINTS = {
        BASE_URL: "rest",
        USER_REQUEST_PATH: "users",
        TEAM_REQUEST_PATH: "teams",
        TASK_REQUEST_PATH: "tasks",
        PROJECT_REQUEST_PATH: "projects",
        EVENT_REQUEST_PATH: "events"

    };

    LimaApp
        .constant('ENDPOINTS', ENDPOINTS)
    ;

    return LimaApp;
};