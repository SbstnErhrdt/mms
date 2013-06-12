/*

	Tabs Projekt! Spaces könnt ihr nehmen, dann aber VIER und ja nicht 2!!!
	I DREH EICH OIGAHENDIG S KNICK ROM A WENN IH
*/


var MMSApp = angular.module("MMS", ["ngCookies"]);

// Partials URL
var pURL = "./partials/";

// Host URL
var hURL = "http://sopra.ex-studios.net:8080/mms/";

MMSApp.config(function($routeProvider, $httpProvider) {

	$httpProvider.defaults.withCredentials = true;

	/*
	*	SHOW ROUTES
	*/
	$routeProvider.when("/home", {
		templateUrl: pURL+"home.html",
		controller: homeCtrl
	});
	$routeProvider.when("/overview", {
		templateUrl: pURL+"overview.html",
		controller: overviewCtrl
	});
	$routeProvider.when("/show/studycourses", {
		templateUrl: pURL+"show/studycourses.html",
		controller: showStudycoursesCtrl
	});
	$routeProvider.when("/show/studycourse", {
		templateUrl: pURL+"show/studycourse.html",
		controller: showStudycourseCtrl
	});
	$routeProvider.when("/show/modulehandbooks", {
		templateUrl: pURL+"show/modulehandbooks.html",
		controller: showModuleHandbooksCtrl
	});
	$routeProvider.when("/show/modulehandbook", {
		templateUrl: pURL+"show/modulehandbook.html",
		controller: showModuleHandbookCtrl
	});
	$routeProvider.when("/show/subjects", {
		templateUrl: pURL+"show/subjects.html",
		controller: showSubjectsCtrl
	});
	$routeProvider.when("/show/subject", {
		templateUrl: pURL+"show/subject.html",
		controller: showSubjectCtrl
	});
	$routeProvider.when("/show/modules", {
		templateUrl: pURL+"show/modules.html",
		controller: showModulesCtrl
	});
	$routeProvider.when("/show/module", {
		templateUrl: pURL+"show/module.html",
		controller: showModuleCtrl
	});
	$routeProvider.when("/show/events", {
		templateUrl: pURL+"show/events.html",
		controller: showEventsCtrl
	});
	$routeProvider.when("/show/event", {
		templateUrl: pURL+"show/event.html",
		controller: showEventCtrl
	});
	$routeProvider.when("/show/users", {
		templateUrl: pURL+"show/users.html",
		controller: showUsersCtrl
	});
	$routeProvider.when("/show/user", {
		templateUrl: pURL+"show/user.html",
		controller: showUserCtrl
	});

	/*
	*	CREATE ROUTES
	*/
	$routeProvider.when("/create/user", {
		templateUrl: pURL+"create/user.html"
		//controller: createUserCtrl
	});

	$routeProvider.when("/create/studycourse", {
		templateUrl: pURL+"create/studycourse.html"
		//controller: createStudycourseCtrl
	});

	$routeProvider.when("/create/modulehandbook", {
		templateUrl: pURL+"create/modulehandbook.html"
		//controller: createModulehandbookCtrl
	});

	$routeProvider.when("/create/subject", {
		templateUrl: pURL+"create/subject.html"
		//controller: createSubjectCtrl
	});

	$routeProvider.when("/create/module", {
		templateUrl: pURL+"create/module.html"
		//controller: createModuleCtrl
	});

	$routeProvider.when("/create/event", {
		templateUrl: pURL+"create/event.html"
		//controller: createEventCtrl
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
});

/*

	activeUserFactory

	-> Der eingeloggte Benutzer
 */
MMSApp.factory("ActiveUserFactory", function($http, $q, UserFactory) {
	var factory = {};

	var ActiveUser = {
		address: String,
		email: String,
		emailVerified: Boolean,
		employeeRights: Object,
		firstName: String,
		graduation: String,
		isEmployee: Boolean,
		lastName: String,
		matricNum: Number,
		password: Number,
		phoneNum: String,
		semester: Number,
		talkTime: String,
		title: String,
		userRights: Object
	};

	factory.getActiveUser = function() {
	};

	factory.setActiveUser = function(activeUser) {
		ActiveUser = activeUser;
	};

	return factory;
});

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

	factory.createUser = function(user) {
		// BENÖTIGTE FELDER - FIX THIS
		if(user.email) {


		} else {
			// Error
			console.log("Error: Es wurden nicht alle Felder ausgefüllt.");
		}
	};

	factory.getUser = function(email) {

		var url = hURL+"read/user";

		if(email) {
			url = url+"?email="+email;
		} else {
			// ERROR
			console.log("ERROR in factory.getUser");
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {

			if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// Success
				User = data;
				deferred.resolve(User);
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.getUsers = function() {

		var url = hURL+"read/users";

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {

			if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// Success
				Users = data;
				deferred.resolve(Users);
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.deleteUser = function(email) {

		var url = hURL+"delete/user";

		if(email) {
			url = url+"?email="+email;
		} else {
			// ERROR
			console.log("ERROR in factory.getUser");
		}

		var deferred = $q.defer();
		$http({
			method: "GET",
			url: url
		}).success(function(data, status) {

			if(data.email === email) {
				// Success
				User.email = data.email;
				deferred.resolve(User);
			} else if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// ERROR
				console.log("ERROR in factory.deleteUser");
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
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

	factory.createEmployee = function(employee) {
		// BENÖTIGTE FELDER - FIX THIS
		if(employee.name) {


		} else {
			// Error
			console.log("Error: Es wurden nicht alle Felder ausgefüllt.");
		}
	};

	factory.getEmployee = function(email) {

		var url = hURL+"read/employee";

		if(email) {
			url = url+"?email="+email;
		} else {
			// ERROR
			console.log("ERROR in factory.getEmployee");
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {

			if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// Success
				Employee = data;
				deferred.resolve(Employee);
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.getEmployees = function() {

		var url = hURL+"read/employees";

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {

			if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// Success
				Employees = data;
				deferred.resolve(Employees);
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.deleteEmployee = function(email) {

		var url = hURL+"delete/employee";

		if(email) {
			url = url+"?email="+email;
		} else {
			// ERROR
			console.log("ERROR in factory.deleteEmployee");
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {

			if(data.email === email) {
				// Success
				Employee.email = data.email;
				deferred.resolve(Employee);
			} else if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// ERROR
				console.log("ERROR in factory.deleteEmployee");
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
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

	factory.createEvent = function(_event) {
		// BENÖTIGTE FELDER - FIX THIS
		if(_event.name && _event.lecturer_email && _event.sws) {

			var url = factory.checkSingularURL("create");
			if(url.error) {
				return url.error;
			}

			$http({
				method: "POST",
				url: url,
				data: _event,
				headers: {
			        	"withCredentials": true,
			        	"X-Requested-With": false
			        }
			}).
				success(function(data, status, headers, config) {

					if(data.error) {
						// Error
						console.log("Servernachricht: "+data.error.message);
					} else if(data === "null") {
						// Error
						console.log("Der Server lieferte 'null' zurück.");
					} else {
						if(_event.name == data.name) {
							console.log("Event wurde erstellt.");
						} else {
							console.log("Event wurde nicht erstellt.");
						}
					}

				}).
				error(function(data, status, headers, config) {
					console.log("Error: "+data);
				});

		} else {
			// Error
			console.log("Error: Es wurden nicht alle Felder ausgefüllt.");
		}
	};

	/*
	 * getEvent: Holt ein Event mit einer bestimmten eventID vom Server
	 */
	factory.getEvent = function(eventID) {

		var url = factory.checkSingularURL("read", eventID);
		if(url.error) {
			return url.error;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {

			if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// Success
				Event = data;
				deferred.resolve(Event);
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	/*
	 * getEvents: Holt alle Events einer bestimmten Kategorie vom Server
	 */
	factory.getEvents = function(moduleID) {

		var url = hURL+"read/events";

		if(moduleID) {
			url = url+"?moduleID="+moduleID;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {

			if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// Success
				Events = data; // FIX THIS
				deferred.resolve(Events);
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.deleteEvent = function(eventID) {

		var url = factory.checkSingularURL("delete", eventID);
		if(url.error) {
			return url.error;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			console.log(data);
			if(data.eventID === parseInt(eventID)) {
				// Success
				Event = {};
				Event.eventID = data.eventID;
			} else if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// ERROR
				console.log("ERROR in factory.deleteEvent");
			}
			deferred.resolve(Event);
		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.checkSingularURL = function(method, eventID) {
		if(method) {

			var url = hURL+method+"/event";

			if(eventID) {
				url  = url+"?eventID="+eventID;
			}

			return url;

		} else {
			// ERROR
			console.log("ERROR in Eventfactory.checkSingularURL");
			return {
				error: {
					message: "Method in checkSingularURL wasn't definded."
				}
			};
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

	factory.createModule = function(module) {
		// BENÖTIGTE FELDER - FIX THIS
		if(module.name) {


		} else {
			// Error
			console.log("Error: Es wurden nicht alle Felder ausgefüllt.");
		}
	};

	factory.getModule = function(moduleID) {

		var url = factory.checkSingularURL("read", moduleID);
		if(url.error) {
			return url.error;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {

			if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// Success
				Module = data;
				deferred.resolve(Module);
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.getModules = function(subjectID) {

		var url = hURL+"read/modules";

		if(subjectID) {
			url = url+"?subjectID="+subjectID;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {

			if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// Success
				Modules = data;
				deferred.resolve(Modules);
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.deleteModule = function(moduleID) {

		var url = factory.checkSingularURL("delete", moduleID);
		if(url.error) {
			return url.error;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(data.moduleID === moduleID && data.name && data.subjects_subjectID) {
				Module.moduleID = data.moduleID;
				Module.name = data.name;
				Module.subjects_subjectID = data.subjects_subjectID;
			} else if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// ERROR
				console.log("ERROR in Modulefactory.deleteModule");
			}
			deferred.resolve(Module);
		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.checkSingularURL = function(method, moduleID) {
		if(method) {

			var url = hURL+method+"/module";

			if(moduleID) {
				url  = url+"?moduleID="+moduleID;
			}

			return url;

		} else {
			// ERROR
			console.log("ERROR in Eventfactory.checkSingularURL");

			return {
				error: {
					message: "Method in checkSingularURL wasn't definded."
				}
			};
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

	factory.createSubject = function(subject) {
		// BENÖTIGTE FELDER - FIX THIS
		if(subject.name) {


		} else {
			// Error
			console.log("Error: Es wurden nicht alle Felder ausgefüllt.");
		}
	};

	factory.getSubject = function(subjectID) {

		var url = factory.checkSingularURL("read", subjectID);
		if(url.error) {
			return url.error;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {

			if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// Success
				Subject = data;
				deferred.resolve(Subject);
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.getSubjects = function(moduleHandbookID) {

		var url = hURL+"read/subjects";

		if(moduleHandbookID) {
			url = url+"?moduleHandbookID="+moduleHandbookID;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {

			if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// Success
				Subjects = data;
				deferred.resolve(Subjects);
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.deleteSubject = function(subjectID) {

		var url = factory.checkSingularURL("delete", subjectID);
		if(url.error) {
			return url.error;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(subjectID === data.subjectID && data.module_handbooks_moduleHandbookID && data.name) {
				Subject.subjectID = data.subjectID;
				Subject.module_handbooks_moduleHandbookID = data.module_handbooks_moduleHandbookID;
				Subject.name = data.name;
			} else if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// ERROR
				console.log("ERROR in Subjectfactory.deleteSubject");
			}
			deferred.resolve(Subject);
		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.checkSingularURL = function(method, subjectID) {
		if(method) {
			var url = hURL+method+"/subject";

			if(subjectID) {
				url = url+"?subjectID="+subjectID;
			}

			return url;
		} else {
			// ERROR
			console.log("ERROR in Eventfactory.checkSingularURL");

			return {
				error: {
					message: "Method in checkSingularURL wasn't definded."
				}
			};
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

	factory.createModuleHandbook = function(moduleHandbook) {
		// BENÖTIGTE FELDER - FIX THIS
		if(moduleHandbook.name) {


		} else {
			// Error
			console.log("Error: Es wurden nicht alle Felder ausgefüllt.");
		}
	};

	factory.getModuleHandbook = function(moduleHandbookID) {

		var url = factory.checkSingularURL("read", moduleHandbookID);
		if(url.error) {
			return url.error;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {

			if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// Success
				ModuleHandbook = data;
				deferred.resolve(ModuleHandbook);
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.getModuleHandbooks = function(studycourseID) {

		var url = hURL+"read/modulehandbooks";

		if(studycourseID) {
			url = url+"?studycourseID="+studycourseID;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {

			if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// Success
				ModuleHandbooks = data;
				deferred.resolve(ModuleHandbooks);
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;

	};

	factory.deleteModuleHandbook = function(moduleHandbookID) {

		var url = factory.checkSingularURL("delete", moduleHandbookID);
		if(url.error) {
			return url.error;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(moduleHandbookID === data.moduleHandbookID && data.studycourses_studycourseID && data.name) {
				ModuleHandbook.moduleHandbookID = data.moduleHandbookID;
				ModuleHandbook.studycourses_studycourseID = data.studycourses_studycourseID;
				ModuleHandbook.name = data.name;
			} else if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// ERROR
				console.log("ERROR in ModuleHandbookfactory.checkSingularURL");
			}
			deferred.resolve(ModuleHandbook);
		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.checkSingularURL = function(method, moduleHandbookID) {
		if(method) {
			var url = hURL+method+"/modulehandbook";

			if(moduleHandbookID) {
				url = url+"?moduleHandbookID="+moduleHandbookID;
			}

			return url;

		} else {
			// ERROR
			console.log("ERROR in Eventfactory.checkSingularURL");

			return {
				error: {
					message: "Method in checkSingularURL wasn't definded."
				}
			};
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

	factory.createStudycourse = function(studycourse) {
		// BENÖTIGTE FELDER - FIX THIS
		if(studycourse.name) {


		} else {
			// Error
			console.log("Error: Es wurden nicht alle Felder ausgefüllt.");
		}
	};

	factory.getStudycourse = function(studycourseID) {

		var url = factory.checkSingularURL("read", studycourseID);
		if(url.error) {
			return url.error;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {

			if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// Success
				Studycourse = data;
				deferred.resolve(Studycourse);
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.getStudycourses = function() {


		var url = hURL+"read/studycourses";

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {

			if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// Success
				Studycourses = data;
				deferred.resolve(data);
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.deleteStudycourse = function(studycourseID) {

		var url = factory.checkSingularURL("delete", studycourseID);
		if(url.error) {
			return url.error;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(studycourseID === data.studycourseID && data.name && data.archived) {
				Studycourse.studycourseID = data.studycourseID;
				Studycourse.name = data.name;
				Studycourse.archived = data.archived;
			} else if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// ERROR
				console.log("ERROR in Studycoursefactory.deleteStudycourse");
			}
			deferred.resolve(Studycourse);
		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.checkSingularURL = function(method, studycourseID) {
		if(method) {

			var url = hURL+method+"/studycourse";

			if(studycourseID) {
				url = url+"?studycourseID="+studycourseID;
			}

			return url;

		} else {
			// ERROR
			console.log("ERROR in Eventfactory.checkSingularURL");

			return {
				error: {
					message: "Method in checkSingularURL wasn't definded."
				}
			};
		}
	};
	return factory;
});