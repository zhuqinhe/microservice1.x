'use strict';

angular.module('myApp.directives').directive('fsvTitle', function() {
    /**
     * 
     * 
     * 例子：
     *  <label fsv-title="hello word" direction="top"  ></label>
     *  
     * 可替代title
     * 
     * fsvTitle title值
     * direction 弹出方向
     *
     */
    return {
        restrict: 'A',
        scope: {
            fsvTitle: "@",
            direction:"@"
        },
        link: function($scope, $element, $attr) {
            
            var directionOptions = ['top','bottom','right','left']
           if(!directionOptions.includes($scope.direction)){
                $scope.direction = right;
           }

            var options = {
                placement:$scope.direction,
                content:$scope.fsvTitle
            };

           
            $element.attr('title', $scope.fsvTitle);
            $element.tooltip(options);
            
        }
    }
});