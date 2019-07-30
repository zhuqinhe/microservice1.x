angular.module('myApp.directives').directive('fsvDaterangpicker',function(){
	
	var rangepickerLocale = {
        	'zh-cn':{
        		'Locale':{
        			applyLabel: '确定',
                    cancelLabel: '取消',
                    fromLabel: '从',
                    toLabel: '到',
                    weekLabel: '周',
                    customRangeLabel: '自定义范围',
                    firstDay: 1
        		},
        		'Ranges': {
                    '今天': [moment(), moment()],
                    '昨天': [moment().subtract('days', 1), moment().subtract('days', 1)],
                    '最近七天': [moment().subtract('days', 6), moment()]
        		},
        		'Ranges1': {
                    '今天': [moment(), moment()],
                    '近一周': [moment().subtract('days', 6), moment()],
                    '近一个月':[moment().subtract('days', 29), moment()],
                    '近半年':[moment().subtract('days', 365/2), moment()],
                    '近一年':[moment().subtract('days', 365), moment()]
        		},
        		'Ranges2': {
                    '近三天': [moment().subtract('days', 2), moment()],
                    '近七天': [moment().subtract('days', 6), moment()],
                    '近一个月':[moment().subtract('days', 29), moment()]
        		}
        	},
        	'zh-tw':{
        		'Locale':{
        			applyLabel: '確定',
                    cancelLabel: '取消',
                    fromLabel: '從',
                    toLabel: '到',
                    weekLabel: '周',
                    customRangeLabel: '自定義範圍',
                    firstDay: 1
        		},
        		'Ranges':{
        			'今天': [moment(), moment()],
                    '昨天': [moment().subtract('days', 1), moment().subtract('days', 1)],
                    '最近七天': [moment().subtract('days', 6), moment()]
        		}
        	},
        	'en-us':{
        		'Locale':{
        			 applyLabel: 'Apply',
                     cancelLabel: 'Cancel',
                     fromLabel: 'From',
                     toLabel: 'To',
                     weekLabel: 'W',
                     customRangeLabel: 'Custom Range',
                     firstDay: 1
        		},
        		'Ranges':{
        			'Today': [moment(), moment()],
                    'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
                    'Last 7 Days': [moment().subtract('days', 6), moment()],
        		}
        	}
        };
	
	
	return {
		  restrict: 'AC',
		  scope:{
			  startDate:'=startDateModel',
			  endDate:'=endDateModel'
		  },
		  link:function($scope, element, attrs){
		
			  element.daterangepicker(
						{
							locale : rangepickerLocale[$scope.$parent.language].Locale,
							format: 'YYYY/MM/DD',
							separator: ' to ',
							showDropdowns: true,
							startDate: moment(),
							endDate: moment(),
							maxDate: moment(),
							ranges: rangepickerLocale[$scope.$parent.language].Ranges
						},function(startDate,endDate){
							  $scope.startDate = startDate.format('YYYY-MM-DD');
							  $scope.endDate = endDate.format('YYYY-MM-DD');
							  $scope.$apply()
						 }).prev().on(ace.click_event, function(){
					$(this).next().focus();
				});	
			  
			
			  
			 
			  
		  }
		  
		
	}
});