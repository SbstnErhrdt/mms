/*

	Tabs Projekt! Spaces könnt ihr nehmen, dann aber VIER und ja nicht 2!!!
	I DREH EICH OIGAHENDIG S KNICK ROM A WENN IH
*/

function activeUserCtrl($scope, $cookies, ActiveUserFactory) {
	if($cookies['JSESSIONID']) {
		console.log("DEBUG: loged in");
		$scope.activeUser = ActiveUserFactory.getActiveUser();
		console.log(ActiveUserFactory.getActiveUser());
	} else {
		console.log("DEBUG: NOT loged in");
		ActiveUserFactory.setActiveUser({});
		$scope.activeUser = {};
	}
}

function homeCtrl($scope, $http, $cookies, $location, ActiveUserFactory) {
	$scope.login = function() {
		if($scope.email && $scope.password) {
			$http({
			        method: 'POST',
			        url: 'http://sopra.ex-studios.net:8080/mms/login?email='+$scope.email,
			        data: {
			        	email: $scope.email,
			        	password: $scope.password
			        },
			        headers: {
			        	"withCredentials": true,
			        	"X-Requested-With": false
			        }
		    }).
		        success(function(data, status, headers, config) {
		        	if(data.error) {
		        		// Error
		        		console.log(data.error.message);
		        	} else {
		        		if(data[1].jsessionID) {
		        			$cookies.JSESSIONID = data[1].jsessionID;
		        			ActiveUserFactory.setActiveUser(data[0]);
		        			$location.path("/overview");
		        		} else {
		        			// Error
		        			console.log("Error: Es wurden keine Cookies übertragen");
		        		}
		        	}
		        }).
		        error(function(data, status, headers, config) {
		            console.log("Error: "+data);
		        });
		    
		} else {
			// Butterbar
			console.log("Es wurden nicht alle Felder ausgefüllt.");
		}
	};		
}

function overviewCtrl($scope) {
	// Does nothing
}

function navigationCtrl($scope, $cookies, $location) {
	$scope.logout = function() {
		delete $cookies['JSESSIONID'];
		$location.path("/home");
	};
}

/*
* STUDYCOURSE CONTROLLER
*/
function showStudycoursesCtrl($scope, StudycourseFactory) {
	StudycourseFactory.getStudycourses().then(function(studycourses) {
		$scope.studycourses = studycourses;
	}, function(error) {
		console.log("Error: "+error);
	});
}

function showStudycourseCtrl($scope, $routeParams, StudycourseFactory, ModuleHandbookFactory) {
	if($routeParams.studycourseID) {
		StudycourseFactory.getStudycourse($routeParams.studycourseID).then(function(studycourse) {
			$scope.studycourse = studycourse;
			ModuleHandbookFactory.getModuleHandbooks(studycourse.studycourseID).then(function(moduleHandbooks) {
				$scope.moduleHandbooks = moduleHandbooks;
			}, function(error) {
				console.log("Error: "+error);
			});
		}, function(error) {
			console.log("Error: "+error);
		});
	} else {
		// Error
		console.log("ERROR: No query");
	}
}

function deleteStudycourseCtrl($scope, $routeParams, $location, StudycourseFactory) {
	StudycourseFactory.deleteStudycourse($routeParams.studycourseID).then(function(studycourse) {
		if($routeParams.studycourseID === studycourse.studycourseID) {
			StudycourseFactory.getStudycourses().then(function(studycourses) {
				for(var i = 0; i < studycourses.length; i++) {
					if(studycourses[i].studycourseID === $routeParams.studycourseID) {
						studycourses.splice(i, 1);
						break;
					}
				}
				$location.url("/show/studycourses");
				$scope.studycourses = studycourses;
			}, function(error) {
				console.log("Error: "+error);
			});
		} else {
			// ERROR
			console.log("Error: Couldn't delete Studycourse");
		}
	}, function(error) {
		console.log("Error: "+error);
	});
}

/*
* MODULEHANDBOOK CONTROLLER
*/
function showModuleHandbooksCtrl($scope, ModuleHandbookFactory, StudycourseFactory) {
	// Get Modulehandbooks and Studycourses and add them to the GUI
	ModuleHandbookFactory.getModuleHandbooks().then(function(moduleHandbooks) {
		$scope.moduleHandbooks = moduleHandbooks;
	}, function(error) {
		console.log("Error: "+error);
	});

	StudycourseFactory.getStudycourses().then(function(studycourses) {
		$scope.studycourses = studycourses;
	}, function(error) {
		console.log("Error: "+error);
	});
}

function showModuleHandbookCtrl($scope, $routeParams, ModuleHandbookFactory, SubjectFactory) {
	if($routeParams.modulehandbookID) {
		ModuleHandbookFactory.getModuleHandbook($routeParams.modulehandbookID).then(function(moduleHandbook) {
			$scope.moduleHandbook = moduleHandbook;
			SubjectFactory.getSubjects(moduleHandbook.moduleHandbookID).then(function(subjects) {
				$scope.subjects = subjects;
			}, function(error) {
				console.log("Error: "+error);
			});
		}, function(error) {
			console.log("Error: "+error);
		});
	} else {
		// Error
		console.log("ERROR: No query");
	}
}

function deleteModuleHandbookCtrl($scope, $routeParams, $location, ModuleHandbookFactory) {
	ModuleHandbookFactory.deleteModuleHandbook($routeParams.modulehandbookID).then(function(moduleHandbook) {
		if($routeParams.modulehandbookID === moduleHandbook.moduleHandbookID) {
			ModuleHandbookFactory.getModuleHandbooks().then(function(moduleHandbooks) {
				for(var i = 0; i < moduleHandbooks.length; i++) {
					if(moduleHandbooks[i].modulehandbookID === $routeParams.modulehandbookID) {
						moduleHandbooks[i].splice(i, 1);
						break;
					}
				}
				$location.url("/show/modulehandbooks");
				$scope.moduleHandbooks = moduleHandbooks;
			}, function(error) {
				console.log("Error: "+error);
			});
		} else {
			// ERROR
			console.log("Error: Couldn't delete Modulehandbook");
		}
	}, function(error) {
		console.log("Error: "+error);
	});
}

/*
* SUBJECT CONTROLLER
*/
function showSubjectsCtrl($scope, SubjectFactory, StudycourseFactory) {
	StudycourseFactory.getStudycourses().then(function(studycourses) {
		$scope.studycourses = studycourses;
	}, function(error) {
		console.log("Error: "+error);
	});

	SubjectFactory.getSubjects().then(function(subjects) {
		$scope.subjects = subjects;
	}, function(error) {
		console.log("Error: "+error);
	});
}

function showSubjectCtrl($scope, $routeParams, SubjectFactory, ModuleFactory) {
	if($routeParams.subjectID) {
		SubjectFactory.getSubject($routeParams.subjectID).then(function(subject) {
			$scope.subject = subject;
			ModuleFactory.getModules(subject.subjectID).then(function(modules) {
				$scope.modules = modules;
			}, function(error) {
				console.log("Error: "+error);
			});
		}, function(error) {
			Console.log("Error: "+error);
		});
	} else {
		// Error
		console.log("ERROR: No query");
	}
}

function deleteSubjectCtrl($scope, $routeParams, $location, SubjectFactory) {
	SubjectFactory.deleteSubject($routeParams.subjectID).then(function(subject) {
		if($routeParams.subjectID === subject.subjectID) {
			SubjectFactory.getSubjects().then(function(subjects) {
				for(var i = 0; i < subjects.length; i++) {
					if(subjects[i].subjectID === $routeParams.subjectID) {
						subjects.splice(i, 1);
						break
					}
				}
				$location.url("/show/subjects");
				$scope.subjects = subjects;
			}, function(error) {
				console.log("Error: "+error);
			});
		} else {
			// ERROR
			console.log("Error: Couldn't delete Subject");
		}
	}, function(error) {
		console.log("Error: "+error);
	});
}

/*
* MODULE CONTROLLER
*/
function showModulesCtrl($scope, ModuleFactory, SubjectFactory) {
	SubjectFactory.getSubjects().then(function(subjects) {
		$scope.subjects = subjects;
	}, function(error) {
		console.log("Error: "+error);
	});

	ModuleFactory.getModules().then(function(modules) {
		$scope.modules = modules;
	}, function(error) {
		console.log("Error: "+error);
	});
}

function showModuleCtrl($scope, $routeParams, ModuleFactory, EventFactory) {
	if($routeParams.moduleID) {
		ModuleFactory.getModule($routeParams.moduleID).then(function(module) {
			$scope.module = module;
			EventFactory.getEvents(module.moduleID).then(function(events) {
				$scope.events = events;
			}, function(error) {
				console.log("Error: "+error);
			});
		}, function(error) {
			Console.log("Error: "+error);
		});
	} else {
		// Error
		console.log("ERROR: No query");
	}
}

function deleteModuleCtrl($scope, $routeParams, $location, ModuleFactory) {
	ModuleFactory.deleteModule($routeParams.moduleID).then(function(module) {
		if(parseInt($routeParams.moduleID) === module.moduleID) {
			$location.path("/show/modules");
		} else {
			// ERROR
			console.log("Error: Couldn't delete Module");
		}
	}, function(error) {
		console.log("Error: "+error);
	});
}

/*
* EVENT CONTROLLER
*/
function showEventsCtrl($scope, EventFactory, ModuleFactory) {
	ModuleFactory.getModules().then(function(modules) {
		$scope.modules = modules;
	}, function(error) {
		console.log("Error: "+error);
	});

	EventFactory.getEvents().then(function(events) {
		$scope.events = events;
	}, function(error) {
		console.log("Error: "+error);
	});
}

function showEventCtrl($scope, $routeParams, EventFactory, ModuleFactory) {
	if($routeParams.eventID) {
		EventFactory.getEvent($routeParams.eventID).then(function(_event) {
			$scope._event = _event;
		}, function(error) {
			console.log("Error: "+error);
		});
	} else {
		// Error
		console.log("ERROR: No query");
	}
}

function createEventCtrl($scope, EventFactory) {
	$scope.createEvent = function() {
		var _event = {
			moduleIDs: [2],
			name: "Türsteherrei",
			sws: 3,
			lecturer_email: "Harkan@Oehmer.de"
		};
		console.log(_event);
		EventFactory.createEvent(_event);
	};
}

function deleteEventCtrl($scope, $routeParams, $location, EventFactory) {
	// "event" ist in Javascript bereits reserviert, deswegen "_event"
	EventFactory.deleteEvent($routeParams.eventID).then(function(_event) {
		if(parseInt($routeParams.eventID) === _event.eventID) {
			$location.path("/show/events");
		} else {
			// ERROR
			console.log("Error: Couldn't delete Event");
		}
	}, function(error) {
		console.log("Error: "+error);
	});
}

/*
* USER CONTROLLER
*/
function showUsersCtrl($scope, UserFactory) {
	UserFactory.getUsers().then(function(users) {
		$scope.users = users;
	}, function(error) {
		console.log("Error: "+error);
	});
}

function showUserCtrl($scope, $routeParams, UserFactory) {
	if($routeParams.email) {
		UserFactory.getUser($routeParams.email).then(function(user) {
			$scope.user = user;
		}, function(error) {
			console.log("Error: "+error);
		});
	} else {
		// Error
		console.log("ERROR: No query");
	}
}

function deleteUserCtrl($scope, $routeParams, $location, UserFactory) {
	UserFactory.deleteUser($routeParams.email).then(function(user) {
		if(user.email === $routeParams.email) {
			$location.path("/show/users");
		} else {
			// ERROR
			console.log("Error: Couldn't delete User");
		}
	}, function(error) {
		console.log("Error: "+error);
	});
}

