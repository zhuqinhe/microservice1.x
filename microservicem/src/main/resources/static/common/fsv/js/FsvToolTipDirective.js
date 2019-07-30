'use strict';

angular.module('myApp.directives').directive('fsvToolTip', function() {
    /**
     * 
     * 例子：
     *  <fsv-tool-tip class="icon-info-sign" title="hello world" direction="top" ></fsv-tool-tip>
     * 
     *  带头标题的按钮
     * 
     * title 标题
     * direction 弹出方向
     */
    return {
        restrict: 'EA',
        replace: true,
        scope: {
            title: "@",
            direction: "@"
        },
        template: '<a></a>',
        link: function($scope, $element, $attr) {
            var options = {
                placement:$scope.direction,
                content:$scope.title
            };

            $element.tooltip(options);
            
        }
    }
});

