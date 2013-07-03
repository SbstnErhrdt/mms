var App = angular.module('App', []);

App.controller('User', function($scope, $http) {
  $http.get('seb.json')
       .then(function(res){
          $scope.userdata = res.data;
        });
});