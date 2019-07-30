'use strict'

angular.module('myApp.controllers')
.controller('oneKeyShutdownCtrl',['$rootScope','$scope','$q','sharedProperties','$http','movieService','$log','commonMethod','$translate', 
                               function($rootScope, $scope,$q,sharedProperties,$http,movieService,$log,commonMethod,$translate) {
	
	$scope.getPageData = function () {
		
		movieService.list4MovieList($scope.queryParams.contentIdOrName, $scope.queryParams.cpId,
			$scope.queryParams.cpCode, $scope.queryParams.totalPublishStatus, $scope.queryParams.distributeType,
			$scope.queryParams.recycle, ($scope.pagingOptions.currentPage-1)*$scope.pagingOptions.pageSize, $scope.pagingOptions.pageSize, null, null).then(function (data) {
				$scope.pagingOptions.total = data.total;// 存放list总记录数
				$scope.pagingOptions.maxPages = Math.ceil($scope.pagingOptions.total/$scope.pagingOptions.pageSize);
				$scope.items = data.list;
			});

	}
	
	$scope.init = function(){
		$scope.pagingOptions = {// 分页字段
				pageSizes: [5,10, 20, 30, 50], 
			    pageSize: 5,
			    currentPage: 1,
			    total: 0,// 存放list总记录数
			    maxPages: 0// 最大页数
		};
		$scope.queryParams = {
				contentIdOrName: null,
				cpCode: null,
				cpId: "",
				totalPublishStatus: "",
				distributeType: "",
				recycle: 0
		}
		$scope.getPageData();
	}
	
	$scope.selectRow = function(item){
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
		if($scope.selectItem){
			$rootScope.movieContentId = $scope.selectItem.contentId;
		}else{
			$rootScope.movieContentId = "";
		}
		$scope.confirm();
    }
    
    $scope.init();
}])