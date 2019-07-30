'use strict';

angular.module('myApp.controllers')
.controller('userdataCtrl', ['$rootScope','$scope','sharedProperties','$http','$log','loginService', '$routeParams','commonMethod','$q', function($rootScope,$scope,sharedProperties,$http,$log,loginService,$routeParams,commonMethod,$q) {

    $scope.savePageOptions=function() {//点击修改或者媒体内容按钮保存当前的查询条件及分页参数
        sessionStorage.setItem("parentLocation", window.location.href);
        $rootScope.userdata_pagingOptions = $scope.pagingOptions;
        $rootScope.userdata_queryOptions = $scope.queryOptions;
    };
    //do search
    $scope.search=function(){
    	$scope.selectItem=null;
    	$scope.pagingOptions.currentPage = 1;
        $scope.getPageData();
    };
    //reset search condition
    $scope.reset = function() {
    	$scope.selectItem=null;
        $scope.queryOptions = {// 查询字段
        		id: null
        };
        $scope.pagingOptions.currentPage = 1;
        $scope.getPageData();
    }

   
    $scope.getPageData = function() {
    	if ($scope.pagingOptions.currentPage == 0) {
			$scope.pagingOptions.currentPage = 1;
		}
    	var cpId=$scope.queryOptions.cpId;
		var name=$scope.queryOptions.name;
		if(cpId!=null&&cpId!=""){
			cpId=encodeURIComponent(cpId.replace("%","\\%"));
		}
		if(name!=null&&name!=""){
			name=encodeURIComponent(name.replace("%","\\%"));
		}
		
        $scope.url = sharedProperties.getServerUrl()+"/ui/v1/userid/list?userId="+encodeURIComponent($scope.queryOptions.id)+"&begin="+($scope.pagingOptions.currentPage-1)*$scope.pagingOptions.pageSize+"&pageSize="+$scope.pagingOptions.pageSize;
        $http({method:'GET',url:$scope.url}).success(function (largeLoad,status,headers,config) {
            $scope.pagingOptions.total = largeLoad.total;//total count
            $scope.pagingOptions.maxPages = Math.ceil($scope.pagingOptions.total/$scope.pagingOptions.pageSize);
            $scope.items = largeLoad.list;
            $scope.selectItem=null;
            $log.info(">>>>>items count:"+$scope.items.length);
            if($scope.items!=null){
                angular.forEach($scope.items, function(item) {
                    if(item.poster){
                        item.poster =sharedProperties.getFileUrl()+item.poster;
                    }
                });
            }
            
        });
    };
    
   
    $scope.orderProp = 'id';

    
    $scope.selected2 = function() {
        if ($scope.selectItem == null) {
            return true;
        } else {
        	return false;
        }
    };
    
    $scope.selected4review=function(){
    	if($scope.selectItem==null){
    		return true;
    	}else if($scope.selectItem.status!=8&&$scope.selectItem.status!=14){
    		return true;
    	}else if($scope.selectItem.objectStatus!=2){
    		return true;
    	}else{
    		return false;
    	}
    };//状态不为未发布的不可申请审核
      
    $scope.selectRow = function(item) {
        $scope.selectItem = item;
        item.checked = !item.checked;
        $scope.checkAll = true;
        angular.forEach($scope.items, function(item) {
            if(true != item.checked){
                $scope.checkAll = false;
                return;
            }
        });
    }
    $scope.selectAll = function() {
    	angular.forEach($scope.items, function(item) {
    		item.checked = $scope.checkAll;
    	})
    };
    //判断有无选中的item
    $scope.hasCheckedItem=function() {
    	var checkFlag = false;
    	angular.forEach($scope.items, function(item) {
    		if(item.checked == true) {
    			checkFlag = true;
    		}
    	})
    	return checkFlag;
    };
    //是否只有一个选中
    $scope.selectSingleFlag = function() {
    	var count = 0;
    	if($scope.items != null	){
    		for(var i=0; i<$scope.items.length; i++) {
        		var item = $scope.items[i];
        		if(item.checked) {
        			count++;
        		}
        	}
    	}
    	if(count == 1) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
    $scope.dodelete = function(deleteFlag) {
    	//deleteFlag为删除标记，1为在回收站中删除，0为在列表中删除
    	$scope.deleteFlag=deleteFlag;
       	$scope.message = $rootScope.l10n.areyousure;
       	var deleteDialog;
        if(deleteFlag == 0) {
        	deleteDialog = commonMethod.confirmDialog($scope.message);
        }else {
        	deleteDialog = commonMethod.confirmDialog($scope.message);
        }
        deleteDialog.then(function(value) {
       		var ids = [];
       		var i = 0;
    		$scope.items.forEach(function(item) {
    			if(item.checked == true) {
    				ids[i] = item.id;
    				i++
    			}
    		});
			var data={};
        	// 如有关联则不能删除
			//增加了删除标记，区分物理删除还是逻辑删除
    		data = {
    					"ids":ids,
    					}
                //删除标记位batchdelete,获取Delete的删除url
            var appDeleteUrl=sharedProperties.getServerUrl()+"/ui/v1/bookmark/delete";//delete的restful请求
            $http.post(appDeleteUrl,data).success(function(data){
                if(data.resultCode==0){
                    // $scope.init();
                    $scope.getPageData();
                    $scope.checkAll = false;
                }else{
                    $scope.message = $rootScope.l10n.epgDeleteFail;
                    commonMethod.tipDialog($scope.message);
                }
            });
       	});
    };

  	$scope.init = function() {
  		var editFlag = $routeParams.editFlag;//0:表示分页参数初始化; 1:使用之前的分页及搜索参数
		if (editFlag == "1" && $rootScope.userdata_pagingOptions) {
			$scope.pagingOptions = $rootScope.userdata_pagingOptions;
			$scope.queryOptions = $rootScope.userdata_queryOptions;
		} else {
			$scope.queryOptions = {// 查询字段
					id: null,
					cpId: "null",
	  		        name: null,
	  		        role: 'null',
	  		        gender: 'null'
	  		};
			$scope.pagingOptions = {
	  		        pageSizes: [10, 20, 30, 50, 100],
	  		        pageSize: 10,
	  		        currentPage: 1,
	  		        total: 0,//total count
	  		        maxPages: 0//max pages
			};
		}
		var lan = sessionStorage.getItem('language');
		$scope.selectItem = null;
		$scope.getPageData();
	};
	$scope.init();
}]);