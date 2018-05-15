/**
* YoutubeCrawlerTool web application JavaScript library
*
* @author jsanchezmend
*
*/

var csvExportVideosFields = ["id", "title", "description", "publishedAt", "duration", "viewCount", "likeCount", "dislikeCount", "commentCount", "scopeRange", "embedHtml"];
var csvExportVideosNumericFields = ["viewCount", "likeCount", "dislikeCount", "commentCount", "scopeRange"];

var csvExportChannelsFields = ["id", "name", "description", "publishedAt", "thumbnailUrl", "subscribersCount", "videoCount", "viewCount", "commentCount"];
var csvExportChannelsNumericFields = ["subscribersCount", "videoCount", "viewCount", "commentCount"];

var csvExportEdgesFields = ["outgoing", "incoming", "outgoingType", "incomingType"];

var videosTableColumDefinitions = [
  {
    targets: 0,
    data: "title",
    orderable: true,
    render: function (data, type, row, meta) {
    	return trimString(row.title, 40);
    }
  },
  {
    targets: 1,
    data: "channel",
    orderable: true,
    render: function (data, type, row, meta) {
    	var html = "";
    	if(row.channel) {
    		html = "<a href='/channels/"+row.channel.id+"'>"+trimString(row.channel.name, 25)+"</a>";
    	}
    	return html;
    }
  },
  {
    targets: 2,
    data: "category",
    orderable: true,
    render: function (data, type, row, meta) {
    	var html = "";
    	if(row.category) {
    		html = row.category.name;
    	}
    	return html;
    }
  },
  {
    targets: 4,
    data: "options",
    orderable: false,
    render: function (data, type, row, meta) {
    	var html = "<a class='btn btn-danger' href='/videos/"+row.id+"'>View</a>";
    	return html
    }
  }
];

var videosTableColumnsConfiguration = [
	{ title: 'Video title', data: 'title', width: '40%' },
	{ title: 'Channel', data: 'channel', width: '25%' },
	{ title: 'Category', data: 'category', width: '15%' },
	{ title: 'Scope range', data: 'scopeRange', width: '10%' },
	{ title: '', data: 'options', width: '10%' }
];

var doBack = function () {
	window.history.back();
}

var doGet = function (requestUrl, callback) {
	console.log("doGet for url: " + requestUrl);
	$.ajax({ 
        url: requestUrl, 
        method: 'GET', 
        success: callback
	});
}

var doPost = function (requestUrl, body, callback) {
	console.log("doPost for url: " + requestUrl + ", with body:" + JSON.stringify(body));
	$.ajax({ 
        url: requestUrl, 
        method: 'POST', 
        dataType: 'json', 
        data: JSON.stringify(body),
        contentType: 'application/json',
        mimeType: 'application/json',
        success: callback
	});
}

var doPut = function (requestUrl, body, callback) {
	console.log("doPut for url: " + requestUrl + ", with body:" + JSON.stringify(body));
	$.ajax({ 
        url: requestUrl, 
        method: 'PUT', 
        dataType: 'json', 
        data: JSON.stringify(body),
        contentType: 'application/json',
        mimeType: 'application/json',
        success: callback
	});
}

var doDelete = function (requestUrl, callback) {
	console.log("doDelete for url: " + requestUrl);
	$.ajax({ 
        url: requestUrl, 
        method: 'DELETE', 
        success: callback
	});
}

var getFormFields = function (formId) {
	var formFields = {};
	$("#" + formId).find(":input").each(function() {
		var inputType = this.tagName.toUpperCase() === "INPUT" && this.type.toUpperCase();
	    if (inputType !== "BUTTON" && inputType !== "SUBMIT" && inputType !== false) {
	    	if(inputType == "CHECKBOX") {
	    		if (this.checked == true){
	    			formFields[this.name] = "true";
	    		} else {
	    			formFields[this.name] = "false";
	    		}
	    	} else {
	    		formFields[this.name] = $(this).val();
	    	}
	    }
	});
	$("#" + formId).find("select").each(function() {
	    formFields[this.name] = $(this).val();
	});
	console.log("getFormFields for form with id: " + formId);
	console.log(formFields);
	return formFields
}

var generateNodeContent = function(nodeData) {
	if(nodeData.typeCode == "v") {
		return generateVideoNodeContent(nodeData.video);
	} else if(nodeData.typeCode == "c") {
		return generateChannelNodeContent(nodeData.channel);
	}
	return "";
}

var generateVideoNodeContent = function(video) {
	var html =
	  "<div class='panel panel-default panel-node'>"
	+ "<div class='panel-heading node-title'>"+ video.embedHtml.replace("width=\"480\" height=\"270\"", "width=\"200\" height=\"140\"") +"</div>"
	+ "  <div class='panel-body'>"
	+ "  <h5 class='node-title'>"+ trimString(video.title, 50) +"</h5>"
	+ "  <p><small><b>Published:</b> " + video.publishedAt.substring(0, 10) +"</small></p>"
	+ "  <p><small><b>Scope range:</b> " + video.scopeRange +" <i class='fa fa-star'></i></small></p>"
	+ "  <p><small><b>Views:</b> " + video.viewCount +" <i class='fa fa-eye'></i></small></p>"
	+ "  <p><small><b>Likes:</b> " + video.likeCount +" <i class='fa fa-thumbs-o-up'></i></small></p>"
	+ "  <p><small><b>Dislikes:</b> " + video.dislikeCount +" <i class='fa fa-thumbs-o-down'></i></small></p>"
	+ "  <p><small><b>Comments:</b> " + video.commentCount +" <i class='fa fa-comment-o'></i></small></p>";
	if(video.category) {
		html+= "  <p><small><b>Category:</b> " + video.category.name +"</small></p>"
	}
	html+= "  <p><small><a href='videos/"+ video.id +"'>View more</a></small></p>"
	+ "</div>";
	return html;
}

var generateChannelNodeContent = function(channel) {
	var html =
		  "<div class='panel panel-default panel-node'>"
		+ "<div class='panel-heading'><image src='"+ channel.thumbnailUrl +"' class='center'></div>"
		+ "  <div class='panel-body'>"
		+ "  <h5 class='node-title'>"+ trimString(channel.name, 50) +"</h5>"
		+ "  <p><small>" + trimString(channel.description, 100) +"</small></p>"
		+ "  <p><small><b>Published:</b> " + channel.publishedAt.substring(0, 10) +"</small></p>"
		+ "  <p><small><b>Subscribers:</b> " + channel.subscribersCount +" <i class='fa fa-heart'></i></small></p>"
		+ "  <p><small><b>Videos:</b> " + channel.videoCount +" <i class='fa fa-video-camera'></i></small></p>"
		+ "  <p><small><b>Views:</b> " + channel.viewCount +" <i class='fa fa-eye'></i></small></p>"
		+ "  <p><small><b>Comments:</b> " + channel.commentCount +" <i class='fa fa-comment-o'></i></small></p>"
		+ "  <p><small><a href='channels/"+ channel.id +"'>View more</a></small></p>"
		+ "</div>";
	return html;
}

var validateCategoryFiled = function(categoryFields) {
	if(!categoryFields.name) {
		$('#formMessages').html("<div class='alert alert-danger'>Field 'Name' is required</div>");
		return false;
	} else if(!categoryFields.color) {
		$('#formMessages').html("<div class='alert alert-danger'>Field 'Color' is required</div>");
		return false;
	}
	$('#formMessages').html("");
	return true;
}

var trimString = function(string, length) {
	return string.length > length ? string.substring(0, length) + "..." : string;
}

var convertVideoNodesToCSV = function (objArray) {
    var array = typeof objArray != 'object' ? JSON.parse(objArray) : objArray;
    var str = '';
    for (var i = 0; i < array.length; i++) {
    	var data = array[i].data;
    	if(data.typeCode == "v") {
    		var video = data.video;
	    	if(str == '') {
	    		var header = '';
	    		for(var property in video) {
	    			if(arrayContains(property,csvExportVideosFields)) {
	    				if(header != '') header += ',';
	    				header += property;
	    			}
	    		}
	    		if(header != '') header += ',';
	    		header += 'channelId,categoryName';
	    		str += header + '\r\n';
	    	}
	    	var line = '';
	        for (var index in video) {
	        	if(arrayContains(index,csvExportVideosFields)) {
		            if (line != '') line += ',';
		            if(arrayContains(index,csvExportVideosNumericFields)) {
		            	line += video[index];
		            } else {
		            	var tempLine = video[index].replace(/"/g,'\'');
		            	tempLine = tempLine.replace(/\r?\n|\r/g,'. ');	
		            	line += "\"" + tempLine + "\"";
		            }
		        }
	        }
	        
	        // Adding channel id and category name manually
	        var channel = video.channel;
	        var category = video.category;
	        var categoryName = '';
	        if(category) {
	        	categoryName = category.name;
	        }
	        if (line != '') line += ',';
	        line += channel.id + ',' + categoryName;
	        str += line + '\r\n';
    	}
    }
    return str;
}

var convertChannelNodesToCSV = function (objArray) {
    var array = typeof objArray != 'object' ? JSON.parse(objArray) : objArray;
    var str = '';
    for (var i = 0; i < array.length; i++) {
    	var data = array[i].data;
    	if(data.typeCode == "c") {
    		var channel = data.channel;
	    	if(str == '') {
	    		var header = '';
	    		for(var property in channel) {
	    			if(arrayContains(property,csvExportChannelsFields)) {
	    				if(header != '') header += ',';
	    				header += property;
	    			}
	    		}
	    		str += header + '\r\n';
	    	}
	    	var line = '';
	        for (var index in channel) {
	        	if(arrayContains(index,csvExportChannelsFields)) {
		            if (line != '') line += ',';
		            if(arrayContains(index,csvExportChannelsNumericFields)) {
		            	line += channel[index];
		            } else {
		            	var tempLine = channel[index].replace(/"/g,'\'');
		            	tempLine = tempLine.replace(/\r?\n|\r/g,'. ');	
		            	line += "\"" + tempLine + "\"";
		            }  
		        }
	        }
	        str += line + '\r\n';
    	}
    }
    return str;
}

var convertEdgesToCSV = function (objArray) {
    var array = typeof objArray != 'object' ? JSON.parse(objArray) : objArray;
    var str = '';
    for (var i = 0; i < array.length; i++) {
    	var data = array[i].data;
    	if(str == '') {
    		var header = '';
    		for(var property in data) {
    			if(arrayContains(property,csvExportEdgesFields)) {
    				if(header != '') header += ',';
    				header += property;
    			}
    		}
    		str += header + '\r\n';
    	}
    	var line = '';
        for (var index in data) {
        	if(arrayContains(index,csvExportEdgesFields)) {
	            if (line != '') line += ','
	            line += data[index];
	        }
        }
        str += line + '\r\n';
    }
    return str;
}

var millisToMinutesAndSeconds = function(millis) {
	  var minutes = Math.floor(millis / 60000);
	  var seconds = ((millis % 60000) / 1000).toFixed(0);
	  return minutes + ":" + (seconds < 10 ? '0' : '') + seconds;
}

var arrayContains = function(needle, arrhaystack) {
    return (arrhaystack.indexOf(needle) > -1);
}

var sleep = function (time) {
	 var now = new Date().getTime();
	 while(new Date().getTime() < now + sleepDuration){ /* do nothing */ } 
}

var dateFormat = function () {
    var token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g,
        timezone = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g,
        timezoneClip = /[^-+\dA-Z]/g,
        pad = function (val, len) {
            val = String(val);
            len = len || 2;
            while (val.length < len) val = "0" + val;
            return val;
        };

    // Regexes and supporting functions are cached through closure
    return function (date, mask, utc) {
        var dF = dateFormat;

        // You can't provide utc if you skip other args (use the "UTC:" mask prefix)
        if (arguments.length == 1 && Object.prototype.toString.call(date) == "[object String]" && !/\d/.test(date)) {
            mask = date;
            date = undefined;
        }

        // Passing date through Date applies Date.parse, if necessary
        date = date ? new Date(date) : new Date;
        if (isNaN(date)) throw SyntaxError("invalid date");

        mask = String(dF.masks[mask] || mask || dF.masks["default"]);

        // Allow setting the utc argument via the mask
        if (mask.slice(0, 4) == "UTC:") {
            mask = mask.slice(4);
            utc = true;
        }

        var _ = utc ? "getUTC" : "get",
            d = date[_ + "Date"](),
            D = date[_ + "Day"](),
            m = date[_ + "Month"](),
            y = date[_ + "FullYear"](),
            H = date[_ + "Hours"](),
            M = date[_ + "Minutes"](),
            s = date[_ + "Seconds"](),
            L = date[_ + "Milliseconds"](),
            o = utc ? 0 : date.getTimezoneOffset(),
            flags = {
                d:    d,
                dd:   pad(d),
                ddd:  dF.i18n.dayNames[D],
                dddd: dF.i18n.dayNames[D + 7],
                m:    m + 1,
                mm:   pad(m + 1),
                mmm:  dF.i18n.monthNames[m],
                mmmm: dF.i18n.monthNames[m + 12],
                yy:   String(y).slice(2),
                yyyy: y,
                h:    H % 12 || 12,
                hh:   pad(H % 12 || 12),
                H:    H,
                HH:   pad(H),
                M:    M,
                MM:   pad(M),
                s:    s,
                ss:   pad(s),
                l:    pad(L, 3),
                L:    pad(L > 99 ? Math.round(L / 10) : L),
                t:    H < 12 ? "a"  : "p",
                tt:   H < 12 ? "am" : "pm",
                T:    H < 12 ? "A"  : "P",
                TT:   H < 12 ? "AM" : "PM",
                Z:    utc ? "UTC" : (String(date).match(timezone) || [""]).pop().replace(timezoneClip, ""),
                o:    (o > 0 ? "-" : "+") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4),
                S:    ["th", "st", "nd", "rd"][d % 10 > 3 ? 0 : (d % 100 - d % 10 != 10) * d % 10]
            };

        return mask.replace(token, function ($0) {
            return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);
        });
    };
}();

// Some common format strings
dateFormat.masks = {
    "default":      "ddd mmm dd yyyy HH:MM:ss",
    shortDate:      "m/d/yy",
    mediumDate:     "mmm d, yyyy",
    longDate:       "mmmm d, yyyy",
    fullDate:       "dddd, mmmm d, yyyy",
    shortTime:      "h:MM TT",
    mediumTime:     "h:MM:ss TT",
    longTime:       "h:MM:ss TT Z",
    isoDate:        "yyyy-mm-dd",
    isoTime:        "HH:MM:ss",
    isoDateTime:    "yyyy-mm-dd'T'HH:MM:ss",
    isoUtcDateTime: "UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
};

// Internationalization strings
dateFormat.i18n = {
    dayNames: [
        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
        "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    ],
    monthNames: [
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
        "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    ]
};

// For convenience...
Date.prototype.format = function (mask, utc) {
    return dateFormat(this, mask, utc);
};
