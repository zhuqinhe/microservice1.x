'use strict';

/* Filters */

angular.module('myApp.filters', []).filter('encodeURIComponent', function() {
    return window.encodeURIComponent;
});

angular.module('myApp.filters').filter('ceil', function() {
  return Math.ceil;
});