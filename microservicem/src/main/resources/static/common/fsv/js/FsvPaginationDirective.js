'use strict';

angular.module('myApp.directives')
    .directive('fsvPaginationV2', function () {
        /**
         * fsvPaginationV2 （二代 fsvPagination） (重构原atPagination)
         * 作者:jack
         * 例子：
         *      <fsv-pagination-v2 list="getPageData()" first="first" max="max" total="{{total}}"></fsv-pagination-v2>
         * 
         * 指令参数介绍：
         *
         * list 传入一个无参方法，指令在当前页和单页最大记录数量发生变化后 主动触发
         * first 传入一个model，  指令在当前页和单页最大记录数量发生变化后 计算当前页首位 主动更新这个model 
         * max 传入一个model， 指令在当前页和单页最大记录数量发生变化后 主动更新这个model 
         * total 传入当前总记录数量 用于计算最大页数 并在页面显示
         * 
         * 这个指令将在父scope上建重置first和max的方法 resetCurrentPage()
         * 
         * 备注：
         * 1.采用隔离的Scope，降低该指令的侵入性，更有利于后入阅读和修改。
         * 2.使用双向数据绑定来传first和max，并使用$scope.$$postDigest来调用list方法
         */

        
        return {
            restrict: 'E',
            replace: true,
            scope: {
                first: "=", //当前页首位
                max: "=", //单页最大记录数量
                total: "@", //总记录数量
                list: "&",//读取列表的方法
            },
            templateUrl: 'common/fsv/FsvPagination.html',
            link: function ($scope,element,attrs) {

               

                
              

                $scope.getPageData = function(){
                    $scope.$$postDigest($scope.list);
                }

                $scope.init = function(){
                	$scope.currentPage=$scope.first/$scope.max + 1;
                    $scope.pageSizes = [10, 20, 30, 50, 100];
                };
                
                /*
                 * 跳转到第一页
                 */
                $scope.pageToFirst = function () {
                    $scope.first = 0;
                    $scope.currentPage = $scope.first/$scope.max + 1;
                    $scope.getPageData();
                };

                /*
                 * 跳转到上一页
                 */
                $scope.pageBackward = function () {
                   $scope.first =  $scope.first - $scope.max;
                   $scope.currentPage = $scope.first/$scope.max + 1;
                   $scope.getPageData();
                       
                };

                /*
                 * 跳转到下一页
                 */
                $scope.pageForward = function () {
                   $scope.first = parseInt($scope.first) + parseInt($scope.max);
                   $scope.currentPage = $scope.first/$scope.max + 1;
                   $scope.getPageData();
                       
                };

                /*
                 * 跳转到末页
                 */
                $scope.pageToLast = function () {
                    $scope.first =  (Math.ceil($scope.total/ $scope.max) - 1) * $scope.max
                    $scope.currentPage = $scope.first/$scope.max + 1;
                    $scope.getPageData();
                };

                /**
                 * 修改当前页
                 */
                $scope.changeCurrentPage = function(){
                	if(isNaN($scope.currentPage) || $scope.currentPage < 1){
                		$scope.currentPage=1;
                	}else if($scope.currentPage > Math.ceil($scope.total/ $scope.max)){
                        $scope.currentPage = Math.ceil($scope.total/ $scope.max);
                    }
                    $scope.first = ( $scope.currentPage - 1 ) * $scope.max;
                    $scope.getPageData();
                };
                
                $scope.cantPageForward = function() {
                	if(parseInt($scope.first) + parseInt($scope.max)>=$scope.total){
                		return true;
                	}else{
                		return false;
                	}
                };

                /**
                 * 修改页大小
                 */
                $scope.changePageSize = function(){
                    $scope.first = 0;
                    $scope.currentPage = $scope.first/$scope.max + 1;
                    $scope.getPageData();
                };
                
                /**
                 * 解决：分发任务切换分发类型选项卡，没有重新加载分页指令，导致当前页异常
                 */
                $scope.$watch('first', function (newVal, oldVal) {
            		if (newVal != oldVal && newVal==0) {
            			$scope.currentPage=1;
            		}
            	}, true);

                $scope.init();

            }

        }
    });