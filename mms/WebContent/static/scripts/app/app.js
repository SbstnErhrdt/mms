var MMSApp = angular.module("MMS", []);

var pURL = "../partials/";

MMSApp.config(["$routeProvider", function($routeProvider) {

	/*
	*	SHOW ROUTES
	*/
	$routeProvider.when("/home", {
		templateUrl: pURL+"home.html",
		controller: homeCtrl
	});
	$routeProvider.when("/show/studycourses", {
		templateUrl: pURL+"show/studycourses.html",
		controller: showStudycoursesCtrl
	});
	$routeProvider.when("/show/modulehandbooks", {
		templateUrl: pURL+"show/modulehandbooks.html",
		controller: showModuleHandbooksCtrl
	});
	$routeProvider.when("/show/subjects", {
		templateUrl: pURL+"show/subjects.html",
		controller: showSubjectsCtrl
	});
	$routeProvider.when("/show/modules", {
		templateUrl: pURL+"show/modules.html",
		controller: showModulesCtrl
	});
	$routeProvider.when("/show/events", {
		templateUrl: pURL+"show/events.html",
		controller: showEventsCtrl
	});
	$routeProvider.when("/show/users", {
		templateUrl: pURL+"show/users.html",
		controller: showUsersCtrl
	});


	/*
	*	DELETE ROUTES
	*/
	$routeProvider.when("/delete/studycourse", {
		templateUrl: pURL+"show/studycourses.html",
		controller: deleteStudycourseCtrl
	});
	$routeProvider.when("/delete/modulehandbook", {
		templateUrl: pURL+"show/modulehandbooks.html",
		controller: deleteModuleHandbookCtrl
	});
	$routeProvider.when("/delete/subject", {
		templateUrl: pURL+"show/subjects.html",
		controller: deleteSubjectCtrl
	});
	$routeProvider.when("/delete/module", {
		templateUrl: pURL+"show/modules.html",
		controller: deleteModuleCtrl
	});
	$routeProvider.when("/delete/event", {
		templateUrl: pURL+"show/events.html",
		controller: deleteEventCtrl
	});
	$routeProvider.when("/delete/user", {
		templateUrl: pURL+"show/users.html",
		controller: deleteUserCtrl
	});
	$routeProvider.otherwise({redirectTo: "/home"});
}]);

/*

	UserFactory

 */
MMSApp.factory("UserFactory", function($http, $q) {
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

	factory.getUser = function(email) {

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
			deferred.resolve(User);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.getUsers = function() {

		var url = "/read/users";

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			Users = data;
			deferred.resolve(Users);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.deleteUser = function(email) {

		var url = "/delete/user";

		if(email) {
			url = url+"?email="+email;
		} else {
			// ERROR
			console.log("ERROR in factory.getUser");
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(data.email === email && data.firstName && data.lastName) {
				// Success
				User.email = data.email;
				User.firstName = data.firstName;
				User.lastName = data.lastName;
			} else {
				// ERROR
				console.log("ERROR in factory.deleteUser");
			}
			deferred.resolve(User);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};
	return factory;
});

/*

	EmployeeFactory

 */
MMSApp.factory("EmployeeFactory", function($http, $q) {
	var factory = {};

	var Employee = {
		address: "String",
		phoneNum: "String",
		talkTime: "String"
	};

	var Employees = [];

	factory.getEmployee = function(email) {

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
			deferred.resolve(Employee);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.getEmployees = function() {

		var url = "read/employees";

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			Employees = data;
			deferred.resolve(Employees);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.deleteEmployee = function(email) {

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
			deferred.resolve(Employee);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};
	return factory;
});

/*

	EventFactory

 */
MMSApp.factory("EventFactory", function($http, $q) {
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
	factory.getEvent = function(eventID, studycourseID, moduleHandbookID, subjectID, moduleID) {

		var url = factory.checkSingularURL("read", studycourseID, moduleHandbookID, subjectID, moduleID, eventID);

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			Event = data;
			deferred.resolve(Event);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	/*
	 * getEvents: Holt alle Events einer bestimmten Kategorie vom Server
	 */
	factory.getEvents = function(moduleID, subjectID, moduleHandbookID, studycourseID) {

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
			deferred.resolve(Events);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.deleteEvent = function(eventID, studycourseID, moduleHandbookID, subjectID, moduleID) {

		var url = factory.checkSingularURL("delete", studycourseID, moduleHandbookID, subjectID, moduleID, eventID);

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(data.eventID === eventID && data.name) {
				Event.eventID = data.eventID;
				Event.name = data.name;
			} else {
				// ERROR
				console.log("ERROR in factory.deleteEvent");
			}
			deferred.resolve(Event);
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
	return factory;
});

/*

	ModuleFactory

 */
MMSApp.factory("ModuleFactory", function($http, $q) {
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
		literature: "String", // MGL: Array?!
		archived: "boolean"
	};
	var Modules = [];

	factory.getModule = function(moduleID, studycourseID, moduleHandbookID, subjectID) {

		var url = factory.checkSingularURL("read", studycourseID, moduleHandbookID, subjectID, moduleID);

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			Module = data;
			deferred.resolve(Module);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.getModules = function(subjectID, moduleHandbookID, studycourseID) {

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
			deferred.resolve(Modules);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.deleteModule = function(moduleID, studycourseID, moduleHandbookID, subjectID) {

		var url = factory.checkSingularURL("delete", studycourseID, moduleHandbookID, subjectID, moduleID);

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(data.moduleID === moduleID && data.name && data.subjects_subjectID) {
				Module.moduleID = data.moduleID;
				Module.name = data.name;
				Module.subjects_subjectID = data.subjects_subjectID;
			} else {
				// ERROR
				console.log("ERROR in Modulefactory.deleteModule");
			}
			deferred.resolve(Module);
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
			} else if(moduleID) {
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
	return factory;
});

/*

	SubjectFactory

 */
MMSApp.factory("SubjectFactory", function($http, $q) {
	var factory = {};
	var Subject = {
		subjectID: "Number",
		module_handbooks_moduleHandbookID: "Number",
		name: "String",
		archived: "boolean"
	};
	var Subjects = [];

	factory.getSubject = function(subjectID, studycourseID, moduleHandbookID) {

		var url = factory.checkSingularURL("read", studycourseID, moduleHandbookID, subjectID);

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			Subject = data;
			deferred.resolve(Subject);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.getSubjects = function(moduleHandbookID, studycourseID) {

		var url = "/read/subjects";

		if(studycourseID && !moduleHandbookID) {
			url = url+"?studycourseID="+studycourseID;
		} else if(studycourseID && moduleHandbookID) {
			url = url+"?studycourseID="+studycourseID+"&moduleHandbookID="+moduleHandbookID;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			Subjects = data;
			deferred.resolve(Subjects);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.deleteSubject = function(subjectID, studycourseID, moduleHandbookID) {

		var url = factory.checkSingularURL("delete", studycourseID, moduleHandbookID, subjectID);

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(subjectID === data.subjectID && data.module_handbooks_moduleHandbookID && data.name) {
				Subject.subjectID = data.subjectID;
				Subject.module_handbooks_moduleHandbookID = data.module_handbooks_moduleHandbookID;
				Subject.name = data.name;
			} else {
				// ERROR
				console.log("ERROR in Subjectfactory.deleteSubject");
			}
			deferred.resolve(Subject);
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
	return factory;
});

/*

	ModuleHandbookFactory

 */
MMSApp.factory("ModuleHandbookFactory", function($http, $q) {
	var factory = {};
	var ModuleHandbook = {
		moduleHandbookID: "Number",
		studycourses_studycourseID: "Number",
		name: "String",
		semester: "Number",
		archived: "boolean"
	};
	var ModuleHandbooks = [];

	factory.getModuleHandbook = function(moduleHandbookID, studycourseID) {

		var url = factory.checkSingularURL("read", studycourseID, moduleHandbookID);

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			ModuleHandbook = data;
			deferred.resolve(ModuleHandbook);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.getModuleHandbooks = function(studycourseID) {

		var url = "/read/modulehandbooks";

		if(studycourseID) {
			url = url+"?studycourseID="+studycourseID;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			ModuleHandbooks = data;
			deferred.resolve(ModuleHandbooks);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;

	};

	factory.deleteModuleHandbook = function(moduleHandbookID, studycourseID) {

		var url = factory.checkSingularURL("delete", studycourseID, moduleHandbookID);

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(moduleHandbookID === data.moduleHandbookID && data.studycourses_studycourseID && data.name) {
				ModuleHandbook.moduleHandbookID = data.moduleHandbookID;
				ModuleHandbook.studycourses_studycourseID = data.studycourses_studycourseID;
				ModuleHandbook.name = data.name;
			} else {
				// ERROR
				console.log("ERROR in ModuleHandbookfactory.checkSingularURL");
			}
			deferred.resolve(ModuleHandbook);
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
	return factory;
});

/*

	StudycourseFactory

 */
MMSApp.factory("StudycourseFactory", function($http, $q) {
	var factory = {};
	var Studycourse = {
		studycourseID: "Number",
		name: "String",
		archived: "boolean"
	};
	var Studycourses = [];

	factory.getStudycourse = function(studycourseID) {

		var url = factory.checkSingularURL("read", studycourseID);

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			Studycourse = data;
			deferred.resolve(Studycourse);
		}).error(function(data, status) {
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.getStudycourses = function() {

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

	factory.deleteStudycourse = function(studycourseID) {

		var url = factory.checkSingularURL("delete", studycourseID);

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(studycourseID === data.studycourseID && data.name && data.archived) {
				Studycourse.studycourseID = data.studycourseID;
				Studycourse.name = data.name;
				Studycourse.archived = data.archived;
			} else {
				// ERROR
				console.log("ERROR in Studycoursefactory.deleteStudycourse");
			}
			deferred.resolve(Studycourse);
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
	return factory;
});