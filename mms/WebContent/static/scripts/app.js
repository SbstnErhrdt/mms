var MMSApp = angular.module("MMS", []);

MMSApp.config("$routeProvider", function($routeProvider) {
	$routeProvider.when("/", {
		templateUrl: "/partials/home.html",
		controller: HomeController
	});
});

MMSApp.factory("UserFactory", function() {
	var factory = {};
	var user = {
		firstName: "Vorname",
		lastName: "Nachname",
		title: "Titel",
		email: "Email-Adresse",
		graduation: "Abschluss",
		matricNum: "Matrikelnummer",
		semester: "Semester"
	};
});

MMSApp.factory("EventFactory", function() {
	
});