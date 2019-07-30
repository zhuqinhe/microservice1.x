angular.module('myApp.directives')
	.directive('atPagination',function() {
        return {
            restrict: 'E',
            replace: true,
            scope: true,
            templateUrl: 'common/atPagination/atPagination.html',
            link: function($scope, element, attrs) {
                $scope.footerRowHeight = 35;

                $scope.footerStyle = function() {
                    return { "height": $scope.footerRowHeight + "px" };
                };

                /*
                 * 判断是否可以向前一页或者跳转到第一页
                 */
                $scope.cantPageBackward = function() {// judge is the first page or
                    // can go to first
                    if ($scope.pagingOptions) {
                        if ($scope.pagingOptions.currentPage <= 1) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }

                };

                /*
                 * 判断是否可以向后一页或者跳转到末页
                 */
                $scope.cantPageForward = function() {
                    if ($scope.pagingOptions) {
                        if ($scope.pagingOptions.currentPage < $scope.pagingOptions.maxPages) {
                            return false;
                        } else {
                            return true;
                        }
                    }

                };

                /*
                 * 跳转到第一页
                 */
                $scope.pageToFirst = function() {
                    if ($scope.pagingOptions) {
                        $scope.pagingOptions.currentPage = 1;
                        //$scope.getPageData();
                    }
                };

                /*
                 * 跳转到上一页
                 */
                $scope.pageBackward = function() {
                    if ($scope.pagingOptions) {
                        if ($scope.pagingOptions.currentPage > 1) {
                            $scope.pagingOptions.currentPage = parseInt($scope.pagingOptions.currentPage) - 1;
                        }
                        //$scope.getPageData();
                    }
                };

                /*
                 * 跳转到下一页
                 */
                $scope.pageForward = function() {
                    if ($scope.pagingOptions) {

                        if ($scope.pagingOptions.currentPage < $scope.pagingOptions.maxPages) {
                            $scope.pagingOptions.currentPage = parseInt($scope.pagingOptions.currentPage) + 1;
                        }
                        //$scope.getPageData();
                    }
                };

                /*
                 * 跳转到末页
                 */
                $scope.pageToLast = function() {
                    $scope.pagingOptions.currentPage = $scope.pagingOptions.maxPages;
                    //$scope.getPageData();
                };

                /*
                 * 监视翻页
                 */
                $scope.$watch('pagingOptions', function(newVal, oldVal) {
                    if (newVal.currentPage !== oldVal.currentPage) {
                        if ($scope.pagingOptions.currentPage != '') {
                            if (!isNaN($scope.pagingOptions.currentPage)) {// 为数字
                                if ($scope.pagingOptions.currentPage > $scope.pagingOptions.maxPages) {
                                    $scope.pagingOptions.currentPage = $scope.pagingOptions.maxPages;
                                }
                                $scope.getPageData();
                            } else {
                                // 不为数字
                                $scope.pagingOptions.currentPage = oldVal.currentPage;
                            }
                        }
                    }
                    if (newVal.pageSize !==oldVal.pageSize) {
                        $scope.pagingOptions.currentPage = 1;
                        $scope.getPageData();
                    }
                }, true);

            }

        }
    });