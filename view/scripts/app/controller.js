"use strict";

/*

	Datei: controller.js

	Beschreibung: Die controller.js realisiert den Aufruf, sowie die Kommunikation zwischen
	Client und Server. Sie dient, wie der Name es schon suggeriert, als Controller
	Alle Methoden die im Interface sichtbar sind (im $scope sichtbar) werden in dieser Datei implementiert.

*/


/*

	Funktion activeUserCtrl

	Beschreibung: Die Aufgabe dieser Methode ist es zu überprüfen ob der Benutzer eingeloggt ist oder nicht.
	Sie realisiert außerdem die Methode "login", welche die vom Benutzer eingegebenen Logindaten an den Server weiterschickt.

	Außerdem wird mit $cookies['JSESSIONID'] die Cookies des eventuell schon eingeloggten Benutzer abgefragt.
	Gibt der Server nach Anfrage ein activeUser-Objekt, so ist der User tatsächlich eingelogt.

	Zusätzlich werden einige Methoden für den modalen Löschen Dialog implementiert.

 */
function activeUserCtrl($scope, $cookies, $http, $location, ActiveUserFactory) {

	// Methode login: Überprüft und schickt die vom Benutzer eingegebenen Logindaten an den Server
	// und wartet darauf eine Antwort auszuwerten
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

						if(data.error.method ==="login(...)") {
							$("#ueberschriftModal").html("Fehler beim Einloggen!");
							$("#informModal").html("<p>Im Moment können Sie sich nicht einloggen, da Sie ihre E-Mail-Adresse noch nicht bestätigt haben. Öffnen Sie zur Lösung des Problems die von uns erhaltene Email und folgen Sie dem angegebenen Link.</p>");

							$("#inform").modal("show");
						} else {
							sendError("Servernachricht: "+data.error.message);
						}
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

	// Überprüfung ob der Benutzer eingeloggt ist, also ob er zur Zeit ein Cookie besitzt oder nicht

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
			sendError(error);
		});
	} else {
		console.log("DEBUG: NOT loged in");
		ActiveUserFactory.setActiveUser({});
		$scope.activeUser = {};
	}


	// DELETE MODAL Funktionen
	// 
	// Methode deleteUserModal: Wird von der GUI aufgerufen und ändert den Link des Löschen Buttons des modalen Dialogs
	// Methode wird nur aufgerufen beim Löschen eines Benutzers
	$scope.deleteUserModal = function(email) {
		$scope.toDeleteObject = {
			name: email
		}

		$("#deleteButton").attr("href", "#/delete/user?email="+email);
	};

	// Methode deleteContentModal: Diese Methode wird immer dann aufgerufen, wenn ein Contentobjekt, also
	// alle unten im If-Statement aufgelisteten Objekte, gelöscht wird.
	// Sie ist, wie deleteUserModal, dafür zuständig den Link des modalen Dialogs zu manipulieren.
	$scope.deleteContentModal = function(id, name, type) {
		if(type || type === "studycourse" || type === "modulehandbook" || type === "module" || type === "subject" || type === "event") {
			$scope.toDeleteObject = {
				name: name
			}
			$("#deleteButton").attr("href", "#/delete/"+type+"?"+type+"ID="+id);
		} else {
			sendError("Es wurde kein Typ angegeben bei deleteContentModal.");
		}	
	};

	// Methode hide: Versteckt den modalen Dialog #delete
	$scope.hide = function() {
		$("#delete").modal("hide");
		$("#enable").modal("hide");
	};


	// Modale Dialoge für Freigeben und Revidieren
	$scope.enableContentModal = function(id, name, type, enable) {
		if(type || type === "studycourse" || type === "modulehandbook" || type === "module" || type === "subject" || type === "event") {
			if(enable) {
				$("#ueberschriftEnable").html("Hinweis: "+name+" freigeben?");

				if(type === "studycourse") $("#enableModal").html("Möchten Sie den Studiengang <b>"+name+"</b> wirklich freigeben?");
				if(type === "modulehandbook") $("#enableModal").html("Möchten Sie das Modulhandbuch <b>"+name+"</b> wirklich freigeben?");
				if(type === "module") $("#enableModal").html("Möchten Sie das Modul <b>"+name+"</b> wirklich freigeben?");
				if(type === "subject") $("#enableModal").html("Möchten Sie das Fach <b>"+name+"</b> wirklich freigeben?");
				if(type === "event") $("#enableModal").html("Möchten Sie die Veranstaltung <b>"+name+"</b> wirklich freigeben?");
				
				// Freigebenbutton hinzufügen
				$("#enableButtons").html("")
				$("#enableButtons").append('<a href="#" class="btn" data-dismiss="modal">Schließen</a>');
				$("#enableButtons").append('<a href="#/enable/'+type+'?'+type+'ID='+id+'&enable=true" class="btn btn-info" ng-click="hide()">Freigeben</a>');
			} else {
				$("#ueberschriftEnable").html("Hinweis: "+name+" revidieren?");

				if(type === "studycourse") $("#enableModal").html("Möchten Sie den Studiengang <b>"+name+"</b> wirklich revidieren?");
				if(type === "modulehandbook") $("#enableModal").html("Möchten Sie das Modulhandbuch <b>"+name+"</b> wirklich revidieren?");
				if(type === "module") $("#enableModal").html("Möchten Sie das Modul <b>"+name+"</b> wirklich revidieren?");
				if(type === "subject") $("#enableModal").html("Möchten Sie das Fach <b>"+name+"</b> wirklich revidieren?");
				if(type === "event") $("#enableModal").html("Möchten Sie die Veranstaltung <b>"+name+"</b> wirklich revidieren?");
				
				// Freigebenbutton hinzufügen
				$("#enableButtons").html("")
				$("#enableButtons").append('<a href="#" class="btn" data-dismiss="modal">Schließen</a>');
				$("#enableButtons").append('<a href="#/enable/'+type+'?'+type+'ID='+id+'&enable=false" class="btn btn-warning" ng-click="hide()">Revidieren</a>');

			}
		} else {
			sendError("Es wurde kein Typ angegeben bei enableContentModal.");
		}
	};
}

function updateActiveUserCtrl($scope) {

}

function homeCtrl($scope, $http, $cookies, $location, ActiveUserFactory) {
}

function overviewCtrl($scope) {
	// Does nothing
}

/*

	Funktion: navigationCtrl

	Beschreibung: Beinhaltet die Methode logout: Drückt der Benutzer auf ausloggen, wird mit dieser Methode
	sein Cookie gelöscht, er wird via GET request vom Server ausgeloggt und die Seite wird neu geladen.

 */
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

/*

	Funktion: registerCtrl

	Beschreibung: Die Methode implementiert die Registrierenfunktion des Programms,
	sie stellt zunächst sicher, dass nur ausgeloggte Benutzer sich registrieren können, validiert die gemachten
	Eingaben des unregistrierten Benutzers, kreiert dann einen activeUser und schickt diesen via factory an den Server

 */
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
* 
* 
* STUDYCOURSE CONTROLLER
*
* 
*/

/*

	Funktion: showStudycourseCtrl

	Beschreibung: Holt via factory alle Studiengänge vom Server und schickt diese an die View
	Überprüft außerdem welche Rechte der Benutzer hat Studiengänge zu manipulieren

 */
function showStudycoursesCtrl($scope, StudycourseFactory, ActiveUserFactory) {

	// Studiengänge holen
	StudycourseFactory.getStudycourses().then(function(studycourses) {
		$scope.studycourses = cleanResults(studycourses);
	}, function(error) {
		sendError(error);
	});

	// Rechte überprüfen
    $scope.isAuthorised = function() {
    	return ActiveUserFactory.isAuthorised("studycourse");
    };
	$scope.canEdit = function (id) {
        return ActiveUserFactory.canEdit(id, "studycourse");
    };
    $scope.canDeblockContent = function () {
        return ActiveUserFactory.canDeblockContent();
    };
	$scope.canDelete = function (id) {
        return ActiveUserFactory.canDelete(id, "studycourse");
    };
}

/*

	Funktion: showStudycourseCtrl

	Beschreibung: Holt einen bestimmten Studiengang und dessen Modulhandbücher vom Server und zeigt diese in der View an

 */
function showStudycourseCtrl($scope, $routeParams, StudycourseFactory, ModuleHandbookFactory) {
	if($routeParams.studycourseID) {

		// Studiengang holen
		StudycourseFactory.getStudycourse($routeParams.studycourseID).then(function(studycourse) {
			$scope.studycourse = cleanResults(studycourse);

			// Modulhandbücher des Studiengangs holen
			ModuleHandbookFactory.getModuleHandbooks(studycourse.studycourseID).then(function(moduleHandbooks) {
				$scope.moduleHandbooks = cleanResults(moduleHandbooks);
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

	Funktion: updateStudycourseCtrl

	Beschreibung: Ist für das Bearbeiten eines Studiengangs zuständig
	Holt sich dazu Studiengang, sowie alle zugehörigen Modulhandbücher vom Server
	Gibt es kein aktuell ausgewähltes Modulhandbuch, so wird ein modaler Dialog aufgerufen, der den Benutzer 
	darauf aufmerktsam macht

 */
function updateStudycourseCtrl($scope, $routeParams, $location, StudycourseFactory, ModuleHandbookFactory) {
	if($routeParams.studycourseID) {

		// Studiengang holen
		StudycourseFactory.getStudycourse($routeParams.studycourseID).then(function(studycourse) {
			$scope.studycourse = cleanResults(studycourse);
			
			// Modulhandbuch holen
			ModuleHandbookFactory.getModuleHandbooks(studycourse.studycourseID).then(function(moduleHandbooks) {
				$scope.moduleHandbooks = cleanResults(moduleHandbooks);

				// ggf. modalen Dialog aufrufen
				if(moduleHandbooks.length < 1 || studycourse.current_moduleHandbook === 0) {
					// Informiere den Benutzer über seine auswählbaren Modulhandbücher
					$("#ueberschriftModal").html("Hinweis: Fehlende Modulhandbücher");
					$("#informModal").html("<p style=\"font-style:italic\">Warum ist die bei der Auswahlmöglichkeit 'Zugehöriges Modulhandbuch' kein Modulhandbuch auswählbar?</p><p></p><p><b>Antwort:</b> Sie müssen zunächst Modulhandbüchern im Reiter <a href=\"#/show/modulehandbooks\">Modulhandbücher</a> einen Studiengang auswählen. Erst dann tauchen die einzelnen Modulhandbücher in dieser Ansicht auf.</p>");
					
					$("#inform").modal("show");
				}

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

	// Methode update: Sendet die bearbeiten Dateien via factory zum Server
	$scope.update = function() {
		StudycourseFactory.updateStudycourse($scope.studycourse, function() {
			$location.path("/show/studycourses");
		});
	};
}

/*

	Funktion: deleteStudycourseCtrl

	Beschreibung: Die Methode löscht einen bestimmten Studiengang aus dem System
	und ruft anschließend wieder die Studiengangsübersicht auf

 */
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

	Funktion: enable Ctrl

	Beschreibung:

 */
function enableStudycourseCtrl($scope) {

}

/*

	Funktion: createStudycourseCtrl

	Beschreibung: Implementiert das Erstellen eines Studiengangs, dazu wird eine Methode send ins
	scope gegegben die den Studiengang via factory an den Server sendet

 */
function createStudycourseCtrl ($scope, $location, StudycourseFactory, ModuleHandbookFactory) {
    $scope.send = function () {
        StudycourseFactory.createStudycourse($scope.studycourse, function() {
            $location.url("/show/studycourses");
        });
    };
}

/*
* 
* 
* MODULEHANDBOOK CONTROLLER
*
* 
*/

/*

	Funktion: showModuleHandbooksCtrl

	Beschreibung: Holt via factory alle Modulhandbücher vom Server und schickt diese an die View
	Es wird außerdem überprüft ob der Benutzer genügende Rechte hat um Modulhandbücher zu manipulieren

 */
function showModuleHandbooksCtrl($scope, ModuleHandbookFactory, StudycourseFactory, ActiveUserFactory) {
	// Modulhandbücher holen
	ModuleHandbookFactory.getModuleHandbooks().then(function(moduleHandbooks) {
		$scope.moduleHandbooks = cleanResults(moduleHandbooks);
	}, function(error) {
		sendError(error);
	});

	// Studiengänge holen
	StudycourseFactory.getStudycourses().then(function(studycourses) {
		$scope.studycourses = cleanResults(studycourses);
	}, function(error) {
		sendError(error);
	});

	// Rechte des Benutzers überprüfen
    $scope.isAuthorised = function() {
    	return ActiveUserFactory.isAuthorised("moduleHandbook");
    };
	$scope.canEdit = function (id) {
        return ActiveUserFactory.canEdit(id, "moduleHandbook");
    };
    $scope.canDeblockContent = function () {
        return ActiveUserFactory.canDeblockContent();
    };
	$scope.canDelete = function (id) {
        return ActiveUserFactory.canDelete(id, "moduleHandbook");
    };
}

/*

	Funktion: showModulhandbookCtrl

	Beschreibung: Implementiert das Anzeigen eines bestimmten Modulhandbuchs. Dazu werden außerdem zugehörige Studiengänge
	und Deadlines benötigt

 */
function showModuleHandbookCtrl($scope, $routeParams, ModuleHandbookFactory, SubjectFactory, StudycourseFactory, DeadlineFactory) {
	if($routeParams.modulehandbookID) {

		// Modulhandbuch holen
		ModuleHandbookFactory.getModuleHandbook($routeParams.modulehandbookID).then(function(moduleHandbook) {
			$scope.moduleHandbook = cleanResults(moduleHandbook);
			SubjectFactory.getSubjects(moduleHandbook.moduleHandbookID).then(function(subjects) {
				$scope.subjects = cleanResults(subjects);
			}, function(error) {
				sendError(error);
			});

			// Studiengänge holen
			StudycourseFactory.getStudycourse(moduleHandbook.studycourses_studycourseID).then(function(studycourse) {
				$scope.studycourse = cleanResults(studycourse);
			}, function(error) {
				sendError(error);
			});

			// Deadline holen
			DeadlineFactory.getDeadline(moduleHandbook.sose, moduleHandbook.year).then(function(deadline) {
				$scope.deadline = cleanResults(deadline);
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

	Funktion: updateModuleHandbookCtrl

	Beschreibung: Implementiert das Bearbeiten eines Modulhandbuchs, um die aktuellen Daten dieses zu bekommen
	wird der Einfachheit halber showModuleHandbook aufgerufen und anschließend noch alle Studiengänge in die scope gespeißt

	Es wird außerdem eine Methode update zur Verfügung gestellt, die via factory das bearbeitete Modulhandbuch an den 
	Server übergibt

 */
function updateModuleHandbookCtrl($scope, $routeParams, $location, ModuleHandbookFactory, SubjectFactory, StudycourseFactory, DeadlineFactory) {
	
	// Lade Modulhandbuch in die View
	showModuleHandbookCtrl($scope, $routeParams, ModuleHandbookFactory, SubjectFactory, StudycourseFactory, DeadlineFactory);

	// Hole Studiengänge
	StudycourseFactory.getStudycourses().then(function (studycourses) {
        $scope.studycourses = cleanResults(studycourses);

    });

	// Methode update: Sende bearbeitetes Modulhandbuch via factory an den Server
    $scope.update = function() {
    	ModuleHandbookFactory.updateModuleHandbook($scope.moduleHandbook, function() {
    		$location.path("/show/modulehandbooks");
    	});
    };

}

/*

	Funktion: createModulehandbookCtrl

	Beschreibung: Implementiert das Erstellen eines Modulhandbuchs, holt dazu alle eventuell zugehörigen
	Studiengänge vom Server und läd diese in die View.

	Die Methode create() sendet die vom Benutzer eingegebenen Daten via factory an den Server weiter

 */
function createModulehandbookCtrl ($scope, $location, ModuleHandbookFactory, StudycourseFactory) {

	// Studiengänge holen
    StudycourseFactory.getStudycourses().then(function (studycourses) {
        $scope.studycourses = cleanResults(studycourses);

    });

    // modulHandbook Variable sicherheitshalber leeren
    $scope.moduleHandbook = {};

    // Methode create: Um die Daten weiterzuschicken
    $scope.create = function () {
        ModuleHandbookFactory.createModuleHandbook($scope.moduleHandbook, function () {
            $location.url("/show/modulehandbooks");
        });
    }
}

/*

	Funktion: deleteModuleHandbookCtrl

	Beschreibung: Die Methode löscht ein bestimmtes Modulhandbuch aus dem System
	und ruft anschließend wieder die Modulhandbuchübersicht auf

 */
function deleteModuleHandbookCtrl($scope, $routeParams, $location, ModuleHandbookFactory) {

	// An Server weiterleiten
	ModuleHandbookFactory.deleteModuleHandbook($routeParams.modulehandbookID).then(function(moduleHandbook) {
		if(parseInt($routeParams.modulehandbookID, 10) === moduleHandbook.moduleHandbookID) {

			// Übersicht aufrufen
			$location.url("/show/modulehandbooks");
		} else {
			// ERROR
			sendError("Couldn't delete Modulehandbook");

			// Übersicht aufrufen
			$location.url("/show/modulehandbooks");
		}
	}, function(error) {
		sendError(error);
	});
}

/*

	Funktion: enableModuleHandbookCtrl

	Beschreibung:

 */
function enableModuleHandbookCtrl($scope) {

}

/*
* 
* 
* PRINT MODULEHANDBOOK CONTROLLER
*
* 
*/

/*

	Funktion: printModulehandbookCtrl

	Beschreibung: Die Methode implementiert das Anzeigen einer Druckansicht eines Modulhandbuchs mit allen zugehörigen Elementen
	Dafür muss iterativ durch alle Elemente eines Modulhandbuchs durchgegangen werden: Es werden also Fach, Module und Veranstaltungen
	zum Anzeigen benötigt.

	Das ganze ist realisiert mit zwei rekursiven Funktionen "outerForFunct" und "innerForFunct", welche garantieren
	dass der jeweiligen Callback welcher vom Server erwartet wird nicht verworfen wird

 */
function printModulehandbookCtrl($scope, $routeParams, ModuleHandbookFactory, StudycourseFactory, ActiveUserFactory, SubjectFactory, ModuleFactory, EventFactory) {
	if($routeParams.modulehandbookID) {

		// Hole Modulhandbuch
		ModuleHandbookFactory.getModuleHandbook($routeParams.modulehandbookID).then(function(moduleHandbook) {
			$scope.moduleHandbook = moduleHandbook;

			// Hole alle Fächer eines Modulhandbuchs
			SubjectFactory.getSubjects(moduleHandbook.moduleHandbookID).then(function(subjects) {
				
				$scope.subjects = cleanResults(subjects);

				// Hole von allen Fächern, alle Module, daher die outerForFunct
				(function outerForFunct(index1) {
					var i = index1;

					if(i >= subjects.length) {
						return 0;
					} else {
						ModuleFactory.getModules($scope.subjects[i].subjectID).then(function(modules) {
							//console.log(modules);

							//console.log($scope.subjects[y]);
							$scope.subjects[i].modules = cleanResults(modules);
			
							// Hole aus allen Fächern alle Module und deren jeweilige Veranstaltungen
							(function innerForFunct(index) {
								if(index >= modules.length) {
									return 0;
								} else {
									EventFactory.getEvents(modules[index].moduleID, function(events) {
										
										$scope.subjects[i].modules[index].events = cleanResults(events);
										//console.log("modules[j]"+$scope.subjects[y].modules[index].events);
									});

									return innerForFunct(index+1);
								}
							})(0); // Sofortige initialisierung der Funktion bei 0
						})
						//console.log("$scope.subjects["+y+"]");
						//console.log($scope.subjects[y]);
						return outerForFunct(i+1);
					}
				})(0); // Sofortige initialisierung der Funktion bei 0
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
* 
* 
* SUBJECT CONTROLLER
*
* 
*/

/*

	Funktion: showSubjectsCtrl

	Beschreibung: Holt via factory alle Fächer vom Server und schickt diese an die View,
	es wird außerdem noch überprüft ob der Benutzer die Rechte hat die Fächer zu manipulieren

 */
function showSubjectsCtrl($scope, SubjectFactory, StudycourseFactory, ActiveUserFactory) {

	// Hole Fächer
	SubjectFactory.getSubjects().then(function(subjects) {
		$scope.subjects = cleanResults(subjects);
	}, function(error) {
		sendError(error);
	});
	
	// Überprüfe die Rechte des Benutzers
    $scope.isAuthorised = function() {
		return ActiveUserFactory.isAuthorised("subject");
    };
	$scope.canEdit = function (id) {
        return ActiveUserFactory.canEdit(id, "subject");
    };
    $scope.canDeblockContent = function () {
        return ActiveUserFactory.canDeblockContent();
    };
	$scope.canDelete = function (id) {
        return ActiveUserFactory.canDelete(id, "subject");
    };
}

/*

	Funktion: showSubjectCtrl

	Beschreibung: Implementiert das Anzeigen eines bestimmten Fachs und dessen zugehörige Module

 */
function showSubjectCtrl($scope, $routeParams, SubjectFactory, ModuleFactory, ModuleHandbookFactory) {
	if($routeParams.subjectID) {

		// Hole Fächer
		SubjectFactory.getSubject($routeParams.subjectID).then(function(subject) {
			
			$scope.subject = cleanResults(subject);

			// Hole Module
			ModuleFactory.getModules(subject.subjectID).then(function(modules) {
				$scope.modules = cleanResults(modules);
			}, function(error) {
				sendError(error);
			});

			// Hole Modulhandbücher, zur Navigation wichtig
			ModuleHandbookFactory.getModuleHandbook(subject.moduleHandbooks_moduleHandbookID).then(function(moduleHandbook) {
				
				$scope.modulehandbook = cleanResults(moduleHandbook);
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

/*

	Funktion: createSubjectCtrl

	Beschreibung: Implementiert das 

 */
function createSubjectCtrl($scope, $location, SubjectFactory, ModuleHandbookFactory) {

	ModuleHandbookFactory.getModuleHandbooks().then(function(data) {;
        $scope.moduleHandbooks = cleanResults(data);
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
        };

		SubjectFactory.createSubject(subject, function() {
        	$location.path("/show/subjects");
		});
	};
}

/*

	Funktion: 

	Beschreibung: 

 */
function updateSubjectCtrl($scope, $routeParams, $location, SubjectFactory, ModuleFactory, ModuleHandbookFactory) {
	showSubjectCtrl($scope, $routeParams, SubjectFactory, ModuleFactory, ModuleHandbookFactory);

	ModuleHandbookFactory.getModuleHandbooks().then(function(data) {;
        $scope.moduleHandbooks = cleanResults(data);
	}, function(error) {
		sendError("Error: "+error);
	});

	$scope.update = function() {
		SubjectFactory.updateSubject($scope.subject, function() {
			$location.path("/show/subjects");
		});
	};
}

/*

	Funktion: 

	Beschreibung: Die Methode löscht ein bestimmtes Fachs aus dem System
	und ruft anschließend wieder die Fächerübersicht auf

 */
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

	Funktion: enableSubjectCtrl

	Beschreibung:

 */
function enableSubjectCtrl($scope) {

}

/*
* 
* 
* MODULE CONTROLLER
*
* 
*/

/*

	Funktion: 

	Beschreibung: Holt via factory alle Module vom Server und schickt diese an die View

 */
function showModulesCtrl($scope, ModuleFactory, SubjectFactory, ActiveUserFactory) {
	SubjectFactory.getSubjects().then(function(subjects) {
		$scope.subjects = cleanResults(subjects);
	}, function(error) {
		sendError(error);
	});

	ModuleFactory.getModules().then(function(modules) {
		$scope.modules = cleanResults(modules);
	}, function(error) {
		sendError(error);
	});
    $scope.isAuthorised = function() {
    	return ActiveUserFactory.isAuthorised("module");
    };
	$scope.canEdit = function (id) {
        return ActiveUserFactory.canEdit(id, "module");
    };
    $scope.canDeblockContent = function () {
        return ActiveUserFactory.canDeblockContent();
    };
	$scope.canDelete = function (id) {
        return ActiveUserFactory.canDelete(id, "module");
    };
}

/*

	Funktion: 

	Beschreibung: 

 */
function showModuleCtrl($scope, $routeParams, ModuleFactory, EventFactory, SubjectFactory) {
	if($routeParams.moduleID) {
		ModuleFactory.getModule($routeParams.moduleID).then(function(module) {
			$scope.module = cleanResults(module);
			EventFactory.getEvents(module.moduleID).then(function(events) {
				$scope.events = cleanResults(events);
			}, function(error) {
				sendError(error);
			});

			$scope.subjects = [];

			for(var i = 0; i < module.subjectIDs.length; i++) {
				SubjectFactory.getSubject(module.subjectIDs[i]).then(function(subject) {
					$scope.subjects.push(cleanResults(subject));
				}, function(error) {
					sendError("Error: "+error);
				});
			}
		}, function(error) {
			sendError(error);
		});
	} else {
		// Error
		sendError("No query");
	}
}

/*

	Funktion: 

	Beschreibung: 

 */
function createModuleCtrl($scope, $location, ModuleFactory, SubjectFactory, EmployeeFactory) {
	$scope.subjectList = [];
	var subjects;
	$scope.idList = [];
	$scope.idList.length = $scope.subjectList.length;

	SubjectFactory.getSubjects().then(function(data) {
		subjects = data;
		$scope.subjects = cleanResults(data);
		$scope.subjectList.push(data);
	}, function(error) {
		sendError("Error: "+error);
	});

	EmployeeFactory.getEmployees().then(function(employees) {
		$scope.users = employees;
	}, function(error) {
		sendError(error);
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

/*

	Funktion: 

	Beschreibung: 

 */
function updateModuleCtrl($scope, $routeParams, $location, ModuleFactory, EventFactory, SubjectFactory, EmployeeFactory) {
	showModuleCtrl($scope, $routeParams, ModuleFactory, EventFactory, SubjectFactory);

	SubjectFactory.getSubjects().then(function(subjects) {
		$scope.subjects = cleanResults(subjects);
	}, function(error) {
		sendError(error);
	});

	EmployeeFactory.getEmployees().then(function(employees) {
		$scope.users = employees;
	}, function(error) {
		sendError(error);
	});

	$scope.update = function() {
		ModuleFactory.updateModule($scope.module, function() {
			$location.path("/show/modules");
		});
	};
}

/*

	Funktion: 

	Beschreibung: Die Methode löscht ein bestimmtes Modul aus dem System
	und ruft anschließend wieder die Modulsübersicht auf

 */
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

	Funktion: enableModuleCtrl

	Beschreibung:

 */
function enableModuleCtrl($scope) {

}

/*
* 
* 
* EVENT CONTROLLER
*
* 
*/

/*

	Funktion: 

	Beschreibung: Holt via factory alle Veranstaltungen vom Server und schickt diese an die View

 */
function showEventsCtrl($scope, EventFactory, ModuleFactory, ActiveUserFactory) {
	ModuleFactory.getModules().then(function(modules) {
		$scope.modules = cleanResults(modules);
	}, function(error) {
		sendError(error);
	});

	EventFactory.getEvents().then(function(events) {
		$scope.events = cleanResults(events);

	}, function(error) {
		sendError(error);
	});
    $scope.isAuthorised = function() {
    	return ActiveUserFactory.isAuthorised("event");
    };
    $scope.canEdit = function (id) {
        return ActiveUserFactory.canEdit(id, "event");
    };
    $scope.canDeblockContent = function () {
        return ActiveUserFactory.canDeblockContent();
    };
    $scope.canDelete = function (id) {
        return ActiveUserFactory.canDelete(id, "event");
    };
}

/*

	Funktion: 

	Beschreibung: 

 */
function showEventCtrl($scope, $routeParams, EventFactory, ModuleFactory, UserFactory) {
	if($routeParams.eventID) {
		EventFactory.getEvent($routeParams.eventID).then(function(_event) {
			$scope._event = cleanResults(_event);

			UserFactory.getUser(_event.lecturer_email).then(function(user) {
				$scope._event.lecturer_name = user.firstName+" "+user.lastName;
			}, function(error) {
				sendError("Error: "+error);
			});

			$scope.modules = [];

			for(var i = 0; i < _event.moduleIDs.length; i++) {
				ModuleFactory.getModule(_event.moduleIDs[i]).then(function(module) {
					$scope.modules.push(cleanResults(module));
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

/*

	Funktion: 

	Beschreibung: 

 */
function updateEventCtrl($scope, $location, $routeParams, EventFactory, ModuleFactory, UserFactory) {

	EventFactory.getEvent($routeParams.eventID).then(function(event) {
        $scope._event = cleanResults(event);
    }, function(error) {
        sendError(error);
    });

    UserFactory.getUsers().then(function(users) {
		for(var i = 0; i < users.length; i++) {
			if(!users[i].isEmployee) {
				users.splice(i, 1);
			}
		}
		$scope.lecturers = cleanResults(users);
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
		$scope.modules = cleanResults(modules);
	}, function(error) {
		sendError(error);
	});

    $scope.log = function () {
        console.log($scope._event);
    }
}

/*

	Funktion: 

	Beschreibung: 

 */
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

/*

	Funktion: 

	Beschreibung: Die Methode löscht eine bestimmte Veranstaltung aus dem System
	und ruft anschließend wieder die Veranstaltungsübersicht auf

 */
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

	Funktion: enableEventCtrl

	Beschreibung:

 */
function enableEventCtrl($scope) {

}

/*
* 
* 
* USER CONTROLLER
*
* 
*/

/*

	Funktion: 

	Beschreibung: Holt via factory alle Benutzer vom Server und schickt diese an die View

 */
function showUsersCtrl($scope, UserFactory) {
	UserFactory.getUsers().then(function(users) {
		$scope.users = cleanResults(users);
	}, function(error) {
		sendError(error);
	});
}

/*

	Funktion: 

	Beschreibung: 

 */
function showUserCtrl($scope, $routeParams, UserFactory) {
	if($routeParams.email) {
		UserFactory.getUser($routeParams.email).then(function(user) {

			$scope.user = cleanResults(user);

		}, function(error) {
			sendError(error);
		});
	} else {
		// Error
		sendError("No query");
	}
}

/*

	Funktion: 

	Beschreibung: 

 */
function createUserCtrl($scope, $location, UserFactory, StudycourseFactory, ModuleHandbookFactory, SubjectFactory, ModuleFactory, EventFactory) {

	$scope.createUser = function() {
		if($scope.user.email && $scope.user.firstName && $scope.user.lastName && $scope.user.password1 && $scope.user.password2) {
			if($scope.user.password1 === $scope.user.password2) {
				if(!$scope.user.isEmployee) {
					var user = {
						email: $scope.user.email,
						password: $scope.user.password1,
						isEmployee: $scope.user.isEmployee,
						firstName: $scope.user.firstName,
						lastName: $scope.user.lastName,
						matricNum: $scope.user.matricNum,
						semester: $scope.user.semester,
						graduation: $scope.user.graduation,
						title: $scope.user.title,
						userRights: {
							canLogin: $scope.user.canLogin
						}
					};
				} else {
					var user = {
						email: $scope.user.email,
						password: $scope.user.password1,
						isEmployee: $scope.user.isEmployee,
						firstName: $scope.user.firstName,
						lastName: $scope.user.lastName,
						matricNum: $scope.user.matricNum,
						semester: $scope.user.semester,
						graduation: $scope.user.graduation,
						title: $scope.user.title,
						userRights: {
							canLogin: $scope.user.canLogin
						},
						employeeRights: {
							isAdmin: $scope.user.employeeRights.isAdmin,
							canDeblockModule: $scope.user.employeeRights.canDeblockModule,
							canDeblockCriticalModule: $scope.user.employeeRights.canDeblockCriticalModule,
                            studycourseRightsList : $scope.user.employeeRights.studycourseRightsList,
                            subjectRightsList : $scope.user.employeeRights.subjectRightsList,
                            moduleRightsList : $scope.user.employeeRights.moduleRightsList,
                            eventRightsList : $scope.user.employeeRights.eventRightsList,
                            moduleHandbookRightsList : $scope.user.employeeRights.moduleHandbookRightsList

						},
						address: $scope.user.adress,
						talkTime: $scope.user.talkTime,
						phoneNum: $scope.user.phoneNum

					};
				}

				console.log(user);

				UserFactory.createUser(user, function() {
					$location.path("/show/users");
				});
			} else {
				sendError("Die beiden Passwörter stimmen nicht überein.");
			}

		} else {
			sendError("Ihre Angaben sind nicht vollständig.");
		}
	};

    var employeeRights = {
        studycourseRightsList : [],
        subjectRightsList : [],
        moduleHandbookRightsList : [],
        moduleRightsList : [],
        eventRightsList : []
    }
    $scope.user = {};
    $scope.user.employeeRights = employeeRights;

    function init () {
        StudycourseFactory.getStudycourses().then(function (studycourses) {
            $scope.StudycourseList = studycourses;
            console.log(studycourses);
        });
        ModuleHandbookFactory.getModuleHandbooks().then(function (modulehandbooks) {
            $scope.ModuleHandbookList = modulehandbooks;
        });

        SubjectFactory.getSubjects().then(function (subjects) {
            $scope.SubjectList = subjects;
        });

        ModuleFactory.getModules().then(function (modules) {
            $scope.ModuleList = modules;
        });

        EventFactory.getEvents().then(function (events) {
            $scope.EventList = events;
            console.log(events);
        });
    }
    init();

    $scope.addStudyCourse = function () {
        var newCourse = {
            canCreateChilds : false,
            canDelete : false,
            canEdit : false,
            studycourseID : 1
        }
        $scope.user.employeeRights.studycourseRightsList.push(newCourse);
    }

    $scope.removeStudyCourse = function () {
        $scope.user.employeeRights.studycourseRightsList.pop();
    }

    $scope.addModuleHandbook = function () {
        var newHandbook = {
            canDelete: false,
            canEdit: false,
            moduleHandbookID: 1
        }
        $scope.user.employeeRights.moduleHandbookRightsList.push(newHandbook);
    }

    $scope.removeModuleHandbook = function () {
        $scope.user.employeeRights.moduleHandbookRightsList.pop();
    }

    $scope.addSubject = function () {
        var newSubject = {
            canCreateChilds: false,
            canDelete: false,
            canEdit: false,
            subjectID: 1
        }
        $scope.user.employeeRights.subjectRightsList.push(newSubject);
    }

    $scope.removeSubject = function () {
        $scope.user.employeeRights.subjectRightsList.pop();
    }

    $scope.addModule = function () {
        var newModule = {
            canCreateChilds: false,
            canDelete: false,
            canEdit: false,
            moduleID: 1
        }
        $scope.user.employeeRights.moduleRightsList.push(newModule);
    }

    $scope.removeModule = function () {
        $scope.user.employeeRights.moduleRightsList.pop();
    }

    $scope.addEvent = function () {
        var newEvent = {
            canDelete: false,
            canEdit: false,
            eventID: 1
        }
        $scope.user.employeeRights.eventRightsList.push(newEvent);
    }

    $scope.removeEvent = function () {
        $scope.user.employeeRights.eventRightsList.pop();
    }
}

/*

	Funktion: 

	Beschreibung: 

 */
function updateUserCtrl($scope, $location, $routeParams, UserFactory, StudycourseFactory, ModuleHandbookFactory, SubjectFactory, ModuleFactory, EventFactory) {
    var user;
    if($routeParams.email) {
		UserFactory.getUser($routeParams.email).then(function(data) {
            
            user = cleanResults(data);
			$scope.user = data;
		}, function(error) {
			sendError(error);
		});
	} else {
		// Error
		sendError("No query");
	}

    function init () {
        StudycourseFactory.getStudycourses().then(function (studycourses) {
            $scope.StudycourseList = studycourses;
        });
        ModuleHandbookFactory.getModuleHandbooks().then(function (modulehandbooks) {
            $scope.ModuleHandbookList = modulehandbooks;
        });

        SubjectFactory.getSubjects().then(function (subjects) {
            $scope.SubjectList = subjects;
        });

        ModuleFactory.getModules().then(function (modules) {
            $scope.ModuleList = modules;
        });

        EventFactory.getEvents().then(function (events) {
            $scope.EventList = events;
            console.log(events);
        });
    }
    init();

    $scope.addStudyCourse = function () {
        var newCourse = {
            canCreateChilds : false,
            canDelete : false,
            canEdit : false,
            studycourseID : 1
        }
        $scope.user.employeeRights.studycourseRightsList.push(newCourse);
    }

    $scope.removeStudyCourse = function () {
        $scope.user.employeeRights.studycourseRightsList.pop();
    }

    $scope.addModuleHandbook = function () {
        var newHandbook = {
            canDelete: false,
            canEdit: false,
            moduleHandbookID: 1
        }
        $scope.user.employeeRights.moduleHandbookRightsList.push(newHandbook);
    }

    $scope.removeModuleHandbook = function () {
        $scope.user.employeeRights.moduleHandbookRightsList.pop();
    }

    $scope.addSubject = function () {
        var newSubject = {
            canCreateChilds: false,
            canDelete: false,
            canEdit: false,
            subjectID: 1
        }
        $scope.user.employeeRights.subjectRightsList.push(newSubject);
    }

    $scope.removeSubject = function () {
        $scope.user.employeeRights.subjectRightsList.pop();
    }

    $scope.addModule = function () {
        var newModule = {
            canCreateChilds: false,
            canDelete: false,
            canEdit: false,
            moduleID: 1
        }
        $scope.user.employeeRights.moduleRightsList.push(newModule);
    }

    $scope.removeModule = function () {
        $scope.user.employeeRights.moduleRightsList.pop();
    }

    $scope.addEvent = function () {
        var newEvent = {
            canDelete: false,
            canEdit: false,
            eventID: 1
        }
        $scope.user.employeeRights.eventRightsList.push(newEvent);
    }

    $scope.removeEvent = function () {
        $scope.user.employeeRights.eventRightsList.pop();
    }

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

/*

	Funktion: 

	Beschreibung: Die Methode löscht einen bestimmten Benutzer aus dem System
	und ruft anschließend wieder die Benutzerübersicht auf

 */
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

/*
* 
* 
* DEADLINE CONTROLLER
*
* 
*/

/*

	Funktion: 

	Beschreibung: 

 */
function createDeadlineCtrl($scope, $location, DeadlineFactory) {
	$scope.create = function() {
		console.log($scope.deadline);
		DeadlineFactory.createDeadline($scope.deadline, function() {
			$location.path("/show/deadlines");
		});
	};
}

/*

	Funktion: 

	Beschreibung: 

 */
function showDeadlinesCtrl($scope, DeadlineFactory) {
		DeadlineFactory.getDeadlines().then(function(deadlines) {
			console.log(deadlines[0]);
		$scope.deadlines = cleanResults(deadlines);
	}, function(error) {
		sendError(error);
	});
}

/*

	Funktion: 

	Beschreibung: 

 */
function showDeadlineCtrl($scope, $routeParams, DeadlineFactory) {
	if($routeParams.sose && $routeParams.year) {
		DeadlineFactory.getDeadline($routeParams.sose, $routeParams.year).then(function(deadline) {
			$scope.deadline = cleanResults(deadline);
		}, function(error) {
			sendError(error);
		});
	} else {
		// Error
		sendError("missing parameters in query");
	}
}

/*

	Funktion: 

	Beschreibung: 

 */
function updateDeadlineCtrl($scope, $routeParams, $location, DeadlineFactory) {
	showDeadlineCtrl($scope, $routeParams, DeadlineFactory);

	$scope.update = function() {
		DeadlineFactory.updateDeadline($scope.deadline, function() {
			$location.path("/show/deadlines");
		});
	};
}

/*

	Funktion: 

	Beschreibung: Die Methode löscht eine bestimmte Deadline aus dem System
	und ruft anschließend wieder die Deadlineübersicht auf

 */
function deleteDeadlineCtrl($scope) {

}

/*

	Funktion: 

	Beschreibung: 

 */
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

/*

	Funktion: 

	Beschreibung: 

 */
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

/*

	Funktion: cleanResults

	Beschreibung: Bereinigt die vom Server erhaltenen Objekte von null-Einträgen

 */
function cleanResults(object) {
	for(var prop in object) {
		if(object[prop] === "null") {
			object[prop] = "";
		}
	}
	return object;
}