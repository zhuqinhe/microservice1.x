'use strict';

angular.module('myApp.directives').directive('fsvPictureSelect', ['commonMethod','sysConfigService','sharedProperties',function(commonMethod,sysConfigService,sharedProperties) {
    /**
     * 图片选择
     * 例子：
     *  <fsv-picture-select ng-model="selectPicList" ng-change="changeModel()" mediatype="channel" key="picChannelLogo"></fsv-picture-select>
     * 
     * ngModel 双向绑定一个list，用来存放图片地址列表
     * ngChange 绑定一个方法，当ngModel修改时触发ngChange
     * mediatype 传入媒体类型（字符串） 不同类型格式不一样
     * key 传入一个值，媒体类型对应多种图片类型 key对应其中一种
     * 
     */
    return {
        restrict: 'E',
        replace: true,
        scope: {
            ngModel:'=',
            ngChange:'&',
            mediatype:'@',
            readonly:"@fsvReadonly",
            key:"@"
        },
        templateUrl: 'common/fsv/FsvPictureSelect.html',
        link: function($scope, $element, $attr) {
          
            $scope.fileUrl = sharedProperties.getFileUrl();
            sysConfigService.listPicConfigs().then(function(r){
                $scope.pictureConfigs = r[$scope.mediatype][$scope.key].data;
                if($scope.pictureConfigs.length == 0){
                    $scope.pictureConfigs = [{width:100,height:100}];
                }
            });


            $scope.openVideoFileDlg = function(uploadFlag,index,event) {
                var t = commonMethod.openPicDialog('picture',event);
                t.closePromise.then(function() {
                $scope.ngModel[index] = $scope.$root.tmpbg || $scope.ngModel[index];
                        if($scope.ngChange){
                            $scope.$$postDigest($scope.ngChange);
                        }
                });
            }
        }
    }
}]);

