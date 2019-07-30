//$(document).ajaxStart(function() {
//	console.info('hello');
//	
//});

$.ajaxSetup({
	
	beforeSend:function(xhr) {
		
		var token = sessionStorage.getItem("token");
		// token = "sysadmin:F080D9949F18E1FCD0BCEBD3FE6165FC";
		var login = sessionStorage.getItem("login");
		var headers ={};
		var encrypti_obj = {};
		
		if(token!= null && token.length!=0 && login=="true" &&token!= "null"){
			headers = {"Token": token, "Terminal-Type": "3", 
					"Device-Id": "", "Timestamp": Date.now().toString(), 
					"Timezone-Offset": "Asia/Shanghai", "Random": Math.floor(Math.random()*10000000).toString(), "Service-Id": "CMS-310"};
			encrypti_obj = {"Token":headers.Token , "Terminal-Type": "3", 
					"Device-Id": "", "TS1": headers.Timestamp, 
					"TSZ": "Asia/Shanghai", "RND": headers.Random, "Service-Id": "CMS-310"};
			for(x in headers){
				xhr.setRequestHeader(x,headers[x]);
			}
			
		}
		if (token) {
			var key = CryptoJS.MD5(CryptoJS.MD5(token).toString().toUpperCase() + token.substring(5, 10)).toString().toUpperCase();
			var message = this.data ? (typeof this.data == 'object' ? angular.toJson(this.data) : this.data) : '{}';
			var hmacsha256 = CryptoJS.HmacSHA256('{"Header":'+JSON.stringify(encrypti_obj)+',"Body":'+ message +'}', key);
			var hmac = CryptoJS.enc.Base64.stringify(hmacsha256);
			xhr.setRequestHeader('Hmac', hmac);
		}
		console.info(xhr);
   }

});