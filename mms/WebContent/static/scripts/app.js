var MMSApp = angular.module("MMS", []);

MMSApp.config("$routeProvider", function($routeProvider) {
	$routeProvider.when("/", {
		templateUrl: "/partials/home.html",
		controller: HomeController
	});
	// USW
});

/*

	UserFactory

 */
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

	factory.getUser = function($http, $q, email) {

		var url = "/read/user";

		if(email) {
			url = url+"?email="+email;
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

	factory.deleteUser = function($http, $q, email) {

		var url = "/delete/user";

		if(email) {
			url = url+"?email="+email;
		} else {
			// ERROR
			console.log("ERROR in factory.getUser");
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(data.email === email) {
				// Success
				User.email = data.email;
				User.firstName = data.firstName;
				User.lastName = lastName;
			} else {
				// ERROR
				console.log("ERROR in factory.deleteUser");
			}
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};
});

/*

	EmployeeFactory

 */
MMSApp.factory("EmployeeFactory", function() {
	var factory = {};

	var Employee = {
		address: "String",
		phoneNum: "String",
		talkTime: "String"
	};

	var Employees = [];

	factory.getEmployee = function($http, $q, email) {

		var url = "read/employee";

		if(email) {
			url = url+"?email="+email;
		} else {
			// ERROR
			console.log("ERROR in factory.getEmployee");
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			Employee = data;
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.getEmployees = function($http, $q) {

		var url = "read/employees";

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			Employees = data;
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.deleteEmployee = function($http, $q, email) {

		var url = "delete/employee";

		if(email) {
			url = url+"?email="+email;
		} else {
			// ERROR
			console.log("ERROR in factory.deleteEmployee");
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(data.email === email) {
				Employee.email = data.email;
			} else {
				// ERROR
				console.log("ERROR in factory.deleteEmployee");
			}
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};
});

/*

	EventFactory

 */
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

		var url = checkSingularURL("read", studycourseID, moduleHandbookID, subjectID, moduleID, eventID);

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

	factory.deleteEvent = function($http, $q, studycourseID, moduleHandbookID, subjectID, moduleID, eventID) {

		var url = checkSingularURL("delete", studycourseID, moduleHandbookID, subjectID, moduleID, eventID);

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(data.eventID === eventID) {
				Event.eventID = data.eventID;
				Event.name = data.name;
			} else {
				// ERROR
				console.log("ERROR in factory.deleteEvent");
			}
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.checkSingularURL = function(method, studycourseID, moduleHandbookID, subjectID, moduleID, eventID) {
		if(method) {

			var url = "/"+method+"/event";

			if(studycourseID && moduleHandbookID && subjectID && moduleID && eventID) {
				url  = url+"?studycourseID="+studycourseID+"&moduleHandbookID="+moduleHandbookID+"&subjectID="+subjectID+"&moduleID="+moduleID+"&eventID="+eventID;
			} else if(eventID) {
				url  = url+"?eventID="+eventID;
			} else {
				// ERROR
				console.log("ERROR in Eventfactory.checkSingularURL");
			}

			return url;
		} else {
			// ERROR
			console.log("ERROR in Eventfactory.checkSingularURL");
		}
	};
});

/*

	ModuleFactory

 */
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

		var url = checkSingularURL("read", studycourseID, moduleHandbookID, subjectID, moduleID);

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

	factory.deleteModule = function($http, $q, studycourseID, moduleHandbookID, subjectID, moduleID) {

		var url = checkSingularURL("delete", studycourseID, moduleHandbookID, subjectID, moduleID);

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(data.moduleID === moduleID) {
				Module.moduleID = data.moduleID;
				Module.name = data.name;
				Module.subjects_subjectID = data.subjects_subjectID;
			} else {
				// ERROR
				console.log("ERROR in Modulefactory.deleteModule");
			}
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.checkSingularURL = function(method, studycourseID, moduleHandbookID, subjectID, moduleID) {
		if(method) {

			var url = "/"+method+"/module";

			if(studycourseID && moduleHandbookID && subjectID && moduleID) {
				url  = url+"?studycourseID="+studycourseID+"&moduleHandbookID="+moduleHandbookID+"&subjectID="+subjectID+"&moduleID="+moduleID;
			} else if(eventID) {
				url  = url+"?moduleID="+moduleID;
			} else {
				// ERROR
				console.log("ERROR in Modulefactory.checkSingularURL");
			}

			return url;
		} else {
			// ERROR
			console.log("ERROR in Modulefactory.checkSingularURL");
		}
	};
});

/*

	SubjectFactory

 */
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

		var url = checkSingularURL("read", studycourseID, moduleHandbookID, subjectID);

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

	factory.deleteSubject = function($http, $q, studycourseID, moduleHandbookID, subjectID) {

		var url = checkSingularURL("delete", studycourseID, moduleHandbookID, subjectID);

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(subjectID === data.subjectID) {
				Subject.subjectID = data.subjectID;
				Subject.module_handbooks_moduleHandbookID = data.module_handbooks_moduleHandbookID;
				Subject.name = data.name;
			} else {
				// ERROR
				console.log("ERROR in Subjectfactory.deleteSubject");
			}
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.checkSingularURL = function(method, studycourseID, moduleHandbookID, subjectID) {
		if(method) {
			var url = "/"+method+"/subject";

			if(studycourseID && moduleHandbookID && subjectID) {
				url = url+"?studycourseID="+studycourseID+"&moduleHandbookID="+moduleHandbookID+"&subjectID="+subjectID;
			} else if(subjectID) {
				url = url+"?subjectID="+subjectID;
			} else {
				// ERROR
				console.log("ERROR in Subjectfactory.checkSingularURL");
			}

			return url;
		} else {
			// ERROR
				console.log("ERROR in Subjectfactory.checkSingularURL");
		}
	};
});

/*

	ModuleHandbookFactory

 */
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

		var url = checkSingularURL("read", studycourseID, moduleHandbookID);

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

	factory.deleteModuleHandbook = function($http, $q, studycourseID, moduleHandbookID) {

		var url = checkSingularURL("delete", studycourseID, moduleHandbookID);

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(moduleHandbookID === data.moduleHandbookID) {
				ModuleHandbook.moduleHandbookID = data.moduleHandbookID;
				ModuleHandbook.studycourses_studycourseID = data.studycourses_studycourseID;
				ModuleHandbook.name = data.name;
			} else {
				// ERROR
				console.log("ERROR in ModuleHandbookfactory.checkSingularURL");
			}
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.checkSingularURL = function(method, studycourseID, moduleHandbookID) {
		if(method) {
			var url = "/"+method+"/modulehandbook";

			if(studycourseID && moduleHandbookID) {
				url = url+"?studycourseID="+studycourseID+"&moduleHandbookID="+moduleHandbookID;
			} else if(moduleHandbookID) {
				url = url+"?moduleHandbookID="+moduleHandbookID;
			} else {
				// ERROR
				console.log("ERROR in ModuleHandbookfactory.checkSingularURL");
			}

			return url;
		} else {
			// ERROR
			console.log("ERROR in ModuleHandbookfactory.checkSingularURL");
		}
	};
});

/*

	StudycourseFactory

 */
MMSApp.factory("StudycourseFactory", function() {
	var factory = {};
	var Studycourse = {
		studycourseID: "Number",
		name: "String",
		archived: "boolean"
	};
	var Studycourses = [];

	factory.getStudycourse = function($http, $q, studycourseID) {

		var url = checkSingularURL("read", studycourseID);

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

	factory.deleteStudycourse = function($http, $q, studycourseID) {

		var url = checkSingularURL("delete", studycourseID);

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(studycourseID === data.studycourseID) {
				Studycourse.studycourseID = data.studycourseID;
				Studycourse.name = data.name;
				Studycourse.archived = data.archived;
			} else {
				// ERROR
				console.log("ERROR in Studycoursefactory.deleteStudycourse");
			}
			deferred.resolve(data);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.checkSingularURL = function(method, studycourseID) {
		if(method) {
			var url = "/"+method+"/studycourse";

			if(studycourseID) {
				url = url+"?studycourseID="+studycourseID;
			} else {
				// ERROR
				console.log("ERROR in Studycoursefactory.checkSingularURL");
			}

			return url;
		} else {
			// ERROR
			console.log("ERROR in Studycoursefactory.checkSingularURL");
		}
	};
});