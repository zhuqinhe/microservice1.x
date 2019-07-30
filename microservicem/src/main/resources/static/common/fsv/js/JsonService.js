'use strict';

angular.module('myApp.services').service(
		'jsonService',
		[function() {

            var self = this;
            
            //解析错误打印
            jsonlint.yy.parseError = function(str,hash){
                
            };
           

            /**
				解析成功 返回解析后的json  失败返回false
			*/
            self.parse = function(v){
               return jsonlint.parse(v);
            };
                
            
}]);









