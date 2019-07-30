'use strict';

angular.module('myApp.directives')
    .directive('fsvSpanCp', ['sharedProperties', function (sharedProperties) {
        /**
         * fsvSelectCp  
         * 作者:jack
         * 例子：
         *      <fsv-span-cp ng-model="cpId" ></fsv-span-cp>
         * 
         * 指令参数介绍：
         *
         * ng-model 绑定一个值
         * 
         */


        return {
            restrict: 'E',
            replace: true,
           
            scope: {
                cpId: "=ngModel",
                needAll:"@needAll"
            },
            templateUrl: 'common/fsv/FsvSpanCp.html',
            link: function ($scope) {
                var cpsfromSession = sessionStorage.getItem("ContentProviders")
                if(cpsfromSession!=null){
                    $scope.cps = JSON.parse(cpsfromSession);
                }else{
                    sharedProperties.getCpsByCurrUser().then(function(r){
                        $scope.cps = r.vo;
                        sessionStorage.setItem("ContentProviders",JSON.stringify($scope.cps))
                    });
                }

               
               
            }
        }

    }]);