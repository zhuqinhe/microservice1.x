'use strict';
(function () {
	var config = {
		baseUrl:"systemManagement", //模块文件夹名字
		paths:[
		 		"schedulerJob/controllers_schedulerjob",
		 		"sysConfig/controllers_sysconfig",
		 		'sysConfig/js/SysConfigService',
		 		'const/js/ConstService'
		 	],
	}
	jsImport(config);
})();
