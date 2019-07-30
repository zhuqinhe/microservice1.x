'use strict';

/* Controllers */
'use strict';

/* Controllers */

var Controllers_contentImport = angular.module('Controllers_contentImport', ['angularFileUpload']);

Controllers_contentImport.controller('ImportCtrl',['$http','$scope','$location','$routeParams','loginService','sharedProperties','$log','$upload','$rootScope','$q','commonMethod',function($http,$scope,$location,$routeParams,loginService,sharedProperties,$log,$upload,$rootScope,$q,commonMethod){
	 	if(!loginService.isLogin()){
	        loginService.gotoLoginPage();
	        return false;
	    };
	    $scope.type=$routeParams.type;
	    // file upload
	    $scope.init=function(){
	    	$("#dialog-browserfile").hide();
	    	$("#selectFileDlg").hide();
	    	$("#dialog-con").hide();
	    	$scope.lastfilename="";
	    }
	    $scope.onFileSelect = function($files) {
	        // $files: an array of files selected, each file has name, size, and
			// type.
	        for (var i = 0; i < $files.length; i++) {
	          var file = $files[i];
	          if($scope.type==11 && (! ((/\.(xml)$/i).test(file.name)||(/\.(tar.gz)$/i).test(file.name)))){
	        	  //alert($rootScope.l10n.pleaseSelectXMLOrTargz);
	        	  commonMethod.tipDialog($rootScope.l10n.pleaseSelectXMLOrTargz);
	              return false;
	          }else if($scope.type!=11 && ! (/\.(xml)$/i).test(file.name)){
	        	  //alert($rootScope.l10n.pleaseSelectXML);
	        	  commonMethod.tipDialog($rootScope.l10n.pleaseSelectXML);
	              return false;
	          };
	          $scope.txtname=file.name;
	          $scope.xmlfile=file;
	        }
	    };
	     
	     
	     //生成进度条的方法
	    $scope.opendivshow=function(width,title,href){
	     	document.getElementById('lightshow').style.display='block';
	     	document.getElementById('lightshow').style.width=width+"px";
	     	document.getElementById('lightshow').style.marginLeft=-(width/2)+"px";
	     	document.getElementById('fadeshow').style.display='block';
	     };
	     
	    
	     //打开文件浏览
	     $scope.openFileDlg = function() {
	     	$("#dialog-browserfile").show();
	     	$("#selectFileDlg").show();
	 		$scope.txtname = "";
	     };
	     // 取消文件选择
	     $scope.cancelFile = function() {
	     	$("#dialog-browserfile").hide();
	     	$("#selectFileDlg").hide();
	     };
	     
	     $scope.cancelUploadFile=function(){
	    	//跳转type:0 program,1 series,2 kalaok,3 music,4 app,5 game 11 epg模板
             if($scope.type==0){
             	$location.path('/program/list/0'); 
             }else if($scope.type==1){
             	$location.path('/tvshow/list/0/null'); 
             }else if($scope.type==2){//kalaok
             	$location.path('/album/list/0/0'); 
             }else if($scope.type==3){//music
             	$location.path('/album/list/0/1'); 
             }else if($scope.type==4){
            	 $location.path('/app/list/0/APP');
             }else if($scope.type==5){
            	 $location.path('/app/list/0/GAME');
             }else if($scope.type==11){//EPG模板导入
            	 $location.path('/uitemplate/list/0');
             }else if($scope.type==12) { //新模板导入
            	 $location.path('/template/list/0');
             }
	     }
	     
	     $scope.url='';
	     $scope.uploadXml=function(){
	    	console.log($scope.xmlfile.name);
	    	var uploadPath = "schedule";
    		if($scope.type == 11 || $scope.type == 12){
    			uploadPath = "uitemplate";
    		}
	    	if($scope.type==11 && (! ((/\.(xml)$/i).test($scope.xmlfile.name)||(/\.(tar.gz)$/i).test($scope.xmlfile.name)))){
	        	  commonMethod.tipDialog($rootScope.l10n.pleaseSelectXMLOrTargz);
	              return false;
	        }else if($scope.type!=11 && ! (/\.(xml)$/i).test($scope.xmlfile.name)){
	        	  commonMethod.tipDialog($rootScope.l10n.pleaseSelectXML);
	              return false;
	        };
	        $("#dialog-browserfile").hide();
	     	$("#selectFileDlg").hide();
	     	$scope.opendivshow();
			$scope.upload = $upload.upload({
				url: sharedProperties.getServerUrl()+"/rest/v1/upload/file/"+uploadPath,
				method: 'POST',
				file: $scope.xmlfile,
			}).success(function(data, status, headers, config) {
				var rspjson=data;
			    if (rspjson.resultCode==0) {
			      $log.info("xml File Upload Success.");
//			      $scope.url=rspjson.pictureUrl;
			      $scope.lastfilename = $scope.xmlfile.name;
			      $scope.xmlfile='';
			      var path;
		          for(var key in rspjson.uploadResult){
		        	  path = rspjson.uploadResult[key];
		          }
		          $http.get(sharedProperties.getServerUrl() + '/rest/v1/sys/config/value?key=filepath').
                  success(function (data, status, headers, config) {
                	  if(!data.vo.localPath.endWith("/")&&!path.startWith("/")){
                		  $scope.url = data.vo.localPath+"/"+path;
                	  }else{
                		  $scope.url = data.vo.localPath+path;
                	  }
                  });
			      
			   }else{
				   $scope.url='';
				   $scope.xmlfile='';
				   commonMethod.tipDialog(" xml File Upload failed.");
				   $log.error("xml File Upload failed.");
			   }
			   //upload success or failed should hide process bar both.
			   document.getElementById('lightshow').style.display='none';
			   document.getElementById('fadeshow').style.display='none';
			}).error(function(status, headers, config){
				$log.info(">>>>>File Upload Failed.");
				//alert("File Upload failed.");
				commonMethod.tipDialog("File Upload failed.");
				document.getElementById('lightshow').style.display='none';
				document.getElementById('fadeshow').style.display='none';
			});
	 	};   
	 	
	 	$scope.uploadContentXml=function(){
	 		$scope.language=sessionStorage.getItem("language");
	 		 console.log(">>>>>upload file.");
	 		var uploadPath = "schedule";
    		if($scope.type == 11 || $scope.type == 12){
    			uploadPath = "uitemplate";
    		}
	    	 if( $scope.url==""){
	             $log.info(">>>>>File is not uploaded.");
	             commonMethod.tipDialog($rootScope.l10n.pleaseUploadFile);
	             return;
	         }else if((/\.(xml)$/i).test($scope.url)||(/\.(txt)$/i).test($scope.url)||(/\.(tar.gz)$/i).test($scope.url)){
	        	 $scope.opendivshow();//进度条的方法
	        	 $("#dialog-con").show();
	        	 $http.post(sharedProperties.getServerUrl()+"/rest/v1/import/file/"+uploadPath,{url:$scope.url,type:$scope.type,fileName:$scope.lastfilename})
                 .success(function (rsp, status, headers, config) {
		        	if(rsp.resultCode==0){
		        		$( "#dialog-con" ).hide();
		        		var description=rsp.description;
		        		if($scope.type==1){
		        			commonMethod.tipDialog($rootScope.l10n.contentImportSuccess);
		        			$location.path('/tvshow/list/0/null');
		        		}else if($scope.type==0){
		        			commonMethod.tipDialog($rootScope.l10n.contentImportSuccess);
		        			$location.path('/program/list/0/null');
		        		}else if($scope.type==11){
		        			commonMethod.tipDialog($rootScope.l10n.contentImportSuccess);
		        			$location.path('/uitemplate/list/0/null');
		        		}else if($scope.type==12){
		        			commonMethod.tipDialog($rootScope.l10n.contentImportSuccess);
		        			$location.path('/template/list/0');
		        		}else{
		        			var descriptionjson=JSON.parse(description);
		        			$scope.successCount=descriptionjson.success;
		        			$scope.failcount=descriptionjson.failed;
		        			document.getElementById('lightshow').style.display='none';
		        			document.getElementById('fadeshow').style.display='none';
		        			var deleteTipDlg= $("#dialog-tip" ).removeClass('hide').dialog({
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
		        				        	  html: "<i class='icon-ok bigger-110'></i>"+$rootScope.l10n.ok,
		        				        	  "class" : "btn btn-info btn-xs",
		        				        	  click: function() {
		        				        		  $( this ).dialog( "close" );
		        				        		  //跳转
		        				        		  if($scope.type==0){
		        				        			  $location.path('/program/list/0'); 
		        				        		  }else if($scope.type==1){
		        				        			  $location.path('/tvshow/list/0/null'); 
		        				        		  }else if($scope.type==2){//kalaok
		        				        			  $location.path('/album/list/0/0'); 
		        				        		  }else if($scope.type==3){//music
		        				        			  $location.path('/album/list/0/1'); 
		        				        		  }else if($scope.type==4){
		        				        			  $location.path('/app/list/0/APP');
		        				        		  }else if($scope.type==5){
		        				        			  $location.path('/app/list/0/GAME');
		        				        		  }else if($scope.type==11){
		        				        			  $location.path('/uitemplate/list/0');
		        				        		  }else if($scope.type==12) {
		        				        			  $location.path('/template/list/0');
		        				        		  }
		        				        	  }
		        				          }
		        				          ]
		        			});
		        		}
			        		//跳转
	                        if($scope.type==0){
	                        	$location.path('/program/list/0'); 
	                        }else if($scope.type==1){
	                        	$location.path('/tvshow/list/0/null'); 
	                        }else if($scope.type==2){//kalaok
	                        	$location.path('/album/list/0/0'); 
	                        }else if($scope.type==3){//music
	                        	$location.path('/album/list/0/1'); 
	                        }else if($scope.type==4){
	                        	$location.path('/app/list/0/APP');
	                        }else if($scope.type==5){
	                        	$location.path('/app/list/0/GAME');
	                        }else if($scope.type==11){
	                        	$location.path('/uitemplate/list/0');
	                        }else if($scope.type==12) {
		        			  $location.path('/template/list/0');
		        		  }
		      	   }else{
		      		  //$( "#dialog-con" ).hide();
		      		 document.getElementById('lightshow').style.display='none';
					 document.getElementById('fadeshow').style.display='none';
					  var dialogname="dialog-tip2";
					  if(rsp.resultCode==-4||rsp.resultCode==-5){
						  dialogname = "dialog-uitemplate-msg";
					  }
		      		  var deleteTipDlg= $( "#"+dialogname).removeClass('hide').dialog({
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
				                    html: "<i class='icon-ok bigger-110'></i>"+$rootScope.l10n.ok,
				                    "class" : "btn btn-info btn-xs",
				                    click: function() {
				                        $( this ).dialog( "close" );
				                    }
				                }
				            ]
				        });
		      	  }
	          });
	       }
	 	}
	    $scope.init();
}]);