'use strict';

angular.module('myApp.directives').directive('fsvTab', function() {
    /**
     *  还没研究好 暂时不用
     *  <fsv-tab tab-configs="tabConfigs" ng-model="model" label-key="label" ng-change="onChangeModel()" value-key="value" need-remove="true" need-translate="true"><fsv-tab>
     */
    return {
        restrict: 'E',
        replace: true, 
        transclude:true,
        scope: {
            tabConfigs:"=tabConfigs",
            valueKey:"@valueKey",
            labelKey:"@labelKey",
            ngChange:"&ngChange",
            needRemove:"@needRemove",
            needTranslate:"@needTranslate"
        },
        templateUrl: 'common/fsv/FsvTab.html',
        link:function($scope, element, attrs, ctrl,transclude){
           
            $scope.ngModel = $scope.$parent[attrs.ngModel];
            
            $scope.setModel = function(val){
                $scope.ngModel = val;
                $scope.$parent[attrs.ngModel] = $scope.ngModel;
                if(attrs.ngChange){
                    $scope.ngChange();
                }
               
            }

            $scope.getModel = function(){
                $scope.ngModel = $scope.$parent[attrs.ngModel] ;
                return $scope.ngModel;
            }

            $scope.$watch('tabConfigs',function(nVal,oVal){
                if(!nVal){
                    return;
                }

                $scope.tabLength = $scope.tabConfigs.length;
                $scope.selectFirstTab();
                
            });

           

            $scope.selectTab = function(tab){
                angular.forEach($scope.tabConfigs,function(t){
                    t.active = false;
                });
                tab.active = true;
                var valueKey = $scope.valueKey?$scope.valueKey:'value';
                $scope.setModel(tab[valueKey]);
                
               
            };

            
          
            $scope.selectFirstTab = function(){
                for(var i=0;i<$scope.tabConfigs.length;i++){
                    if($scope.tabConfigs[i].hide){
                        continue;
                    }
                    $scope.selectTab($scope.tabConfigs[i]);
                    break;

                }
            };

           


            $scope.deleteTab = function(tab,$event){
                for(var i=0;i<$scope.tabConfigs.length;i++){
                    var t = $scope.tabConfigs[i];
                    var valueKey = $scope.valueKey?$scope.valueKey:'value';

                    if(t[valueKey] == tab[valueKey]){
                        t.hide = true;
                        $scope.tabLength --;
                        if($scope.ngModel == tab[valueKey]){
                            $scope.selectFirstTab();
                        }
                        break;
                    }
                }
                $event.stopPropagation();
              
            };
           

        }
            
    }
});