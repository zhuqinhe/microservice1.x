'use strict';
/* Controllers */
angular.module('myApp.controllers')
.controller('schedulerJobListControllers', ['$scope','$http','$routeParams','$location','$log','$window','sharedProperties','loginService','$translate','$rootScope','$cookieStore','commonMethod',function($scope, $http, $routeParams, $location, $log,$window, sharedProperties, loginService,$translate,$rootScope,$cookieStore,commonMethod) {
   
	var serverip=sharedProperties.getServerUrl();

    $scope.savePageOptions = function() {// 点击修改或者媒体内容按钮保存当前的查询条件及分页参数
    	$rootScope.schdljob_jobgroup = $scope.jobgroup;
    };
    
    $(".expiredTimepicker").datetimepicker({
		showSecond: true,
		timeFormat: 'HH:mm:ss',
		changeMonth: true,
		changeYear: true,
		dateFormat: 'yy-mm-dd'
    });
    
    $scope.init = function() {
    	 $scope.checkAll=false;
    	//创建页面的标签页  ，后台获取对应的定时器组（需标签模块配合）
    //	1:cms分发  2：cdn 分发  3 :bms分发 4:epg  5  TVGW&TCGS 6：c2注入   7：c1注入  8：c3注入   9：其他  10:DRM  11:SeeLib  12:EPGTEMPLATE
    	 $scope.Gorup=[{'key':1,"name":'UDC'}
    	               ];
    	
    	if($rootScope.schdljob_jobgroup!=null||$rootScope.schdljob_jobgroup!=undefined){
    		$scope.jobgroup=$rootScope.schdljob_jobgroup;
    	}
    	else{
    		$scope.jobgroup=1;
    	}
    	 $scope.changeGorup($scope.jobgroup);
		
    };
    
    $scope.changeGorup=function(jobgroup){
    	$scope.checkAll=false;
    	$scope.jobgroup=jobgroup;
    	$scope.getPageData();
    }
    $scope.getPageData = function() {
    	
    	
        $scope.url = sharedProperties.getServerUrl()+"/ui/v1/sys/schdljobmng/list?jobgroup="+$scope.jobgroup
    	$http.get($scope.url).success(function (largeLoad) {
            $scope.items = largeLoad.list;
            
        });
    };
    
   
    
    $scope.selectAll = function(){
        angular.forEach($scope.items, function(item) {
            item.checked = $scope.checkAll;
        });
    };
    // select a row
    $scope.seledCunt = 0;
    $scope.selectRow = function(item) {
    	$scope.selectItem = item;
    	console.info("selectItem: " + $scope.selectItem.jobName);
    }
    
    $scope.isSelected=function(){
			var isSelected = false;
			angular.forEach($scope.items, function(item) {
				if(true == item.checked){
					isSelected = true;
				}
			});
			return !isSelected;
	 	}
   
    $scope.selected = function() {
    	var cnt = 0;
    	angular.forEach($scope.items, function(item) {
				if(true == item.checked){
					cnt++;
					if (cnt == 1) {
						$scope.seledId = item.jobName;
					} else if (cnt > 1) {
						return false;
					}
				}
    	});
    	return cnt != 1 ? true : false;
    };
    $scope.selectedd = function() {
    	var cnt = 0;
    	angular.forEach($scope.items, function(item) {
				if(true == item.checked){
					cnt++;
					if (cnt == 1) {
						$scope.seledId = item.jobName;
					} else if (cnt > 1) {
						return false;
					}
				}
    	});
    	return cnt > 0 ? false : true;
    };
	
    // 判断是否允许修改
    $scope.selectModifyFlag = function() {
    	var count=0;
    	$scope.singleCheckItem; 
    	for(var i=0;i<$scope.items.length;i++){
    		var item=$scope.items[i];
    		if(item.checked){
    			count++;
    			$scope.singleCheckItem=item;
    		}
    	}
    	if(count==1){
    		if($scope.singleCheckItem.status==12){
    			return false;// 不允许修改
    		}else{
    			return true;
    		}
    	}else{
    		return false;
    	}
    };
    // 是否只有一个选中
    $scope.selectSingleFlag=function(){
    	var count=0;
    	for(var i=0;i<$scope.items.length;i++){
    		var item=$scope.items[i];
    		if(item.checked){
    			count++;
    		}
    	}
    	if(count==1){
    		return true;
    	}else{
    		return false;
    	}
    }
    $scope.toEnable=function(id,event){
         	$scope.status = $('#enableChckbox_'+id).is(":checked");
         	event.preventDefault();
         	/*if($scope.status) {
         		$scope.message = $rootScope.l10n.msg_cnfrm_start;
         	}else {
         		$scope.message = $rootScope.l10n.msg_cnfrm_stop;
         	}*/
         //	commonMethod.confirmDialog($scope.message).then(function(value) {
         		$http["post"](sharedProperties.getServerUrl()+"/ui/v1/sys/schdljobmng/enable/"+id)
                .success(function(data,status){
                	if(data.resultCode==0){
                		 $scope.getPageData();
                	}
                	else{
                		commonMethod.tipDialog($translate.instant("operateFailed"));
                	}
                }).error(function() {
                	 commonMethod.tipDialog($translate.instant("systemError"));
                });
         //	});
    };
    $scope.batchEnable=function(status){
    var	ids=[];
    	angular.forEach($scope.items, function(item) {
			if(true == item.checked){
				ids.push(item.jobName);
			}
		});
     	$http["post"](sharedProperties.getServerUrl()+"/ui/v1/sys/schdljobmng/bathenable?status="+status+"&ids="+ids)
            .success(function(data,status){
            	if(data.resultCode==0){
            		$scope.checkAll=false;
            		 $scope.getPageData();
            	}
            	else{
            		$scope.checkAll=false;
            		 $scope.getPageData();
            	}
            }).error(function() {
            	 commonMethod.tipDialog($translate.instant("systemError"));
            });
     //	});
}
	
    $scope.init();// init
}])
.controller('schedulerJobEditControllers',['$scope','$routeParams','$log', '$http','sortKey','$location','$timeout','sharedProperties','fileUpload','$rootScope','$q','$interval','commonMethod',function($scope, $routeParams,$log, $http,sortKey, $location,$timeout,sharedProperties,fileUpload,$rootScope,$q,$interval,commonMethod) {
   

    
    $scope.reviewFlag=$rootScope.reviewFlag;
    $scope.id =  $routeParams.id;
    
    
    $scope.showMsg = function() {
    	$( "#dialog-confirm" ).removeClass('hide').dialog({
            resizable: false,
            modal: true,
            title: $rootScope.l10n.msg,
            title_html: true,
            close:function(){
	            	 $( this ).dialog( "destroy" );
	            	 $(this).addClass('hide');
	            },
            buttons: [
                {
                	html: '<img src="img/btn_x.png"></img>'+$rootScope.l10n.close,
                    "class" : "btn btn-fsw-cancel btn-xs bigger-110",
                    click: function() {
                        $( this ).dialog( "close" );
                    }
                }
            ]
        });
    }
    /*
     * if(!
     * (/\.(avi|flv|mkv|mp4|rmvb|mkv|wmv|rm|mpg|mepg|mov|ts|m3u8)$/i).test(file.name)){
     * alert($rootScope.l10n.pleaseSelectVideo); return false; };
     */

    $scope.tree="";
    $scope.formattedCategory=[];
    $scope.init = function() {
    	 $scope.Gorup=[{'key':1,"name":'UDC'}];
		$("#test").hide();
		$http.get(sharedProperties.getServerUrl()+"/ui/v1/sys/schdljobmng/detail/" + $scope.id).success(function (largeLoad) {
        	$scope.item = largeLoad.vo;
        	console.info("SchedulerJobVO");
        	console.info($scope.item);
        	$scope.exprsndmns = [];
            $scope.exprsndmns.push({name:$rootScope.l10n['cronsecond'],value:'s'});
            $scope.exprsndmns.push({name:$rootScope.l10n['cronminute'],value:'mi'});
            $scope.exprsndmns.push({name:$rootScope.l10n['cronhour'],value:'h'});
            $scope.exprsndmns.push({name:$rootScope.l10n['dayofmon'],value:'dofm'});
            $scope.exprsndmns.push({name:$rootScope.l10n['mon'],value:'m'});
            $scope.exprsndmns.push({name:$rootScope.l10n['dayofweek'],value:'dofw'});
            $scope.seledDomain = 's';
            $scope.dmnvals = ['s','mi','h','dofm','m','dofw'];
            $scope.dmnminvals = [0,0,0,0,1,1];
            $scope.dmnmaxvals = [59,59,23,31,12,7];
            $scope.dmnMinVal = 0;
            $scope.dmnMaxVal = 59;
//            $scope.ptrns = [
//                            ['/^(\\d|[1-5]\\d)(\\,(\\d|[1-5]\\d))*$/', '/^(\\d|[1-5]\\d)\\-(\\d|[1-5]\\d)$/', '', '/^(\d|[1-5]\d)\/(\d|[1-5]\d)'],
//                            ['/^(\\d|[1-5]\\d)(\\,(\\d|[1-5]\\d))*$/', '/^(\d|[1-5]\d)\-(\d|[1-5]\d)', '', '/^(\d|[1-5]\d)\/(\d|[1-5]\d)'],
//                            ['/^(\\d|1\\d|2[0-3])(\\,(\\d|1\\d|2[0-3]))*$/', '/^(\d|1\d|2[0-3])\-(\d|1\d|2[0-3])', '', '/^(\d|1\d|2[0-3])\/(\d|1\d|2[0-3])'],
//                            ['/^(\\d|[1-2]\\d|3[0-1])(\\,(\\d|[1-2]\\d|3[0-1]))*$/', '/^(\\d|[1-2]\\d|3[0-1])\-(\\d|[1-2]\\d|3[0-1])', '', '/^(\d|[1-2]\d|3[0-1])\/(\d|[1-2]\d|3[0-1])', '', '/^(\d|[1-2]\d|3[0-1])\L', '/^(\d|[1-2]\d|3[0-1])\W', '/^(\d|[1-2]\d|3[0-1])\C'],
//                            ['/^([1-9]|1[0-2])(\\,([1-9]|1[0-2]))*$/', '([1-9]|1[0-2])\-([1-9]|1[0-2])', '', '([1-9]|1[0-2])\/([1-9]|1[0-2])'],
//                            ['/^[1-7](\\,[1-7])*$/', '[1-7]\-[1-7]$/', '', '[1-7]\/[1-7]', '', '[1-7]\L', '[1-7]\C', '[1-7]\#[1-5]']
//            ];
//            $scope.vlptrns = ['/^(\\d|[1-5]\\d)(\\,(\\d|[1-5]\\d))*$/', '/^(\\d|[1-5]\\d)(\\,(\\d|[1-5]\\d))*$/', '/^(\\d|1\\d|2[0-3])(\\,(\\d|1\\d|2[0-3]))*$/', '/^(\\d|[1-2]\\d|3[0-1])(\\,(\\d|[1-2]\\d|3[0-1]))*$/', '/^([1-9]|1[0-2])(\\,([1-9]|1[0-2]))*$/', '/^[1-7](\\,[1-7])*$/'];
            $scope.vlform = {name:$rootScope.l10n['valuelist'], value:'vl'};// ,
            $scope.rgform = {name:$rootScope.l10n['range'], value:'rg'};// -
            $scope.wcform = {name:$rootScope.l10n['wildcard'], value:'wc'};//*
            $scope.wc1form = {name:$rootScope.l10n['wildcard1'], value:'wc1'};//?
            $scope.biform = {name:$rootScope.l10n['beginInterval'], value:'bi'};// /
            $scope.lstform = {name:$rootScope.l10n['lst'], value:'lst'};//L
            $scope.wdform = {name:$rootScope.l10n['workday'], value:'wd'};//W
            $scope.cldform = {name:$rootScope.l10n['calendar'], value:'cld'};//C
            $scope.odform = {name:$rootScope.l10n['ordinal'], value:'od'};//#
            $scope.basicFormArray = [$scope.vlform,$scope.rgform,$scope.wcform,$scope.biform];
            $scope.dofmFormArray = [$scope.vlform,$scope.rgform,$scope.wcform,$scope.biform,$scope.wc1form,$scope.lstform,$scope.wdform,$scope.cldform];
            $scope.dofwFormArray = [$scope.vlform,$scope.rgform,$scope.wcform,$scope.biform,$scope.wc1form,$scope.lstform,$scope.cldform,$scope.odform];
            $scope.formList = [
                                   [$scope.vlform,$scope.rgform,$scope.wcform,$scope.biform],
                                   [$scope.vlform,$scope.rgform,$scope.wcform,$scope.biform],
                                   [$scope.vlform,$scope.rgform,$scope.wcform,$scope.biform],
                                   [$scope.vlform,$scope.rgform,$scope.wcform,$scope.biform,$scope.wc1form,$scope.lstform,$scope.wdform,$scope.cldform],
                                   [$scope.vlform,$scope.rgform,$scope.wcform,$scope.biform],
                                   [$scope.vlform,$scope.rgform,$scope.wcform,$scope.biform,$scope.wc1form,$scope.lstform,$scope.cldform,$scope.odform]
            ];
//            $scope.formValueList = [
//                                    	['vl', 'rg', 'wc', 'bi'],
//                                    	['vl', 'rg', 'wc', 'bi'],
//                                    	['vl', 'rg', 'wc', 'bi'],
//                                    	['vl', 'rg', 'wc', 'bi', 'wc1', 'lst', 'wd', 'cld'],
//                                    	['vl', 'rg', 'wc', 'bi'],
//                                    	['vl', 'rg', 'wc', 'bi', 'wc1', 'lst', 'cld', 'od']
//                                    ];
            $scope.dsplayforms = [$scope.vlform,$scope.rgform,$scope.wcform,$scope.biform];
//            $scope.dsplayFormValues = ['vl', 'rg', 'wc', 'bi'];
            $scope.seledForm = 'vl';// $scope.vlform
//            ng-pattern='/^([0-9]|[1-5][0-9])(\,([0-9]|[1-5][0-9]))*$/'
//            $('#vlinput').attr('ng-pattern', '/^([0-9]|[1-5][0-9])(\\,([0-9]|[1-5][0-9]))*$/');
            $scope.vlValue = '';
            $scope.oldvlValue = '';
//            $scope.oldrvlValue = 59;
            $scope.oldSeledForm = 'vl';
            $scope.sValue = '*', $scope.miValue = '*', $scope.hValue = '*', $scope.dofmValue = '*', $scope.mValue = '*', $scope.dofwValue = '?';
            console.log("job",$scope.item);
           //$scope.item.newExprsn = $scope.item.cronExpression;
            $scope.newExprsn = $scope.item.cronExpression;
            $('#cronString').val($scope.newExprsn);
		});
    };

    $scope.changeDomain = function() {
    	$scope.domainIndx = $scope.dmnvals.indexOf($scope.seledDomain);
    	$scope.dmnMinVal = $scope.dmnminvals[$scope.domainIndx];
    	$scope.dmnMaxVal = $scope.dmnmaxvals[$scope.domainIndx];
    	if ($scope.domainIndx == 0 || $scope.domainIndx == 1 || $scope.domainIndx == 2 || $scope.domainIndx == 4) {
    		$scope.dsplayforms = $scope.basicFormArray;
    	} else if ($scope.domainIndx == 3) {
    		$scope.dsplayforms = $scope.dofmFormArray;
    	} else if ($scope.domainIndx == 5) {
    		$scope.dsplayforms = $scope.dofwFormArray;
    	}

    	$scope.seledForm = $scope.dsplayforms[0].value;
    	$scope.vlValue = "";

    }
    
    $scope.saveOldValue = function() {
    	console.info($scope.vlValue);
    	if ($scope.seledForm == 'vl') {
    		$scope.oldvlValue = $scope.vlValue;
		}
//    	$scope.oldSeledForm = $scope.seledForm;
    }
    
    $scope.saveDomainValue = function() {
    	var tmpValue = '';
    	switch ($scope.seledForm) {
		case 'vl':
			tmpValue = $scope.vlValue;
//			$scope.sValue = $scope.lrgValue + '/' + $scope.rrgValue;
			break;
		case 'rg':
			tmpValue = $scope.lrgValue + '-' + $scope.rrgValue;
			break;
		case 'wc':
			tmpValue = '*';
			break;
		case 'bi':
			tmpValue = $scope.lbiValue + '/' + $scope.rbiValue;
			break;
		case 'wc1':
			tmpValue = '?';
			break;
		case 'lst':
			tmpValue = $scope.lstValue + 'L';
			break;
		case 'wd':
			tmpValue = $scope.wdValue + 'W';
			break;
		case 'cld':
			tmpValue = $scope.cldValue + 'C';
			break;
		case 'od':
			tmpValue = $scope.lodValue + '#' + $scope.rodValue;
			break;
		default:
			break;
		}
    	switch ($scope.seledDomain) {
		case 's':
			$scope.sValue = tmpValue;
			break;
		case 'mi':
			$scope.miValue = tmpValue;
			break;
		case 'h':
			$scope.hValue = tmpValue;
			break;
		case 'dofm':
			$scope.dofmValue = tmpValue;
			break;
		case 'm':
			$scope.mValue = tmpValue;
			break;
		case 'dofw':
			$scope.dofwValue = tmpValue;
			break;
		default:
			break;
		}
    	$scope.item.newExprsn = $scope.sValue + ' ' + $scope.miValue + ' ' + $scope.hValue + ' ' + $scope.dofmValue + ' ' + $scope.mValue + ' ' + $scope.dofwValue;
    }
    
    
    $scope.changeForm = function() {
    	console.info($scope.seledForm);
    	if ($scope.seledForm == 'vl') {
			$scope.vlValue = $scope.oldvlValue;
		}
    	$scope.saveDomainValue();

    }
    
    $scope.checkRGNum = function(left) {
    	if (left) {
    		if( $scope.lrgValue > $scope.dmnMaxVal ) {
    			$scope.lrgValue = $scope.dmnMaxVal;
    		} else if ($scope.lrgValue == undefined || $scope.lrgValue < $scope.dmnMinVal) {
    			$scope.lrgValue = $scope.dmnMinVal;
    		}
    	} else {
    		if( $scope.rrgValue > $scope.dmnMaxVal ) {
    			$scope.rrgValue = $scope.dmnMaxVal;
    		} else if ($scope.rrgValue == undefined || $scope.rrgValue < $scope.dmnMinVal) {
    			$scope.lrgValue = $scope.dmnMinVal;
    		}
    	}
    }
    
    $scope.checkBINum = function(left) {
    	if (left) {
    		if( $scope.lbiValue > $scope.dmnMaxVal ) {
    			$scope.lbiValue = $scope.dmnMaxVal;
    		} else if ($scope.lbiValue == undefined || $scope.lbiValue < $scope.dmnMinVal) {
    			$scope.lbiValue = $scope.dmnMinVal;
    		}
    	} else {
    		if( $scope.rbiValue > $scope.dmnMaxVal ) {
    			$scope.rbiValue = $scope.dmnMaxVal;
    		} else if ($scope.rbiValue == undefined || $scope.rbiValue < $scope.dmnMinVal) {
    			$scope.rbiValue = $scope.dmnMinVal;
    		}
    	}
    }
    
    $scope.checkLSTNum = function() {
    	if( $scope.lstValue > $scope.dmnMaxVal ) {
    		$scope.lstValue = $scope.dmnMaxVal;
    	} else if ($scope.lstValue == undefined || $scope.lstValue < $scope.dmnMinVal) {
    		$scope.lstValue = $scope.dmnMinVal;
    	}
    }

    $scope.checkWDNum = function() {
    	if( $scope.wdValue > $scope.dmnMaxVal ) {
    		$scope.wdValue = $scope.dmnMaxVal;
    	} else if ($scope.wdValue == undefined || $scope.wdValue < $scope.dmnMinVal) {
    		$scope.wdValue = $scope.dmnMinVal;
    	}
    }

    $scope.checkCLDNum = function() {
    	if( $scope.cldValue > $scope.dmnMaxVal ) {
    		$scope.cldValue = $scope.dmnMaxVal;
    	} else if ($scope.cldValue == undefined || $scope.cldValue < $scope.dmnMinVal) {
    		$scope.cldValue = $scope.dmnMinVal;
    	}
    }

    $scope.checkODNum = function(left) {
    	if (left) {
    		if( $scope.lodValue > $scope.dmnMaxVal ) {
    			$scope.lodValue = $scope.dmnMaxVal;
    		} else if ($scope.lodValue == undefined || $scope.lodValue < $scope.dmnMinVal) {
    			$scope.lodValue = $scope.dmnMinVal;
    		}
    	} else {
    		if( $scope.rodValue > $scope.dmnMaxVal ) {
    			$scope.rodValue = $scope.dmnMaxVal;
    		} else if ($scope.rodValue == undefined || $scope.rodValue < $scope.dmnMinVal) {
    			$scope.rodValue = $scope.dmnMinVal;
    		}
    	}
    }
    
    $scope.initFlag = false;
    

    // 提交保存
    $scope.saveItem = function() {
    	$scope.newExprsn = $('#cronString').val();
    	var result = $scope.validateCronExpression($scope.newExprsn);
		if (result == true) {
			//alert("格式正确");
			//commonMethod.tipDialog($rootScope.l10n.correctFormat);
		} else {
			commonMethod.tipDialog($rootScope.l10n.errorFormat);
			return ;
		}
    	
    	//console.log("exprsn",$scope.newExprsn);
    	var data = {"cronExpression": $scope.newExprsn,"jobName":$scope.item.jobName,"id":$scope.item.id,"description":$scope.item.description,"status":$scope.item.status,"jobGroup":$scope.item.jobGroup};
    	$http.put(sharedProperties.getServerUrl()+'/ui/v1/sys/schdljobmng/edit',data).success(function(result) {
        	if (result.resultCode != 0) {
						$scope.message = $rootScope.l10n.saveFail;
						commonMethod.tipDialog($scope.message);
					} else {
						$location.path('/sys/schedulejob/list/');
					}
        });
    	
    };
    
    $scope.reRest=function(){
    	 $scope.init();
    };
    $scope.init();
    
    /**
     * 定时表达式校验
     */
	$scope.cronValidate = function() {
		var cron =  $('#cronString').val();
		var result = $scope.validateCronExpression(cron);
		if (result == true) {
			//alert("格式正确");
			commonMethod.tipDialog($rootScope.l10n.correctFormat);
		} else {
			commonMethod.tipDialog($rootScope.l10n.errorFormat);
		}
		return $scope.validateCronExpression(cron);
	}

	$scope.validateCronExpression = function(value) {
		var results = true;
		if (value == null || value.length == 0) {
			return false;
		}

		// split and test length
		var expressionArray = value.split(" ");
		console.log("expressionArray",expressionArray);
		var len = expressionArray.length;
		if ((len != 6) && (len != 7)) {
			return false;
		}

		// check only one question mark
		var match = value.match(/\?/g);
		if (match != null && match.length > 1) {
			return false;
		}

		// check only one question mark
		var dayOfTheMonthWildcard = "";

		// if appropriate length test parts
		// [0] Seconds 0-59 , - * /
		if ($scope.isNotWildCard(expressionArray[0], /[\*]/gi)) {
			if (!$scope.segmentValidator("([0-9\\\\,-\\/])",
					expressionArray[0], [0, 59], "seconds")) {
				return false;
			}
		}

		// [1] Minutes 0-59 , - * /
		if ($scope.isNotWildCard(expressionArray[1], /[\*]/gi)) {
			if (!$scope.segmentValidator("([0-9\\\\,-\\/])",
					expressionArray[1], [0, 59], "minutes")) {
				return false;
			}
		}

		// [2] Hours 0-23 , - * /
		if ($scope.isNotWildCard(expressionArray[2], /[\*]/gi)) {
			if (!$scope.segmentValidator("([0-9\\\\,-\\/])",
					expressionArray[2], [0, 23], "hours")) {
				return false;
			}
		}

		// [3] Day of month 1-31 , - * ? / L W C
		if ($scope.isNotWildCard(expressionArray[3],
				/[\*\?]/gi)) {
			if (!$scope.segmentValidator(
					"([0-9LWC\\\\,-\\/])", expressionArray[3], [1, 31],
					"days of the month")) {
				return false;
			}
		} else {
			dayOfTheMonthWildcard = expressionArray[3];
		}

		// [4] Month 1-12 or JAN-DEC , - * /
		if ($scope.isNotWildCard(expressionArray[4], /[\*]/gi)) {
			expressionArray[4] = $scope
					.convertMonthsToInteger(expressionArray[4]);
			if (!$scope.segmentValidator("([0-9\\\\,-\\/])",
					expressionArray[4], [1, 12], "months")) {
				return false;
			}
		}

		// [5] Day of week 1-7 or SUN-SAT , - * ? / L C #
		if ($scope.isNotWildCard(expressionArray[5],
				/[\*\?]/gi)) {
			expressionArray[5] = $scope.convertDaysToInteger(expressionArray[5]);
			if (!$scope.segmentValidator(
					"([0-9LC#\\\\,-\\/])", expressionArray[5], [1, 7],
					"days of the week")) {
				return false;
			}
		} else {
			if (dayOfTheMonthWildcard == String(expressionArray[5])) {
				return false;
			}
		}

		// [6] Year empty or 1970-2099 , - * /
		if (len == 7) {
			if ($scope.isNotWildCard(expressionArray[6],
					/[\*]/gi)) {
				if (!$scope.segmentValidator(
						"([0-9\\\\,-\\/])", expressionArray[6], [1970, 2099],
						"years")) {
					return false;
				}
			}
		}
		return true;
	}

	// ----------------------------------
	// isNotWildcard 静态方法;
	// ----------------------------------
	$scope.isNotWildCard = function(value, expression) {
		var match = value.match(expression);
		return (match == null || match.length == 0) ? true : false;
	}

	// ----------------------------------
	// convertDaysToInteger 静态方法;
	// ----------------------------------
	$scope.convertDaysToInteger = function(value) {
		var v = value;
		v = v.replace(/SUN/gi, "1");
		v = v.replace(/MON/gi, "2");
		v = v.replace(/TUE/gi, "3");
		v = v.replace(/WED/gi, "4");
		v = v.replace(/THU/gi, "5");
		v = v.replace(/FRI/gi, "6");
		v = v.replace(/SAT/gi, "7");
		return v;
	}

	// ----------------------------------
	// convertMonthsToInteger 静态方法;
	// ----------------------------------
	$scope.convertMonthsToInteger = function(value) {
		var v = value;
		v = v.replace(/JAN/gi, "1");
		v = v.replace(/FEB/gi, "2");
		v = v.replace(/MAR/gi, "3");
		v = v.replace(/APR/gi, "4");
		v = v.replace(/MAY/gi, "5");
		v = v.replace(/JUN/gi, "6");
		v = v.replace(/JUL/gi, "7");
		v = v.replace(/AUG/gi, "8");
		v = v.replace(/SEP/gi, "9");
		v = v.replace(/OCT/gi, "10");
		v = v.replace(/NOV/gi, "11");
		v = v.replace(/DEC/gi, "12");
		return v;
	}

	// ----------------------------------
	// segmentValidator 静态方法;
	// ----------------------------------
	$scope.segmentValidator = function(expression, value,
			range, segmentName) {
		var v = value;
		var numbers = new Array();

		// first, check for any improper segments
		var reg = new RegExp(expression, "gi");
		if (!reg.test(v)) {
			return false;
		}

		// check duplicate types
		// check only one L
		var dupMatch = value.match(/L/gi);
		if (dupMatch != null && dupMatch.length > 1) {
			return false;
		}

		// look through the segments
		// break up segments on ','
		// check for special cases L,W,C,/,#,-
		var split = v.split(",");
		var i = -1;
		var l = split.length;
		var match;

		while (++i < l) {
			// set vars
			var checkSegment = split[i];
			var n;
			var pattern = /(\w*)/;
			match = pattern.exec(checkSegment);

			// if just number
			pattern = /(\w*)\-?\d+(\w*)/;
			match = pattern.exec(checkSegment);

			if (match
					&& match[0] == checkSegment
					&& checkSegment.indexOf("L") == -1
					&& checkSegment.indexOf("l") == -1
					&& checkSegment.indexOf("C") == -1
					&& checkSegment.indexOf("c") == -1
					&& checkSegment.indexOf("W") == -1
					&& checkSegment.indexOf("w") == -1
					&& checkSegment.indexOf("/") == -1
					&& (checkSegment.indexOf("-") == -1 || checkSegment
							.indexOf("-") == 0)
					&& checkSegment.indexOf("#") == -1) {
				n = match[0];

				if (n && !(isNaN(n)))
					numbers.push(n);
				else if (match[0] == "0")
					numbers.push(n);
				continue;
			}
			// includes L, C, or w
			pattern = /(\w*)L|C|W(\w*)/i;
			match = pattern.exec(checkSegment);

			if (match
					&& match[0] != ""
					&& (checkSegment.indexOf("L") > -1
							|| checkSegment.indexOf("l") > -1
							|| checkSegment.indexOf("C") > -1
							|| checkSegment.indexOf("c") > -1
							|| checkSegment.indexOf("W") > -1 || checkSegment
							.indexOf("w") > -1)) {

				// check just l or L
				if (checkSegment == "L" || checkSegment == "l")
					continue;
				pattern = /(\w*)\d+(l|c|w)?(\w*)/i;
				match = pattern.exec(checkSegment);

				// if something before or after
				if (!match || match[0] != checkSegment) {
					continue;
				}

				// get the number
				var numCheck = match[0];
				numCheck = numCheck.replace(/(l|c|w)/ig, "");

				n = Number(numCheck);

				if (n && !(isNaN(n)))
					numbers.push(n);
				else if (match[0] == "0")
					numbers.push(n);
				continue;
			}

			var numberSplit;

			// includes /
			if (checkSegment.indexOf("/") > -1) {
				// take first #
				numberSplit = checkSegment.split("/");

				if (numberSplit.length != 2) {
					continue;
				} else {
					n = numberSplit[0];

					if (n && !(isNaN(n)))
						numbers.push(n);
					else if (numberSplit[0] == "0")
						numbers.push(n);
					continue;
				}
			}

			// includes #
			if (checkSegment.indexOf("#") > -1) {
				// take first #
				numberSplit = checkSegment.split("#");

				if (numberSplit.length != 2) {
					continue;
				} else {
					n = numberSplit[0];

					if (n && !(isNaN(n)))
						numbers.push(n);
					else if (numberSplit[0] == "0")
						numbers.push(n);
					continue;
				}
			}

			// includes -
			if (checkSegment.indexOf("-") > 0) {
				// take both #
				numberSplit = checkSegment.split("-");

				if (numberSplit.length != 2) {
					continue;
				} else if (Number(numberSplit[0]) > Number(numberSplit[1])) {
					continue;
				} else {
					n = numberSplit[0];

					if (n && !(isNaN(n)))
						numbers.push(n);
					else if (numberSplit[0] == "0")
						numbers.push(n);
					n = numberSplit[1];

					if (n && !(isNaN(n)))
						numbers.push(n);
					else if (numberSplit[1] == "0")
						numbers.push(n);
					continue;
				}
			}

		}
		i = -1;
		l = numbers.length;

		if (l == 0)
			return false;

		while (++i < l) {
			
			if (numbers[i] < range[0] || numbers[i] > range[1]) {
				return false;
			}
		}
		return true;
	}
}]);



