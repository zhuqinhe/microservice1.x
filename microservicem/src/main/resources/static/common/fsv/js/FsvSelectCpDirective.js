'use strict';

angular.module('myApp.directives')
    .directive('fsvSelectCp', ['sharedProperties', function (sharedProperties) {
        /**
         * fsvSelectCp  
         * 作者:jack
         * 例子：
         *      <fsv-select-cp ng-model="cpId" ></fsv-select-cp>
         * 
         * 指令参数介绍：
         *
         * ng-model 双向绑定一个值，存放选中的cpId
         * 
         */


        return {
            restrict: 'E',
            replace: true,
           
            scope: {
                cpId: "=ngModel",
                needAll:"@needAll"
            },
            templateUrl: 'common/fsv/FsvSelectCp.html',
            link: function ($scope) {
        
                var cpsfromSession = sessionStorage.getItem("ContentProviders")
                if(cpsfromSession!=null){
                    $scope.cps = JSON.parse(cpsfromSession);
                    if(!$scope.needAll&&!$scope.cpId){
                        $scope.cpId = $scope.cps[0].cpId;
                    }
                }else{
                    sharedProperties.getCpsByCurrUser().then(function(r){
                        $scope.cps = r.vo;
                        sessionStorage.setItem("ContentProviders",JSON.stringify($scope.cps))
                        if(!$scope.needAll&&!$scope.cpId){
                            $scope.cpId = $scope.cps[0].cpId;
                        }
                    });
                }

            }
        }

    }]);