angular.module('myApp.directives').directive('associatedContentModal',['$http','sharedProperties','$q', function($http,sharedProperties,$q) {
	return {
			restrict: 'E',
			scope: false,
			templateUrl: 'common/associatedContentModal/associatedContentModal.html',
			link: function($scope, element, attrs) {
				var tree=null;
				
				// 关联内容
				 $scope.openContentDlg = function(re_contentType,scheduleFlag) {
					 $scope.scheduleFlag=scheduleFlag;//设置scheduleFlag标记
					 $scope.queryOptions = {// 查询字段
			             id:null,
			             cpid:null,
			             name: null,
			             status: 'null',
			             objectStatus: 'null',
			             releaseYear:"null",
						 areaCode:"null",
						 date:null
			         };
					 if ((re_contentType == 1 && $scope.data.action != 109)||re_contentType == 8) {
			     		// 打开分类对话框   109:排行榜
			     		$('#categoryModal').modal('toggle');
			     		$scope.category();
			     	} else {
			     		// 打开内容对话框
			     		$('#contentModal').modal('toggle');
			     		$scope.re_contentType = $scope.data.re_contentType;
			     		$scope.getPageData($scope.re_contentType);
			     	}
			     }
				 
				 $scope.category = function() {
			 		var select_id=0;
			 		if(tree != null){
			 			tree.deleteChildItems(0);
			 		}
                     var models = [0,2,3,4,5,6,8];
			 		$q.all([sharedProperties.getCategorys(models,$scope.cpId)]).then(function(results) {
			    		
			            //tree.deleteChildItems(0);
			            var list = results[0].vo;
			            $scope.categoriesRaw = results[0].vo;
			            $scope.categories=[];
			            $scope.parentCategoryFlag=null;
			            // 设置根栏目	
			  			if(list!=null&&list.length>0){
							for(var i=0;i<list.length;i++){
								if(list[i].spcategory==true||list[i].spcategory=="true"){
									$scope.parentCategoryFlag=list[i];
								}
								$scope.categories.push([list[i].contentId,list[i].parentContentId,list[i].name]);
							}
						}
			  			var select_id = $scope.parentCategoryFlag.parentContentId;
			            $("#treeboxbox_tree").text("");
			            tree = new dhtmlXTreeObject('treeboxbox_tree', '100%', '100%',$scope.parentCategoryFlag.parentContentId);
			            tree.setSkin('dhx_skyblue');
			            tree.setImagePath("plugin/ace/js/dhtmlx/imgs/csh_dhx_skyblue/");
			            tree.enableDragAndDrop(false);
			            console.log($scope.categories);
			            // tree.setIconSize(50,50);
			            tree.loadJSArray($scope.categories);                     
			            tree.openItem(select_id);
			            tree.selectItem(select_id);
			            tree.focusItem(select_id);
			            tree.setOnClickHandler(tonclick);
			    	}); 	
			 	};
			 	
			 	function tonclick(id){
			 		// 替换特殊字符&amp;->&
			 		var replacename=tree.getItemText(id).replace("&amp;","&");
			 		// if($scope.data.re_contentType==8){
			 			for(var i=0;i<$scope.categoriesRaw.length;i++){
			 				var cate=$scope.categoriesRaw[i];
			 				if(cate.contentId==id){
			 					$scope.addOptions = {// 选中分类用到ID 和name
			 	    					id: cate.identityno,
			 	    					name:replacename,
			 	    			};
			 					$scope.data.re_content = cate.identityno;
			 					$scope.data.re_contentName = replacename;
			 					break;
			 				}
			 			}
			 			
			 			for(var i=0;i<$scope.categoriesRaw.length;i++){
			 				var cate=$scope.categoriesRaw[i];
			 				if(cate.id==id){
			 					// 设置style
			 					$scope.style=cate.styleCode;
			 					break;
			 				}
			 			}
			 	}

			 	
			 	
			 	$scope.okcloseFileDlg = function() {
			 		$("#categoryModal").modal("toggle");
			    };
			     // 撤销修改并关闭文件对话框
			     $scope.tree="";
			     $scope.formattedCategory=[];
			     
			     $scope.cancelcloseFileDlg = function() {
			     	$scope.addOptions = {// 查询字段
			 				id: null,
			 				name:null,
			 		};
			     	$("#categoryModal").modal("toggle");
			     };
			     $scope.cantPageBackward = function() {// 判断是否可以向前一页或者跳转到第一页
			 		if ($scope.pagingOptions.currentPage == "" || $scope.pagingOptions.currentPage <= 1) {
			 			return true;
			 		} else {
			 			return false;
			 		}
			 	};
			 	$scope.pageToFirst = function() {
			 		$scope.pagingOptions.currentPage = 1;
			 		// 发送请求
			 		$scope.getPageData($scope.data.re_contentType);
			 	};
			 	$scope.pageBackward = function() {
			 		if ($scope.pagingOptions.currentPage > 1) {
			 			$scope.pagingOptions.currentPage -= 1;
			 		}
			 		// 发送请求
			 		$scope.getPageData($scope.data.re_contentType);
			 	};
			 	$scope.cantPageForward = function() {
			 		if ($scope.pagingOptions.currentPage != "" && $scope.pagingOptions.currentPage < $scope.pagingOptions.maxPages) {
			 			return false;
			 		} else {
			 			return true;
			 		}
			 	};
			 	$scope.pageForward = function() {
			 		if ($scope.pagingOptions.currentPage < $scope.pagingOptions.maxPages) {
			 			$scope.pagingOptions.currentPage = parseInt($scope.pagingOptions.currentPage) + 1;
			 		}
			 		// 发送请求
			 		$scope.getPageData($scope.data.re_contentType);
			 	};
			 	$scope.pageToLast = function() {
			 		$scope.pagingOptions.currentPage = $scope.pagingOptions.maxPages;
			 		// 发送请求
			 		$scope.getPageData($scope.data.re_contentType);
			 	};
			 	
			 	 // do search
			     $scope.search=function(){
			     	// console.log("type",$scope.re_contentType);
			         $scope.getPageData($scope.re_contentType);
			     };
			     // reset search condition
			     $scope.reset = function() {
			         $scope.queryOptions = {// 查询字段
			             id:null,
			             cpid:null,
			             name: null,
			             status: 'null',
			             objectStatus: 'null',
			             releaseYear:"null",
			 			areaCode:"null",
			 			date:null
			         };
			         // console.log("type",$scope.re_contentType);
			         $scope.getPageData($scope.re_contentType);
			     };
			 	$scope.getPageData = function(contentType) {
			 		
			 		if ($scope.pagingOptions.currentPage == 0) {
			 			$scope.pagingOptions.currentPage = 1;
			 		}
			 		var name=$scope.queryOptions.name;
			 		var cp_id=$scope.queryOptions.cpid;
			 		if(name!=null&&name!=""){
			 			name=encodeURIComponent(name.replace("%","\\%"));
			 		}
			 		if(cp_id!=null&&cp_id!=""){
			 			cp_id=encodeURIComponent(cp_id.replace("%","\\%"));
			 		}
			 		if(contentType==2){
			 			$q.all([sharedProperties.getSeriesList(name,cp_id,null,$scope.queryOptions.releaseYear,null,$scope.queryOptions.areaCode,($scope.pagingOptions.currentPage-1)*$scope.pagingOptions.pageSize,$scope.pagingOptions.pageSize)])
			            .then(function(results) {
			                var list = results[0].list;
			                $scope.pagingOptions.total = results[0].total;// 存放list总记录数
			                $scope.pagingOptions.maxPages = Math.ceil($scope.pagingOptions.total/$scope.pagingOptions.pageSize);
			                $scope.items = list;
			        	}); 
			 		}else if(contentType==10){// app
			 			$scope.url = sharedProperties.getServerUrl()+"/rest/v1/app/list?recycleFlag=0"+"&first="+($scope.pagingOptions.currentPage-1)*$scope.pagingOptions.pageSize+"&max="+$scope.pagingOptions.pageSize;
			      		$http.get($scope.url).success(function (largeLoad) {
			     			$scope.pagingOptions.total = largeLoad.total;// 存放list总记录数
			     			$scope.pagingOptions.maxPages = Math.ceil($scope.pagingOptions.total/$scope.pagingOptions.pageSize);
			             	$scope.items = largeLoad.list;
			             	
			             }); 
			 		}else if(contentType==13){// 关联分集
			 			console.log(">>>>>get program list");
			            $q.all([sharedProperties.getProgramList(name,cp_id,$scope.queryOptions.releaseYear,null,$scope.queryOptions.areaCode,($scope.pagingOptions.currentPage-1)*$scope.pagingOptions.pageSize,$scope.pagingOptions.pageSize)])
			            .then(function(results) {
			                var list = results[0].list;
			                $scope.pagingOptions.total = results[0].total;// 存放list总记录数
			                $scope.pagingOptions.maxPages = Math.ceil($scope.pagingOptions.total/$scope.pagingOptions.pageSize);
			                $scope.items = list;
			        	}); 
			 			
			 		}else if(contentType==12){
			 			console.log(">>>>>get ad position list");
			 			$q.all([sharedProperties.getADList(encodeURIComponent($scope.queryOptions.id),null,null,null,name,($scope.pagingOptions.currentPage-1)*$scope.pagingOptions.pageSize,$scope.pagingOptions.pageSize,0)])
			            .then(function(results) {
			                var list = results[0].data;
			                $scope.pagingOptions.total = results[0].total;// 存放list总记录数
			                $scope.pagingOptions.maxPages = Math.ceil($scope.pagingOptions.total/$scope.pagingOptions.pageSize);
			                $scope.items = list;
			        	}); 
			 		}else if(contentType==15||$scope.data.action==109){// 专题 ; action
			 															// 109为排行榜
			 			console.log(">>>>>get specialtopic list");
			 			var model=1;
			 			if($scope.data.action==109){
			 				model=4;
			 			}
			 			$scope.url = sharedProperties.getServerUrl()+"/rest/v1/category/speciallist?model="+model+"&name="+name+"&recycle=0&stylecode=null"+"&first="+($scope.pagingOptions.currentPage-1)*$scope.pagingOptions.pageSize+"&max="+$scope.pagingOptions.pageSize;
			      		$http.get($scope.url).success(function (largeLoad) {
			      			$scope.pagingOptions.total = largeLoad.totalCount;// 存放list总记录数
			      			$scope.pagingOptions.maxPages = Math.ceil($scope.pagingOptions.total/$scope.pagingOptions.pageSize);
			      			$scope.items = largeLoad.list;
			      		});
			 		}else if(contentType==4 && $scope.scheduleFlag==0){// 频道
			 			console.log(">>>>>get channel list");
			 			$q.all([sharedProperties.getChannelList($scope.queryOptions.name,null,1,($scope.pagingOptions.currentPage-1)*$scope.pagingOptions.pageSize,$scope.pagingOptions.pageSize,0)])
			            .then(function(results) {
			                var list = results[0].list;
			                $scope.pagingOptions.total = results[0].total;// 存放list总记录数
			                $scope.pagingOptions.maxPages = Math.ceil($scope.pagingOptions.total/$scope.pagingOptions.pageSize);
			                $scope.items = list;
			        	});
			 		}else if(contentType==17){// 电视精选
			 			console.log(">>>>>get tvcollection list");
			 			$q.all([sharedProperties.getTvCollection(null,($scope.pagingOptions.currentPage-1)*$scope.pagingOptions.pageSize,$scope.pagingOptions.pageSize,0)])
			            .then(function(results) {
			                var list = results[0].list;
			                $scope.pagingOptions.total = results[0].total;// 存放list总记录数
			                $scope.pagingOptions.maxPages = Math.ceil($scope.pagingOptions.total/$scope.pagingOptions.pageSize);
			                $scope.items = list;
			        	});
			 		}else if($scope.data.action==206 && $scope.scheduleFlag==1){// 选择节目单
			 			console.log(">>>>>get schedulerecord list");
			 			$scope.url = sharedProperties.getServerUrl()+"/rest/v1/tvod/schedule_record/list?channel_contentid="+$scope.channelId+"&name="+name+"&date="+$scope.queryOptions.date+
			 						 "&recycle=0&first="+($scope.pagingOptions.currentPage-1)*$scope.pagingOptions.pageSize+"&max="+$scope.pagingOptions.pageSize;
			 			$http.get($scope.url).success(function (largeLoad) {
			  			$scope.pagingOptions.total = largeLoad.total;// 存放list总记录数
			  			$scope.pagingOptions.maxPages = Math.ceil($scope.pagingOptions.total/$scope.pagingOptions.pageSize);
			  			$scope.items = largeLoad.list;
			  		});
			 		}else if(contentType==18){// 互动专题
			 			console.log(">>>>>get interactspecial list");
//			 			$scope.url = sharedProperties.getServerUrl()+"/rest/interactspecial/list?name="+name+
//			 						 "&first="+($scope.pagingOptions.currentPage-1)*$scope.pagingOptions.pageSize+"&max="+$scope.pagingOptions.pageSize;
//			 			$http.get($scope.url).success(function (largeLoad) {
//			  			$scope.pagingOptions.total = largeLoad.total;// 存放list总记录数
//			  			$scope.pagingOptions.maxPages = Math.ceil($scope.pagingOptions.total/$scope.pagingOptions.pageSize);
			  			$scope.items = null;
			 		//});
			 		}
			 	};
				$scope.selectRow = function(item) {
					$scope.selectItem = item;
				};
			 	$scope.selectMovieOk = function() {
					// 根据选择类型判断id取值
					if($scope.scheduleFlag == 0){
						
						if($scope.data.re_contentType==12){
							// 广告使用id
							$scope.addOptions = {// 选中分类用到广告位ID 和name
									id:$scope.selectItem.positionID,
									name:$scope.selectItem.name,
							};
							$scope.data.re_content = $scope.selectItem.contentId;
							$scope.data.re_contentName = $scope.selectItem.name;
						}else if($scope.data.re_contentType==2){
							$scope.addOptions = {// 选中分类用到ID 和name
									id:$scope.selectItem.contentId,
									name:$scope.selectItem.name,
					    	};
							console.log("$scope.selectItem 2",$scope.selectItem);
							$scope.data.re_content = $scope.selectItem.contentId;
							$scope.data.re_contentName = $scope.selectItem.name;
							var params = $scope.data.re_params!=null&&$scope.data.re_params!=""?angular.fromJson($scope.data.re_params):{"programType":""};
							params.programType =  $scope.selectItem.programType;
							$scope.data.re_params = JSON.stringify(params);
						}else if($scope.data.re_contentType==15||$scope.data.re_contentType==17||$scope.data.action==109){
							// 专题使用 电视精选 排行榜
							$scope.addOptions = {// 选中分类用到广告位ID 和name
									id:$scope.selectItem.id,
									name:$scope.selectItem.name,
							};
							
							if($scope.data.re_contentType==15){
								$scope.data.re_content = $scope.selectItem.identityno;					
					    	} else {
					    		$scope.data.re_content = $scope.selectItem.id;
					    	}
							$scope.data.re_contentName = $scope.selectItem.name;
							if($scope.data.re_contentType==15){
								// 专题将专题的样式保存下来
								$scope.style=$scope.selectItem.styleCode;
								$scope.data.style=$scope.selectItem.styleCode;
							}
						}else if($scope.data.re_contentType==13){
							// 分集需获取所属连续剧contentid
							$scope.url = sharedProperties.getServerUrl()+"/rest/series/single/"+$scope.selectItem.id;
							$http.get($scope.url).success(function (largeLoad,status,headers,config) {
								$log.info(">>>>>get series by programId success.");
								$scope.series=largeLoad.series;
								if($scope.series!=null){
									$scope.addOptions = {// 分集对应的合集的contentid
											id:$scope.series.contentId,
											name:$scope.series.name,
									};
									$scope.data.re_content = $scope.series.contentId;
									$scope.data.re_contentName = $scope.series.name;
									// $scope.data.re_params = $scope.selectItem.sequence;
								}
							});
							
						}else if($scope.data.re_contentType==18){
							// 互动专题使用标识
							$scope.addOptions = {// 
									id:$scope.selectItem.code,
									name:$scope.selectItem.name,
							};
							$scope.data.re_content = $scope.selectItem.contentId;
							$scope.data.re_contentName = $scope.selectItem.name;
							if($scope.data.re_contentType==18){
								// 互动专题将互动专题的样式保存下来
								$scope.data.style=$scope.selectItem.style;
							}
							
						}else{
						
							$scope.addOptions = {// 选中分类用到ID 和name
									id:$scope.selectItem.contentId,
									name:$scope.selectItem.name,
							};
							$scope.data.re_content = $scope.selectItem.contentId;
							$scope.data.re_contentName = $scope.selectItem.name;
							// 打开回看播放器需要存channelId
							if($scope.data.action == 206){
								$scope.channelId = $scope.selectItem.contentId;
                                $scope.scheduleRecordId='';
                                $scope.scheduleName='';
                                $scope.date='';
                                $scope.startTime='';
                                $scope.endTime='';
							}
						}
					}else if($scope.scheduleFlag == 1){
						$scope.scheduleRecordId=$scope.selectItem.contentId;
						$scope.scheduleName=$scope.selectItem.name;
						$scope.date=$scope.selectItem.date;
						$scope.startTime=$scope.selectItem.startTime;
						$scope.endTime=$scope.selectItem.endTime;
					}
					$("#contentModal").modal("toggle");
				};
			 	$scope.selectCancel = function(){
			 		$("#contentModal").modal("toggle");
			 	};
				$scope.selectCancel = function(){
					$("#contentModal").modal("toggle");
				};
			 	$scope.$watch('pagingOptions', function (newVal, oldVal) {
			 		if (newVal !== oldVal) {
			 			if ($scope.pagingOptions.currentPage != '') {
			 				if (!isNaN($scope.pagingOptions.currentPage)) {// 为数字
			 					if ($scope.pagingOptions.currentPage > $scope.pagingOptions.maxPages) {
			 						$scope.pagingOptions.currentPage = $scope.pagingOptions.maxPages
			 					}
			 					$scope.getPageData($scope.data.re_contentType);
			 				} else {// 不为数字
			 					$scope.pagingOptions.currentPage = oldVal.currentPage;
			 				}
			 			}
			 		}
			 	}, true);
			}
	}
}])