$(function() {
	// init
	loginBox();
});

// LoginBox Triggers
function loginBox() {
	$(".loginClick").click(function() {
			$('#loginBar').fadeToggle();
	});
}


/*

	BUTTERBAR Methods
 */

var butterbarActive = false;


function sendError(message) {
	if(message) {

		console.log(message);

		if(butterbarActive) {
			butterbarUp(function() {
				butterbarDown(message);
			});
		} else {
			butterbarDown(message);
		}
	} else {

		console.log("Es ist ein unbekannter Fehler aufgetreten.");

		butterbarDown("Es ist ein unbekannter Fehler aufgetreten.");
	}
}

function butterbarDown(message) {

	butterbarActive = true;

	$(".message").text(message);

	// Butterbar SlideDown
	$(".butterbar").css("display", "block");
	$(".view").animate({"margin-top": "20px"}, 300);
	$(".butterbar").animate({"height": "30px"}, 300);
	$(".butterbarClose, .butterbar h1, .butterbar h2").animate({"opacity": "1"});

	// ButterbarClose Hover Effect
	$(".butterbarClose").mouseover(function() {
		$(this).addClass("icon-white");
	});
	$(".butterbarClose").mouseout(function() {
		$(this).removeClass("icon-white");
	});

	// On click ButterbarClose
	$(".butterbarClose").click(function() {
		butterbarUp();
	});
}

function butterbarUp(callback) {

	butterbarActive = false;

	// Butterbar SlideUp
	$(".view").animate({"margin-top": "0px"}, 500);
	$(".butterbar").animate({"height": "0px"}, 500, function() {
		$(".butterbar").css("display", "none");
		if(typeof callback !== "undefined") {
			callback();
		}
	});
}

