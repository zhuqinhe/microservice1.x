


'use strict';

angular.module('myApp.directives')
    .directive('fsvSelectDirstributeType', ['distributeConfigService', function (distributeConfigService) {
        /**
         * fsvSelectDirstributeType  
         * 作者:jack
         * 例子：
         *      <fsv-select-dirstribute-type ng-model="" type="VOD"  ></fsv-select-dirstribute-type>
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
                distributeType: "=ngModel",
                type:'@'
            },
            templateUrl: 'common/fsv/FsvSelectDirstributeType.html',
            link: function ($scope) {
                if($scope.type == 'VOD'){
                    distributeConfigService.listDistributeTypes4VOD().then(function (r) {
                    	$scope.distributeType = "";
                        $scope.distributeConfigs = r;
                        if($scope.distributeConfigs!=null && $scope.distributeConfigs.length>0){
                        	$scope.distributeType = $scope.distributeConfigs[0].value;
                        }
                    });
               
                }else if($scope.type == 'TVOD'){
                    distributeConfigService.listDistributeTypes4TVOD().then(function (r) {
                    	$scope.distributeType = "";
                        $scope.distributeConfigs = r;
                        if($scope.distributeConfigs!=null && $scope.distributeConfigs.length>0){
                        	$scope.distributeType = $scope.distributeConfigs[0].value;
                        }
                    });
                }
              
            }
        }

    }]);