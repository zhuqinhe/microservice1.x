'use strict';

angular.module('myApp.services').service(
		'constService',
		[
				'$http',
				'sharedProperties',
				'$q',
				function($http, sharedProperties, $q) {
					var self = this;
					var prefixUrl = sharedProperties.getServerUrl()
							+ "/v1/sys/const/";
					var constCache = {};

					var list = function(key) {
						if (constCache.hasOwnProperty(key)) {
							var d = $q.defer();
							d.resolve(getCloneFromConstCache(key));
							return d.promise;
						}

						var url = prefixUrl + "list?key=" + key;
						return $http.get(url).then(function(r) {
							constCache[key] = r.data.list;

							return getCloneFromConstCache(key);
						});
					}

					var getCloneFromConstCache = function(key) {
						var list = constCache[key]
						var retList = [];

						if (list != null) {
							list.forEach(function(item) {
								var newItem = {};
								for ( var k in item) {
									newItem[k] = item[k];

								}

								retList.push(newItem);
							});
						}

						return retList;
					}

					self.listMediaTypes = function() {
						return list('MediaType').then(function(r) {

							r.forEach(function(item) {
								var newItem = {};
								item.label = item.label.toLowerCase();
							});
							return r;
						});
					};

					self.listDistributeTypes = function() {
						return list('DistributeType').then(function(r){
							var retlist = [];
							r.forEach(function(i){
								if(i.value != 99){
									retlist.push(i);
								}
							})
							return retlist;
						});
					}

					self.listTaskActions = function() {
						return list('TaskAction');
					};
					self.listTaskProcessConfigs = function() {
						return list('TaskProcess');
					};
					
					self.listProtocol = function(type){
						var d = $q.defer();
						 self.listDistributeTypes().then(function(r){
							var typeLabel = null;
							for(var i=0;i<r.length;i++){
								var item = r[i];
								if(item.value == type){
									typeLabel = item.label;
									break;
								}
							}
							list('Protocol$'+typeLabel).then(function(r2){
								d.resolve(r2);
							});
							
						});
						 
						 return d.promise;
					}

				} ]);