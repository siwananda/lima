module.exports = function(LimaApp){
    LimaApp.directive('submissionModal', function($http) {

        return {
            restrict: 'E',
            template: require('./submission-modal.html'),
            transclude: true,
            scope: {
                modalId: '@',
                title: '@',
                attachedTo: '@',
                onSubmit: '=',
                onCancel: '='
            },
            link: function(scope, element, attrs){

                scope.afterRender = function(elem){
                    if(scope.attachedTo){
                        $(elem)
                            .modal('attach events', scope.attachedTo, 'show')
                        ;
                    }
                };
            }
        }

    });

    LimaApp.directive('afterRender', ['$timeout', function ($timeout) {
        return {
            restrict: 'A',
            terminal: true,
            link: function (scope, element, attrs) {
                $timeout(scope.$eval(attrs.afterRender), 0, true, element);  //Calling a scoped method
            }
        };
    }]);
};