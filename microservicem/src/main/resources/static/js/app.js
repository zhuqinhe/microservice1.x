'use strict';

//login.html
angular.module('login', [
    'ngRoute',
    'login.controllers',
    'myApp.filters',
    'myApp.services',
    'myApp.directives',
    'pascalprecht.translate',
    'ngCookies',
    'InterceptorFactory',
])
    .config(['$translateProvider', function ($translateProvider) {
        $translateProvider.useStaticFilesLoader({
            prefix: 'l10n/',
            suffix: '.json'
        });

    }]).config(['$httpProvider', function ($httpProvider) {
    $httpProvider.interceptors.push('LoadingInterceptor');
}]);


//index.html
angular.module('myApp', [
    'ngRoute',
    'myApp.filters',
    'myApp.services',
    'myApp.directives',
    'pascalprecht.translate',
    'ngCookies',
    'InterceptorFactory',
    'ui.bootstrap',
    'ngGrid',
    'ngDialog',
    'ngTagsInput',
    'multi-select',
    'ngSanitize',
    'ui.select',
    'myApp.addPicController',
    'Controllers_contentImport',
    'myApp.controllers'
]).config(['$routeProvider', function ($routeProvider) {

    //公共模板路由
    $routeProvider.when('/cmsContentImport/:type', {
        templateUrl: 'common/contentImport/contentimport.html',
        controller: 'ImportCtrl'
    });
    $routeProvider.when('/error', {templateUrl: 'error.html', controller: 'indexCtrl'});
    $routeProvider.when('/about', {templateUrl: 'about.html', controller: 'indexCtrl'})

    //系统配置 模块
    /*系统配置tab列表也*/
    $routeProvider.when('/sys/sysconfig/list/:manFlag', {templateUrl: 'modules/systemManagement/sysConfig/list.html', controller: 'sysconfigControllers'})
    /*定时器管理*/
    $routeProvider.when('/sys/schedulejob/list', {templateUrl: 'modules/systemManagement/schedulerJob/list.html',controller: 'schedulerJobListControllers'})
                  .when('/sys/schedulejob/edit/:id', {templateUrl: 'modules/systemManagement/schedulerJob/edit.html', controller: 'schedulerJobEditControllers'});
    /*系统配置*/
    $routeProvider.when('/sys/config/edit/:configId', {templateUrl: 'modules/systemManagement/sysConfig/config_edit.html',controller: 'cfgContentEditCtrl'});
    //用户模块
    $routeProvider.when('/security/user/list/:id', {templateUrl: 'modules/security/user/list.html',controller: 'userListCtrl'})
                  .when('/security/user/add/:id', {templateUrl: 'modules/security/user/add.html', controller: 'userAddCtrl'})
                  .when('/security/user/edit', {templateUrl: 'modules/security/user/edit.html', controller: 'userEditCtrl'})
                  .when('/security/user/review/:id', {templateUrl: 'modules/security/user/review.html',controller: 'userReviewCtrl'});
    //角色模块
    $routeProvider.when('/security/role/list/:id', {templateUrl: 'modules/security/role/list.html',controller: 'roleListCtrl'});
    $routeProvider.when('/security/role/add/:id', {templateUrl: 'modules/security/role/add.html',controller: 'roleAddCtrl' });
    //收藏模块
    $routeProvider.when('/favorite/list/:userid', {templateUrl: 'modules/userdata/favorite/list.html',controller: 'favoriteCtrl' });
    //书签
    $routeProvider.when('/bookmark/list/:userid', {templateUrl: 'modules/userdata/bookmark/list.html',controller: 'bookmarkCtrl' });
    //用户列表
    $routeProvider.when('/userdata/list/:editFlag', {templateUrl: 'modules/userdata/user/list.html',controller: 'userdataCtrl' });
    //评分
    $routeProvider.when('/score/list/:editFlag', {templateUrl: 'modules/userdata/score/list.html',controller: 'scoreCtrl' });
    $routeProvider.when('/scorelist/list/:userid', {templateUrl: 'modules/userdata/score/scorelist.html',controller: 'scoreListCtrl' });
    //踩赞
    $routeProvider.when('/av/list/:editFlag', {templateUrl: 'modules/userdata/av/list.html',controller: 'avCtrl' });
    $routeProvider.when('/avlist/list/:userid', {templateUrl: 'modules/userdata/av/avlist.html',controller: 'avListCtrl' });
    //评论
    $routeProvider.when('/comment/list/:userid', {templateUrl: 'modules/userdata/comment/list.html',controller: 'commentCtrl' });
    //提醒
    $routeProvider.when('/reminder/list/:userid', {templateUrl: 'modules/userdata/reminder/list.html',controller: 'reminderCtrl' });

    
    
}]).config(['$translateProvider', function ($translateProvider) {
    $translateProvider.useStaticFilesLoader({
        prefix: 'l10n/',
        suffix: '.json'
    });
}]).config(['$httpProvider', function ($httpProvider) {
    $httpProvider.interceptors.push('LoadingInterceptor');
}]).run(['$rootScope', '$location', '$http', 'sharedProperties', 'loginService', '$window', 'commonMethod', function ($rootScope, $location, $http, sharedProperties, loginService, $window, commonMethod) {
	  /* 监听路由的状态变化 */
    $rootScope.$on('$routeChangeSuccess', function (evt, next, current) {
        // 检查是否有权限访问页面
        var myurl = window.location.hash.substring(1);
        if (sessionStorage.getItem("login") == "true") {
            if (myurl.charAt(0) != '/') {
                myurl = '/' + myurl;
            }
            if (myurl == '/error') {
                return false;
            }
        } else {
            loginService.gotoLoginPage();
            return false;
        }
        //路由变化时,移除第三方插件
        $("[data-toggle='popover']").popover("hide");
        var parent = document.getElementById("body");
        var child_daterangepicker = document.getElementsByClassName("daterangepicker");
        var child_dialog = document.getElementsByClassName("ui-dialog");
        while (child_daterangepicker.length > 0) {
            parent.removeChild(child_daterangepicker[0]);
        }
        //检查登录密码
        $rootScope.check = false;
        if (!$rootScope.check) {
            if (myurl != '/security/user/edit') {
                var host = sharedProperties.getServerUrl();
                $http["get"](host + "/rest/ui/v1/security/check/default/password")
                    .success(function (data) {
                        if (data.vo == true) {
                            commonMethod.tipDialog($rootScope.l10n.initpassword_PleaseEdit);
                            $window.location.href = host + "/index.html#/security/user/edit";
                        } else {
                            $rootScope.check = true;
                            //检查密码是否过期
                            if (data.resultCode == 60001 && sessionStorage.getItem("checkPassTimeOut") == "one") {
                                sessionStorage.setItem("checkPassTimeOut", "two");
                                commonMethod.tipDialog($rootScope.l10n.passwordouttime);
                                $window.location.href = host + "/index.html#/security/user/edit";
                            }
                        }
                    });
            }
        }

    })
}]);
