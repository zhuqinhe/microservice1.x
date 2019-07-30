'use strict';

angular.module('myApp.filters').directive('fsvSysConfigSpan',['sysConfigService','translateFilter',function(sysConfigService,translateFilter) {
    /**
     *  系统配置 显示 Span
     *  <fsv-sys-config-span sys-config-key="CHANNEL_TYPE_CONFIG" value="{{item.channelType}}" ></fsv-sys-config-span>
     *      
     *  sysConfigKey 可选  
     *  sysConfigMethodKey 可选
     *  以上二个参数选填一个 都sysConfigMethodKey对应的是sysConfigService.js里的方法  sysConfigKey对应系统配置里的Key
     *  value 输入一个值
     */


     return {
        restrict: 'E',
        replace: true,
        scope: {
            sysConfigKey: "@sysConfigKey",
            sysConfigMethodKey: "@sysConfigMethodKey",
            value:"@value"

        },
        templateUrl: 'common/fsv/FsvSysConfigSpan.html',
        link: function ($scope) {

            $scope.$watch('value',function(nVal,oVal){
                if(nVal){
                   

                    if($scope.sysConfigKey){
                        sysConfigService.listConfigs($scope.sysConfigKey).then(function (configs) {
                            configs.forEach(function(c){
                                if(c.value == $scope.value){
                                    $scope.label = translateFilter(c.key);
                                }
                            })
        
                         });
                    }else{
                        sysConfigService[$scope.sysConfigMethodKey]().then(function (configs) {
                            configs.forEach(function(c){
                                if(c.value == $scope.value){
                                   
                                    $scope.label = translateFilter(c.label);
                                }
                            })
        
                         });
                    }
                }

            });

           
           
           
        }
     }
    
}]);