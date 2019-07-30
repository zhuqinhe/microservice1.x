'use strict';

/* Controllers */

angular.module('myApp.controllers')
.controller('roleListCtrl', ['$rootScope','$scope', '$http', 'sharedProperties','loginService','$window','$routeParams','commonMethod',function($rootScope,$scope, $http, sharedProperties, loginService,$window,$routeParams,commonMethod) {
	$scope.getPageData = function() {
		if ($scope.pagingOptions.currentPage == 0) {
			$scope.pagingOptions.currentPage = 1;
		}
		var name=$scope.queryOptions.name;
		var url = sharedProperties.getServerUrl()+"/v1/security/role/list?id="+encodeURIComponent($scope.queryOptions.id)+"&name="+encodeURIComponent(name)+"&first="+($scope.pagingOptions.currentPage-1)*$scope.pagingOptions.pageSize+"&max="+$scope.pagingOptions.pageSize;
		$http.get(url).success(function (largeLoad) {
			console.log(largeLoad);
			$scope.pagingOptions.total = largeLoad.total;//存放list总记录数
			$scope.pagingOptions.maxPages = Math.ceil($scope.pagingOptions.total/$scope.pagingOptions.pageSize);
        	$scope.items = largeLoad.list;
        }).error(function(){
        	//alert("error!");
        });
	};
	// 搜索
	$scope.search = function() {
		$scope.pagingOptions.currentPage = 1;
		$scope.getPageData();
	};
	$scope.reset = function() {
		$scope.queryOptions = {//查询字段
				id: null,
				name: null
		};
		$scope.pagingOptions.currentPage = 1;
		$scope.getPageData();
	};
	//select a row
	$scope.selectRow = function(item) {
		$scope.selectItem = item;
	};
	
	$scope.username = sessionStorage.getItem('username');
	//判断是否选择某个记录
	$scope.selected = function() {
		if ($scope.selectItem == null) {
			return true;
		} 
		else if($scope.username == 'sysadmin'){
			return false;
		}
		else if($scope.selectItem.creator != $scope.username){
			return true;
		}else{
			return false;
		}
	};
	//判断是否选择某个记录
	$scope.selected_delete= function() {
		if($scope.username=='sysadmin'){
			return false;
		}
		else if ($scope.selectItem == null) {
			return true;
		} 
		else if($scope.selectItem.creator != $scope.username){
			return true;
		} 
		else {
			return false;
		}
	};
	
	$scope.changePrivi = function(privis) {
		var privis_name = "";
		if (privis != null && privis != "") {
			var privis_array = privis.split(",");
			for (var i = 0; i < privis_array.length; i++) {
				var privi = privis_array[i];
				if (i<privis_array.length-1) {
					privis_name += $rootScope.l10n[privi]+",";
				} else {
					privis_name += $rootScope.l10n[privi];
				}
			}
		}
		
		return privis_name;
	};
	
	
	$scope.deleteItem = function() {
		$scope.message = $rootScope.l10n.areyousure;
		commonMethod.confirmDialog($scope.message).then(function(value) {
			var ids = [$scope.selectItem.id];
        	$http["post"](sharedProperties.getServerUrl()+"/v1/security/role/remove",ids).success(function (data) {
        		if(data.resultCode != 0){
        			$scope.message = $rootScope.l10n.roleDeleteFail;
        			commonMethod.tipDialog($scope.message)
    		  	}
      			$scope.selectItem = null;
      			$scope.getPageData();
            });
		});
	}
	$scope.savePageOptions = function() {//点击修改或者媒体内容按钮保存当前的查询条件及分页参数
    	$rootScope.userg_pagingOptions = $scope.pagingOptions;
    	$rootScope.userg_queryOptions = $scope.queryOptions;
    };
	$scope.init = function() {
		var editFlag = $routeParams.editFlag;//0:表示分页参数初始化; 1:使用之前的分页及搜索参数
		if (editFlag == "1" && $rootScope.userg_pagingOptions) {
			$scope.pagingOptions = $rootScope.userg_pagingOptions;
			$scope.queryOptions = $rootScope.userg_queryOptions;
		} else {
			$scope.pagingOptions = {//分页字段
					pageSizes: [10, 20, 30, 50, 100],
					pageSize: 10,
				    currentPage: 1,
				    total: 0,//存放list总记录数
				    maxPages: 0,//最大页数
			};
			$scope.queryOptions = {//查询字段
					id: null,
					name: null
			};
		}
		$scope.selectItem = null;
		$scope.getPageData();
	};
	$scope.init();
}])
.controller('roleAddCtrl',['$scope', '$http', 'sharedProperties', '$routeParams', '$location', 'loginService', '$rootScope','commonMethod', function($scope, $http, sharedProperties,$routeParams,$location,loginService, $rootScope,commonMethod){
	$scope.saveItem = function() {
		var privStrs = $scope.tree.getAllChecked();
//		var privStrs = $scope.tree.getAllCheckedBranches();
		var privArray = privStrs.split(',');
		if (privStrs == "") {
			privArray = [];
		}
		$scope.data.priId = [];
		for (var j = 0; j < privArray.length; j++) {
			
			$scope.data.priId.push(privArray[j]);
		}
//		if ($scope.data.priId != "") {
//			$scope.data.priId = $scope.data.privId.substr(0,$scope.data.privId.length-1);
//		}
        
		if ($scope.id == 0) {
			$http.post(sharedProperties.getServerUrl()+'/v1/security/role/add',$scope.data)
				.success(function(data) {
					if (data.resultCode != 0) {//新增失败
						if(data.resultCode == '101') { //用户名或密码为空
							$scope.message = $scope.l10n.saveRoleFailedEmpty;
						} else if (data.resultCode == '102') { //用户名已存在
							$scope.message = $scope.l10n.saveRoleFailedName;
						}
						$scope.data.name = "";
						commonMethod.tipDialog($scope.message);
					} else {
						$location.path('/security/role/list/0');
					}
				}).error(function(data,status){
					$location.path('/security/role/list/0');	
				});
		} else {
			delete $scope.data.privileges;
			$http.put(sharedProperties.getServerUrl()+'/v1/security/role/edit',$scope.data)
				.success(function(data) {
					if (data.resultCode != 0) {//用户名已经存在
						//$scope.data.name = $scope.name;
						$scope.message = $scope.l10n.saveFail;
						commonMethod.tipDialog($scope.message);
					} else {
						$location.path('/security/role/list/1');
					}
				}).error(function(data,status){
					$location.path('/security/role/list/1');
				});
		};
		
	};
	$scope.resetItem = function() {
		if ($scope.id == 0) {
			//Add
			console.log("add role");
			$scope.addReset();
		} else {
			//Edit
			$scope.editReset();
		}
	};
	$scope.init = function() {
		$scope.id = $routeParams.id;
		$scope.data = {
				"name":"",
				"description":"",
				"priId":[]
		}
		//获取权限列表
		$http.get(sharedProperties.getServerUrl()+'/v1/security/privilege/list').success(function(data){
			var list = data.list;
            $scope.privilegesRaw=data.list;
            $scope.privileges=[];
            $scope.privilegeIds=[];
            $scope.parentIds=[];
            for(var i = 0; i < list.length; i++) {
            	var item = list[i];
            	var privMeta = new Array();
            	privMeta.push(parseInt(item.id));
            	privMeta.push(parseInt(item.parentId));
            	privMeta.push($rootScope.l10n[item.menuTitle]);
            	$scope.privileges.push(privMeta);
            	$scope.privilegeIds.push(item.id);
            	var hasParentFlag = false;
            	for(var j = 0; j < $scope.parentIds.length;j++) {
            		if ($scope.parentIds[j]==item.parentId) {
            			hasParentFlag = true;
            			break;
            		}
            	}
            	if (!hasParentFlag) {
            		$scope.parentIds.push(item.parentId);
            	}
            }
            
            //初始化权限树
    		$("#privilegeTree").text("");
            $scope.tree= new dhtmlXTreeObject("privilegeTree", "100%", "100%", 0);
            $scope.tree.setSkin('dhx_skyblue');
            $scope.tree.setImagePath("plugin/ace/js/dhtmlx/imgs/csh_dhx_skyblue/");
            $scope.tree.enableCheckBoxes(1);               //设置为复选框
            $scope.tree.enableThreeStateCheckboxes(true);  //级联选中
            $scope.tree.loadJSArray($scope.privileges);
            $scope.tree.openItem(1);
            console.log("选中了什么：",$scope.tree.getAllChecked());
    		$scope.resetItem();
		}); 
		
		
	};
	$scope.addReset = function() {
		$scope.data.name = "";
	};
	$scope.editReset = function() {
		$http.get(sharedProperties.getServerUrl()+'/v1/security/role/detail?id='+$scope.id).success(function(data){
			$scope.data = data.vo;
			$scope.name = $scope.data.name;//存储初始名字
			
			$scope.priviArray=$scope.data.priId;
			console.log("1111",$scope.priviArray);
    		if ($scope.priviArray != null) {
                 for(var i = 0; i < $scope.priviArray.length; i++) {
                	 if ($scope.priviArray[i] == 1) {
						continue;
					}
                     $scope.tree.setCheck( $scope.priviArray[i],true);
                 };
             }
		}); 
		console.log("选中了：",$scope.tree.getAllChecked());
	};
	$scope.init();
}]);