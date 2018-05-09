/**
* YoutubeCrawlerTool web application JavaScript library
*
* @author jsanchezmend
*
*/

var csvExportEdgesFields = ["outgoing", "incoming", "outgoingType", "incomingType"];

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
	console.log("getFormFields for form with id: " + formId);
	console.log(formFields);
	return formFields
}

var convertVideoNodesToCSV = function (objArray) {
    var array = typeof objArray != 'object' ? JSON.parse(objArray) : objArray;
    var str = '';
    for (var i = 0; i < array.length; i++) {
    	var data = array[i].data;
    	if(data.typeCode == "v") {
	    	if(str == '') {
	    		var header = Object.keys(data);
	    		str += header.join(',') + '\r\n';
	    	}
	    	var line = '';
	        for (var index in data) {
	            if (line != '') line += ','
	            line += data[index];
	        }
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
	    	if(str == '') {
	    		var header = Object.keys(data);
	    		str += header.join(',') + '\r\n';
	    	}
	    	var line = '';
	        for (var index in data) {
	            if (line != '') line += ','
	            line += data[index];
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
    			console.log(property);
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
