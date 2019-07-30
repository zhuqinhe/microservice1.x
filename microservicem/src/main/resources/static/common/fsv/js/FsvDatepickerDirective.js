angular.module('myApp.directives').directive('fsvDatepicker', function () {
    /**
     * 时间范围选择
     * 
     * 例子：
     *  
     *  <input type="text" class="fsv-datepicker" ng-model="queryParams.date" />
     * 
     * 备注：
     *  替代原先在controller中使用jquery进行绑定的做法（直接改动Dom，会导致双向绑定失效，这是Angularjs不提倡的做法）
     * 
     * 参数说明：
     * ng-model 时间 支持双向数据绑定
     */


    return {
        restrict: 'AC',
        scope: {
            ngModel: '=ngModel'
        },
        link: function ($scope, element, attrs) {
            element.attr('readonly', 'readonly');


           element.datepicker({
                dateFormat: 'yy-mm-dd',
                showOtherMonths: true,
                selectOtherMonths: false,
                changeMonth: true,
                changeYear: true,
                showButtonPanel: true,
                onClose: function (selectedDate, inst) {
                    $scope.ngModel = selectedDate;
                    $scope.$apply();
                }

            });

        
        }


    }
});