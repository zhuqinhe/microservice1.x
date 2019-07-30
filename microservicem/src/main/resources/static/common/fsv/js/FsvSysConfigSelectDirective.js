


'use strict';

angular.module('myApp.directives')
    .directive('fsvSysConfigSelect', ['sysConfigService', function (sysConfigService) {
        /**
         * 
         * 系统配置 Select
         * 
         * 例子:
         *  <fsv-sys-config-select class="form-control " sys-config-key="CHANNEL_TYPE_CONFIG" ng-model="formData.channelType"></fsv-sys-config-select>
         *  sysConfigKey 可选  
         *  sysConfigMethodKey 可选
         *  以上二个参数选填一个 都sysConfigMethodKey对应的是sysConfigService.js里的方法  sysConfigKey对应系统配置里的Key
         * 
         * needAll 是否开启所以的选择项
         * ngModel 绑定一个值
         */


        return {
            restrict: 'E',
            replace: true,

            scope: {
                sysConfigKey: "@sysConfigKey",
                sysConfigMethodKey: "@sysConfigMethodKey",
                needAll: "@needAll",
                ngModel:"=ngModel"

            },
            templateUrl: 'common/fsv/FsvSysConfigSelect.html',
            link: function ($scope) {

                if ($scope.sysConfigMethodKey) {
                    sysConfigService[$scope.sysConfigMethodKey]().then(function (r) {
                        $scope.configs = r;
                        if(!$scope.needAll&&$scope.ngModel==null){
                            $scope.ngModel = $scope.configs[0].value;
                        }
                    });
                } else {
                    sysConfigService.listConfigs($scope.sysConfigKey).then(function (r) {
                        $scope.configs = r;
                        if(!$scope.needAll&&$scope.ngModel==null){
                            $scope.ngModel = $scope.configs[0].value;
                        }
                    });
                }

                



            }
        }

    }]);