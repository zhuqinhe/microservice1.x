'use strict';
/* Controllers */
angular.module('myApp.controllers').controller(
		'sysconfigControllers',
		[
				'$scope',
				'$http',
				'$routeParams',
				'$location',
				'$log',
				'$window',
				'sharedProperties',
				'loginService',
				'$translate',
				'$rootScope',
				'$cookieStore',
				'commonMethod',
				'sysConfigService',
				function($scope, $http, $routeParams, $location, $log, $window, sharedProperties, loginService, $translate, $rootScope, $cookieStore, commonMethod,
						sysConfigService) {

					$scope.formatDate = function(date) {
						if (date != null) {
							var date = new Date(date);
							return date.format("yyyy-MM-dd hh:mm:ss");
						}
					}
					$scope.checkTime = function(time) {
						if (time && time != "") {
							var reg = /^([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2}):([0-9]{2}):([0-9]{2})$/;
							if (!reg.test(time.trim()) || isNaN(get_unix_time(time))) {
								time = null;
							}
						}
					}

					$scope.changeManagePage = function(flag) {
						$scope.manFlag = flag;
						$rootScope.manFlag = flag;
						$scope.getPageData();
					}

					// 添加界面跳转
					$scope.toAddPage = function(flag) {
						$scope.manFlag = flag;
						$location.path('/sys/sysconfig/contentid/add/' + flag);
						$rootScope.manFlag = flag;
					}

					$scope.saveItem = function(manFlag) {
						console.log("save", $scope.data);
						if (1 == manFlag) {// 保存图片
							var url = sharedProperties.getServerUrl() + '/ui/v1/sys/picconfig/update';
							$scope.pics = {
								vod : $scope.vod,
								channel : $scope.channel,
								app : $scope.app,
								cast : $scope.cast,
								category : $scope.category,
								other : $scope.other,
								specialtopic : $scope.specialtopic,
								ad : $scope.ad
							}
							$http.put(url, $scope.pics).success(function(data) {
								// alert("提交成功了");
								if (data.resultCode != 0) {
									// 保存失败
									$scope.message = $rootScope.l10n.saveFail;
									commonMethod.tipDialog($scope.message);
								} else {
									$scope.message = $rootScope.l10n.savaSuccess;
									commonMethod.tipDialog($scope.message); // 保存成功弹框提示
								}
								$routeParams.manFlag = 1;
								$scope.init();
							});
						} else {
							var exeTime = null;
							if ($scope.data.exeTime && $scope.data.exeTime != "") {
								exeTime = get_unix_time($scope.data.exeTime);
							}
							$scope.data.exeTime = exeTime;

							$scope.datatmp = {
								"head" : $scope.data.head,
								"number" : $scope.data.number,
								"exeTime" : $scope.data.exeTime
							};
							$http["post"](sharedProperties.getServerUrl() + '/ui/v1/sys/contentid/add', $scope.datatmp, {
								headers : {
									'content-Type' : 'application/json'
								}
							}).success(function(data) {
								if (data.resultCode != 0) {
									$scope.message = $rootScope.l10n.saveFail;
									commonMethod.tipDialog($scope.message);
								} else {
									$scope.manFlag = manFlag;
									$location.path('/sys/sysconfig/list/0');
								}
							}).error(function(xhr, data) {
								$scope.manFlag = manFlag;
								$location.path('/sys/sysconfig/list/0');
							});
						}

						/*
						 * $.ajax({ url : sharedProperties.getServerUrl() + '/rest/v1/sys/contentid/add', type : "put", dataType : "json", contentType :
						 * "application/json;charset=utf-8", data : JSON.stringify($scope.datatmp), success : function(data) { if (data.resultCode != 0) { $scope.message =
						 * $rootScope.l10n.saveFail; commonMethod.tipDialog($scope.message); } else { $scope.manFlag = manFlag; $location.path('/sys/sysconfig/list/0'); } },
						 * error:function(data){ $scope.manFlag = manFlag; $location.path('/sys/sysconfig/list/0'); } });
						 */

					}

					$scope.checkHead = function() {

						if ($scope.data.head != null && $scope.data.head != "") {
							$http["get"](sharedProperties.getServerUrl() + "/ui/v1/sys/contentid/checkhead/" + $scope.data.head).success(function(data, status) {
								$scope.repeatHead = data.checkheadResult;
							}).error(function() {
								$scope.repeatHead = data.checkheadResult;
							});
						}
					};

					$scope.init = function() {

						$scope.collapsed = true;// 默认更多条件是否展开
						$scope.repeatHead = 0;
						$scope.pagingOptions = {// paging fields
							pageSizes : [ 10, 20, 30, 50, 100 ],
							pageSize : 10,
							currentPage : 1,
							total : 0,// total list count
							maxPages : 0
						};
						$scope.queryOptions = {// 查询字段
							head : null,
							status : "null",
						};
						$(".validtimepicker").datetimepicker({
							showSecond : true,
							timeFormat : 'HH:mm:ss',
							changeMonth : true,
							changeYear : true,
							dateFormat : 'yy/mm/dd'
						});
						// 初始进入控制器默认选中contentids对应的tab标签页
						// 值 0：contentIds 1:图片配置 2系统参数 3：图标配置
						if ($routeParams.manFlag != null && $routeParams.manFlag != undefined) {
							$scope.manFlag = 2;
						} else {
							$scope.manFlag = 2;
						}
						$scope.getPageData();
					};
					// contentids 查询
					$scope.contentidsSearch = function() {
						$scope.getPageData();
					}
					$scope.contentidsRest = function() {
						$scope.queryOptions = {// 查询字段
							head : null,
							status : "null"
						};
						$scope.getPageData();
					}
					/*
					 * manFlag 0:contentid 1:picconfig 2:sysConfig 3:sysConstant 4:logConfig
					 */
					$scope.getPageData = function() {
						if ($scope.pagingOptions.currentPage <= 0) {
							$scope.pagingOptions.currentPage = 1;
						}
						if ($scope.manFlag == 0) {} else if ($scope.manFlag == 1) {} else if ($scope.manFlag == 2) {
						
							$scope.name = "";
							$http.get(sharedProperties.getServerUrl() + '/ui/v1/sys/config/list').success(function(largeLoad) {
								$scope.pagingOptions.total = largeLoad.list.length; // 存放list总记录数
								$scope.pagingOptions.maxPages = Math.ceil($scope.pagingOptions.total / $scope.pagingOptions.pageSize);
								$scope.items = largeLoad.list;
								// console.log(largeLoad);
							});
						} else if ($scope.manFlag == 3) {} else if ($scope.manFlag == 4) {
							$http.get(sharedProperties.getServerUrl() + '/ui/v1/sys/config/bykey?key=logo_pic_config').success(function(largeLoad) {

								$scope.data = largeLoad.vo;
								if (largeLoad.vo != null) {
									var value = JSON.parse($scope.data.value)
									$scope.login_logo = value.login;
									$scope.system_logo = value.system;
								} else {
									$scope.data = {"value":""};
								}
							});
						}
					};

					/*
					 * 打开文件选择对话框
					 */
					$scope.openFileDlg = function(event) {

						var t = commonMethod.openPicDialog(event);
						t.closePromise.then(function() {
							if (event.currentTarget.id == "login_logo") {
								$scope.login_logo = $rootScope.tmpbg || $scope.login_logo;
							} else if (event.currentTarget.id == "system_logo") {
								$scope.system_logo = $rootScope.tmpbg || $scope.system_logo;
							}
							// console.log($scope.data);
						});
					};

					// 保存logo配置
					$scope.saveLogo = function() {
						var url = sharedProperties.getServerUrl() + '/ui/v1/sys/config/update';
						var temp = {
							"login" : $scope.login_logo,
							"system" : $scope.system_logo
						}
						$scope.data.value = JSON.stringify(temp);
						$http.put(url, $scope.data).success(function(data) {
							if (data.resultCode != 0) {
								// 保存失败弹框提示
								$scope.message = $rootScope.l10n.saveFail;
								commonMethod.tipDialog($scope.message);
							} else {
								// 保存成功弹框提示
								$scope.message = $rootScope.l10n.savaSuccess;
								commonMethod.tipDialog($scope.message);
								$location.path('/sys/sysconfig/list/0');
							}
						})
					};

					// 增加对应配置的记录
					$scope.addPicConfig = function(config, editFlag, index) {
						// editFlag为true，添加时默认为编辑状态；为false时为只读状态
						// $scope.poster.editFlag = editFlag;
						var len = config.length;
						if (len == 0) {
							var meta = {
								width : 0,
								height : 0,
								isDefault : true,
								process : true,
								editFlag : true
							};
						} else {
							meta = {
								width : 0,
								height : 0,
								isDefault : false,
								process : true,
								editFlag : true
							};
						}
						config.push(meta);
					};
					// 删除对应配置的记录
					$scope.removePicConfig = function(config, index) {
						config.splice(index, 1);
					};
					// 键被弹起
					$scope.event_onkeyup = function(poster, type) {
						if (type == 1) {
							poster.width = poster.width.replace(/\D/g, '');
						} else if (type == 2) {
							poster.height = poster.height.replace(/\D/g, '');
						}
					};
					// 日期校验
					function get_unix_time(dateStr) {
						var newstr = dateStr.replace(/-/g, '/');
						var date = new Date(newstr);
						var time_str = date.getTime().toString();
						return time_str;
					}
					// 启用配置
					$scope.startStopConfig = function(cfg, event) {
						// event.preventDefault();
						// console.log("enable config :" + cfg.key);
						var url = sharedProperties.getServerUrl() + "/ui/v1/sys/config/enable?key=" + cfg.key;
						$http['put'](url).success(function(data) {
							sysConfigService.clearCache();
							// alert("提交成功了");
							if (data.resultCode != 0) {
								// 保存失败弹框提示
								$scope.message = $translate.instant("saveFail");
								commonMethod.tipDialog($scope.message);
							} else {
								$scope.message = $translate.instant("saveSuccess");
								commonMethod.tipDialog($scope.message);
							}

							$routeParams.manFlag = $scope.manFlag;
							$scope.init();
						})
					};

					$scope.refresh = function() {
							$scope.init();
					};

					$scope.toContentEdit = function(config) {
						$rootScope.cfgValues = config;
						window.location = sharedProperties.getServerUrl() + "/index.html#/sys/config/edit/" + config.key;
					};
					// 清理系統緩存
					$scope.clean = function() {
						var url = sharedProperties.getServerUrl() + '/ui/v1/sys/config/clean';
						$http.put(url).success(function(data) {
							sysConfigService.clearCache();
							// alert("提交成功了");
							if (data.resultCode != 0) {
								// 保存失败弹框提示
								$scope.message = $translate.instant("cleanfail");
								commonMethod.tipDialog($scope.message);
							} else {
								$scope.message = $translate.instant("cleansuccess");
								commonMethod.tipDialog($scope.message);
							}
							$routeParams.manFlag = $scope.manFlag;
							$scope.init();
						})
					}

					$scope.init();// 调用初始化函数

				} ])
// 系统配置修改页面控制器
.controller(
		'cfgContentEditCtrl',
		[
				'$rootScope',
				'$scope',
				'$modal',
				'$log',
				'$http',
				'$routeParams',
				'sharedProperties',
				'loginService',
				'$translate',
				'$location',
				'commonMethod',
				function($rootScope, $scope, $modal, $log, $http, $routeParams, sharedProperties, loginService, $translate, $location, commonMethod) {
					$scope.id = $routeParams.configId;
					$scope.addRest = function() {
						$scope.data = {
							"key" : "",
							"value" : [ {} ],
							"desc" : "",
							"enable" : true
						};
					}
					$scope.editRest = function() {
						$http.get(sharedProperties.getServerUrl() + '/ui/v1/sys/config/detail?key=' + $scope.id).success(function(result) {
							if (result.resultCode != 0) {
								$location.path('/sys/config/edit/2');
							} else {
								$scope.data = result.vo;
							
								// $scope.data.value = JSON.stringify($scope.data.value, null, 4);
							}
						}).error(function(e) {
							console.log(">>>>>get config  data error.");
						});
					}

					// $scope.data = null;
					$scope.init = function() {
						$scope.data = {};
						if ($scope.id == 0) {
							$scope.addRest();
						} else {
							$scope.editRest();
						}
					}
					$scope.saveValue = function() {
						var url = sharedProperties.getServerUrl() + '/ui/v1/sys/config/update';
						$http.put(url, $scope.data).success(function(data) {
							if (data.resultCode != 0) {
								// 保存失败弹框提示
								$scope.message = $translate.instant("saveFail");
								commonMethod.tipDialog($scope.message);
							} else {
								$scope.message = $translate.instant("saveSuccess");
								commonMethod.tipDialog($scope.message);
								if($scope.data.type=="config"){
									$location.path('/sys/sysconfig/list/2');
								}
								else if ($scope.data.type=="constant"){
									$location.path('/sys/sysconfig/list/3');
								}
							}
						})
					}
					
					// json文本格式化工具方法--开始
					window.SINGLE_TAB = "  ";
					window.ImgCollapsed = "modules/systemManagement/sysConfig/Collapsed.gif";
					window.ImgExpanded = "modules/systemManagement/sysConfig/Expanded.gif";
					window.QuoteKeys = true;
					window._dateObj = new Date();
					window._regexpObj = new RegExp();
					function $id(id) {
						return document.getElementById(id);
					}
					// 判断是否为json数组
					$scope.IsArray = function(obj) {
						return obj && typeof obj === 'object' && typeof obj.length === 'number' && !(obj.propertyIsEnumerable('length'));
					}
					$scope.GetRow = function(indent, data, isPropertyContent) {
						var tabs = "";
						// console.log("GetRow data:"+data);
						for (var i = 0; i < indent && !isPropertyContent; i++)
							tabs += window.TAB;
						if (data != null && data.length > 0 && data.charAt(data.length - 1) != "\n")
							data = data + "\n";
						return tabs + data;
					}
					// 格式化json
					$scope.Process = function() {
						// console.log("process json:",$scope.data.value);
						$scope.SetTab();
						window.IsCollapsible = $id("CollapsibleView").checked;
						var json = $scope.data.value;
						var html = "";
						try {
							if (json == "")
								json = "\"\"";
							var obj = eval("[" + json + "]");
							html = $scope.ProcessObject(obj[0], 0, false, false, false);
							$id("Canvas").innerHTML = "<PRE class='CodeContainer'>" + html + "</PRE>";
						} catch (e) {

							console.log("JSON数据格式不正确:\n" + e.name + ":" + e.message + "\n" + e.stack);
							// alert("JSON数据格式不正确:\n"+e.message);
							$id("Canvas").innerHTML = "<font color=\"red\">JSON数据格式不正确 :\n <br/>" + e.message + "</font>";
						}
					}

					$scope.ProcessObject = function(obj, indent, addComma, isArray, isPropertyContent) {
						var html = "";
						var comma = (addComma) ? "<span class='Comma'>,</span> " : "";
						var type = typeof obj;
						var clpsHtml = "";
						if ($scope.IsArray(obj)) {
							console.log("obj is a array");
							if (obj.length == 0) {
								html += $scope.GetRow(indent, "<span class=\'ArrayBrace\'>[ ]</span>" + comma, isPropertyContent);
							} else {
								clpsHtml = window.IsCollapsible ? "<span><img src=\"" + window.ImgExpanded
										+ "\" onclick=\"ExpImgClicked(this)\" /></span><span class='collapsible'>" : "";
								html += $scope.GetRow(indent, "<span class='ArrayBrace'>[</span>" + clpsHtml, isPropertyContent);
								for (var i = 0; i < obj.length; i++) {
									html += $scope.ProcessObject(obj[i], indent + 1, i < (obj.length - 1), true, false);
								}
								clpsHtml = window.IsCollapsible ? "</span>" : "";
								html += $scope.GetRow(indent, clpsHtml + "<span class=\'ArrayBrace\'>]</span>" + comma);
							}
						} else if (type == 'object') {
							console.log("obj is not a array");
							if (obj == null) {
								html += $scope.FormatLiteral("null", "", comma, indent, isArray, "Null");
							} else if (obj.constructor == window._dateObj.constructor) {
								html += $scope.FormatLiteral("new Date(" + obj.getTime() + ") /*" + obj.toLocaleString() + "*/", "", comma, indent, isArray, "Date");
							} else if (obj.constructor == window._regexpObj.constructor) {
								html += $scope.FormatLiteral("new RegExp(" + obj + ")", "", comma, indent, isArray, "RegExp");
							} else {
								var numProps = 0;
								for ( var prop in obj)
									numProps++;
								if (numProps == 0) {
									html += $scope.GetRow(indent, "<span class='ObjectBrace'>{ }</span>" + comma, isPropertyContent);
								} else {
									clpsHtml = window.IsCollapsible ? "<span><img src=\"" + window.ImgExpanded
											+ "\" onclick=\"ExpImgClicked(this)\" /></span><span class='collapsible'>" : "";
									html += $scope.GetRow(indent, "<span class='ObjectBrace'>{</span>" + clpsHtml, isPropertyContent);
									var j = 0;
									for ( var prop in obj) {
										var quote = window.QuoteKeys ? "\"" : "";
										html += $scope.GetRow(indent + 1, "<span class='PropertyName'>" + quote + prop + quote + "</span>: "
												+ $scope.ProcessObject(obj[prop], indent + 1, ++j < numProps, false, true));
									}
									clpsHtml = window.IsCollapsible ? "</span>" : "";
									html += $scope.GetRow(indent, clpsHtml + "<span class='ObjectBrace'>}</span>" + comma);
								}
							}
						} else if (type == 'number') {
							html += $scope.FormatLiteral(obj, "", comma, indent, isArray, "Number");
						} else if (type == 'boolean') {
							html += $scope.FormatLiteral(obj, "", comma, indent, isArray, "Boolean");
						} else if (type == 'function') {
							if (obj.constructor == window._regexpObj.constructor) {
								html += $scope.FormatLiteral("new RegExp(" + obj + ")", "", comma, indent, isArray, "RegExp");
							} else {
								obj = FormatFunction(indent, obj);

								html += $scope.FormatLiteral(obj, "", comma, indent, isArray, "Function");
							}
						} else if (type == 'undefined') {
							html += $scope.FormatLiteral("undefined", "", comma, indent, isArray, "Null");
						} else {
							html += $scope.FormatLiteral(obj.toString().split("\\").join("\\\\").split('"').join('\\"'), "\"", comma, indent, isArray, "String");
						}
						return html;
					}
					$scope.FormatLiteral = function(literal, quote, comma, indent, isArray, style) {
						if (typeof literal == 'string')
							literal = literal.split("<").join("&lt;").split(">").join("&gt;");
						var str = "<span class='" + style + "'>" + quote + literal + quote + comma + "</span>";
						if (isArray)
							str = $scope.GetRow(indent, str);
						return str;

					}
					$scope.SetTab = function() {
						var select = $id("TabSize");
						window.TAB = $scope.MultiplyString(parseInt(select.options[select.selectedIndex].value), window.SINGLE_TAB);
					}
					$scope.TabSizeChanged = function() {
						$scope.Process();
					}
					$scope.QuoteKeysClicked = function() {
						window.QuoteKeys = $id("QuoteKeys").checked;
						$scope.Process();
					}
					$scope.MultiplyString = function(num, str) {
						var sb = [];
						for (var i = 0; i < num; i++) {
							sb.push(str);
						}
						return sb.join("");
					}
					// 选择所有内容
					$scope.SelectAllClicked = function() {
						if (!!document.selection && !!document.selection.empty) {
							document.selection.empty();
						} else if (window.getSelection) {
							var sel = window.getSelection();
							if (sel.removeAllRanges) {
								window.getSelection().removeAllRanges();
							}
						}
						var range = (!!document.body && !!document.body.createTextRange) ? document.body.createTextRange() : document.createRange();
						if (!!range.selectNode)
							range.selectNode($id("Canvas"));
						else if (range.moveToElementText)
							range.moveToElementText($id("Canvas"));
						if (!!range.select)
							range.select($id("Canvas"));
						else
							window.getSelection().addRange(range);
					}
					$scope.CollapsibleViewClicked = function() {
						$id("CollapsibleViewDetail").style.visibility = $id("CollapsibleView").checked ? "visible" : "hidden";
						$scope.Process();
					}
					// 收起选项
					$scope.CollapseAllClicked = function() {
						$scope.EnsureIsPopulated();
						$scope.TraverseChildren($id("Canvas"), function(element) {
							if (element.className == 'collapsible') {
								$scope.MakeContentVisible(element, false);
							}
						}, 0);
					}
					// 展开所有
					$scope.ExpandAllClicked = function() {
						$scope.EnsureIsPopulated();
						$scope.TraverseChildren($id("Canvas"), function(element) {
							if (element.className == 'collapsible') {
								$scope.MakeContentVisible(element, true);
							}
						}, 0);
					}
					$scope.EnsureIsPopulated = function() {
						if (!$id("Canvas").innerHTML && !!$id("RawJson").value)
							$scope.Process();
					}
					$scope.TraverseChildren = function(element, func, depth) {
						for (var i = 0; i < element.childNodes.length; i++) {
							$scope.TraverseChildren(element.childNodes[i], func, depth + 1);
						}
						func(element, depth);
					}
					$scope.MakeContentVisible = function(element, visible) {
						var img = element.previousSibling.firstChild;
						if (!!img.tagName && img.tagName.toLowerCase() == "img") {
							element.style.display = visible ? 'inline' : 'none';
							element.previousSibling.firstChild.src = visible ? window.ImgExpanded : window.ImgCollapsed;
						}
					}
					// 图片点击事件
					window.ExpImgClicked = function(img) {
						// console.log("ExpImgClicked");
						var container = img.parentNode.nextSibling;
						if (!container)
							return;
						var disp = "none";
						var src = window.ImgCollapsed;
						if (container.style.display == "none") {
							disp = "inline";
							src = window.ImgExpanded;
						}
						container.style.display = disp;
						img.src = src;
					}
					// 按等级展开
					$scope.CollapseLevel = function(level) {
						$scope.EnsureIsPopulated();
						$scope.TraverseChildren($id("Canvas"), function(element, depth) {
							if (element.className == 'collapsible') {
								if (depth >= level) {
									$scope.MakeContentVisible(element, false);
								} else {
									$scope.MakeContentVisible(element, true);
								}
							}
						}, 0);
					}
					// json格式化代码--结束

					$scope.init();

				} ]).directive(
		'picGrid',
		function() {
			return {
				template : '<div ng-repeat="poster in picGroup.data" style="display: inline-block; margin-right: 9px; vertical-align: bottom">'
						//其他图片可编辑
						+ '<div ng-show="!poster.editFlag&&!poster.isDefault" style="text-align:center; width: 100px; height: 80px; padding-top: 10px;" >'
						+ '<span style="cursor:pointer;font-size: larger; display: inherit; height: 30px;" '
						+ 'ng-click="poster.editFlag=!poster.editFlag" title="{{\'clickToEdit\'|translate}}">' + '{{poster.width}}*{{poster.height}}' + '</span>'
						+ '<input ng-model="poster.process" type="checkbox" checked="checked" />{{\'needProcess\'|translate}}' + '</div>'
						//原图不可编辑
						+'<div ng-show="poster.isDefault" style="text-align:center; width: 100px; height: 80px; padding-top: 10px;" >'
						+ '<span style="cursor:pointer;font-size: larger; display: inherit; height: 30px;" '
						+ 'title="{{\'artworktitle\'|translate}}">' + '{{\'artwork\'|translate}}' + '</span>'
						+ '<input ng-model="poster.process" type="checkbox" checked="checked" ng-disabled="poster.isDefault"/>{{\'needProcess\'|translate}}' + '</div>'
						//操作按钮
						+ '<div style="width: 100px; height:60px; vertical-align: baseline;padding-top: 10px;" ' + 'ng-show="poster.editFlag">' + '<!-- edit -->' + '<span>'
						+ '<input ng-change="event_onkeyup(poster,1)" style="width: 45px;"  ng-model="poster.width"/>*'
						+ '<input ng-change="event_onkeyup(poster,2)" style="width: 45px;" ng-model="poster.height"/>'
						+ '</span><hr style="margin-top: 0; border-top: 0; margin-bottom: 0" />'
						+ '<button class="btn btn-success " style="margin-top: 25px; padding:3px 4px 3px 4px;" ' + 'ng-click="poster.editFlag=!poster.editFlag">'
						+ '<i class="icon-ok "></i>' + '</button>' +

						'</div>' + '<button ng-disabled="poster.isDefault"  class="btn btn-danger pull-right" style="padding:3px 4.5px 3px 4.5px;" ' + 'ng-click="removePicConfig(picGroup.data,$index)">'
						+ '<img src="img/tool/icon_delete.png" ></img>' + '</button>' + '</div>'
						+ '<button class="btn btn-info " ng-click="addPicConfig(picGroup.data,true,$index)" ' + 'style=" padding:3px 4.5px 3px 4.5px; ">'
						+ '<img src="img/tool/icon_+.png" ></img>' + '</button>'
			}
		});
