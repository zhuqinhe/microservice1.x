'use strict';

/* Controllers */

angular.module('myApp.controllers')
.controller('userListCtrl', ['$rootScope','$scope', '$http', 'sharedProperties','loginService','$window','$routeParams','$q','$log','commonMethod',function($rootScope,$scope, $http, sharedProperties, loginService,$window,$routeParams,$q,$log,commonMethod) {
    $scope.enableUser = function(item){
    	$log.info(">>>>enable user id:"+item.id+",rawstatus:"+item.enable);
    	///user/enableuser
    	var userid = item.id;
    	var url = sharedProperties.getServerUrl()+"/ui/v1/security/user/enable?id="+item.id;
		$http.put(url).success(function (data) {
			if(data.resultCode==0){
				$log.info(">>>>>enable(disable) user "+item.id +" success.");
				item.enable = !item.enable;
			}
        }).error(function(){
        	$log.error(">>>>>enable(disable) user "+item.id +" failed.");
        });
    }
    
    $scope.resetPassword = function(item){
    	$log.info(">>>>enable user id:"+item.id+",rawstatus:"+item.enable);
    	///user/enableuser
    	var userid = item.id;
    	var url = sharedProperties.getServerUrl()+"/ui/v1/security/user/password/reset?id="+item.id;
		$http.put(url).success(function (data) {
			if(data.resultCode==0){
				$log.info(">>>>>user resetPassword "+item.id +" success.");
				//item.enable = !item.enable;
				if(userid == 1) {
					$scope.message = $rootScope.l10n.saveUserSuccess;
					commonMethod.tipDialog($scope.message);
					sessionStorage.setItem('login', false);
					sessionStorage.removeItem('token');
					sessionStorage.removeItem('username');
					//sessionStorage.removeItem("role");
					sessionStorage.removeItem("movie");
					//sessionStorage.removeItem('tv');
					//sessionStorage.removeItem('channel');
					//sessionStorage.removeItem('music');
					//sessionStorage.removeItem("book");
					//sessionStorage.removeItem("app");
					//sessionStorage.removeItem("mediacontent");
					$timeout(function() {
						loginService.gotoLoginPage();
					},1000);
					//loginService.gotoLoginPage();	//密码修改成功，返回登录页面
				}
			}
        }).error(function(){
        	$log.error(">>>>>user resetPassword"+item.id +" failed.");
        });
    }
    
	$scope.getPageData = function() {
		if ($scope.pagingOptions.currentPage == 0) {
			$scope.pagingOptions.currentPage = 1;
		}
		var name=$scope.queryOptions.name;
		var sp_id=$scope.queryOptions.sp_id;
		var cp_id=$scope.queryOptions.cp_id;
		var url = sharedProperties.getServerUrl()+"/ui/v1/security/user/list?userId="+encodeURIComponent($scope.queryOptions.id)+"&name="+encodeURIComponent(name)+"&spid="+encodeURIComponent(sp_id)+"&cpid="+encodeURIComponent(cp_id)+"&first="+($scope.pagingOptions.currentPage-1)*$scope.pagingOptions.pageSize+"&max="+$scope.pagingOptions.pageSize;
		$http.get(url).success(function (largeLoad) {
			console.log(largeLoad);
			$scope.pagingOptions.total = largeLoad.total;//存放list总记录数
			$scope.pagingOptions.maxPages = Math.ceil($scope.pagingOptions.total/$scope.pagingOptions.pageSize);
        	$scope.items = largeLoad.list;
        }).error(function(){
        	$window.alert("time out !");
       		sessionStorage.setItem('login', false);

			loginService.gotoLoginPage();
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
				name: null,
				sp_id: "null",
				cp_id: "null",
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
		} else if($scope.selectItem.userId != $scope.parentUserId){
			return true;
		} else {
			return false;
		}
	};
	//判断是否选择某个记录
	$scope.selected_delete= function() {
		if ($scope.selectItem == null || $scope.selectItem.id === 1 ||$scope.selectItem.userId=='sysadmin') {
			return true;
        }else{
        	return false;
        }
	};
	
	
	$scope.deleteItem = function() {
		$scope.message = $rootScope.l10n.areyousure;
		commonMethod.confirmDialog($scope.message).then(function(value) {
			var ids = [$scope.selectItem.id];
        	$http['post'](sharedProperties.getServerUrl()+"/ui/v1/security/user/remove",ids).success(function () {
      			$scope.selectItem = null;
      			$scope.getPageData();
            });
		});
	}
	$scope.savePageOptions = function() {//点击修改或者媒体内容按钮保存当前的查询条件及分页参数
		sessionStorage.setItem("parentLocation", window.location.href);
    	$rootScope.user_pagingOptions = $scope.pagingOptions;
    	$rootScope.user_queryOptions = $scope.queryOptions;
    };
    
    
	$scope.init = function() {
		var editFlag = $routeParams.editFlag;//0:表示分页参数初始化; 1:使用之前的分页及搜索参数
		if (editFlag == "1" && $rootScope.user_pagingOptions) {
			$scope.pagingOptions = $rootScope.user_pagingOptions;
			$scope.queryOptions = $rootScope.user_queryOptions;
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
					name: null,
					sp_id: null,
					cp_id: "null",
			};
		}
		$scope.selectItem = null;
		$scope.getPageData();
	};

	$scope.init();
}])
.controller('userAddCtrl',['$scope', '$http', 'sharedProperties', '$routeParams', '$location', 'loginService', '$rootScope', '$q','commonMethod', function($scope, $http, sharedProperties,$routeParams,$location,loginService, $rootScope, $q,commonMethod){
	$scope.getRoles = function() {
		var deferred = $q.defer(); // 声明延后执行，表示要去监控后面的执行  
		$http.get(sharedProperties.getServerUrl()+'/ui/v1/security/role/list').  
		success(function(data, status, headers, config) {  
	    	deferred.resolve(data);  // 声明执行成功，即http请求数据成功，可以返回数据了  
	    }).  
	    error(function(data, status, headers, config) {
	    	deferred.resolve({"list":[]});  // 声明执行失败，即服务器返回错误  
	    });  
		return deferred.promise;   // 返回承诺，这里并不是最终数据，而是访问最终数据的API 
	};
	$scope.saveItem = function() {
		$scope.data.roles = $scope.checkRoles();
		
		if ($scope.id == 0) {
			//$scope.data.id = 0;
			$http.post(sharedProperties.getServerUrl()+'/ui/v1/security/user/add',$scope.data)
				.success(function(data) {
					if (data.resultCode != 0) {//新增失败
						//$("#dialog-tip p").hide();
						if(data.resultCode == '101') { //用户名或密码为空
							$scope.message = $rootScope.l10n.saveUserFailedEmpty;
						} else if (data.resultCode == '200') { //用户名已存在
							$scope.message = $rootScope.l10n.saveUserFailedName;
						} else { //cp或者sp为空
							$scope.message = $rootScope.l10n.saveFail;
						}
						$scope.data.name = "";
						commonMethod.tipDialog($scope.message);
					} else {
						$location.path('/security/user/list/0');
					}
				}).error(function(data,status){
					$location.path('/security/user/list/0');	
				});
		} else {
			//修改信息
			$http.put(sharedProperties.getServerUrl()+'/ui/v1/security/user/edit',$scope.data)
				.success(function(data) {
					if (data.resultCode != 0) {//用户名已经存在
						$scope.data.name = $scope.name;
						$scope.message = $rootScope.l10n.saveFail;
						commonMethod.tipDialog($scope.message);
					} else {
						$location.path('/security/user/list/1');
					}
				}).error(function(data,status){
					$location.path('/security/user/list/1');
				});
		};
		
	};
	//保存对象生成roleIds
	$scope.checkRoles = function() {
		var roles = [];
		for (var i in $scope.groups) {
			var obj = $scope.groups[i];
			for (var o in obj) {
				if (o == "checked" && obj[o]) {
					roles.push(obj);
					break;
				}
			}
		}
		return roles;
	};
	$scope.checkUserId = function() {
    	$scope.repeatName = false;
    	if ($scope.data.userId != null && $scope.data.userId != "" ) {
    		$http.get(sharedProperties.getServerUrl()+"/ui/v1/security/user/check/unique?name="+$scope.data.userId)
            .success(function(data,status){
                $scope.repeatName = !data.vo;
            }).error(function() {
            	$scope.repeatName = false;
            });
    	}
    };
	$scope.resetItem = function() {
		if ($scope.id == 0) {
			//Add
			$scope.addReset();
		} else {
			//Edit
			$scope.editReset();
		}
	};
	$scope.init = function() {
		$scope.username = sessionStorage.getItem('username');
		$scope.id = $routeParams.id;//从请求参数中获取到id
		$scope.selectcps = [];
		$scope.cps_tmp = [];
		$scope.data = {
				id:0,
				role:'cp',
				cpId:'',
				spId:'',
				userId:'',
				nickName:'',
				organization:'',
				enable:true,
				enableIPBinding:false,
				ipAddr:"",
				locked:false,
				roleIds:[],
				cps:[],
				userType:'admin'
		}
		if($rootScope.role == "cp") {
			$scope.roles = [{"name":"CP","value":"cp"}];
		}else {
			$scope.roles = [{"name":"CP","value":"cp"},{"name":"SP","value":"sp"}];
		}
		$scope.resetItem();
	};
	$scope.addReset = function() {
		
	};
	$scope.editReset = function() {
		var url = sharedProperties.getServerUrl()+'/ui/v1/security/user/detail?id='+$scope.id;
		$http.get(url).success(function(data){
			$scope.data = data.vo;
			$scope.name = $scope.data.userId;//存储初始名字
			$scope.data.pwFlag = false;
			//$scope.data.enable = data.vo.enable?"true":"false";
			//$scope.data.enableIPBinding = data.vo.enable?"true":"false";
			//sp用户获取cp
			//初始化权限
			var roleArray = [];
			if ($scope.data.roles!=null && $scope.data.roles!="") {
				roleArray = $scope.data.roles;
				for (var i = 0; i < roleArray.length; i++) {
					var id = roleArray[i].id;
					for (var j = 0; j < $scope.groups.length; j++){
						var group = $scope.groups[j];
						if (id == group.id) {
							group.checked = true;
							break;
						}
					}
				}
			}
			
		}); 
	};
	$scope.init();

}])
.controller('userReviewCtrl', ['$scope','$http','sortKey','$location','sharedProperties','loginService','$routeParams','$q','$rootScope',function($scope,$http,sortKey,$location,sharedProperties,loginService,$routeParams,$q,$rootScope){
	$scope.id = $routeParams.id;
	
	$scope.returnLastPage = function() {
//    	window.location.href=$rootScope.parentLocation;
    	var backLocation = sessionStorage.getItem("parentLocation");
    	var local = sharedProperties.getServerUrl() + "/index.html#/security/user/list/0";
    	if (backLocation != null && backLocation != undefined && backLocation != local) {
    		window.location = backLocation;
		} else {
			window.location = '#/security/user/list/1';
		}
    }
	
	$scope.init = function() {
		$scope.url = sharedProperties.getServerUrl()+'/ui/v1/security/user/detail?id='+$scope.id;
		$http.get($scope.url).success(function(data){
			$scope.data = data.vo;
			$scope.roleNames = "";
			//console.log($scope.data.roles);
            for (var i =0; i< $scope.data.roles.length; i++) {
            	$scope.roleNames += $scope.data.roles[i].name;
            	if(i!=$scope.data.roles.length-1){
            		$scope.roleNames += ",";
            	}
			}
		});
	}
	$scope.init();
}])
.controller('userEditCtrl',['$scope', '$http', 'sharedProperties','$location', 'loginService', '$rootScope','commonMethod','$timeout', function($scope, $http, sharedProperties, $location, loginService, $rootScope,commonMethod,$timeout){
	$scope.init = function() {
		$scope.data = {
				name : sessionStorage.getItem('username'),
				pwFlag: false
		};
		$(".form-horizontal").on("click", function(e){
			if(e.target && e.target.id == "edit_switch") {
				$(".help-button").popover();
			}
		});  
	};
	$scope.saveItem = function() {
		if($scope.data.newPw1!==$scope.data.newPw2){
			//$("#dialog-p2").show();
			$scope.message = $rootScope.l10n.saveUserFailedPW2;
			commonMethod.tipDialog($scope.message);
			return;
		}
		var body = {"oldPw":$scope.data.oldPw,
				"newPw":$scope.data.newPw1};
		$http.put(sharedProperties.getServerUrl()+"/ui/v1/security/user/password/edit",body)
			.success(function(data) {
				if (data.resultCode != 0) {//密码修改失败
					$scope.data.oldPw = "";
					$scope.data.newPw1 = "";
					$scope.data.newPw2 = "";
					$("#dialog-tip p").hide();
					if (data.resultCode == '200') {//用户名已存在
						$scope.message = $rootScope.l10n.saveUserFailedName;
						//$("#dialog-p1").show();
						$scope.data.name = sessionStorage.getItem('username');
					} else if (data.resultCode == '400') {//旧密码不匹配
						$("#dialog-p2").show();
						$scope.message = $rootScope.l10n.saveUserFailedPW;
					} else {//两次输入的密码不一致
						$("#dialog-p3").show();
						$scope.message = $rootScope.l10n.saveUserFailedPW2;
					}
					commonMethod.tipDialog($scope.message);
				} else {
					$scope.message = $rootScope.l10n.saveUserSuccess;
					commonMethod.tipDialog($scope.message);
					sessionStorage.setItem('login', false);

					$timeout(function() {
						loginService.gotoLoginPage();
					},1000);
					//loginService.gotoLoginPage();	//密码修改成功，返回登录页面
				}
			});
	};
	$scope.cancelItem = function() {
		$location.path(document.referrer);
	}
	$scope.init();
}]);