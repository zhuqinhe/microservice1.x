$.fn.croneditor = function(opts) {

	var el = this;

	// Write the HTML template to the document
	$(el).html(tmpl);

	var cronArr = ["*", "*", "*", "*", "*", "?"];
	if (typeof opts.value === "string") {
		cronArr = opts.value.split(' ');
	}

	$(".tabs")
			.tabs(
					{
						activate : function(event, ui) {
							switch ($(ui.newTab).attr('id')) {

								// Seconds
								case 'button-second-every' :
									cronArr[0] = "*";
									break;
								case 'button-second-n' :
									cronArr[0] = "*/"
											+ $("#tabs-second .slider").slider(
													"value");
									break;
								case 'button-second-range' :
									cronArr[0] = "1-2";// + $(
														// "#tabs-second-range
														// .slider"
														// ).slider("value");
									break;
								case 'button-second-each' :
									cronArr[0] = "*";
									// TODO: toggle off selected minutes on load
									// $('.tabs-minute-format
									// input[checked="checked"]').click()
									$('.tabs-second-format').html('');
									drawEachSeconds();
									break;
								// Minutes
								case 'button-minute-every' :
									cronArr[1] = "*";
									break;
								case 'button-minute-n' :
									cronArr[1] = "*/"
											+ $("#tabs-minute .slider").slider(
													"value");
									break;
								case 'button-minute-range' :
									cronArr[1] = "1-2";// + $(
														// "#tabs-second-range
														// .slider"
														// ).slider("value");
									break;
								case 'button-minute-each' :
									cronArr[1] = "*";
									// TODO: toggle off selected minutes on load
									// $('.tabs-minute-format
									// input[checked="checked"]').click()
									$('.tabs-minute-format').html('');
									drawEachMinutes();
									break;

								// Hours
								case 'button-hour-every' :
									cronArr[2] = "*";
									break;
								case 'button-hour-n' :
									cronArr[2] = "*/"
											+ $("#tabs-hour .slider").slider(
													"value");
									break;
								case 'button-hour-range' :
									cronArr[2] = "0-2";
									break;
								case 'button-hour-each' :
									cronArr[2] = "*";
									$('.tabs-hour-format').html('');
									drawEachHours();
									break;

								// Days
								case 'button-day-every' :
									cronArr[3] = "*";
									break;
								case 'button-day-n' :
									cronArr[3] = "*/"
											+ $("#tabs-day .slider").slider(
													"value");
									break;
								case 'button-day-range' :
									cronArr[3] = "1-2";
									break;
								case 'button-day-each' :
									cronArr[3] = "*";
									$('.tabs-day-format').html('');
									drawEachDays();
									break;
								case 'button-day-nearest' :
									cronArr[3] = "1W";
									break;
								case 'button-day-last' :
									cronArr[3] = "L";
									break;
								// Months
								case 'button-month-every' :
									cronArr[4] = "*";
									break;
								case 'button-month-n' :
									cronArr[4] = "*/"
											+ $("#tabs-month .slider").slider(
													"value");
									break;
								case 'button-month-range' :
									cronArr[4] = "1-2";
									break;
								case 'button-month-each' :
									cronArr[4] = "*";
									$('.tabs-month-format').html('');
									drawEachMonths();
									break;

								// Weeks
								case 'button-week-every' :
									cronArr[5] = "*";
									break;
								case 'button-week-last' :
									cronArr[5] = "1L";
									break;
								case 'button-week-each' :
									cronArr[5] = "?";
									$('.tabs-week-format').html('');
									drawEachWeek();
									break;
								case 'button-week-specify' :
									cronArr[5] = "1#1";
									break;
								case 'button-week-none' :
									cronArr[5] = "?";
									break;

							}

							drawCron();
						}
					});

	function drawCron() {

		var newCron = cronArr.join(' ');
		$('#cronString').val(newCron);
		// TODO: add back next estimated cron time
		/*
		 * var last = new Date(); $('.next').html(''); var job = new
		 * cron.CronTime(newCron); var next = job._getNextDateFrom(new Date());
		 * $('.next').append('<span id="nextRun">' + dateformat(next, "ddd mmm
		 * dd yyyy HH:mm:ss") + '</span><br/>');
		 */
		/*
		 * setInterval(function(){ drawCron(); }, 500);
		 */
		/*
		 * $('#cronString').keyup(function(){ cronArr =
		 * $('#cronString').val().split(' '); console.log('updated', cronArr)
		 * });
		 */
	}

	$('#clear').click(function() {
		$('#cronString').val('* * * * * ?');
		cronArr = ["*", "*", "*", "*", "*", "?"];

	});

	$('#startSecond').bind('input propertychange', function() {
		if ($(this).val() > 59) {
			$(this).val(59);
			cronArr[0] = 59 + "/" + cronArr[0].split("/")[1];
		} else {
			cronArr[0] = $(this).val() + "/" + cronArr[0].split("/")[1];
		}

		drawCron();
	});
	$('#startMinute').bind('input propertychange', function() {
		if ($(this).val() > 59) {
			$(this).val(59);
			cronArr[1] = 59 + "/" + cronArr[1].split("/")[1];
		} else {
			cronArr[1] = $(this).val() + "/" + cronArr[1].split("/")[1];
		}
		drawCron();
	});
	$('#startHour').bind('input propertychange', function() {
		if ($(this).val() > 59) {
			$(this).val(59);
			cronArr[2] = 59 + "/" + cronArr[2].split("/")[1];
		} else {
			cronArr[2] = $(this).val() + "/" + cronArr[2].split("/")[1];
		}
		drawCron();
	});
	$('#startDay').bind('input propertychange', function() {
		if ($(this).val() > 31) {
			$(this).val(31);
			cronArr[3] = 31 + "/" + cronArr[3].split("/")[1];
		} else {
			cronArr[3] = $(this).val() + "/" + cronArr[3].split("/")[1];
		}
		drawCron();
	});
	$('#nearestDay').bind('input propertychange', function() {
		cronArr[3] = $(this).val() + "W";
		drawCron();
	});
	$('#monthDay').bind('input propertychange', function() {
		if ($(this).val() > 31) {
			$(this).val(31);
			cronArr[4] = 31 + "/" + cronArr[4].split("/")[1];
		} else {
			cronArr[4] = $(this).val() + "/" + cronArr[4].split("/")[1];
		}
		drawCron();
	});
	$('#monthWeek').bind('input propertychange', function() {
		if ($(this).val() > 4) {
			$(this).val(4);
			cronArr[5] = "4L";
		} else {
			cronArr[5] = $(this).val() + "L";
		}
		drawCron();
	});
	$('#theWeeks').bind('input propertychange', function() {
		if ($(this).val() > 4) {
			$(this).val(4);
			cronArr[5] = 4 + "#" + cronArr[5].split("#")[1];
		} else {
			cronArr[5] = $(this).val() + "#" + cronArr[5].split("#")[1];
		}
		drawCron();
	});
	$('#specifyWeek').bind('input propertychange', function() {
		if ($(this).val() > 7) {
			$(this).val(7);
			cronArr[5] = cronArr[5].split("#")[0] + "#" + 7;
		} else {
			cronArr[5] = cronArr[5].split("#")[0] + "#" + $(this).val();
		}
		drawCron();
	});

	$("#tabs-second .slider").slider({
		min : 1,
		max : 59,
		slide : function(event, ui) {
			cronArr[0] = cronArr[0].split("/")[0] + "/" + ui.value;
			$('#tabs-second-n .preview').html(ui.value);
			drawCron();
		}
	});

	$("#tabs-second-range .slider").slider({
		range : true,
		min : 1,
		max : 59,
		values : [1, 2],
		slide : function(event, ui) {
			cronArr[0] = ui.values[0] + "-" + ui.values[1];
			$('#tabs-second-range .preview0').html(ui.values[0]);
			$('#tabs-second-range .preview1').html(ui.values[1]);
			drawCron();
		}
	});

	$("#tabs-minute .slider").slider({
		min : 1,
		max : 59,
		slide : function(event, ui) {
			cronArr[1] = cronArr[1].split("/")[0] + "/" + ui.value;
			$('#tabs-minute-n .preview').html(ui.value);
			drawCron();
		}
	});
	$("#tabs-minute-range .slider").slider({
		range : true,
		min : 1,
		max : 59,
		values : [1, 2],
		slide : function(event, ui) {
			cronArr[1] = ui.values[0] + "-" + ui.values[1];
			$('#tabs-minute-range .preview0').html(ui.values[0]);
			$('#tabs-minute-range .preview1').html(ui.values[1]);
			drawCron();
		}
	});
	$("#tabs-hour .slider").slider({
		min : 1,
		max : 23,
		slide : function(event, ui) {
			cronArr[2] = "*/" + ui.value;
			$('#tabs-hour-n .preview').html(ui.value);
			drawCron();
		}
	});
	$("#tabs-hour-range .slider").slider({
		range : true,
		min : 0,
		max : 23,
		values : [0, 2],
		slide : function(event, ui) {
			cronArr[2] = ui.values[0] + "-" + ui.values[1];
			$('#tabs-hour-range .preview0').html(ui.values[0]);
			$('#tabs-hour-range .preview1').html(ui.values[1]);
			drawCron();
		}
	});
	$("#tabs-day .slider").slider({
		min : 1,
		max : 31,
		slide : function(event, ui) {
			cronArr[3] = "*/" + ui.value;
			$('#tabs-day-n .preview').html(ui.value);
			drawCron();
		}
	});
	$("#tabs-day-range .slider").slider({
		range : true,
		min : 1,
		max : 31,
		values : [1, 2],
		slide : function(event, ui) {
			cronArr[3] = ui.values[0] + "-" + ui.values[1];
			$('#tabs-day-range .preview0').html(ui.values[0]);
			$('#tabs-day-range .preview1').html(ui.values[1]);
			drawCron();
		}
	});
	$("#tabs-month .slider").slider({
		min : 1,
		max : 12,
		slide : function(event, ui) {
			cronArr[4] = cronArr[4].split("/")[0] + "/" + ui.value;
			$('#tabs-month-n .preview').html(ui.value);
			drawCron();
		}
	});
	$("#tabs-month-range .slider").slider({
		range : true,
		min : 1,
		max : 12,
		values : [1, 2],
		slide : function(event, ui) {
			cronArr[4] = ui.values[0] + "-" + ui.values[1];
			$('#tabs-month-range .preview0').html(ui.values[0]);
			$('#tabs-month-range .preview1').html(ui.values[1]);
			drawCron();
		}
	});

	// TOOD: All draw* functions can be combined into a few smaller methods
	function drawEachSeconds() {
		// seconds
		for (var i = 0; i < 60; i++) {
			var padded = i;
			if (padded.toString().length === 1) {
				padded = "0" + padded;
			}
			$('.tabs-second-format').append(
					'<input type="checkbox" id="second-check' + i
							+ '"><label for="second-check' + i + '">' + padded
							+ '</label>');
			if (i !== 0 && (i + 1) % 10 === 0) {
				$('.tabs-second-format').append('<br/>');
			}
		}
		$('.tabs-second-format input').button();
		$('.tabs-second-format').buttonset();

		$('.tabs-second-format input[type="checkbox"]').click(function() {
			var newItem = $(this).attr('id').replace('second-check', '');
			if (cronArr[0] === "*") {
				cronArr[0] = $(this).attr('id').replace('second-check', '');
			} else {

				// if value already in list, toggle it off
				var list = cronArr[0].split(',');
				if (list.indexOf(newItem) !== -1) {
					list.splice(list.indexOf(newItem), 0);
					cronArr[0] = list.join(',');
				} else {
					// else toggle it on
					cronArr[0] = cronArr[0] + "," + newItem;
				}
				if (cronArr[0] === "") {
					cronArr[0] = "*";
				}
			}
			drawCron();
		});
	};

	function drawEachMinutes() {
		// minutes
		for (var i = 0; i < 60; i++) {
			var padded = i;
			if (padded.toString().length === 1) {
				padded = "0" + padded;
			}
			$('.tabs-minute-format').append(
					'<input type="checkbox" id="minute-check' + i
							+ '"><label for="minute-check' + i + '">' + padded
							+ '</label>');
			if (i !== 0 && (i + 1) % 10 === 0) {
				$('.tabs-minute-format').append('<br/>');
			}
		}
		$('.tabs-minute-format input').button();
		$('.tabs-minute-format').buttonset();

		$('.tabs-minute-format input[type="checkbox"]').click(function() {
			var newItem = $(this).attr('id').replace('minute-check', '');
			if (cronArr[1] === "*") {
				cronArr[1] = $(this).attr('id').replace('minute-check', '');
			} else {

				// if value already in list, toggle it off
				var list = cronArr[1].split(',');
				if (list.indexOf(newItem) !== -1) {
					list.splice(list.indexOf(newItem), 1);
					cronArr[1] = list.join(',');
				} else {
					// else toggle it on
					cronArr[1] = cronArr[1] + "," + newItem;
				}
				if (cronArr[1] === "") {
					cronArr[1] = "*";
				}
			}
			drawCron();
		});

	};

	function drawEachHours() {
		// hours
		for (var i = 0; i < 24; i++) {
			var padded = i;
			if (padded.toString().length === 1) {
				padded = "0" + padded;
			}
			$('.tabs-hour-format').append(
					'<input type="checkbox" id="hour-check' + i
							+ '"><label for="hour-check' + i + '">' + padded
							+ '</label>');
			if (i !== 0 && (i + 1) % 12 === 0) {
				$('.tabs-hour-format').append('<br/>');
			}
		}

		$('.tabs-hour-format input').button();
		$('.tabs-hour-format').buttonset();

		$('.tabs-hour-format input[type="checkbox"]').click(function() {
			var newItem = $(this).attr('id').replace('hour-check', '');
			if (cronArr[2] === "*") {
				cronArr[2] = $(this).attr('id').replace('hour-check', '');
			} else {

				// if value already in list, toggle it off
				var list = cronArr[2].split(',');
				if (list.indexOf(newItem) !== -1) {
					list.splice(list.indexOf(newItem), 1);
					cronArr[2] = list.join(',');
				} else {
					// else toggle it on
					cronArr[2] = cronArr[2] + "," + newItem;
				}
				if (cronArr[2] === "") {
					cronArr[2] = "*";
				}
			}
			drawCron();
		});

	};

	function drawEachDays() {

		// days
		for (var i = 1; i < 32; i++) {
			var padded = i;
			if (padded.toString().length === 1) {
				padded = "0" + padded;
			}
			$('.tabs-day-format').append(
					'<input type="checkbox" id="day-check' + i
							+ '"><label for="day-check' + i + '">' + padded
							+ '</label>');
			if (i !== 0 && (i) % 7 === 0) {
				$('.tabs-day-format').append('<br/>');
			}
		}

		$('.tabs-day-format input').button();
		$('.tabs-day-format').buttonset();

		$('.tabs-day-format input[type="checkbox"]').click(function() {
			var newItem = $(this).attr('id').replace('day-check', '');
			if (cronArr[3] === "*") {
				cronArr[3] = $(this).attr('id').replace('day-check', '');
			} else {

				// if value already in list, toggle it off
				var list = cronArr[3].split(',');
				if (list.indexOf(newItem) !== -1) {
					list.splice(list.indexOf(newItem), 1);
					cronArr[3] = list.join(',');
				} else {
					// else toggle it on
					cronArr[3] = cronArr[3] + "," + newItem;
				}
				if (cronArr[3] === "") {
					cronArr[3] = "*";
				}

			}
			drawCron();
		});

	};

	function drawEachMonths() {
		// months

		var months = [null, 'Jan', 'Feb', 'March', 'April', 'May', 'June',
				'July', 'Aug', 'Sept', 'Oct', 'Nov', 'Dec'];
		var language = sessionStorage.getItem("language");
		if (language == "zh-cn") {
			months = [null, '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月',
					'九月', '十月', '十一月', '十二月'];
		}
		// console.log("cron language",language);
		for (var i = 1; i < 13; i++) {
			var padded = i;
			if (padded.toString().length === 1) {
				// padded = "0" + padded;
			}
			$('.tabs-month-format').append(
					'<input type="checkbox" id="month-check' + i
							+ '"><label for="month-check' + i + '">'
							+ months[i] + '</label>');
		}

		$('.tabs-month-format input').button();
		$('.tabs-month-format').buttonset();

		$('.tabs-month-format input[type="checkbox"]').click(function() {
			var newItem = $(this).attr('id').replace('month-check', '');
			if (cronArr[4] === "*") {
				cronArr[4] = $(this).attr('id').replace('month-check', '');
			} else {

				// if value already in list, toggle it off
				var list = cronArr[4].split(',');
				if (list.indexOf(newItem) !== -1) {
					list.splice(list.indexOf(newItem), 1);
					cronArr[4] = list.join(',');
				} else {
					// else toggle it on
					cronArr[4] = cronArr[4] + "," + newItem;
				}
				if (cronArr[4] === "") {
					cronArr[4] = "*";
				}

			}
			drawCron();
		});

	};

	function drawEachWeek() {
		// weeks
		var days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday',
				'Friday', 'Saturday'];
		var language = sessionStorage.getItem("language");
		if (language == "zh-cn") {
			days = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
		}
		for (var i = 0; i < 7; i++) {
			var padded = i;
			var value = i + 1;
			if (padded.toString().length === 1) {
				// padded = "0" + padded;
			}

			$('.tabs-week-format').append(
					'<input type="checkbox" id="week-check' + value
							+ '"><label for="week-check' + value + '">'
							+ days[i] + '</label>');
		}

		$('.tabs-week-format input').button();
		$('.tabs-week-format').buttonset();

		$('.tabs-week-format input[type="checkbox"]').click(function() {
			var newItem = $(this).attr('id').replace('week-check', '');
			if (cronArr[5] === "*" || cronArr[5] === "?") {
				cronArr[5] = $(this).attr('id').replace('week-check', '');
			} else {

				// if value already in list, toggle it off
				var list = cronArr[5].split(',');
				if (list.indexOf(newItem) !== -1) {
					list.splice(list.indexOf(newItem), 1);
					cronArr[5] = list.join(',');
				} else {
					// else toggle it on
					cronArr[5] = cronArr[5] + "," + newItem;
				}
				if (cronArr[5] === "") {
					cronArr[5] = "*";
				}

			}
			drawCron();
		});

	};

	// TODO: Refactor these methods into smaller methods
	drawEachMinutes();
	drawEachHours();
	drawEachDays();
	drawEachMonths();
	drawCron();
};

// HTML Template for plugin
var tmpl = '<input type="text" id="cronString" ng-bind="newExprsn" size="80" />\
	<input type="button" value="{{\'reset\'|translate}}" class="btn btn-default btn-tool btn-fon" id="clear" />\
<br/>\
<!-- TODO: add back next estimated time -->\
<!-- <span>Will run next at:<em><span class="next"></span></em></span> -->\
<!-- the cron editor will be here -->\
<div id="tabs" class="tabs">\
  <ul>\
    <li><a href="#tabs-second">{{\'second\'|translate}}</a></li>\
    <li><a href="#tabs-minute">{{\'minute\'|translate}}</a></li>\
    <li><a href="#tabs-hour">{{\'hour\'|translate}}</a></li>\
    <li><a href="#tabs-day">{{\'day\'|translate}}</a></li>\
    <li><a href="#tabs-month">{{\'month\'|translate}}</a></li>\
    <li><a href="#tabs-week">{{\'week\'|translate}}</a></li>\
  </ul>\
  <div id="tabs-second">\
    <div class="tabs">\
      <ul>\
        <li id="button-second-every"><a href="#tabs-second-every">{{\'every\'|translate}} {{\'second\'|translate}}</a></li>\
        <li id="button-second-n"><a href="#tabs-second-n">{{\'every\'|translate}} n {{\'seconds\'|translate}}</a></li>\
		<li id="button-second-range"><a href="#tabs-second-range">{{\'range\'|translate}}</a></li>\
	    <li id="button-second-each"><a href="#tabs-second-each">{{\'eachSelectedSecond\'|translate}}</a></li>\
      </ul>\
      <div id="tabs-second-every" class="preview">\
        <div>*</div>\
        <div>{{\'every\'|translate}} {{\'second\'|translate}}</div>\
      </div>\
      <div id="tabs-second-n">\
		<label>{{\'startTime\'|translate}}:</label>\
		<input type="number" id="startSecond" size="20" max="59" min="0"/><span>{{\'second\'|translate}}</span>\
        <div style="padding: 10px;font-size: 24px;"> {{\'every\'|translate}} <span class="preview">1</span> {{\'seconds\'|translate}}</div>\
        <div class="slider"></div>\
      </div>\
	  <div id="tabs-second-range">\
        <div style="padding: 10px;font-size: 24px;"> {{\'range\'|translate}} <span class="preview0">1</span><span>-</span><span class="preview1">59</span> {{\'seconds\'|translate}}</div>\
        <div class="slider"></div>\
      </div>\
	  <div id="tabs-second-each" class="preview">\
    	<div>{{\'eachSelectedSecond\'|translate}}</div><br/>\
    	<div class="tabs-second-format"></div>\
	  </div>\
    </div>\
  </div>\
  <div id="tabs-minute">\
    <div class="tabs">\
      <ul>\
        <li id="button-minute-every"><a href="#tabs-minute-every">{{\'every\'|translate}} {{\'minute\'|translate}}</a></li>\
        <li id="button-minute-n"><a href="#tabs-minute-n">{{\'every\'|translate}} n {{\'minute\'|translate}}</a></li>\
		<li id="button-minute-range"><a href="#tabs-minute-range">{{\'range\'|translate}}</a></li>\
		<li id="button-minute-each"><a href="#tabs-minute-each">{{\'eachSelectedMinute\'|translate}}</a></li>\
      </ul>\
      <div id="tabs-minute-every" class="preview">\
        <div>*</div>\
        <div>{{\'every\'|translate}} {{\'minute\'|translate}}</div>\
      </div>\
      <div id="tabs-minute-n">\
		<label>{{\'startTime\'|translate}}:</label>\
		<input type="number" id="startMinute" size="20" max="59" min="0"/><span>{{\'minute\'|translate}}</span>\
        <div style="padding: 10px;font-size: 24px;"> {{\'every\'|translate}} <span class="preview">1</span> {{\'minutes\'|translate}}</div>\
        <div class="slider"></div>\
      </div>\
	  <div id="tabs-minute-range">\
		<div style="padding: 10px;font-size: 24px;"> {{\'range\'|translate}} <span class="preview0">1</span><span>-</span><span class="preview1">2</span> {{\'minute\'|translate}}</div>\
		<div class="slider"></div>\
      </div>\
      <div id="tabs-minute-each" class="preview">\
        <div>{{\'eachSelectedMinute\'|translate}}</div><br/>\
        <div class="tabs-minute-format"></div>\
      </div>\
    </div>\
  </div>\
  <div id="tabs-hour">\
    <div class="tabs">\
      <ul>\
        <li id="button-hour-every"><a href="#tabs-hour-every">{{\'every\'|translate}} {{\'hour\'|translate}}</a></li>\
        <li id="button-hour-n"><a href="#tabs-hour-n">{{\'every\'|translate}} n {{\'hours\'|translate}}</a></li>\
		<li id="button-hour-range"><a href="#tabs-hour-range">{{\'range\'|translate}}</a></li>\
	    <li id="button-hour-each"><a href="#tabs-hour-each">{{\'eachSelectedHour\'|translate}}</a></li>\
      </ul>\
      <div id="tabs-hour-every" class="preview">\
        <div>*</div>\
        <div>{{\'every\'|translate}} {{\'hour\'|translate}}</div>\
      </div>\
      <div id="tabs-hour-n">\
		<label>{{\'startTime\'|translate}}:</label>\
		<input type="number" id="startHour" size="20" max="59" min="0"/><span>{{\'hour\'|translate}}</span>\
        <div style="padding: 10px;font-size: 24px;">{{\'every\'|translate}}<span class="preview"> 1 </span>{{\'hours\'|translate}}</div>\
        <div class="slider"></div>\
      </div>\
	  <div id="tabs-hour-range">\
		<div style="padding: 10px;font-size: 24px;"> {{\'range\'|translate}} <span class="preview0">0</span><span>-</span><span class="preview1">2</span> {{\'hour\'|translate}}</div>\
		<div class="slider"></div>\
	  </div>\
      <div id="tabs-hour-each" class="preview">\
        <div>{{\'eachSelectedHour\'|translate}}</div><br/>\
        <div class="tabs-hour-format"></div>\
      </div>\
    </div>\
  </div>\
  <div id="tabs-day">\
    <div class="tabs">\
      <ul>\
        <li id="button-day-every"><a href="#tabs-day-every">{{\'every\'|translate}} {{\'day\'|translate}}</a></li>\
		<li id="button-day-n"><a href="#tabs-day-n">{{\'every\'|translate}} n {{\'day\'|translate}}</a></li>\
		<li id="button-day-range"><a href="#tabs-day-range">{{\'range\'|translate}}</a></li>\
        <li id="button-day-each"><a href="#tabs-day-each">{{\'eachSelectedDay\'|translate}}</a></li>\
	    <li id="button-day-last"><a href="#tabs-day-last">{{\'everyMonthLastDay\'|translate}}</a></li>\
		<li id="button-day-nearest"><a href="#tabs-day-nearest">{{\'nearestWorkDay\'|translate}}</a></li>\
      </ul>\
      <div id="tabs-day-every" class="preview">\
        <div>*</div>\
        <div>{{\'every\'|translate}} {{\'day\'|translate}}</div>\
      </div>\
	  <div id="tabs-day-n">\
		<label>{{\'startTime\'|translate}}:</label>\
		<input type="number" id="startDay" size="20" max="31" min="0"/><span>{{\'day\'|translate}}</span>\
        <div style="padding: 10px;font-size: 24px;">{{\'every\'|translate}}<span class="preview"> 1 </span>{{\'day\'|translate}}</div>\
    	<div class="slider"></div>\
	  </div>\
	  <div id="tabs-day-range">\
		<div style="padding: 10px;font-size: 24px;"> {{\'range\'|translate}} <span class="preview0">1</span><span>-</span><span class="preview1">2</span> {{\'day\'|translate}}</div>\
		<div class="slider"></div>\
      </div>\
      <div id="tabs-day-each" class="preview">\
        <div>{{\'eachSelectedDay\'|translate}}</div><br/>\
        <div class="tabs-day-format"></div>\
      </div>\
	  <div id="tabs-day-nearest">\
	 	<label>{{\'from\'|translate}}</label>\
		<input type="number" id="nearestDay" size="10" value="1" />\
		<span>{{\'nearestWorkDay\'|translate}}</span>\
      </div>\
    </div>\
  </div>\
  <div id="tabs-month">\
    <div class="tabs">\
      <ul>\
        <li id="button-month-every"><a href="#tabs-month-every">{{\'every\'|translate}} {{\'month\'|translate}}</a></li>\
		<li id="button-month-n"><a href="#tabs-month-n">{{\'every\'|translate}} n {{\'month\'|translate}}</a></li>\
		<li id="button-month-range"><a href="#tabs-month-range">{{\'range\'|translate}}</a></li>\
	    <li id="button-month-each"><a href="#tabs-month-each">{{\'eachSelectedMonth\'|translate}}</a></li>\
      </ul>\
      <div id="tabs-month-every" class="preview">\
        <div>*</div>\
        <div>{{\'every\'|translate}} {{\'month\'|translate}}</div>\
      </div>\
	  <div id="tabs-month-n">\
		<label>{{\'from\'|translate}}:</label>\
		<input type="number" id="monthDay" size="20" max="31" min="1"/><span>{{\'day\'|translate}}</span>\
		<div style="padding: 10px;font-size: 24px;">{{\'every\'|translate}}<span class="preview"> 1 </span>{{\'month\'|translate}}</div>\
		<div class="slider"></div>\
      </div>\
	  <div id="tabs-month-range">\
		<div style="padding: 10px;font-size: 24px;"> {{\'range\'|translate}} <span class="preview0">1</span><span>-</span><span class="preview1">2</span> {{\'month\'|translate}}</div>\
		<div class="slider"></div>\
	  </div>\
      <div id="tabs-month-each" class="preview">\
        <div>{{\'eachSelectedMonth\'|translate}}</div><br/>\
        <div class="tabs-month-format"></div>\
      </div>\
    </div>\
  </div>\
  <div id="tabs-week">\
    <div class="tabs">\
      <ul>\
        <li id="button-week-every"><a href="#tabs-week-every">{{\'every\'|translate}} {{\'week\'|translate}}</a></li>\
        <li id="button-week-each"><a href="#tabs-week-each">{{\'eachSelectedWeek\'|translate}}</a></li>\
		<li id="button-week-last"><a href="#tabs-week-last">{{\'monthLastWeek\'|translate}}</a></li>\
	    <li id="button-week-specify"><a href="#tabs-week-specify">{{\'specify\'|translate}}</a></li>\
		<li id="button-week-none"><a href="#tabs-week-none">{{\'unspecified\'|translate}}</a></li>\
      </ul>\
      <div id="tabs-week-every" class="preview">\
        <div>*</div>\
        <div>{{\'every\'|translate}} {{\'day\'|translate}}</div>\
      </div>\
      <div id="tabs-week-each">\
        <div class="preview">{{\'eachSelectedWeek\'|translate}}</div><br/>\
        <div class="tabs-week-format"></div>\
      </div>\
	  <div id="tabs-week-last">\
		<label>{{\'month\'|translate}} {{\'last\'|translate}}</label>\
		<span>{{\'week\'|translate}}</span><input type="number" id="monthWeek" size="20" max="4" min="1"/>\
	  </div>\
	  <div id="tabs-week-specify">\
		<label>{{\'the\'|translate}} </label>\
		<input type="number" id="theWeeks" size="20" max="4" min="1" value="1"/><span>{{\'week\'|translate}},</span>\
		<span>{{\'specifyWeek\'|translate}}</span><input type="number" id="specifyWeek" value="1" size="20" max="7" min="1"/>\
      </div>\
    </div>\
  </div>\
</div>';