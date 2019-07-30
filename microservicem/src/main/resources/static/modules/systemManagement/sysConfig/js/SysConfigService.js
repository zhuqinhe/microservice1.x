'use strict';

angular.module('myApp.services').service('sysConfigService',['$http','sharedProperties','$q','$translate',function($http,sharedProperties,$q,$translate){
	
	  var self = this;
	  var AREA_CONFIG = "AREA_CONFIG";
	  var ML_CONFIG = "ML_CONFIG";
	  var LANGUAGES_CONFIG = "LANGUAGES_CONFIG";
	  var MULLANGUAGES_CONFIG = "MULLANGUAGES_CONFIG";
	  var RESOLUTION_CONFIG = "RESOLUTION_CONFIG";
	  var DEFINITION_CONFIG = "DEFINITION_CONFIG";
	  var DRM_TYPE_CONFIG = "DRM_TYPE_CONFIG";
	  var IS_3D_CONFIG = "IS_3D_CONFIG";
	  var VIDEO_TYPE_CONFIG = "VIDEO_TYPE_CONFIG";
	  var VIDEO_PROFILE_CONFIG = "VIDEO_PROFILE_CONFIG";
	  var AUDIO_TYPE_CONFIG = "AUDIO_TYPE_CONFIG";
	  var AUDIO_FORMAT_CONFIG = "AUDIO_FORMAT_CONFIG";
	  var SCREEN_TYPE_CONFIG = "SCREEN_TYPE_CONFIG";
	  var SYSTEM_LAYER_CONFIG = "SYSTEM_LAYER_CONFIG";
	  var SERVICE_TYPE_CONFIG = "SERVICE_TYPE_CONFIG";
	  var PROGRAM_TYPE_CONFIG = "PROGRAM_TYPE_CONFIG";
	  var CHANNEL_TYPE_CONFIG = "CHANNEL_TYPE_CONFIG";
	  var RATES_CONFIG = "RATES_CONFIG";
	  var NETWORKDOMAIN = "NETWORKDOMAIN";
	  var RELEASEYEAR_CONFIG="RELEASEYEAR_CONFIG";
	  var SRC_CAST_TYPE_CONFIG="SRC_CAST_TYPE_CONFIG";
	  var DEST_CAST_TYPE_CONFIG  ="DEST_CAST_TYPE_CONFIG ";
	  var prefixUrl = sharedProperties.getServerUrl()+"/v1/sys/config";
	  var configCache = {}; 
	  
	  self.listConfigs = function(key){
		  var valueInCache = getCloneValueFromCache(key); 
		  if(valueInCache != null){
			  var defer = $q.defer();
			  defer.resolve(valueInCache);
			  return defer.promise;
		  }
		  
		  var url = prefixUrl + "/value?key="+key;
		 return $http.get(url).then(function(r){
			 var vo = r.data.vo;
			 configCache[key] = vo;
			 return getCloneValueFromCache(key); 
		  });
	  };
	  
	  
	  var getCloneValueFromCache = function(key) {
			var vo = configCache[key]
			if(vo!=null){
				var cloneVo = JSON.parse(JSON.stringify(vo));
				return cloneVo;
			}
			return null;
		}

	  
	  self.clearCache = function(){
		  configCache = {};
	  };
	  
	  
	  
	  
	  self.listAreaConfigs = function(){
		 // return self.listConfigs(AREA_CONFIG);
		  return self.listConfigs(AREA_CONFIG).then(function(rs){
				var retList = [];
				 rs.forEach(function(r){
					 if(r.key!='all'){
						 retList.push(r);
					 }
				 });
				 return retList;
			  });
	  };
	  
	  self.listBitRateTypeConfigs = function(){
		  return self.listConfigs(ML_CONFIG).then(function(rs){
			var retList = [];
			 rs.forEach(function(r){
				retList.push({
					value:r.value,
					label:r.key
				});
			 });
			 return retList;
		  });
	  };
	
	  self.listLanguageConfigs = function(){
		  return self.listConfigs(LANGUAGES_CONFIG).then(function(r){
			var languages = r;
			var languagesConfigs = [];
			languages = languages.split(",");
			languages.forEach(function(l){
				languagesConfigs.push({label:l,value:l});
			});
			return languagesConfigs;
		  });
	  };
	
	  self.listMullanguagesConfigs = function(){
		//  return self.listConfigs(MULLANGUAGES_CONFIG);
		  return self.listConfigs(MULLANGUAGES_CONFIG).then(function(rs){
				var retList = [];
				 rs.forEach(function(r){
					 if(r.key!='all'){
						 retList.push(r);
					 }
				 });
				 return retList;
			  });
	  };
	  
	  self.listResolutionConfigs = function(){
		  return self.listConfigs(RESOLUTION_CONFIG).then(function(vo){
			  vo.forEach(function(m){
				  m.value = parseInt(m.value);
			  });
			  
			  return vo;
		  });
	  };
	  
	  self.listDefinitionConfigs = function(){
		  return self.listConfigs(DEFINITION_CONFIG).then(function(vo){

			 
			  vo.forEach(function(m){
				  if(m.key!="all"){
					  m['label'] = m.key;
					  m['value'] = parseInt(m.value);
				  }
			  });
			  
			  return vo;
		  
		  });
	 };
	 //$translate.instant("taskSuccessRate")
	 self.listRatesConfigs = function(){
		return self.listConfigs(RATES_CONFIG).then(function(vo){
			var rateCofnigs = [];
			    vo.forEach(function(l){
			    	if(l.key!="all"){
			    		rateCofnigs.push(
								{
							     label:l.key,
							     value:parseInt(l.value)
							     }
								);
			    	}
				
			});
			return rateCofnigs;

		});
	 }
	 
	 self.listIs3dConfigs = function(){
		 return self.listConfigs(IS_3D_CONFIG).then(function(rs){
			 var retList = [];
		     rs.forEach(function(r){
				retList.push({
					value:parseInt(r.value),
					label:$translate.instant(r.key)
				});
			 });
			 return retList;
	 });
	 };
	 
	 self.listVideoTypeConfigs = function(){
		 return self.listConfigs(VIDEO_TYPE_CONFIG).then(function(rs){
			 var retList = [];
		     rs.forEach(function(r){
				retList.push({
					value:parseInt(r.value),
					label:r.key
				});
			 });
			 return retList;
	 });
	 };
	 
	 self.listVideoProfileConfigs = function(){
		return self.listConfigs(VIDEO_PROFILE_CONFIG).then(function(rs){
			 var retList = [];
		     rs.forEach(function(r){
				retList.push({
					value:parseInt(r.value),
					label:r.key
				});
			 });
			 return retList;
	 }); 
	 };
	 
	 self.listAudioTypeConfigs = function(){
		return self.listConfigs(AUDIO_TYPE_CONFIG).then(function(rs){
			 var retList = [];
		     rs.forEach(function(r){
				retList.push({
					value:parseInt(r.value),
					label:$translate.instant(r.key)
				});
			 });
			 return retList;
	    });  
	 };
	 
	 self.listAudioFormatConfigs = function(){
		return self.listConfigs(AUDIO_FORMAT_CONFIG).then(function(rs){
			 var retList = [];
		     rs.forEach(function(r){
				retList.push({
					value:parseInt(r.value),
					label:r.key
				});
			 });
			 return retList;
	    });  
	 };
	 
	 self.listScreenTypeConfigs = function(){
		return self.listConfigs(SCREEN_TYPE_CONFIG).then(function(rs){
			 var retList = [];
		     rs.forEach(function(r){
				retList.push({
					value:parseInt(r.value),
					label:r.key
				});
			 });
			 return retList;
	    });  
	 };
	 
	 self.listSystemLayerConfigs = function(){
		 return self.listConfigs(SYSTEM_LAYER_CONFIG).then(function(rs){
			 var retList = [];
		     rs.forEach(function(r){
				retList.push({
					value:parseInt(r.value),
					label:r.key
				});
			 });
			 return retList;
	    });  
	 };
	 
	 self.listServiceTypeConfigs = function(){
		 return self.listConfigs(SERVICE_TYPE_CONFIG).then(function(rs){
			 var retList = [];
		     rs.forEach(function(r){
				retList.push({
					value:parseInt(r.value),
					label:$translate.instant(r.key)
				});
			 });
			 return retList;
	    });  
	 };
	  
	 self.listDrmTypeConfigs = function(){
		 return self.listConfigs(DRM_TYPE_CONFIG).then(function(rs){
			 var retList = [];
		     rs.forEach(function(r){
				retList.push({
					value:parseInt(r.value),
					label:r.key
				});
			 });
			 return retList;
	 });
	 };
	 
	 self.listProgramTypeConfigs = function(){
		return self.listConfigs(PROGRAM_TYPE_CONFIG).then(function(rs){
				 var retList = [];
			     rs.forEach(function(r){
					retList.push({
						value:parseInt(r.value),
						label:r.key
					});
				 });
				 return retList;
		 });
	 };
	
     self.listChannelTypeConfigs = function(){
		 return self.listConfigs(CHANNEL_TYPE_CONFIG).then(function(rs){
			 var retList = [];
		     rs.forEach(function(r){
				retList.push({
					value:parseInt(r.value),
					label:$translate.instant(r.key)
				});
			 });
			 return retList;
	 });
	 };
	  self.listChannelSRC_CAST_TYPE_Configs = function(){
			 return self.listConfigs(SRC_CAST_TYPE_CONFIG).then(function(rs){
				 var retList = [];
			     rs.forEach(function(r){
					retList.push({
						value:parseInt(r.value),
						label:r.key
					});
				 });
				 return retList;
		 });
		 };
		  self.listChannelDESC_CAST_TYPE_Configs = function(){
				 return self.listConfigs(DEST_CAST_TYPE_CONFIG).then(function(rs){
					 var retList = [];
				     rs.forEach(function(r){
						retList.push({
							value:parseInt(r.value),
							label:r.key
						});
					 });
					 return retList;
			 });
			 };
			 
			 
		 self.listDIS_PROTOCOL_CONFIG = function(){
			 return self.listConfigs(DIS_PROTOCOL_CONFIG).then(function(rs){
				 var retList = [];
			     rs.forEach(function(r){
					retList.push({
						value:parseInt(r.value),
						label:r.key
					});
				 });
				 return retList;
			 });
		 };
	 self.listPicConfigs = function(){
		return $http.get(sharedProperties.getServerUrl()+'/v1/sys/picconfig/list').then(function(r){
			return r.data.vo;
		});
		
	 };

	 self.listNetworkDomainConfigs = function(){
		return self.listConfigs(NETWORKDOMAIN).then(function(rs){	
			 var retList = [];
		     rs.forEach(function(r){
				retList.push({
					value:r.value,
					label:r.key
				});
			 });
			 return retList;
			 });
	 };
	 self.listYearsConfigs = function(){
		// return self.listConfigs(RELEASEYEAR_CONFIG); 
		  return self.listConfigs(RELEASEYEAR_CONFIG).then(function(rs){
				var retList = [];
				 rs.forEach(function(r){
					 if(r.key!='all'){
						 retList.push(r);
					 }
				 });
				 return retList;
			  });
	 };
	  
	 
}]);