'use strict';

angular.module('myApp.controllers').controller(
		'retryController',
		[
				'$scope',
				'distributeConfigService',
				'commonMethod',
				'movieService','$translate','programService','seriesService',
				'channelService','serviceService','categoryService','scheduleRecordService',
				function($scope, distributeConfigService, commonMethod,
						movieService,$translate,programService,seriesService,
						channelService,serviceService,categoryService,scheduleRecordService) {

					$scope.init = function() {
						$scope.initDistributeInfo();
					};

					$scope.initDistributeInfo = function() {
						distributeConfigService.distributeConfigInfo($scope.mediaType).then(function(r){
							$scope.distributeInfo = r;
							if($scope.distributeInfo==null || $scope.distributeInfo.size==0){
								commonMethod.tipDialog($translate
										.instant("无可用分发类型"));
								$scope.closeThisDialog('Cancel');
							}
							$scope.selectAll();
						})
					};

					$scope.selectAll = function() {
					
							$scope.distributeInfo.forEach(function(d) {
								d.selected = true;
							});
					};


					$scope.retry = function() {
						var dis = [];
						var distributeInfo_temp = $scope.distributeInfo;
						distributeInfo_temp.forEach(function(d) {
							if (d.selected == true) {
								delete d.selected;
								dis.push(d);
							}

						});
						if (dis.length == 0) {
							commonMethod.tipDialog($translate
									.instant("未指定分发类型"));
							$scope.closeThisDialog('Cancel');
						} else {
							if ($scope.mediaType == getMediaType("MOVIE")) {
								movieService.retry($scope.contentIds,
										dis).then(function(data) {
									$scope.confirm('ok');
								});
								return;
							}else if ($scope.mediaType == getMediaType("PROGRAM")) {
								programService.retry($scope.contentIds,
										dis).then(function(data) {
									$scope.confirm('ok');
								});
								return;
							}else if ($scope.mediaType == getMediaType("SERIES")) {
								seriesService.retry($scope.contentIds,
										dis).then(function(data) {
									$scope.confirm('ok');
								});
								return;
							}else if ($scope.mediaType == getMediaType("CHANNEL")) {
								channelService.retry($scope.contentIds,
										dis).then(function(data) {
									$scope.confirm('ok');
								});
								return;
							}else if ($scope.mediaType == getMediaType("SCHEDULE")) {
								scheduleRecordService.retry($scope.contentIds,
										dis).then(function(data) {
									$scope.confirm('ok');
								});
								return;
							}else if ($scope.mediaType == getMediaType("SERVICE")) {
								serviceService.serviceRetry($scope.contentIds,dis).then(function(data) {
									//服务重试
									$scope.confirm('ok');
								});
								return;
							}else if ($scope.mediaType == getMediaType("SERVICEA")) {
								serviceService.mappingRetry($scope.contentIds,dis).then(function(data) {
									//服务重试
									$scope.confirm('ok');
								});
								return;
							}else if ($scope.mediaType == getMediaType("CATEGORY")) {
								categoryService.categoryRetry($scope.contentIds,dis).then(function(data) {
									//服务重试
									$scope.confirm('ok');
								});
								return;
							}else if ($scope.mediaType == getMediaType("CATEGORYA")) {
								categoryService.mappingRetry($scope.contentIds,dis).then(function(data) {
									//服务重试
									$scope.confirm('ok');
								});
								return;
							}
							
							
						}
					};

					$scope.init();

				} ]);