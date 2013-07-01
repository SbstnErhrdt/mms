"use strict"
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
	$routeProvider.when("/imprint", {
		templateUrl: pURL+"imprint.html",
		//controller: imprintCtrl
	});
	$routeProvider.when("/contact", {
		templateUrl: pURL+"contact.html",
		//controller: contectCtrl
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
	$routeProvider.when("/show/deadlines", {
        templateUrl: pURL+"show/deadlines.html",
        controller: showDeadlinesCtrl
    });
	$routeProvider.when("/show/deadline", {
        templateUrl: pURL+"show/deadline.html",
        controller: showDeadlineCtrl
    });
    // DEBUG
    $routeProvider.when("/request", {
        templateUrl: pURL+"requestTest.html",
        controller: requestCtrl
    });


	/*
	*	CREATE ROUTES
	*/
	$routeProvider.when("/create/user", {
		templateUrl: pURL+"create/user.html",
		controller: createUserCtrl
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
		templateUrl: pURL+"create/subject.html",
		controller: createSubjectCtrl
	});

	$routeProvider.when("/create/module", {
		templateUrl: pURL+"create/module.html",
		controller: createModuleCtrl
	});

	$routeProvider.when("/create/event", {
		templateUrl: pURL+"create/event.html",
		controller: createEventCtrl
	});

	$routeProvider.when("/create/deadline", {
		templateUrl: pURL+"create/deadline.html"
		//controller: createDeadlineCtrl
	});

	/*
	*	UPDATE ROUTES
	*/
	$routeProvider.when("/update/user", {
		templateUrl: pURL+"update/user.html",
		controller: updateUserCtrl
	});

	$routeProvider.when("/update/studycourse", {
		templateUrl: pURL+"update/studycourse.html",
		controller: updateStudycourseCtrl
	});

	$routeProvider.when("/update/modulehandbook", {
		templateUrl: pURL+"update/modulehandbook.html",
		controller: updateModuleHandbookCtrl
	});

	$routeProvider.when("/update/subject", {
		templateUrl: pURL+"update/subject.html",
		controller: updateSubjectCtrl
	});

	$routeProvider.when("/update/module", {
		templateUrl: pURL+"update/module.html",
		controller: updateModuleCtrl
	});

	$routeProvider.when("/update/event", {
		templateUrl: pURL+"update/event.html",
		controller: updateEventCtrl
	});

	$routeProvider.when("/update/deadline", {
		templateUrl: pURL+"update/deadline.html",
		//controller: updateDeadlineCtrl
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
	$routeProvider.when("/delete/deadline", {
		templateUrl: pURL+"show/deadline.html",
		//controller: deleteDeadlineCtrl
	});
	$routeProvider.otherwise({redirectTo: "/home"});


	/*
		REGISTER ROUTE
	 */
	$routeProvider.when("/register", {
		templateUrl: pURL+"register.html",
		controller: registerCtrl
	});

	$routeProvider.when("/confirm", {
		templateUrl: pURL+"confirm.html",
		controller: confirmCtrl
	});

	/*
		PRINT ROUTE
	 */
	 $routeProvider.when("/print/modulehandbook", {
		templateUrl: pURL+"print/modulehandbook.html",
		controller: printModulehandbookCtrl
	});

});

/*

	activeUserFactory

	-> Der eingeloggte Benutzer
 */
MMSApp.factory("ActiveUserFactory", function($http, $q, $cookies) {
	var factory = {};

	var ActiveUser = {
		address: "String",
		email: "String",
		employeeRights: {
			isAdmin: "boolean",
			canDeblockModule: "boolean",
			canDeblockCriticalModule: "boolean"
		},
		firstName: "String",
		graduation: "String",
		isEmployee: "boolean",
		lastName: "String",
		matricNum: "Number",
		password: "Number",
		phoneNum: "String",
		semester: "Number",
		talkTime: "String",
		title: "String"
	};

	factory.getActiveUser = function() {

		var url = hURL+"read/activeUser";

		if($cookies["JSESSIONID"]) {
			url = url+"?JSESSIONID="+$cookies["JSESSIONID"];
		} else {
			// ERROR
			sendError("ERROR in factory.getActiveUser");
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {

			if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
				return {
					error: {
						message: data.error.message
					}
				};
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// Success
				ActiveUser = data;
				deferred.resolve(ActiveUser);
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.setActiveUser = function(activeUser) {
		ActiveUser = activeUser;
	};

	factory.createActiveUser = function(activeUser) {

		var url = hURL+"register";

		var deferred = $q.defer();
		$http({
			method: "POST",
			url: url,
			data: activeUser
		}).success(function(data, status, headers, config) {
			ActiveUser = data;
			deferred.resolve(ActiveUser);
		}).error(function(data, status, headers, config) {
			sendError("Error: "+data+" - Status: "+status);
			deferred.reject(data);
		});

		return deferred.promise;
	};

	factory.isAdmin = function() {
		console.log(ActiveUser);
		if (typeof ActiveUser.employeeRights === "undefined") {
			console.log("ActiveUser is no admin (employeeRights === 'undefined')");
			return false;
		} else if(ActiveUser.employeeRights.isAdmin) {
			console.log("ActiveUser is admin");
			return true;
		} else {
			console.log("ActiveUser is no admin");
			return false;
		}
	};

	factory.isEmployee = function() {
		if (typeof ActiveUser.isEmployee === "undefined") {
			console.log("ActiveUser is no employee (isEmployee === 'undefined')");
			return false;
		} else if(ActiveUser.isEmployee) {
			console.log("ActiveUser is employee");
			return true;
		} else {
			console.log("ActiveUser is no employee");
			return false;
		}
	}

	factory.canDeblockContent = function() {
		if (typeof ActiveUser.employeeRights === "undefined") {
			console.log("ActiveUser is not allowed to enable content (employeeRights === 'undefined')");
			return false;
		} else if(ActiveUser.employeeRights.isAdmin) {
			console.log("ActiveUser is admin");
			return true;
		} else if(ActiveUser.employeeRights.canDeblockModule) {
			console.log("ActiveUser is allowed to enable content");
			return true;
		} else {
			console.log("ActiveUser is not allowed to enable content");
			return false;
		}
	}

	factory.canDeblockCriticalModule = function() {
		if (typeof ActiveUser.employeeRights === "undefined") {
			console.log("ActiveUser is not allowed to enable (critical) modules (employeeRights === 'undefined')");
			return false;
		} else if(ActiveUser.employeeRights.isAdmin) {
			console.log("ActiveUser is admin");
			return true;
		} else if(ActiveUser.employeeRights.canDeblockCriticalModule) {
			console.log("ActiveUser is allowed to enable critical modules");
			return true;
		} else {
			console.log("ActiveUser is not allowed to enable critical modules");
			return false;
		}
	}

	factory.isAuthorised = function(content) {
		console.log("function isAutorised");
		if(typeof ActiveUser.employeeRights === "undefined" || typeof ActiveUser.isEmployee === "undefined") {
			console.log("ActiveUser is no admin and no employee (employeeRights || isEmployee === 'undefined')");
			return false;
		} else if(!ActiveUser.employeeRights.isAdmin && !ActiveUser.isEmployee) {
			console.log("ActiveUser is no admin and no employee");
			console.log(ActiveUser.employeeRights);
			return false;
		} else if(ActiveUser.employeeRights.isAdmin) {
			console.log("ActiveUser is admin");
			return true;
		} else if (ActiveUser.isEmployee) {
			console.log("ActiveUser is employee");
			if(content === "event") {
				console.log("content === 'event'");
				if(ActiveUser.employeeRights.eventRightsList.length > 0){
					console.log("ActiveUser.employeeRights.eventRightsList.length is NOT 0");
					return true;
				} else {
					console.log("ActiveUser.employeeRights.eventRightsList.length === 0");
					return false;
				}
			} else if(content === "module") {
				console.log("content === 'module'");
				if(ActiveUser.employeeRights.moduleRightsList.length > 0){
					console.log("ActiveUser.employeeRights.moduleRightsList.length is NOT 0");
					return true;
				} else {
					console.log("ActiveUser.employeeRights.moduleRightsList.length === 0");
					return false;
				}
			} else if(content === "subject") {
				console.log("content === 'subject'");
				if(ActiveUser.employeeRights.subjectRightsList.length > 0){
					console.log("ActiveUser.employeeRights.subjectRightsList.length is NOT 0");
					return true;
				} else {
					console.log("ActiveUser.employeeRights.subjectRightsList.length === 0");
					return false;
				}
			} else if(content === "moduleHandbook") {
				console.log("content === 'moduleHandbook'");
				if(ActiveUser.employeeRights.moduleHandbookRightsList.length > 0){
					console.log("ActiveUser.employeeRights.moduleHandbookRightsList.length is NOT 0");
					return true;
				} else {
					console.log("ActiveUser.employeeRights.moduleHandbookRightsList.length === 0");
					return false;
				}
			} else if(content === "studycourse") {
				console.log("content === 'studycourse'");
				if(ActiveUser.employeeRights.studycoursekRightsList.length > 0){
					console.log("ActiveUser.employeeRights.studycourseRightsList.length is NOT 0");
					return true;
				} else {
					console.log("ActiveUser.employeeRights.studycourseRightsList.length === 0");
					return false;
				}
			}
		} else {
			console.log("unknown case");
			console.log(ActiveUser.employeeRights);
			return false;
		}
	};

	factory.canEdit = function(id, content) {
		if(typeof ActiveUser.employeeRights === "undefined") {
			console.log("ActiveUser is no employee (employeeRights === 'undefined')");
			return false;
		} else if(ActiveUser.employeeRights.isAdmin) {
			console.log("ActiveUser is admin");
			return true;
		} else {
			var list = [];
			// event
			if(content === "event") {
				list = ActiveUser.employeeRights.eventRightsList;
				for(var i=0;i<list.length;i++) {
					var x = list[i];
					if(x.eventID === id) {
						console.log("eventID "+id+" matches an entry in eventRightsList");
						if(x.canEdit) {
							console.log("canEdit = true");
							return true;
						} else {
							console.log("canEdit = false");
						}
					}
				}
				return false;

			// module
			} else if(content === "module") {
				list = ActiveUser.employeeRights.moduleRightsList;
				for(var i=0;i<list.length;i++) {
					var x = list[i];
					if(x.moduleID === id) {
						console.log("moduleID "+id+" matches an entry in moduleRightsList");
						if(x.canEdit) {
							console.log("canEdit = true");
							return true;
						} else {
							console.log("canEdit = false");
						}
					}
				}
				return false;

			// subject
			} else if(content === "subject") {
				list = ActiveUser.employeeRights.subjectRightsList;
				for(var i=0;i<list.length;i++) {
					var x = list[i];
					if(x.subjectID === id) {
						console.log("subjectID "+id+" matches an entry in subjectRightsList");
						if(x.canEdit) {
							console.log("canEdit = true");
							return true;
						} else {
							console.log("canEdit = false");
						}
					}
				}
				return false;
			} else if(content === "moduleHandbook") {
				list = ActiveUser.employeeRights.moduleHandbookRightsList;
				for(var i=0;i<list.length;i++) {
					var x = list[i];
					if(x.moduleHandbookID === id) {
						console.log("moduleHandbookID "+id+" matches an entry in moduleHandbookRightsList");
						if(x.canEdit) {
							console.log("canEdit = true");
							return true;
						} else {
							console.log("canEdit = false");
						}
					}
				}
				return false;
			} else if(content === "studycourse") {
				list = ActiveUser.employeeRights.studycourseRightsList;
				for(var i=0;i<list.length;i++) {
					var x = list[i];
					if(x.studycourseID === id) {
						console.log("studycourseID "+id+" matches an entry in studycourseRightsList");
						if(x.canEdit) {
							console.log("canEdit = true");
							return true;
						} else {
							console.log("canEdit = false");
						}
					}
				}
				return false;
			}
			console.log("unexpected case in function canEdit");
			return false;
		}
	};

	factory.canDelete = function(id, content) {
		if(typeof ActiveUser.employeeRights === "undefined") {
			console.log("ActiveUser is no employee (employeeRights === 'undefined')");
			return false;
		} else if(ActiveUser.employeeRights.isAdmin) {
			console.log("ActiveUser is admin");
			return true;
		} else {
			var list = [];
			// event
			if(content === "event") {
				list = ActiveUser.employeeRights.eventRightsList;
				for(var i=0;i<list.length;i++) {
					var x = list[i];
					if(x.eventID === id) {
						console.log("eventID "+id+" matches an entry in eventRightsList");
						if(x.canDelete) {
							console.log("canDelete = true");
							return true;
						} else {
							console.log("canDelete = false");
						}
					}
				}
				return false;

			// module
			} else if(content === "module") {
				list = ActiveUser.employeeRights.moduleRightsList;
				for(var i=0;i<list.length;i++) {
					var x = list[i];
					if(x.moduleID === id) {
						console.log("moduleID "+id+" matches an entry in moduleRightsList");
						if(x.canDelete) {
							console.log("canDelete = true");
							return true;
						} else {
							console.log("canDelete = false");
						}
					}
				}
				return false;

			// subject
			} else if(content === "subject") {
				list = ActiveUser.employeeRights.subjectRightsList;
				for(var i=0;i<list.length;i++) {
					var x = list[i];
					if(x.subjectID === id) {
						console.log("subjectID "+id+" matches an entry in subjectRightsList");
						if(x.canDelete) {
							console.log("canDelete = true");
							return true;
						} else {
							console.log("canDelete = false");
						}
					}
				}
				return false;
			} else if(content === "moduleHandbook") {
				list = ActiveUser.employeeRights.moduleHandbookRightsList;
				for(var i=0;i<list.length;i++) {
					var x = list[i];
					if(x.moduleHandbookID === id) {
						console.log("moduleHandbookID "+id+" matches an entry in moduleHandbookRightsList");
						if(x.canDelete) {
							console.log("canDelete = true");
							return true;
						} else {
							console.log("canDelete = false");
						}
					}
				}
				return false;
			} else if(content === "studycourse") {
				list = ActiveUser.employeeRights.studycourseRightsList;
				for(var i=0;i<list.length;i++) {
					var x = list[i];
					if(x.studycourseID === id) {
						console.log("studycourseID "+id+" matches an entry in studycourseRightsList");
						if(x.canDelete) {
							console.log("canDelete = true");
							return true;
						} else {
							console.log("canDelete = false");
						}
					}
				}
				return false;
			}
			console.log("unexpected case in function canEdit");
			return false;
		}
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

	factory.createUser = function(user, callback) {
		// BENÖTIGTE FELDER - FIX THIS
  		if(user.email) {

            var url = hURL+"create/user";

            $http({
                method: "POST",
                url: url,
                data: user
            }).
                success(function(data, status, headers, config) {

                    if(data.error) {
                        // Error
                        console.log("Servernachricht: "+data.error.message);
                    } else if(data === "null") {
                        // Error
                        console.log("Der Server lieferte 'null' zurück.");
                    } else {
                        console.log(data);
                        if(event.name == data.name) {
                            console.log("User wurde erstellt.");
                        } else {
                            console.log("User wurde nicht erstellt.");
                        }
                    }
                    callback();
                }).
                error(function(data, status, headers, config) {
                    sendError("Error: "+data+" - "+status);
                    callback();
                });

        } else {
            // Error
            console.log("Error: Es wurden nicht alle nötigen Felder (email) ausgefüllt.");
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

	factory.updateUser = function(user,callback) {
        if(user.email) {

            var url = hURL+"update/user";

            $http({
                method: "POST",
                url: url,
                data: user
            }).
                success(function(data, status, headers, config) {

                    if(data.error) {
                        // Error
                        console.log("Servernachricht: "+data.error.message);
                    } else if(data === "null") {
                        // Error
                        console.log("Der Server lieferte 'null' zurück.");
                    } else {
                        console.log(data);
                        if(event.name == data.name) {
                            console.log("User wurde geupdated.");
                        } else {
                            console.log("User wurde nicht geupdated.");
                        }
                    }
                    callback();
                }).
                error(function(data, status, headers, config) {
                    sendError("Error: "+data+" - "+status);
                    callback();
                });

        } else {
            // Error
            console.log("Error: Es wurden nicht alle nötigen Felder (email) ausgefüllt.");
        }
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
		archived: "boolean",
		enabled: "boolean",
		room:"String",
		type:"String",
		place:"String"

	};
	var Events = [];

	factory.createEvent = function(_event, callback) {
		// BENÖTIGTE FELDER - FIX THIS
		if(_event.name && _event.moduleIDs) {

			var url = factory.checkSingularURL("create");
			if(url.error) {
				return url.error;
			}

			$http({
				method: "POST",
				url: url,
				data: _event
			}).
				success(function(data, status, headers, config) {

					if(data.error) {
						// Error
						console.log("Servernachricht: "+data.error.message);
					} else if(data === "null") {
						// Error
						console.log("Der Server lieferte 'null' zurück.");
					} else {
						console.log(data);
						if(_event.name == data.name) {
							console.log("Event wurde erstellt.");
						} else {
							console.log("Event wurde nicht erstellt.");
						}
					}
					callback();
				}).
				error(function(data, status, headers, config) {
					sendError("Error: "+data+" - "+status);
					callback();
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
				Events = data;
				deferred.resolve(Events);
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.deleteEvent = function(eventID) {

		eventID = parseInt(eventID);

		var url = factory.checkSingularURL("delete", eventID);
		if(url.error) {
			return url.error;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(data.eventID === eventID) {
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

    factory.updateEvent = function (event, callback) {

        if(event.name && event.moduleIDs) {

            var url = factory.checkSingularURL("update");
            if(url.error) {
                return url.error;
            }

            $http({
                method: "POST",
                url: url,
                data: event
            }).
                success(function(data, status, headers, config) {

                    if(data.error) {
                        // Error
                        console.log("Servernachricht: "+data.error.message);
                    } else if(data === "null") {
                        // Error
                        console.log("Der Server lieferte 'null' zurück.");
                    } else {
                        console.log(data);
                        if(event.name == data.name) {
                            console.log("Event wurde geupdated.");
                        } else {
                            console.log("Event wurde nicht geupdated.");
                        }
                    }
                    callback();
                }).
                error(function(data, status, headers, config) {
                    sendError("Error: "+data+" - "+status);
                    callback();
                });

        } else {
            // Error
            console.log("Error: Es wurden nicht alle Felder ausgefüllt.");
        }
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

	factory.createModule = function(module, callback) {
		// BENÖTIGTE FELDER - FIX THIS
		if(module.name) {

			var url = factory.checkSingularURL("create");
			if(url.error) {
				return url.error;
			}

			$http({
				method: "POST",
				url: url,
				data: module
			}).
				success(function(data, status, headers, config) {

					if(data.error) {
						// Error
						console.log("Servernachricht: "+data.error.message);
					} else if(data === "null") {
						// Error
						console.log("Der Server lieferte 'null' zurück.");
					} else {
						console.log(data);
						if(module.name == data.name) {
							console.log("Event wurde erstellt.");
						} else {
							console.log("Event wurde nicht erstellt.");
						}
					}
					callback();
				}).
				error(function(data, status, headers, config) {
					sendError("Error: "+data+" - "+status);
					callback();
				});

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

		moduleID = parseInt(moduleID);

		var url = factory.checkSingularURL("delete", moduleID);
		if(url.error) {
			return url.error;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(data.moduleID === moduleID) {
				Module.moduleID = data.moduleID;
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

	factory.createSubject = function(subject, callback) {
		if(subject.name) {
			var url = factory.checkSingularURL("create");

			if(url.error) {
				return url.error;
			}

			$http({
				method: "POST",
				url: url,
				data: subject
			}).success(function(data, status, headers, config) {

				if(data.error) {
						// Error
						console.log("Servernachricht: "+data.error.message);
					} else if(data === "null") {
						// Error
						console.log("Der Server lieferte 'null' zurück.");
					} else {
						console.log(data);
						if(subject.name == data.name) {
							console.log("Fach wurde erstellt.");
						} else {
							console.log("Fach wurde nicht erstellt.");
						}
					}
					callback();
			}).error(function(data, status, headers, config) {
				sendError("Error: "+data+" - "+status);
				callback();
			});

		} else {
			// Error
			sendError("Error: Es wurden nicht alle Felder ausgefüllt.");
		}
	};

	factory.deleteSubject = function(subjectID) {

		subjectID = parseInt(subjectID);

		var url = factory.checkSingularURL("delete", subjectID);
		if(url.error) {
			return url.error;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(subjectID === data.subjectID) {
				Subject.subjectID = data.subjectID;
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

		moduleHandbookID = parseInt(moduleHandbookID);

		var url = factory.checkSingularURL("delete", moduleHandbookID);
		if(url.error) {
			return url.error;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(moduleHandbookID === data.moduleHandbookID) {
				ModuleHandbook.moduleHandbookID = data.moduleHandbookID;
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

		studycourseID = parseInt(studycourseID);

		var url = factory.checkSingularURL("delete", studycourseID);
		if(url.error) {
			return url.error;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(studycourseID === data.studycourseID) {
				Studycourse.studycourseID = data.studycourseID;
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
/*

	DeadlineFactory

 */
MMSApp.factory("DeadlineFactory", function($http, $q) {
	var factory = {};
	var deadline = {
		deadline: "String",
		sose: "boolean",
		year: "Number"
	};
	var Deadlines = [];

	factory.createDeadline = function(deadline, callback) {
		// BENÖTIGTE FELDER
		if(deadline.deadline && deadline.sose && deadline.year) {
			var url = factory.checkSingularURL("create");

			if(url.error) {
				return url.error;
			}

			$http({
				method: "POST",
				url: url,
				data: subject
			}).success(function(data, status, headers, config) {

				if(data.error) {
						// Error
						console.log("Servernachricht: "+data.error.message);
					} else if(data === "null") {
						// Error
						console.log("Der Server lieferte 'null' zurück.");
					} else {
						console.log(data);
						if(subject.name == data.name) {
							console.log("Deadline wurde erstellt.");
						} else {
							console.log("Deadline wurde nicht erstellt.");
						}
					}
					callback();
			}).error(function(data, status, headers, config) {
				sendError("Error: "+data+" - "+status);
				callback();
			});

		} else {
			// Error
			console.log("Error: Es wurden nicht alle Felder ausgefüllt.");
		}
	};

	factory.getDeadline = function(sose, year) {

		var url = factory.checkSingularURL("read", sose, year);
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
				deadline = data;
				deferred.resolve(deadline);
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.getDeadlines = function() {


		var url = hURL+"read/deadlines";

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
				Deadlines = data;
				deferred.resolve(data);
			}

		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.deleteDeadline = function(sose, year) {

		var url = factory.checkSingularURL("delete", sose, year);
		if(url.error) {
			return url.error;
		}

		var deferred = $q.defer();
		$http.get(url).success(function(data, status) {
			if(sose === data.sose && year === data.year) {
				deadline.sose = data.sose;
				deadline.year = data.year;
			} else if(data.error) {
				// Error
				console.log("Servernachricht: "+data.error.message);
			} else if(data === "null") {
				// Error
				console.log("Der Server lieferte 'null' zurück.");
			} else {
				// ERROR
				console.log("ERROR in deleteDeadline");
			}
			deferred.resolve(deadline);
		}).error(function(data, status) {
			console.log("Error: "+data+" - Status:"+status);
			deferred.reject(data);
		});
		return deferred.promise;
	};

	factory.checkSingularURL = function(method, sose, year) {
		if(method) {

			var url = hURL+method+"/deadline";

			if(sose && year) {
				url = url+"?sose="+sose+"&year="+year;
			}

			return url;

		} else {
			// ERROR
			console.log("ERROR in DeadlineFactory.checkSingularURL");

			return {
				error: {
					message: "Method in checkSingularURL wasn't definded."
				}
			};
		}
	};
	return factory;
});