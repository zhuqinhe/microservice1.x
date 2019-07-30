'use strict';

angular.module('myApp.directives').directive('fsvHtml', function() {
    /**
     * 
     * 还没研究好  暂时不用
     */
    return {
        restrict: 'E',
        replace: true,
        transclude:true,
        scope: {
            entity: "@",
            action: "@",
            listPath:"@"
        },
        templateUrl: 'common/fsv/FsvHtml.html'
        
    }
});




angular.module('myApp.directives').directive('fsvTransclude', function() {
    return {
        compile: function(tElement, tAttrs, transclude) {
            return function(scope, iElement, iAttrs) {
                transclude(scope.$new(), function(clone) {
                    iElement.append(clone);
                });
            };
        }
    };
});