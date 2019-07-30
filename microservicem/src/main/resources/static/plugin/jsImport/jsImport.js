/*
*author benjie
*2017年9月29日
*/
function jsImport(config){
	for(var i = 0;i<config.paths.length;i++){
		var url = "modules/" + config.baseUrl + "/" + config.paths[i] + ".js?a=${prefix.revision}1";
		document.write("<script src='"+url+"'><\/script>");
	}
	
}


function jsImport2(config){
	for(var i = 0;i<config.paths.length;i++){
		var url =  config.baseUrl + "/" + config.paths[i] + ".js?a=${prefix.revision}1";
		document.write("<script src='"+url+"'><\/script>");
	}
	
}