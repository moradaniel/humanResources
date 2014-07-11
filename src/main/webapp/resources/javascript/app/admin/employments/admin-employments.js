'use strict';

(function () {

	var module = angular.module('admin-employments', [
	                                  'services.rest.api',
	                                  'services.notifications',
	                                  'services.session'
	                                ])


	module.controller('GridCtrl', ["$scope", "$rootScope", "api",
	                           "SessionService","LogNotificationService",function ($scope, $rootScope, api, SessionService,LogNotificationService) {
		$scope.SessionService = SessionService;
		$scope.totalPages = 0;
		$scope.employmentsCount = 0;
		$scope.headers = [
		                  
		                  {
		                	  title: 'Apellido y Nombre',
		                	  value: 'apellidoNombre'
		                  },
		                  {
		                	  title: 'CUIL',
		                	  value: 'cuil',
		                	  klass: 'col-sm-1'

		                  },
		                  {
		                	  title: 'Categoria',
		                	  value: 'category'
		                  },
		                  {
		                	  title: 'Tramo',
		                	  value: 'occupationalGroup'
		                  },
		                  {
		                	  title: 'Agrupamiento',
		                	  value: 'parentOccupationalGroup'
		                  },

		                  {
		                	  title: 'Codigo Centro',
		                	  value: 'codigoCentro'
		                  },

		                  {
		                	  title: 'Nombre Centro',
		                	  value: 'nombreCentro'
		                  },

		                  {
		                	  title: 'Codigo Sector',
		                	  value: 'codigoSector'
		                  },

		                  {
		                	  title: 'Nombre Sector',
		                	  value: 'nombreSector'
		                  },
		                  {
		                	  title: 'Observaciones',
		                	  value: 'observaciones'
		                  },
		                  {
		                	  title: 'Estado',
		                	  value: 'status'
		                  }];

		//Will make an ajax call to fill the drop down menu in the filter of the states
		//$scope.employmentstatuses = api.employmentstatuses();

		//$scope.employmentstatuses = api.employmentstatuses();

		$scope.employmentStatusesCriteria = {
				pageNumber: 1
		};

		$scope.fetchEmploymentStatuses = function () {
			return api.employmentstatuses.search($scope.employmentStatusesCriteria).then(function (response) {
				$scope.employmentstatuses = response.data;
				//$scope.customers = data.Customers;
				//$scope.totalPages = data.TotalPages;
				//$scope.customersCount = data.TotalItems;
			}, function () {
				$scope.employmentstatuses = response.data;
				//$scope.customers = [];
				//$scope.totalPages = 0;
				//$scope.customersCount = 0;
			});
		};

		$scope.fetchEmploymentStatuses();




		/*$scope.fetchEmploymentStatuses().then(function () {
    	      //The request fires correctly but sometimes the ui doesn't update, that's a fix
    	      //$scope.filterCriteria.pageNumber = 1;
    	      $scope.employmentstatuses
    	    });*/

		/* $scope.employmentStatusesCriteria = {}; 
    	  $scope.employmentstatuses = function () {
      	    return api.employmentstatuses2($scope.employmentStatusesCriteria).then(function (data) {
      	    	data.results
      	      //$scope.customers = data.Customers;
      	      //$scope.totalPages = data.TotalPages;
      	      //$scope.customersCount = data.TotalItems;
      	    }, function () {
      	    	data.results
      	      //$scope.customers = [];
      	      //$scope.totalPages = 0;
      	      //$scope.customersCount = 0;
      	    });
      	  };*/

		//default criteria that will be sent to the server
		$scope.filterCriteria = {
				pageNumber: 1,
				sortDir: 'asc',
				sortedBy: 'id',
				employmentstatus:'ACTIVO' //by default retrieve active employments
		};

		//The function that is responsible of fetching the result from the server and setting the grid to the new result
		$scope.fetchResult = function () {
			return api.employments.search($scope.filterCriteria).then(function (response) {
				$scope.employmentsVOs = response.data;
				var meta = response.data.meta;
				$scope.totalPages = meta.TotalPages;
				$scope.employmentsCount = meta.total;
			}, function () {
				$scope.employments = [];
				$scope.totalPages = 0;
				$scope.employmentsCount = 0;
			});
		};

		//called when navigate to another page in the pagination
		$scope.selectPage = function (page) {
			$scope.filterCriteria.pageNumber = page;
			$scope.fetchResult();
		};

		//Will be called when filtering the grid, will reset the page number to one
		$scope.filterResult = function () {
			$scope.filterCriteria.pageNumber = 1;
			$scope.fetchResult().then(function () {
				//The request fires correctly but sometimes the ui doesn't update, that's a fix
				$scope.filterCriteria.pageNumber = 1;
			});
		};

		//call back function that we passed to our custom directive sortBy, will be called when clicking on any field to sort
		$scope.onSort = function (sortedBy, sortDir) {
			$scope.filterCriteria.sortDir = sortDir;
			$scope.filterCriteria.sortedBy = sortedBy;
			$scope.filterCriteria.pageNumber = 1;
			$scope.fetchResult().then(function () {
				//The request fires correctly but sometimes the ui doesn't update, that's a fix
				$scope.filterCriteria.pageNumber = 1;
			});
		};

		//manually select a page to trigger an ajax request to populate the grid on page load
		$scope.selectPage(1);


		$scope.getCurrentDepartment = function() {
			return $rootScope.currentDepartment;
		};


		$scope.editEmployment = function(employment) {
			//$scope.opts = ['on', 'off'];

			if (employment === 'new') {
				$scope.newEmployment = true;
				/*$scope.employment = {name: '', shortname: '', phonenumber: '', state: '', voteoptions: [{id:1, name: ''}]};*/
			}
			else {
				$scope.newEmployment = false;
				$scope.employmentToEdit= angular.copy(employment);
			}
		};


		$scope.saveEmployment = function() {
			return api.employments.save($scope.employmentToEdit).then(function (response) {


				$scope.fetchResult();
				LogNotificationService.log('success',"Los cambios a:    "+$scope.employmentToEdit.person.apellidoNombre+"     fueron guardados");

			}, function (reasonError) {

				LogNotificationService.log('error',"Error!");

			});
		};

	}]);


}());