'use strict';

(function () {

	var module = angular.module('admin-departments', ['ui.bootstrap',

	                                                  'services.rest.api',
	                                                  'services.notifications',
	                                                  'services.session'
	                                                  ]);






	module.controller('departmentsCtrl', ["$scope", "$rootScope", "api",
	                                      "SessionService","LogNotificationService","$q",function ($scope, $rootScope, api, SessionService,LogNotificationService,$q) {
		$scope.SessionService = SessionService;

		$scope.selectDepartment = function (item, model){

			var url = prefixContextPath+"/departments/select?departmentId=" + item.id+ "&currPath=" + "";

			window.location = url;

		};



		$scope.department = {};
		$scope.availableDepartmentsForAccount=[];



		$scope.fetchAvailableDepartmentsForAccount = function () {
			var deferred = $q.defer();


			api.departments.findAvailableDepartmentsForAccount().then(function (response) {
				//-promise fulfilled
				$scope.availableDepartmentsForAccount = response.data;
				//order ascending by code
				$scope.availableDepartmentsForAccount = _.sortBy($scope.availableDepartmentsForAccount, function (obj) { 
					return obj.code;
				}); 

				deferred.resolve();

			}, function (error) {

				deferred.reject(error);

			});


			//return a promise
			return deferred.promise;

		};

		$scope.fetchAvailableDepartmentsForAccount().then(function(){

			if($scope.availableDepartmentsForAccount.length===1){
				$scope.department = $scope.availableDepartmentsForAccount[0];						
			}else{
				//find by id
				$scope.department = _.find($scope.availableDepartmentsForAccount, function(obj) { return obj.id === currentSelectedDepartmentId; });
			}

		}

		);


	}]);


}());