function homeCtrl($scope) {
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

function deleteModuleCtrl($scope, $routeParams, $location, ModuleFactory) {
	ModuleFactory.deleteModule($routeParams.moduleID).then(function(module) {
		if($routeParams.moduleID === module.moduleID) {
			ModuleFactory.getModules().then(function(modules) {
				for(var i = 0; i < modules.length; i++) {
					if(modules[i].modulesID === $routeParams.moduleID) {
						modules.splice(i, 1);
						break;
					}
				}
				$scope.modules = modules;
				$location.url("/show/modules");
			}, function(error) {
				console.log("Error: "+error);
			});
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

function deleteEventCtrl($scope, $routeParams, $location, EventFactory) {
	// "event" ist in Javascript bereits reserviert, deswegen "_event"
	EventFactory.deleteEvent($routeParams.eventID).then(function(_event) {
		if($routeParams.eventID === _event.eventID) {
			EventFactory.getEvents().then(function(events) {
				for(var i = 0; i < events.length; i++) {
					if(events[i].eventID === $routeParams.eventID) {
						events.splice(i, 1);
						break;
					}
				}
				$scope.events = events;
				$location.url("/show/events");
			}, function(error) {
				console.log("Error: "+error);
			});
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

function deleteUserCtrl($scope, $routeParams, $location, UserFactory) {
	UserFactory.deleteUser($routeParams.email).then(function(user) {
		if(user.email === $routeParams.email) {
			UserFactory.getUsers().then(function(users) {
				for(var i = 0; i < users.length; i++) {
					if(users[i].email === user.email) {
						users.splice(i, 1);
						break;
					}
				}
				$scope.users = users;
				$location.url("/show/users");
			}, function(error) {
				console.log("Error: "+error);
			});
		} else {
			// ERROR
			console.log("Error: Couldn't delete User");
		}
	}, function(error) {
		console.log("Error: "+error);
	});
}

