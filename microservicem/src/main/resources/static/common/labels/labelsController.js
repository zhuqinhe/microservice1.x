'use strict';

angular.module('myApp.controllers').controller(
		'labelsController',
		[
				'$scope','commonMethod','$translate','sharedProperties','$http','$rootScope',
				function($scope, commonMethod,$translate,sharedProperties,$http,$rootScope) {

					$scope.init = function() {
						$scope.pagingOptions = {// 分页字段
								pageSizes: [5,10, 20, 30, 50], 
							    pageSize: 5,
							    currentPage: 1,
							    total: 0,// 存放list总记录数
							    maxPages: 0// 最大页数
						};
						$scope.queryOptions = {// 查询字段
								name: ""
						};
//						$scope.isChecked = true;//默认显示已关联的角标
						
						$scope.getPageData();
						
				};
				
				$scope.getPageData = function(){
					$scope.url = sharedProperties.getServerUrl()+"/rest/v1/base/label/list?first="+($scope.pagingOptions.currentPage-1)*$scope.pagingOptions.pageSize
		 			+"&max="+$scope.pagingOptions.pageSize+"&name="+$scope.queryOptions.name+"&type=1";
			         $http.get($scope.url).success(function (data) {
				     	 console.info(data);
				         $scope.items = data.list;
				         $scope.pagingOptions.total = data.total;
				 		 $scope.pagingOptions.maxPages = Math.ceil($scope.pagingOptions.total/$scope.pagingOptions.pageSize);
			         });
				 };
				$scope.selectAll = function(){
		            angular.forEach($scope.items, function(item) {
		                item.checked = $scope.checkAll;
		            });
		        };
		        
		        // 判断有无选中的item
		        $scope.hasCheckedItem=function(){
		        	var checkFlag = false;
		        	if($scope.items==null || $scope.items==undefined){
		        		return checkFlag;
		        	}
		        	angular.forEach($scope.items,function(item){
		        		if(item.checked==true){
		        			checkFlag=true;
		        		}
		        	});
		        	return checkFlag;
		        }
		        
		        $scope.selectRow = function(item) {
		    		$scope.selectItem = item;
		        }
				
			    $scope.search=function(){
			    	$scope.selectItem=null;
			    	$scope.getPageData();
			    };
			    $scope.reset = function() {
			    	$scope.init();
				};
				
				$scope.closeFileDlg = function() {
					var codes = [];
					angular.forEach($scope.items, function(item) {
		                if(item.checked){
		                	codes.push(item.code);
		                }
		            });
					var data = {"contentIds":$scope.contentIds,"codes":codes};
					$http.post($scope.getUrlByScope(),data)
	                 .success(function(data) {
	                	 if(data.resultCode==0){
	                		 commonMethod.tipDialog($rootScope.l10n.operateSuccess);
	                		 $scope.confirm('ok');
	                      }else{
	                    	  commonMethod.tipDialog($translate.instant(data.resultCode+""));
	                      }
	                 })
	                 .error(function(e) {
	                	 commonMethod.tipDialog("error");
	                 });
			    }
				
				$scope.getUrlByScope = function(){
					var postUrl = "";
					if($scope.mediaType==2){
						postUrl = sharedProperties.getServerUrl()+"/rest/v1/vod/series/labels";
					}else if($scope.mediaType==3){
						postUrl = sharedProperties.getServerUrl()+"/rest/v1/tvod/channel/labels";
					}
					return postUrl;
				}
				
				/*$scope.selectCorner = function(type){
					if(type==0 && $scope.isChecked){//已选择
						$scope.getPageData();
					}else{//未选择
						
					}
				}*/
					
				$scope.init();

		} ]);
