'use strict';
(function () {
	var config = {
		baseUrl:"userdata", //模块文件夹名字
		paths:[
		 		"bookmark/bookmark_controllers",
		 		"favorite/favorite_controllers",
		 		"user/user_controllers",
		 		"score/score_controllers",
		 		"score/scorelist_controllers",
		 		"av/av_controllers",
		 		"av/avlist_controllers",
		 		"comment/comment_controllers",
		 		"reminder/reminder_controllers"
		 	],
	}
	jsImport(config);
})();
