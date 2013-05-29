function homeCtrl($scope) {
}

function showStudycoursesCtrl($scope, StudycourseFactory) {
	var promise = StudycourseFactory.getStudycourses();
	promise.then(function(studycourses) {
		$scope.studycourses = studycourses;
	}, function(error) {
		console.log("Error: "+error);
	});
}

function showModuleHandbooksCtrl($scope, ModuleHandbookFactory) {
	var promise = ModuleHandbookFactory.getModuleHandbooks();
	promise.then(function(moduleHandbooks) {
		$scope.moduleHandbooks = moduleHandbooks;
	}, function(error) {
		console.log("Error: "+error);
	});
}

function showSubjectsCtrl($scope SubjectFactory) {
	var promise = SubjectFactory.getSubjects();
	promise.then(function(subjects) {
		$scope.subjects = subjects;
	}, function(error) {
		console.log("Error: "+error);
	});
}

function showModulesCtrl($scope) {
	var promise = ModuleFactory.getModules();
	promise.then(function(modules) {
		$scope.modules = modules;
	}, function(error) {
		console.log("Error: "+error);
	});
}

function showEventsCtrl($scope) {
	var promise = EventFactory.getEvents();
	promise.then(function(events) {
		$scope.events = events;
	}, function(error) {
		console.log("Error: "+error);
	});
}