'use strict';

/* Directives */


angular.module('myApp.directives', []).
directive('appVersion', ['version', function(version) {
    return function(scope, elm, attrs) {
        elm.text(version);
    };
}])
    .directive('fileModel', ['$parse', function ($parse) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                var model = $parse(attrs.fileModel);
                var modelSetter = model.assign;

                element.bind('change', function(){
                    scope.$apply(function(){
                        if (element[0].files) {
                            modelSetter(scope, element[0].files[0]);
                        } else {
                            modelSetter(scope, element[0].children[0].files[0]);
                        }

                    });
                });
            }
        };
    }])
    /**
     * table 列大小可拖动
     */
    .directive('colresizeable', function() {
        return {
            restrict: 'A',
            link: function(scope, elem) {
                setTimeout(function() {
                    elem.colResizable({
                        //具体配置google colResizeable
                    });
                },1000);
            }
        };
    })

    /*
     * 创建自定义翻页指令
     */
         .directive('onFinishRender',['$timeout', '$parse', function ($timeout, $parse) {  
    return {  
        restrict: 'A',  
        link: function (scope, element, attr) {  
            if (scope.$last === true) {  
                $timeout(function () {  
                    scope.$emit('ngRepeatFinished'); //事件通知  
                        var fun = scope.$eval(attr.onFinishRender);  
                        if(fun && typeof(fun)=='function'){    
                            fun();  //回调函数  
                        }    
                });  
            }  
        }  
    }  
  }]);
