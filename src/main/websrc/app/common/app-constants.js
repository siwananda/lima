module.exports = function (LimaApp) {

    var ENDPOINTS = {
        BASE_URL: "rest",
        USER_REQUEST_PATH: "users",
        TEAM_REQUEST_PATH: "teams",
        TASK_REQUEST_PATH: "tasks",
        PROJECT_REQUEST_PATH: "projects",
        EVENT_REQUEST_PATH: "events"

    };

    var STATUSES = {
        BACKLOG: {value: "BACKLOG", label: "Pending", color:"gray"},
        IN_PROGRESS: {value: "IN_PROGRESS", label: "Started", color:"teal"},
        DONE: {value: "DONE", label: "Completed", color:"green"},
        OBSOLETE: {value: "OBSOLETE", label: "Obsolete", color:"red"}
    };

    LimaApp
        .constant('ENDPOINTS', ENDPOINTS)
        .constant('STATUSES', STATUSES)
    ;

    return LimaApp;
};