'use strict';

angular.module('myApp.directives').directive('fsvCheckAll', function() {
    /**
     * 全选 check
     * 适用于所有全选check
     * 
     * 例子：
     *  <fsv-check-all items="items" key-for-checked="checked" ng-model="checkAll" ></fsv-check-all>
     * 
     * 参数介绍：
     * items 传入一个数组 数组元素是需要被全选的项目，通过key-for-checked对应的参数来标识是否被选中
     * key-for-checked 传入一个字符串（注意是字符串，单向传入） 这个字符串可以items的每个元素中找到对应的参数 在用于标识是否被选中
     */
    return {
        restrict: 'E',
        replace: true,
        scope: {
            items: "=items",
            keyForChecked: "@keyForChecked",
            ngChange:"&ngChange",
            ngModel:"=ngModel"
        },
        templateUrl: 'common/fsv/FsvCheckAll.html',
        link: function($scope, $element, $attr) {

            $scope.change = function(){
                $scope.items.forEach(function(item) {
                    item[$scope.keyForChecked] =   $scope.ngModel;
                });
            };
        }
    }
});