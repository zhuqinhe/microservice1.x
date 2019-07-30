angular.module('myApp.directives').directive('fsvDaterangpicker1', function() {
    /**
     * 时间范围选择
     * 
     * 例子：
     *  <input type="text" class="fsv-daterangpicker" start-date-model="queryParams.beginTime" end-date-model="queryParams.endTime" />
     * 
     * 备注：
     *  替代原先在controller中使用jquery进行绑定的做法（直接改动Dom，会导致双向绑定失效，这是Angularjs不提倡的做法）
     * 
     * 参数说明：
     * start-date-model 开始时间 支持双向数据绑定
     *  end-date-model 结束时间 支持双向数据绑定
     */
    var rangepickerLocale = {
        'zh-cn': {
            'Locale': {
                applyLabel: '确定',
                cancelLabel: '取消',
                fromLabel: '从',
                toLabel: '到',
                weekLabel: '周',
                customRangeLabel: '自定义范围',
                firstDay: 1
            },
            'Ranges': {
                '明天': [moment().add('days', 1), moment().add('days', 1)],
                '未来七天': [moment().add('days', 1), moment().add('days', 7)]
            }
        },
        'zh-tw': {
            'Locale': {
                applyLabel: '確定',
                cancelLabel: '取消',
                fromLabel: '從',
                toLabel: '到',
                weekLabel: '周',
                customRangeLabel: '自定義範圍',
                firstDay: 1
            },
            'Ranges': {
                '明天':[moment().add('days', 1), moment().add('days', 1)],
                '未來七天':  [moment().add('days', 1), moment().add('days', 7)]
            }
        },
        'en-us': {
            'Locale': {
                applyLabel: 'Apply',
                cancelLabel: 'Cancel',
                fromLabel: 'From',
                toLabel: 'To',
                weekLabel: 'W',
                customRangeLabel: 'Custom Range',
                firstDay: 1
            },
            'Ranges': {
            	 'Tomorrow':[moment().add('days', 1), moment().add('days', 1)],
                 'The next 7 days':  [moment().add('days', 1), moment().add('days', 7)]
            }
        }
    };


    return {
        restrict: 'AC',
        scope: {
            startDate: '=startDateModel',
            endDate: '=endDateModel',
            needMaxDate:'@'
        },
        link: function($scope, element, attrs) {
            
            $scope.$watch('startDate',function(n,o){
                if(!n){
                    element.val("");
                }
            });

            if(!$scope.needMaxDate){
                element.daterangepicker({
                    locale: rangepickerLocale[$scope.$root.language].Locale,
                    format: 'YYYY-MM-DD',
                    separator: ' to ',
                    showDropdowns: true,
                    startDate: moment(),
                    endDate: moment(),
                    ranges: rangepickerLocale[$scope.$root.language].Ranges
                }, function(startDate, endDate) {
                    $scope.startDate = startDate.format('YYYY-MM-DD');
                    $scope.endDate = endDate.format('YYYY-MM-DD');
                    $scope.$apply();
                }).prev().on(ace.click_event, function() {
                    $(this).next().focus();
                });
            }else{
                element.daterangepicker({
                    locale: rangepickerLocale[$scope.$root.language].Locale,
                    format: 'YYYY-MM-DD',
                    separator: ' to ',
                    showDropdowns: true,
                    startDate: moment(),
                    endDate: moment(),
                    ranges: rangepickerLocale[$scope.$root.language].Ranges
                }, function(startDate, endDate) {
                    $scope.startDate = startDate.format('YYYY-MM-DD');
                    $scope.endDate = endDate.format('YYYY-MM-DD');
                    $scope.$apply();
                }).prev().on(ace.click_event, function() {
                    $(this).next().focus();
                });
            }

           





        }


    }
});