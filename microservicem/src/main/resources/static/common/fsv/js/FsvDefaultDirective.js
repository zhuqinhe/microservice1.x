angular.module('myApp.directives').directive('fsvDefault', function() {
    /**
     * 输入框 默认值 
     * 
     * 例子： <input type="text" ng-model="test" fsv-default="{{default}}"/>
     * 
     */
    

    return {
        restrict: 'A',
        scope: {
            ngModel: '=',
            fsvDefault: '@'
        },
        link: function($scope, element, attrs) {
           if(!$scope.ngModel){
                $scope.ngModel = $scope.fsvDefault;
           }
        }


    }
});