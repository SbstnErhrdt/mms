/*

	Tabs Projekt! Spaces könnt ihr nehmen, dann aber VIER und ja nicht 2!!!
	I DREH EICH OIGAHENDIG S KNICK ROM A WENN IH

*/

function activeUserCtrl($scope, $cookies, $http, $location, ActiveUserFactory) {
	$scope.login = function() {
		if($scope.email && $scope.password) {
			delete $cookies['JSESSIONID'];
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
						sendError("Servernachricht: "+data.error.message);
					} else {
						if(data[1].jsessionID) {
							$cookies.JSESSIONID = data[1].jsessionID;

							// Feedback Loginbar - jQuery
							$("#inputEmail, #inputPassword").val("");

                            $location.path("/overview");
                            document.location.reload(true);
						} else {
							// Error
							sendError("Es wurden keine Cookies übertragen");
						}
					}
				}).
				error(function(data, status, headers, config) {
					sendError(data);
				});

		} else {
			sendError("Es wurden nicht alle Felder ausgefüllt.");
		}
	};

	if($cookies['JSESSIONID']) {
		console.log("DEBUG: loged in");

		ActiveUserFactory.getActiveUser().then(function(activeUser) {
			$scope.activeUser = activeUser;
            $scope.isAdmin = ActiveUserFactory.isAdmin();
            $scope.isEmployee = ActiveUserFactory.isEmployee();
            $scope.canDeblockContent = ActiveUserFactory.canDeblockContent();
            $scope.canDeblockCriticalModule = ActiveUserFactory.canDeblockCriticalModule();
            $scope.isLoggedIn = true;
  		}, function(error) {
 			$scope.isLoggedIn = false;
 			console.log("ERROR HALLO TIM");
			sendError(error);
		});
	} else {
		console.log("DEBUG: NOT loged in");
		ActiveUserFactory.setActiveUser({});
		$scope.activeUser = {};
	}
}

function homeCtrl($scope, $http, $cookies, $location, ActiveUserFactory) {
}

function overviewCtrl($scope) {
	// Does nothing
}

function navigationCtrl($http, $scope, $cookies, $location, ActiveUserFactory) {
	$scope.logout = function() {
        console.log("click");
        $http({method: 'GET', url: 'http://sopra.ex-studios.net:8080/mms/logout'}).
            success(function(data, status, headers, config) {
                delete $cookies['JSESSIONID'];
                document.location.reload(true);
            }).
            error(function(data, status, headers, config) {
                console.log("couldn't log out")
            });
	};
}

function registerCtrl($scope, $cookies, $location, ActiveUserFactory) {
	if($cookies['JSESSIONID']) {
		sendError("Sie sind bereits eingeloggt.");
		$location.path("/home");
	} else {
		$scope.register = function() {
			if($scope.user.email && $scope.user.firstName && $scope.user.lastName && $scope.user.password1 && $scope.user.password2) {
				if($scope.user.password1 === $scope.user.password2) {
					var ActiveUser = {
						email: $scope.user.email,
						firstName: $scope.user.firstName,
						lastName: $scope.user.lastName,
						password: $scope.user.password1
					};
					ActiveUserFactory.createActiveUser(ActiveUser).then(function(activeUser) {
						$location.path("/home");
					}, function(error) {
						sendError("Error: "+error);
					});
				} else {
					sendError("Die beiden Passwörter stimmen nicht überein.");
				}
			} else {
				sendError("Ihre Angaben sind nicht vollständig.");
			}
		};
	}
}

/*
* STUDYCOURSE CONTROLLER
*/
function showStudycoursesCtrl($scope, StudycourseFactory, ActiveUserFactory) {
	StudycourseFactory.getStudycourses().then(function(studycourses) {
		$scope.studycourses = studycourses;
	}, function(error) {
		sendError(error);
	});
    $scope.isAuthorised = function() {
    	return ActiveUserFactory.isAuthorised("studycourse");
    }
	$scope.canEdit = function (id) {
        return ActiveUserFactory.canEdit(id, "studycourse");
    }
	$scope.canDelete = function (id) {
        return ActiveUserFactory.canDelete(id, "studycourse");
    }
}

function showStudycourseCtrl($scope, $routeParams, StudycourseFactory, ModuleHandbookFactory) {
	if($routeParams.studycourseID) {
		StudycourseFactory.getStudycourse($routeParams.studycourseID).then(function(studycourse) {
			$scope.studycourse = studycourse;
			ModuleHandbookFactory.getModuleHandbooks(studycourse.studycourseID).then(function(moduleHandbooks) {
				$scope.moduleHandbooks = moduleHandbooks;
			}, function(error) {
				sendError(error);
			});
		}, function(error) {
			sendError(error);
		});
	} else {
		// Error
		sendError("No query");
	}
}

function updateStudycourseCtrl($scope, $routeParams, StudycourseFactory, ModuleHandbookFactory) {
	showStudycourseCtrl($scope, $routeParams, StudycourseFactory, ModuleHandbookFactory);
}

function deleteStudycourseCtrl($scope, $routeParams, $location, StudycourseFactory) {
	StudycourseFactory.deleteStudycourse($routeParams.studycourseID).then(function(studycourse) {
		if(parseInt($routeParams.studycourseID) === studycourse.studycourseID) {
			$location.path("/show/studycourses");
		} else {
			// ERROR
			sendError("Couldn't delete Studycourse");
			$location.path("/show/studycourses");
		}
	}, function(error) {
		sendError(error);
	});
}

/*
* MODULEHANDBOOK CONTROLLER
*/
function showModuleHandbooksCtrl($scope, ModuleHandbookFactory, StudycourseFactory, ActiveUserFactory) {
	// Get Modulehandbooks and Studycourses and add them to the GUI
	ModuleHandbookFactory.getModuleHandbooks().then(function(moduleHandbooks) {
		$scope.moduleHandbooks = moduleHandbooks;
	}, function(error) {
		sendError(error);
	});

	StudycourseFactory.getStudycourses().then(function(studycourses) {
		$scope.studycourses = studycourses;
	}, function(error) {
		sendError(error);
	});
    $scope.isAuthorised = function() {
    	return ActiveUserFactory.isAuthorised("moduleHandbook");
    }
	$scope.canEdit = function (id) {
        return ActiveUserFactory.canEdit(id, "moduleHandbook");
    }
	$scope.canDelete = function (id) {
        return ActiveUserFactory.canDelete(id, "moduleHandbook");
    }
}

function showModuleHandbookCtrl($scope, $routeParams, ModuleHandbookFactory, SubjectFactory) {
	if($routeParams.modulehandbookID) {
		ModuleHandbookFactory.getModuleHandbook($routeParams.modulehandbookID).then(function(moduleHandbook) {
			$scope.moduleHandbook = moduleHandbook;
			SubjectFactory.getSubjects(moduleHandbook.moduleHandbookID).then(function(subjects) {
				$scope.subjects = subjects;
			}, function(error) {
				sendError(error);
			});
		}, function(error) {
			sendError(error);
		});
	} else {
		// Error
		sendError("No query");
	}
}

function updateModuleHandbookCtrl($scope, $routeParams, ModuleHandbookFactory, SubjectFactory) {
	showModuleHandbookCtrl($scope, $routeParams, ModuleHandbookFactory, SubjectFactory);
}

function deleteModuleHandbookCtrl($scope, $routeParams, $location, ModuleHandbookFactory) {
	ModuleHandbookFactory.deleteModuleHandbook($routeParams.modulehandbookID).then(function(moduleHandbook) {
		if(parseInt($routeParams.modulehandbookID) === moduleHandbook.moduleHandbookID) {
			$location.url("/show/modulehandbooks");
		} else {
			// ERROR
			sendError("Couldn't delete Modulehandbook");
			$location.url("/show/modulehandbooks");
		}
	}, function(error) {
		sendError(error);
	});
}
/*
* PRINT MODULEHANDBOOK CONTROLLER
*/
function printModulehandbookCtrl($scope, ModuleHandbookFactory, StudycourseFactory, ActiveUserFactory) {
	if($routeParams.modulehandbookID) {
		ModuleHandbookFactory.getModuleHandbook($routeParams.modulehandbookID).then(function(moduleHandbook) {
			$scope.moduleHandbook = moduleHandbook;
			SubjectFactory.getSubjects(moduleHandbook.moduleHandbookID).then(function(subjects) {
				$scope.subjects = subjects;
			}, function(error) {
				sendError(error);
			});
		}, function(error) {
			sendError(error);
		});
	} else {
		// Error
		sendError("No query");
	}
}
/*
* SUBJECT CONTROLLER
*/
function showSubjectsCtrl($scope, SubjectFactory, StudycourseFactory, ActiveUserFactory) {
	StudycourseFactory.getStudycourses().then(function(studycourses) {
		$scope.studycourses = studycourses;
	}, function(error) {
		sendError(error);
	});

	SubjectFactory.getSubjects().then(function(subjects) {
		$scope.subjects = subjects;
	}, function(error) {
		sendError(error);
	});
    $scope.isAuthorised = function() {
    	return ActiveUserFactory.isAuthorised("subject");
    }
	$scope.canEdit = function (id) {
        return ActiveUserFactory.canEdit(id, "subject");
    }
	$scope.canDelete = function (id) {
        return ActiveUserFactory.canDelete(id, "subject");
    }
}

function showSubjectCtrl($scope, $routeParams, SubjectFactory, ModuleFactory) {
	if($routeParams.subjectID) {
		SubjectFactory.getSubject($routeParams.subjectID).then(function(subject) {
			$scope.subject = subject;
			ModuleFactory.getModules(subject.subjectID).then(function(modules) {
				$scope.modules = modules;
			}, function(error) {
				sendError(error);
			});
		}, function(error) {
			sendError(error);
		});
	} else {
		// Error
		sendError("No Query.");
	}
}

function createSubjectCtrl($scope, $location, SubjectFactory, ModuleHandbookFactory) {

	ModuleHandbookFactory.getModuleHandbooks().then(function(data) {;
        $scope.moduleHandbooks = data;
        console.log($scope.moduleHandbooks)
	}, function(error) {
		sendError("Error: "+error);
	});

	$scope.createSubject = function() {
        var subject = {
            name : $scope.subject.name,
            content : $scope.subject.content,
            studycourses_studycourseID : $scope.subject.modulehandbook.studycourses_studycourseID,
            moduleHandbooks_moduleHandbookID : $scope.subject.modulehandbook.moduleHandbookID,
            archived : $scope.subject.archived,
            enabled : false
        }

		SubjectFactory.createSubject(subject, function() {
        	$location.path("/show/subjects");
		});

	};
}

function updateSubjectCtrl($scope, $routeParams, SubjectFactory, ModuleFactory) {
	showSubjectCtrl($scope, $routeParams, SubjectFactory, ModuleFactory);
}

function deleteSubjectCtrl($scope, $routeParams, $location, SubjectFactory) {
	SubjectFactory.deleteSubject($routeParams.subjectID).then(function(subject) {
		if(parseInt($routeParams.subjectID) === subject.subjectID) {
			$location.path("/show/subjects");
		} else {
			// ERROR
			sendError("Couldn't delete Subject");
			$location.path("/show/subjects");
		}
	}, function(error) {
		sendError(error);
	});
}

/*
* MODULE CONTROLLER
*/
function showModulesCtrl($scope, ModuleFactory, SubjectFactory, ActiveUserFactory) {
	SubjectFactory.getSubjects().then(function(subjects) {
		$scope.subjects = subjects;
	}, function(error) {
		sendError(error);
	});

	ModuleFactory.getModules().then(function(modules) {
		$scope.modules = modules;
	}, function(error) {
		sendError(error);
	});
    $scope.isAuthorised = function() {
    	return ActiveUserFactory.isAuthorised("module");
    }
	$scope.canEdit = function (id) {
        return ActiveUserFactory.canEdit(id, "module");
    }
	$scope.canDelete = function (id) {
        return ActiveUserFactory.canDelete(id, "module");
    }
}

function showModuleCtrl($scope, $routeParams, ModuleFactory, EventFactory) {
	if($routeParams.moduleID) {
		ModuleFactory.getModule($routeParams.moduleID).then(function(module) {
			$scope.module = module;
			EventFactory.getEvents(module.moduleID).then(function(events) {
				$scope.events = events;
			}, function(error) {
				sendError(error);
			});
		}, function(error) {
			sendError(error);
		});
	} else {
		// Error
		sendError("No query");
	}
}

function createModuleCtrl($scope, $location, ModuleFactory, SubjectFactory) {
	$scope.subjectList = [];
	var subjects;
	$scope.idList = [];
	$scope.idList.length = $scope.subjectList.length;

	SubjectFactory.getSubjects().then(function(data) {
		subjects = data;
		$scope.subjects = data;
		$scope.subjectList.push(data);
	}, function(error) {
		sendError("Error: "+error);
	});

	$scope.addSubject = function () {
		$scope.subjectList.push(subjects);
	};

	$scope.removeSubject = function () {
		$scope.subjectList.pop();
	};

	$scope.createModule = function() {
		idList = [];
		for (var i=0; i < $scope.idList.length; i++) {
			id = $scope.idList[i].split("-");
			idList.push(parseInt(id[0], 10));
		}

		var module = {
			duration: $scope.module.duration,
			subjectIDs: idList,
			name: $scope.module.name,
			token: $scope.module.token,
			englishTitle: $scope.module.englishTitle,
			lp: $scope.module.lp,
			sws: $scope.module.sws,
			language: $scope.module.language,
			director_email: $scope.module.director_email,
			requirement: $scope.module.requirement,
			learningTarget: $scope.module.learningTarget,
			content: $scope.module.content,
			literature: $scope.module.literature,
			archived: $scope.module.archived,
			enabled: $scope.module.enabled
		};

		ModuleFactory.createModule(module, function() {
			$location.path("/show/modules");
		});
	};
}

function updateModuleCtrl($scope, $routeParams, ModuleFactory, EventFactory) {
	showModuleCtrl($scope, $routeParams, ModuleFactory, EventFactory);
}

function deleteModuleCtrl($scope, $routeParams, $location, ModuleFactory) {
	ModuleFactory.deleteModule($routeParams.moduleID).then(function(module) {
		if(parseInt($routeParams.moduleID) === module.moduleID) {
			$location.path("/show/modules");
		} else {
			// ERROR
			sendError("Couldn't delete Module");
			$location.path("/show/modules");
		}
	}, function(error) {
		sendError(error);
	});
}

/*
* EVENT CONTROLLER
*/
function showEventsCtrl($scope, EventFactory, ModuleFactory, ActiveUserFactory) {
	ModuleFactory.getModules().then(function(modules) {
		$scope.modules = modules;
	}, function(error) {
		sendError(error);
	});

	EventFactory.getEvents().then(function(events) {
		$scope.events = events;

	}, function(error) {
		sendError(error);
	});
    $scope.isAuthorised = function() {
    	return ActiveUserFactory.isAuthorised("event");
    }
    $scope.canEdit = function (id) {
        return ActiveUserFactory.canEdit(id, "event");
    }
    $scope.canDelete = function (id) {
        return ActiveUserFactory.canDelete(id, "event");
    }
}

function showEventCtrl($scope, $routeParams, EventFactory, ModuleFactory, UserFactory) {
	if($routeParams.eventID) {
		EventFactory.getEvent($routeParams.eventID).then(function(_event) {
			$scope._event = _event;

			UserFactory.getUser(_event.lecturer_email).then(function(user) {
				console.log("hasdasd");
				$scope._event.lecturer_email = user.firstName+" "+user.lastName;
			}, function(error) {
				sendError("Error: "+error);
			});

			$scope.modules = [];

			for(var i = 0; i < _event.moduleIDs.length; i++) {
				ModuleFactory.getModule(_event.moduleIDs[i]).then(function(module) {
					$scope.modules.push(module);
				}, function(error) {
					sendError("Error: "+error);
				});
			}
		}, function(error) {
			sendError("Error: "+error);
		});
	} else {
		// Error
		sendError("No query");
	}
}

function updateEventCtrl($scope, $location, $routeParams, EventFactory, ModuleFactory, UserFactory) {

	EventFactory.getEvent($routeParams.eventID).then(function(event) {
        $scope._event = event;
        console.log($scope._event);
    }, function(error) {
        sendError(error);
    });

    UserFactory.getUsers().then(function(users) {
		for(var i = 0; i < users.length; i++) {
			if(!users[i].isEmployee) {
				users.splice(i, 1);
			}
		}
		$scope.lecturers = users;
	}, function(error) {
		sendError("Error: "+error);
	});

    $scope.update = function () {
        EventFactory.updateEvent($scope._event, function () {
            $location.path("/show/events");
        });
    }

	// Laden
	ModuleFactory.getModules().then(function(modules) {
		$scope.modules = modules;
	}, function(error) {
		sendError(error);
	});

    $scope.log = function () {
        console.log($scope._event);
    }
}

function createEventCtrl($scope, $location, EventFactory, ModuleFactory, UserFactory) {

    $scope.moduleList = [];
    var modules;
    $scope.idList = [];
    $scope.idList.length = $scope.moduleList.length;

	// Laden
	ModuleFactory.getModules().then(function(data) {
		$scope.modules = data;
        $scope.moduleList.push(data);
        modules = data;
	}, function(error) {
		sendError(error);
	});

	UserFactory.getUsers().then(function(users) {
		for(var i = 0; i < users.length; i++) {
			if(!users[i].isEmployee) {
				users.splice(i, 1);
			}
		}
		$scope.lecturers = users;
	}, function(error) {
		sendError("Error: "+error);
	});

    $scope.addEvent = function () {
        $scope.moduleList.push(modules);
    };

    $scope.removeEvent = function () {
        $scope.moduleList.pop();
    };

	// Abschicken
	$scope.createEvent = function() {

        idList = [];
        for (var i=0; i < $scope.idList.length; i++) {
            id = $scope.idList[i].split("-");
            idList.push(parseInt(id[0], 10));
        }

		var _event = {
			moduleIDs: idList,
			name: $scope._event.name,
			sws: parseInt($scope._event.sws, 10),
			lecturer_email: $scope._event.lecturer,
			archived: $scope._event.archived,
			enabled: $scope._event.enabled,
			room: $scope._event.room,
			type: $scope._event.type,
			place: $scope._event.place,
			content: $scope._event.content,
			times: $scope._event.times
		};

		EventFactory.createEvent(_event, function() {
			$location.path("/show/events");
		});
	};
}



function deleteEventCtrl($scope, $routeParams, $location, EventFactory) {
	// "event" ist in Javascript bereits reserviert, deswegen "_event"
	EventFactory.deleteEvent($routeParams.eventID).then(function(_event) {
		if(parseInt($routeParams.eventID) === _event.eventID) {
			$location.path("/show/events");
		} else {
			// ERROR
			sendError("Couldn't delete Event");
			$location.path("/show/events");
		}
	}, function(error) {
		sendError(error);
	});
}

/*
* USER CONTROLLER
*/
function showUsersCtrl($scope, UserFactory) {
	UserFactory.getUsers().then(function(users) {
		$scope.users = users;
	}, function(error) {
		sendError(error);
	});
}

function showUserCtrl($scope, $routeParams, UserFactory) {
	if($routeParams.email) {
		UserFactory.getUser($routeParams.email).then(function(user) {

			$scope.user = user;

		}, function(error) {
			sendError(error);
		});
	} else {
		// Error
		sendError("No query");
	}
}

function createUserCtrl($scope, $location, UserFactory) {

	$scope.createUser = function() {
		if($scope.user.email && $scope.user.firstName && $scope.user.lastName && $scope.user.password) {
			var user = {
			//	address: "String",
				email: $scope.user.email,
				password: $scope.user.password,
				isEmployee: $scope.user.isEmployee,
				employeeRights: {
					isAdmin: $scope.user.employeeRights.isAdmin,
					canDeblockModule: $scope.user.employeeRights.canDeblockModule,
					canDeblockCriticalModule: $scope.user.employeeRights.canDeblockCriticalModule
				},
				firstName: $scope.user.firstName,
				//graduation: "String",
				//isEmployee: "boolean",
				lastName: $scope.user.lastName,
				matricNum: $scope.user.matricNum,
				//password: "Number",
				//phoneNum: "String",
				semester: $scope.user.semester,
				//talkTime: "String",
				title: $scope.user.title,
				userRights: {canLogin: $scope.user.canLogin}
			};

			console.log(user);

			UserFactory.createUser(user, function() {
				$location.path("/show/users");
			});
			
		} else {
			sendError("Ihre Angaben sind nicht vollständig.");
		}
	};
}

function updateUserCtrl($scope, $location, $routeParams, UserFactory, StudycourseFactory, ModuleHandbookFactory, SubjectFactory, ModuleFactory, EventFactory) {
	if($routeParams.email) {
		UserFactory.getUser($routeParams.email).then(function(user) {
			$scope.user = user;
		}, function(error) {
			sendError(error);
		});
	} else {
		// Error
		sendError("No query");
	}

    var StudycourseList = [];
    var ModuleHandbookList = [];
    var SubjectList = [];
    var ModuleList = [];
    var EventList = [];
    function init () {
        StudycourseFactory.getStudycourses().then(function (studycourses) {
            StudycourseList = studycourses;
            console.log(StudycourseList);
        });
        ModuleHandbookFactory.getModuleHandbooks().then(function (modulehandbooks) {
            ModuleHandbookList = modulehandbooks;
            console.log(ModuleHandbookList);
        });

        SubjectFactory.getSubjects().then(function (subjects) {
            SubjectList = subjects;
            console.log(SubjectList);
        });

        ModuleFactory.getModules().then(function (modules) {
            ModuleList = modules;
            console.log(ModuleList);
        });

        EventFactory.getEvents().then(function (events) {
            EventList = events;
            console.log(ModuleList);
        });
    }
    init();

    $scope.GETSTUDYCOURSE = function (id) {
        for (var i=0; i < StudycourseList.length; i++) {
            if (id === StudycourseList[i].studycourseID) {
                return StudycourseList[i].name;
            }
        }
    };

    $scope.getModuleHandbook = function (id) {
        for (var i=0; i < ModuleHandbookList.length; i++) {
            if (id === ModuleHandbookList[i].moduleHandbookID) {
                return ModuleHandbookList[i].name;
            }
        }
    };

    $scope.getSubject = function (id) {
        for (var i=0; i < SubjectList.length; i++) {
            if (id === SubjectList[i].subjectID) {
                return SubjectList[i].name;
            }
        }
    };

    $scope.getModule = function (id) {
        for (var i=0; i < ModuleList.length; i++) {
            if (id === ModuleList[i].moduleID) {
                return ModuleList[i].name;
            }
        }
    };

    $scope.getEvent = function (id) {
        for (var i=0; i < EventList.length; i++) {
            if (id === EventList[i].eventID) {
                return EventList[i].name;
            }
        }
    };

    $scope.update = function () {
        UserFactory.updateUser($scope.user, function () {
            $location.path("/show/users");
        });
    }

	// Laden
	UserFactory.getUsers().then(function(users) {
		$scope.users = users;
	}, function(error) {
		sendError(error);
	});

    $scope.log = function () {
        console.log($scope._event);
    }
}

function deleteUserCtrl($scope, $routeParams, $location, UserFactory) {
	UserFactory.deleteUser($routeParams.email).then(function(user) {
		if(user.email === $routeParams.email) {
			$location.path("/show/users");
		} else {
			// ERROR
			sendError("Couldn't delete User");
			$location.path("/show/users");
		}
	}, function(error) {
		sendError(error);
	});
}

function confirmCtrl($scope, $routeParams, $http) {

	function init () {
		console.log($routeParams.token);
		$http({
				method: 'GET',
				url: "http://sopra.ex-studios.net:8080/mms/confirm?token=" + $routeParams.token
		}).
			success(function(data, status, headers, config) {
				$scope.success = data.success;
			}).
			error(function(data, status, headers, config) {
				sendError(data);
			});
	}

	init();
}

function showDeadlinesCtrl($scope, DeadlineFactory) {
		DeadlineFactory.getDeadlines().then(function(deadlines) {
		$scope.deadlines = deadlines;
	}, function(error) {
		sendError(error);
	});
}

function showDeadlineCtrl($scope, $routeParams, DeadlineFactory) {
	if($routeParams.sose && $routeParams.year) {
		DeadlineFactory.getDeadline($routeParams.sose, $routeParams.year).then(function(deadline) {
			$scope.deadline = deadline;
		}, function(error) {
			sendError(error);
		});
	} else {
		// Error
		sendError("missing parameters in query");
	}
}




function requestCtrl($scope, $http) {
    $scope.url = "http://sopra.ex-studios.net:8080/mms/";

     $scope.get = function () {
         $http({
             method: 'GET',
             url: $scope.url
         }).
             success(function(data, status, headers, config) {
                 $scope.response = JSON.stringify(data);
                 $scope.status = status;
             }).
             error(function(data, status, headers, config) {
                 sendError(data);
             });
     }

    $scope.post = function () {
        $http({
            method: 'POST',
            url: $scope.url,
            data: $scope.body
        }).
            success(function(data, status, headers, config) {
                $scope.response = JSON.stringify(data);
                $scope.status = status;
            }).
            error(function(data, status, headers, config) {
                sendError(data);
            });
    }
}

