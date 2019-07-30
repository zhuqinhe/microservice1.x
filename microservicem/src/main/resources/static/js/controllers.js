'use strict';

/* Controllers */

angular.module('myApp.controllers', ['ngGrid'])
.controller('indexCtrl',['$log','loginService','$scope','$location', '$translate','$rootScope','sharedProperties','$http','$window','$q','$interval','$timeout','commonMethod',function($log,loginService, $scope, $location, $translate,$rootScope,sharedProperties,$http,$window,$q,$interval,$timeout,commonMethod) {
		if(!loginService.isLogin()){
			loginService.gotoLoginPage();
			return false;
		};
    
	    //获取权限列表
	    $rootScope.rootPris = sessionStorage.getItem("rootPris");;
	    console.info($rootScope.rootPris);
	    console.info("quanxian");
		$scope.user = sessionStorage.getItem("username");
		$rootScope.role = sessionStorage.getItem("role");
		$rootScope.username = sessionStorage.getItem("username");
		$rootScope.language= sessionStorage.getItem("language");
		setClass();
		$translate.use(sessionStorage.getItem("language"));
		setLang(sessionStorage.getItem("language"));
		$scope.exit = function() {
			loginService.logout();
		};
		$scope.showVersion = function() {
			$scope.menuType = '';
			$('.nav-menu ul.navbar-nav li a.open').removeClass('open');
			sessionStorage.removeItem("menuType");
			$location.path('/about');
		};
		$scope.showInformation = function() {
			$scope.menuType = '';
			$('.nav-menu ul.navbar-nav li a.open').removeClass('open');
			sessionStorage.removeItem("menuType");
			$location.path('/security/user/edit');
		};
		$scope.getLanguageJson = function(lang){
			// 修改时应同时修改js/service.js
			$http.get(sharedProperties.getServerUrl()+"/l10n/"+lang+".json")
			.success(function(data){
				$rootScope.l10n = data;
				
			});
		};
		
		$scope.changeLanguage = function(lang) {
			$translate.use(lang);
			$rootScope.language=lang;
			sessionStorage.setItem('language', $rootScope.language);
			$rootScope.movieTypes=movieTypes[$rootScope.language];
			$rootScope.flags=flags[$rootScope.language];
			setLang(lang);
			$scope.getLanguageJson(lang);
			setClass();
		};
      	// 刷新页面时，保持选中菜单高亮
      	$scope.initActiveMenu = function(type) {
      		$('.nav-menu .navbar-nav a').each(function() {
				var id = $(this).attr('data-id');
				if(id == $scope.menuType) {
					$(this).addClass('open');
					var currentUrl = window.location.hash
					$('#sidebar .submenu a').each(function() {
						var v = $(this).attr('href');
						if(v == currentUrl) {
							$(this).closest('li').addClass('active');
						}
					});
				}
			});
      	}

	    
	    function setClass(){
      		if($rootScope.language == "en-us"){
      			$rootScope.node_en = "node-operate-bar-marginLeft-110";	
      		}else {
      			$rootScope.node_en = "";	
      		}
      	}
	    
		$scope.init = function() {
			$rootScope.config = {
					"usePortal":"false",
					"useCheckWorkflow":"false",
					"posterPicSize":"328×454",
	    			"detailPicSize":"243×330"
	    	};
			$q.all([sharedProperties.getSysConfigInfo("languages_config"),sharedProperties.getSysConfigInfo("langs_config")]).then(function(results){
				console.log(results);
				$rootScope.languages = results[0].vo; 
				$rootScope.lang1 = results[1].vo.substring(0,results[1].vo.indexOf(","));
				$rootScope.lang2 = results[1].vo.substring(results[1].vo.indexOf(",")+1);
				$rootScope.lang11 = $rootScope.lang1.replace("-","_");
				$rootScope.lang22 = $rootScope.lang2.replace("-","_");
				$scope.getLanguageJson($rootScope.language);
			});
			$timeout(function() {
				$scope.initActiveMenu($scope.menuType);
			},200);
			
		};
		
		$scope.init();	

	}]);
function setLang(lang) {
	if (lang == 'zh-cn') {
		setzh();
	} else if (lang == 'zh-tw') {
		settw();
	} else {
		seten();
	}
}
function setzh() {
	$.datepicker.regional['zh-CN'] = {
			closeText: '关闭',
			prevText: '&#x3C;上月',
			nextText: '下月&#x3E;',
			currentText: '今天',
			monthNames: ['一月','二月','三月','四月','五月','六月',
			'七月','八月','九月','十月','十一月','十二月'],
			monthNamesShort: ['一月','二月','三月','四月','五月','六月',
			'七月','八月','九月','十月','十一月','十二月'],
			dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
			dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
			dayNamesMin: ['日','一','二','三','四','五','六'],
			weekHeader: '周',
			dateFormat: 'yy-mm-dd',
			firstDay: 1,
			isRTL: false,
			showMonthAfterYear: true,
			yearSuffix: '年'};
		$.datepicker.setDefaults($.datepicker.regional['zh-CN']); 
		$.timepicker.regional['zh-CN'] = {
				timeOnlyTitle: '选择时间',
				timeText: '时间',
				hourText: '小时',
				minuteText: '分钟',
				secondText: '秒钟',
				millisecText: '毫秒',
				microsecText: '微秒',
				timezoneText: '时区',
				currentText: '现在时间',
				closeText: '关闭',
				timeFormat: 'HH:mm',
				timeSuffix: '',
				amNames: ['AM', 'A'],
				pmNames: ['PM', 'P'],
				isRTL: false
		};
		$.timepicker.setDefaults($.timepicker.regional['zh-CN']); 
		// set jqgrid language
		jQuery.extend(jQuery.jgrid.defaults,{
			recordtext:"第{0}-{1}条,共{2}条",
			emptyrecords:"共找到记录:0",
			loadingtext:"加载中...",
			pgtext:"第 {0}/{1}页"
		});
		$(".ui-jqgrid-btable").jqGrid('setGridParam', {
			recordtext:"第{0}-{1}条,共{2}条",
			emptyrecords:"共找到记录:0",
			loadingtext:"加载中...",
			pgtext:"第 {0}/{1}页 ",
		}).trigger("reloadGrid");
		
}
function settw() {
	$.datepicker.regional['zh-TW'] = {
			closeText: '關閉',
			prevText: '&#x3C;上月',
			nextText: '下月&#x3E;',
			currentText: '今天',
			monthNames: ['一月','二月','三月','四月','五月','六月',
			'七月','八月','九月','十月','十一月','十二月'],
			monthNamesShort: ['一月','二月','三月','四月','五月','六月',
			'七月','八月','九月','十月','十一月','十二月'],
			dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
			dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
			dayNamesMin: ['日','一','二','三','四','五','六'],
			weekHeader: '周',
			dateFormat: 'yy-mm-dd',
			firstDay: 1,
			isRTL: false,
			showMonthAfterYear: true,
			yearSuffix: '年'};
		$.datepicker.setDefaults($.datepicker.regional['zh-TW']); 
		$.timepicker.regional['zh-TW'] = {
				timeOnlyTitle: '選擇時間',
				timeText: '時間',
				hourText: '小時',
				minuteText: '分鐘',
				secondText: '秒鐘',
				millisecText: '毫秒',
				microsecText: '微秒',
				timezoneText: '時區',
				currentText: '現在時間',
				closeText: '關閉',
				timeFormat: 'HH:mm',
				timeSuffix: '',
				amNames: ['AM', 'A'],
				pmNames: ['PM', 'P'],
				isRTL: false
		};
		$.timepicker.setDefaults($.timepicker.regional['zh-TW']); 
		// set jqgrid language
		jQuery.extend(jQuery.jgrid.defaults,{
			recordtext:"第{0}-{1}條,共{2}條",
			emptyrecords:"共找到記錄:0",
			loadingtext:"加載中...",
			pgtext:"第 {0}/{1}頁"
		});
		$(".ui-jqgrid-btable").jqGrid('setGridParam', {
			recordtext:"第{0}-{1}頁,共{2}條",
			emptyrecords:"共找到記錄:0",
			loadingtext:"加載中...",
			pgtext:"第 {0}/{1}頁 ",
		}).trigger("reloadGrid");
		
}

function seten() {
	$.datepicker.regional['en-US'] = {
			closeText: "Done", // Display text for close link
			prevText: "Prev", // Display text for previous month link
			nextText: "Next", // Display text for next month link
			currentText: "Today", // Display text for current month link
			monthNames: ["January","February","March","April","May","June",
				"July","August","September","October","November","December"], // Names of months for drop-down and formatting
			monthNamesShort: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"], // For formatting
			dayNames: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"], // For formatting
			dayNamesShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"], // For formatting
			dayNamesMin: ["Su","Mo","Tu","We","Th","Fr","Sa"], // Column headings for days starting at Sunday
			weekHeader: "Wk", // Column header for week of the year
			dateFormat: "mm/dd/yy", // See format options on parseDate
			firstDay: 0, // The first day of the week, Sun = 0, Mon = 1, ...
			isRTL: false, // True if right-to-left language, false if left-to-right
			showMonthAfterYear: false, // True if the year select precedes month, false for month then year
			yearSuffix: "" // Additional text to append to the year in the month headers
		};
		$.datepicker.setDefaults($.datepicker.regional['en-US']); 
		$.timepicker.regional['en-US'] = {
				currentText: 'Now',
				closeText: 'Done',
				amNames: ['AM', 'A'],
				pmNames: ['PM', 'P'],
				timeFormat: 'HH:mm',
				timeSuffix: '',
				timeOnlyTitle: 'Choose Time',
				timeText: 'Time',
				hourText: 'Hour',
				minuteText: 'Minute',
				secondText: 'Second',
				millisecText: 'Millisecond',
				microsecText: 'Microsecond',
				timezoneText: 'Time Zone',
				isRTL: false
		};
		$.timepicker.setDefaults($.timepicker.regional['en-US']); 
		// set jqgrid language
		jQuery.extend(jQuery.jgrid.defaults,{
			recordtext:"View {0}-{1} of {2}",
			emptyrecords:"No records to view",
			loadingtext:"Loading...",
			pgtext:"Page {0} of {1}"
		});
		$(".ui-jqgrid-btable").jqGrid('setGridParam', {
			recordtext:"View {0}-{1} of {2}",
			emptyrecords:"No records to view",
			loadingtext:"Loading...",
			pgtext:"Page {0} of {1}"
		}).trigger("reloadGrid");
}
function get_unix_time(dateStr)
{
	if (dateStr == null) {
		return null;
	} else if (!isNaN(dateStr)) {
		var newDate = new Date();
		newDate.setTime(dateStr);
		return newDate.toISOString();
	}
    var newstr = dateStr.replace(/-/g,'/'); 
    var date =  new Date(newstr); 
    var time_str = date.getTime().toString();
    return time_str;
}



