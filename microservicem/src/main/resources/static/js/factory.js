angular.module('InterceptorFactory',[]).factory('LoadingInterceptor',
		['$log','$rootScope','loginService','$cookieStore','$injector',function($log,$rootScope,loginService,$cookieStore,$injector) {
	
	String.prototype.endWith=function(str){  	   
		if(str==null||str==""||this.length==0||str.length>this.length)  
			return false;  
	    if(this.substring(this.length-str.length)==str)  
	    	return true;  
	    else  
	    	return false;  
	    return true;  
	}	
	
    var LoadingInterceptor = {
		request:function(config){
			var strCookie = document.cookie;
		    var arrCookie = strCookie.split("; ");
		    for(var i = 0; i < arrCookie.length; i++){
		        var arr = arrCookie[i].split("=");
//		        console.log(arr[1]);
		    }
		    
    		//排除掉异步获取栏目内容的接口
    		var url = config.url;

    		if(!url.endWith("/contentcount")){
    			$(".loading").show();
    		}
    		var token = sessionStorage.getItem("token");
    		// token = "sysadmin:F080D9949F18E1FCD0BCEBD3FE6165FC";
    		var login = sessionStorage.getItem("login");
    		var headers ={};
    		var encrypti_obj = {};
    		//在已经登录且token不为空且不是静态文件时在header中带有token
    		if(token!= null && token.length!=0 && login=="true" &&token!= "null"){
    			headers = {"Token": token, "Terminal-Type": "3", 
    					"Device-Id": "", "Timestamp": Date.now().toString(), 
    					"Timezone-Offset": "Asia/Shanghai", "Random": Math.floor(Math.random()*10000000).toString(), "Service-Id": "CMS-310"};
    			encrypti_obj = {"Token":headers.Token , "Terminal-Type": "3", 
    					"Device-Id": "", "TS1": headers.Timestamp, 
    					"TSZ": "Asia/Shanghai", "RND": headers.Random, "Service-Id": "CMS-310"};
    			config.headers = headers;
    		}
    		if (token) {
    			var key = CryptoJS.MD5(CryptoJS.MD5(token).toString().toUpperCase() + token.substring(5, 10)).toString().toUpperCase();
    			var message = config.data ? (typeof config.data == 'object' ? angular.toJson(config.data) : config.data) : '{}';
    			var hmacsha256 = CryptoJS.HmacSHA256('{"Header":'+JSON.stringify(encrypti_obj)+',"Body":'+ message +'}', key);
    			var hmac = CryptoJS.enc.Base64.stringify(hmacsha256);
    			config.headers["Hmac"] = hmac;
			}
    		if(url.indexOf("upload")==-1){
    			config.headers["Content-Type"] = "application/json";
    		}
    		return config;
    	},
    	response:function(response){
    		$(".loading").hide();
    		var token = sessionStorage.getItem("token") ;
    		var login = sessionStorage.getItem("login");
    		var url = response.config.url;
    		//在已经登录且token不为空且不是静态文件时在header中带有token
    		if(token!= null && login=="true" && url.match(".html|.json|.css|.js") == null ){
        		if((response.data.resultCode == "20001" || response.data.resultCode == "20008")&& url.indexOf("logout")==-1){
        			console.log("logout reason:"+response.data.resultCode);
        			sessionStorage.setItem("login",false);
        			loginService.logout();
        		}else if(response.resultCode == "20009"){
        			console.log("logout reason:"+response.data.resultCode);
        			sessionStorage.setItem("login",false);
        			loginService.logout();
        		}
    		}
    		return response;
    	},
    	requestError:function(){
    		$(".loading").hide();
    	
    	},
    	responseError:function(response){
    		$(".loading").hide();
    		if(response.status == 403){
    			loginService.gotoLoginPage();
    		}else if(response.status == 500){
    			//alert($rootScope.l10n.systemError);
    			
    		}else if(response.status == 404){
    			// alert($rootScope.l10n.netError);
    		}
    	}
    };

    return LoadingInterceptor;
}]);