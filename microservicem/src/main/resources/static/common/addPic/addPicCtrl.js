'use strict'

angular.module('myApp.addPicController', ['angularFileUpload'])
.controller('addPicCtrl',['$rootScope','$scope','$q','sharedProperties','$http','$upload','$log','commonMethod','$translate', function($rootScope, $scope,$q,sharedProperties,$http,$upload,$log,commonMethod,$translate) {
	
	$rootScope.picture = [];
	$scope.init = function() {
		$q.all([sharedProperties.getAllFtp(),sharedProperties.getSysConfigInfo("FILEPATH")]).then(function(results) {
			$scope.ftpServers = results[0].list;
			$scope.localPath = results[1].vo.localPath;
		});
		$scope.scene = sessionStorage.getItem("scene");//$rootScope.scene_config;
		$scope.jslt = 'jslt';
		$rootScope.tmpbg = "";
		$scope.uploadFlag = $rootScope.uploadFlag || 2;
		$scope.selectedFtpServerId = "local";
    	$scope.fileType = 1;// 1:表示本地文件 2:表示ftp文件
    	$scope.path = null;
		$scope.browerFile(0);
	}
	//从服务器选区文件
	$scope.selectByServer = function(){
		$scope.selectedFtpServerId = "local";
    	$scope.fileType = 1;// 1:表示本地文件 2:表示ftp文件
	}
	//从本地上传文件
	$scope.selectByLocal = function(){
		
	}
	//输入链接获取文件
	$scope.selectByUrl = function() {
		$scope.fileType = 2;// 1:表示本地文件 2:表示ftp文件
	}
	
	$scope.gridOptions = {
        data: 'files',
        enableRowSelection:false,
        showSelectionCheckbox: true,
        checkboxCellTemplate: '<div class="radio" style="width:100px;"><label><input name="form-field-radio" type="radio" class="ace" /> <span class="lbl" ng-show="row.getProperty(\'type\') == 1" ng-checked="row.selected" ng-click="selectUrl(row.getProperty(\'url\'),row.getProperty(\'name\'))"> </span></label></div>',
        columnDefs: [
                     {field: 'name', displayName: $translate.instant('name'), 'cellTemplate':'<a ng-click="selectFolder(row.getProperty(\'path\'))" ng-show="row.getProperty(\'type\')==0"><i class="icon-folder-close"></i>{{row.getProperty(\'name\')}}</a><a style="text-decoration: none; cursor: default" ng-show="row.getProperty(\'type\')==1">{{row.getProperty(\'name\')}}</a>'}
                     ]
    };
	
	 $scope.ftpServerChange = function(selectedFtpServerId) {
		$scope.selectedFtpServerId = selectedFtpServerId;
		$scope.files = [];
		$scope.path = null;
		if ($scope.selectedFtpServerId == "local") {// 本地文件
			$scope.fileType = 1;
	    	$scope.browerFile(0);	
		} else {
			$scope.fileType = 2;
			if ($scope.selectedFtpServerId && $scope.selectedFtpServerId != "local") {
				$http.get(sharedProperties.getServerUrl()+'/rest/v1/sys/ftpserver/detail?id='+$scope.selectedFtpServerId)
				.success(function(data){
					if (data.resultCode == -1) {
					
						alert($translate.instant('serverConnectFail'));
					} else {
						$scope.path = data.vo.rootDirectory;
						$scope.browerFile(0);
					}
				});
			}
		}
	};
	
	// 浏览文件
    $scope.browerFile = function(flag) {
    	$scope.suffix = null;
    	if($scope.uploadFlag == 2 || $scope.uploadFlag == 3) {
    		$scope.suffix = "(bmp|jpg|jpeg|png|gif)";
    	}else if($scope.uploadFlag == 'sfileUrl') {
    		$scope.suffix = "(srt)";
    	}else if($scope.uploadFlag == 5) {
    		$scope.suffix = ".apk";
    	}
    	var url = "";
    	if ($scope.fileType == "1") {
    		url = sharedProperties.getServerUrl()+'/rest/v1/file/getAllFile?flag='+flag;
    	} else {
    		url = sharedProperties.getServerUrl()+'/rest/v1/sys/ftpserver/getAllFile?flag='+flag+'&ftpId='+$scope.selectedFtpServerId;
    	}
		if($scope.path){
			url += "&path="+$scope.path;
		}
		
		if($scope.suffix){
			url += "&suffix="+$scope.suffix
		} 
		
		$http.get(url).success(function(data){
			$scope.buttonDisable=false;
			$scope.path = data.vo.path;
			$scope.files = data.vo.files;
			$("#dialog-browserfile input[type=radio]").attr("checked",false);
		});
    };
    // 返回上一级菜单
    $scope.returnFile = function() {
    	$scope.buttonDisable=true;
    	$scope.browerFile("-1");
    };
    // 进入文件夹
    $scope.selectFolder = function(name) {
    	$scope.buttonDisable=true;
    	$scope.path = name;
    	$scope.browerFile("1");
    };
    $scope.selectUrl = function(path,filename) {
		$scope.sfile = path;
		$scope.seledFileName = filename;
	};
	// 选择文件
    $scope.selectFile = function(flag,sfile) {// 1:从服务器选择file; 2:传入url;
    	var pattern = new RegExp("[`~!^*()=|{}';',\\[\\]<>?~！#￥……&*（）——|{}【】‘；：”“'。，、？%+]");  
        if(pattern.test(sfile))
        {
        	
        	commonMethod.tipDialog($translate.instant('pictureUrlIsIllegal'));
        }else{
        	if(flag==1 && !sfile.toLowerCase().startWith("ftp")){
        		$rootScope.tmpbg = sharedProperties.getFileUrl()+sfile.replace(/\\/g,"/").replace($scope.localPath,"");
        	}else if(sfile.toLowerCase().startWith("ftp") && 
        			($scope.uploadFlag == 2 || $scope.uploadFlag == 3|| $scope.uploadFlag == 4
        					|| $scope.uploadFlag == 'picture')){//表示图片的参数最好统一，后期统一用picture
        		//ftp图片页面无法显示，需上传到http服务器
        		$scope.uploadFtpImage(sfile);
        		
        	}else{
        		$rootScope.tmpbg = sfile;
        	}
        	$rootScope.apkFile = $scope.files;
        }
    	$scope.closeThisDialog();        
    };
    //从本地上传时选择图片
    $scope.fileSelect = function($files) {
        for (var i = 0; i < $files.length; i++) {
          var file = $files[i];
          if ($scope.uploadFlag == 1) {
          } else if($scope.uploadFlag == 2 || $scope.uploadFlag == 3) {
        	  if(! (/\.(bmp|jpg|jpeg|png|gif)$/i).test(file.name)){
        		
        		  commonMethod.tipDialog(  $translate.instant('pleaseSelectPicture'));
                  return false;
              };
          } 
          $scope.videoname=file.name;
          $scope.videofile=file;
          $("[type='file']").each(function(){
        	  $(this).val("");
          });
        }
    };
    //上传图片
    $scope.uploadPicture=function(){// 1:视频 2：图片 3：海报
    	var type = "picture";
    	var uploadUrl =  sharedProperties.getServerUrl()+"/rest/v1/uploadimage";
    	if($scope.uploadFlag == 'sfileUrl' || $scope.uploadFlag == 'file'){
			type="file";
			uploadUrl = sharedProperties.getServerUrl()+"/rest/v1/uploadimage";
		}
    	if($scope.uploadFlag == 5) {
    		type="app";
    		uploadUrl = sharedProperties.getServerUrl()+"/rest/v1/upload/file/apk";
    	}
    	if(type == "picture") {
    		if(! (/\.(bmp|jpg|jpeg|png|gif)$/i).test($scope.videofile.name)){
    			commonMethod.tipDialog($translate.instant('pleaseSelectPicture'));
                return false;
          }
    	}else if(type == "app") {
    		if(! (/\.(apk)$/i).test($scope.videofile.name)){
    			commonMethod.tipDialog($translate.instant('plSelectApk'));
                return false;
            };
    	}
    	
    	
		$scope.upload = $upload.upload({
//			  url: sharedProperties.getServerUrl()+"/rest/v1/sys/upload/"+type, 
			  url: uploadUrl, 
			  method: 'POST',
			  data: [$scope.videofile],
		}).success(function(data, status, headers, config) {
			 	var rspjson=data;
			    if (rspjson.resultCode==0) {
			        $log.info("File Upload Success.");
			        if($scope.uploadFlag == 5) {
			        	$rootScope.tmpbg=rspjson.uploadResult;
			        	for(var key in rspjson.uploadResult){
			        		$rootScope.tmpbg = rspjson.uploadResult[key];
			        	}
			        	$rootScope.tmpbg = sharedProperties.getFileUrl()+$rootScope.tmpbg;
			        }else {
//			        	$rootScope.tmpbg=rspjson.pictureUrl;
			        	$rootScope.tmpbg=rspjson.uploadResult;
			        	for(var key in rspjson.uploadResult){
			        		$rootScope.tmpbg = rspjson.uploadResult[key];
			        	}
			        	$rootScope.tmpbg = sharedProperties.getFileUrl()+$rootScope.tmpbg;
			        }
			        
			        $scope.closeThisDialog();
			   }else{
				  
				   alert($translate.instant('uploadFail'));
				   $log.error("File Upload failed.");
			   }
			}).error(function(status, headers, config){
				$log.info(">>>>>Video File Upload Failed.");
				alert($translate.instant('uploadFail'));
			});
 	}; 
 	
 	//ftp图片页面无法显示，需上传到http服务器(同步)
 	$scope.uploadFtpImage =function(sfile){
		$.ajax({
            type: 'POST',
            url: sharedProperties.getServerUrl()+"/rest/v1/uploadimage/ftp",
            data:sfile,
            dataType: 'json',
            async: false,
            contentType: "application/json",
            success:function(data,status){
            	if(data.resultCode==0){
            		for(var key in data.uploadResult){
		        		$rootScope.tmpbg = sharedProperties.getFileUrl()+data.uploadResult[key];
		        	}
                }else{
                	commonMethod.tipDialog($translate.instant('uploadFail'));
                }
            },
            error:function(data,status){
            	commonMethod.tipDialog($translate.instant('uploadFail'));
            }
        });
 	}
    
    $scope.init();
}])