var xmlhttp;

/** Captcha */
$(document).ready(function() {
	(jq('#dynamic_recaptcha')).onShow(function() {
		preloadCaptcha();
	}, true);
});

function preloadCaptcha() {
	showRecaptcha();
}

function showRecaptcha() {
	Recaptcha.create(publicKey,
			"dynamic_recaptcha", {
				theme : "white",
				callback : captchaCallback
			});
}

function captchaCallback() {
	Recaptcha.focus_response_field();
	(jq('#dynamic_recaptcha')[0]).style.display = "inline-block";
}

function submitFormData() {
	
	xmlhttp = null;
	if (window.XMLHttpRequest) {// code for IE7, Firefox, Opera, etc.
		xmlhttp = new XMLHttpRequest();
	} else if (window.ActiveXObject) {// code for IE6, IE5
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	if (xmlhttp != null) {
		xmlhttp.onreadystatechange = state_Change;
		url = "postformdata";
		params = JSON.stringify(prepareJSON());
		jq('#status')[0].style.display = "inherit";
		xmlhttp.open("POST", url, true);
		xmlhttp.setRequestHeader("Content-type", "application/json");
		xmlhttp.setRequestHeader("Content-length", params.length);
		xmlhttp.setRequestHeader("Connection", "close");
		xmlhttp.send(params);
	} else {
		alert("Your browser does not support XMLHTTP.");
	}
}

function prepareJSON() {
	return {
		name: jq('$txtName')[0].value,
		firstname: jq('$txtFirstName')[0].value,
		location: jq('$txtLocation')[0].value,
		mobile: jq('$txtMobile')[0].value,
		email: jq('$txtEmail')[0].value,
		message: jq('$txtMessage')[0].value,
		challengeField: Recaptcha.get_challenge(),
		responseField: Recaptcha.get_response()
	}
}

function state_Change() {
	if (xmlhttp.readyState == 4) {// 4 = "loaded"
		if (xmlhttp.status == 200) {
			// 200 = "OK"
			jq('#response')[0].style.color = "black";
		} else {
			// KO
			jq('#response')[0].style.color = "red";
		}
		response = $.evalJSON(xmlhttp.responseText);
		if (response.checkOK) {
			jq('#response')[0].style.color = "black";
		} else {
			jq('#response')[0].style.color = "red";
		}
		// Hides back submitting notification
		jq('#status')[0].style.display = "none";
		// Shows status
		jq('#response')[0].innerHTML = response.message;
		Recaptcha.reload();
		setTimeout(function() {
			jq('#response')[0].innerHTML = "";
		}, 5000);
	}
}