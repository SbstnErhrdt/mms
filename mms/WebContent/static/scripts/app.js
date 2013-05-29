var MMSApp = angular.module("MMS", []);

MMSApp.config("$routeProvider", function($routeProvider) {
	$routeProvider.when("/", {
		templateUrl: "/partials/home.html",
		controller: HomeController
	});
	// USW
});

MMSApp.factory("UserFactory", function() {
	var factory = {};

	var User = {
		firstName: "String",
		lastName: "String",
		title: "String",
		email: "String",
		graduation: "String",
		matricNum: "Number",
		semester: "Number"
	};

	var Users = [];

	factory.getUser = function($http, $q, userEmail) {
		var url = "/read/user";
		if(userEmail) {
			url = url+"?email="+userEmail;
		} else {
			// ERROR
			console.log("ERROR in factory.getUser");
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			User = data;
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.getUsers = function($http, $q) {

		var url = "/read/users";

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			Users = data;
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};
});

MMSApp.factory("EmployeeFactory", function() {
	var factory = {};
	var Employee = {

	};
});

MMSApp.factory("EventFactory", function() {
	var factory = {};
	var Event = {
		eventID: "Number",
		modules_moduleID: "Number",
		name: "String",
		sws: "Number",
		lecturer_email: "String",
		archived: "boolean"
	};
	var Events = [];

	/*
	 * getEvent: Holt ein Event mit einer bestimmten eventID vom Server
	 */
	factory.getEvent = function($http, $q, studycourseID, moduleHandbookID, subjectID, moduleID, eventID) {
		var url = "/read/event";

		if(studycourseID && moduleHandbookID && subjectID && moduleID && eventID) {
			url  = url+"?studycourseID="+studycourseID+"&moduleHandbookID="+moduleHandbookID+"&subjectID="+subjectID+"&moduleID="+moduleID+"&eventID="+eventID;
		} else if(eventID) {
			url  = url+"?eventID="+eventID;
		} else {
			// ERROR
			console.log("ERROR in factory.getEvent");
		}
		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			Event = data;
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	/*
	 * getEvents: Holt alle Events einer bestimmten Kategorie vom Server
	 */
	factory.getEvents = function($http, $q, studycourseID, moduleHandbookID, subjectID, moduleID) {

		var url = "/read/events";

		if(studycourseID && !moduleHandbookID && !subjectID && !moduleID) {
			url = url+"?studycourseID="+studycourseID;
		} else if(studycourseID && moduleHandbookID && !subjectID && !moduleID) {
			url = url+"?studycourseID="+studycourseID+"&moduleHandbookID="+moduleHandbookID;
		} else if(studycourseID && moduleHandbookID && subjectID && !moduleID) {
			url = url+"?studycourseID="+studycourseID+"&moduleHandbookID="+moduleHandbookID+"&subjectID="+subjectID;
		} else if(studycourseID && !moduleHandbookID && subjectID && moduleID) {
			url = url+"?studycourseID="+studycourseID+"&moduleHandbookID="+moduleHandbookID+"&subjectID="+subjectID+"&moduleID="+moduleID;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			Events = data; // FIX THIS
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};
});

MMSApp.factory("ModuleFactory", function() {
	var factory = {};
	var Module = {
		duration: "Number",
		moduleID: "Number",
		subjects_subjectID: "Number",
		name: "String",
		token: "String",
		englishTitle: "String",
		lp: "Number",
		sws: "Number",
		language: "String",
		director_email: "String",
		requirement: "String",
		learningTarget: "String",
		content: "String",
		literature: "String" // MGL: Array?!
	};
	var Modules = [];

	factory.getModule = function($http, $q, studycourseID, moduleHandbookID, subjectID, moduleID) {

		var url = "/read/module";

		if(studycourseID && moduleHandbookID && subjectID && moduleID) {
			url  = url+"?studycourseID="+studycourseID+"&moduleHandbookID="+moduleHandbookID+"&subjectID="+subjectID+"&moduleID="+moduleID;
		} else if(moduleID) {
			url  = url+"?moduleID="+moduleID;
		} else {
			// ERROR
			console.log("ERROR in factory.getModule");
		}
		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			Module = data;
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.getModules = function($http, $q, studycourseID, moduleHandbookID, subjectID) {

		var url = "/read/modules";

		if(studycourseID && !moduleHandbookID && !subjectID) {
			url = url+"?studycourseID="+studycourseID;
		} else if(studycourseID && moduleHandbookID && !subjectID) {
			url = url+"?studycourseID="+studycourseID+"&moduleHandbookID="+moduleHandbookID;
		} else if(studycourseID && moduleHandbookID && subjectID) {
			url = url+"?studycourseID="+studycourseID+"&moduleHandbookID="+moduleHandbookID+"&subjectID="+subjects_subjectID;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			Modules = data;
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};
});

MMSApp.factory("SubjectFactory", function() {
	var factory = {};
	var Subject = {
		subjectID: "Number",
		module_handbooks_moduleHandbookID: "Number",
		name: "String",
		archived: "boolean"
	};
	var Subjects = [];

	factory.getSubject = function($http, $q, studycourseID, moduleHandbookID, subjectID) {

		var url = "/read/subject";

		if(studycourseID && moduleHandbookID && subjectID) {
			url = url+"?studycourseID="+studycourseID+"&moduleHandbookID="+moduleHandbookID+"&subjectID="+subjectID;
		} else if(subjectID) {
			url = url+"?subjectID="+subjectID;
		} else {
			// ERROR
			console.log("ERROR in factory.getSubject");
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			Subject = data;
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.getSubjects = function($http, $q, studycourseID, moduleHandbookID) {

		var url = "/read/subjects";

		if(studycourseID && !moduleHandbookID) {
			url = url+"?studycourseID="+studycourseID;
		} else if(studycourseID && moduleHandbookID) {
			url = url+"?studycourseID="+studycourseID+"&moduleHandbookID="+moduleHandbookID;
		} 

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			Subjects = data;
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};		
});

MMSApp.factory("ModuleHandbookFactory", function() {
	var factory = {};
	var ModuleHandbook = {
		moduleHandbookID: "Number",
		studycourses_studycourseID: "Number",
		name: "String",
		semester: "Number",
		archived: "boolean"
	};
	var ModuleHandbooks = [];

	factory.getModuleHandbook = function($http, $q, studycourseID, moduleHandbookID) {

		var url = "/read/modulehandbook";

		if(studycourseID && moduleHandbookID) {
			url = url+"?studycourseID="+studycourseID+"&moduleHandbookID="+moduleHandbookID;
		} else if(moduleHandbookID) {
			url = url+"?moduleHandbookID="+moduleHandbookID;
		} else {
			// ERROR
			console.log("ERROR in factory.getModuleHandbook");
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			ModuleHandbook = data;
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;

	};

	factory.getModuleHandbooks = function($http, $q, studycourseID) {

		var url = "/read/modulehandbooks";

		if(studycourseID) {
			url = url+"?studycourseID="+studycourseID;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			ModuleHandbooks = data;
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;

	};
});

MMSApp.factory("StudycourseFactory", function() {
	var factory = {};
	var Studycourse = {
		studycourseID: "Number",
		name: "String",
		archived: "boolean"
	};
	var Studycourses = [];

	factory.getStudycourse = function($http, $q, studycourseID) {

		var url = "/read/studycourse";

		if(studycourseID) {
			url = url+"?studycourseID="+studycourseID;
		} else {
			// ERROR
			console.log("ERROR in factory.getStudycourse");
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			Studycourse = data;
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.getStudycourses = function($http, $q) {

		var url = "/read/studycourses";

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			Studycourses = data;
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};
});