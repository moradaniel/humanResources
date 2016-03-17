'use strict';

(function () {

	var module = angular.module('admin-employments', ['ui.bootstrap',
	                                                  'admin-employment-edit',
	                                                  'services.rest.api',
	                                                  'services.notifications',
	                                                  'services.session'
	                                                  ]);


	module.controller('EmploymentsGridCtrl', ["$scope", "$rootScope", "api",
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
		                	  title: 'Reparticion',
		                	  value: 'departmentName'
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
		                	  value: 'notes'
		                  },
		                  {
		                	  title: 'Estado',
		                	  value: 'status'
		                  }];

		//Will make an ajax call to fill the drop down menu in the filter of the states
		//$scope.employmentstatuses = api.employmentstatuses();

		//$scope.employmentstatuses = api.employmentstatuses();

		//default criteria that will be sent to the server
		$scope.filterCriteria = {
				pageNumber: 1,
				pageSize: 10,
				sortDir: 'asc',
				sortedBy: 'id',
				apellidoNombre:null,
				cuil:null,
				department:currentSelectedDepartment,
				employmentstatus:'ACTIVO' //by default retrieve active employments
		};
		
		$scope.employmentStatusesCriteria = {
				pageNumber: 1
		};

		$scope.fetchEmploymentStatuses = function () {
			return api.employmentstatuses.search($scope.employmentStatusesCriteria).then(function (response) {
				$scope.employmentstatuses = response.data;
				
				
				//$scope.customers = data.Customers;
				//$scope.totalPages = meta.total/;
				//$scope.customersCount = data.TotalItems;
			}, function () {
				$scope.employmentstatuses = response.data;
				//$scope.customers = [];
				//$scope.totalPages = 0;
				//$scope.customersCount = 0;
			});
		};

		$scope.fetchEmploymentStatuses();


		$scope.availableDepartmentsForSearch = [
		                                         { name: '--Todas--', id: null }, 
		                                         { name: currentSelectedDepartment.name, id: currentSelectedDepartment.id } 
		                                       ];

		$scope.filterCriteria.department = $scope.availableDepartmentsForSearch[1];
		
		
		
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



		//The function that is responsible of fetching the result from the server and setting the grid to the new result
		$scope.fetchResult = function () {
			var filterCriteria = {
					pageNumber: $scope.filterCriteria.pageNumber,
					pageSize: $scope.filterCriteria.pageSize,
					sortDir: $scope.filterCriteria.sortDir,
					sortedBy: $scope.filterCriteria.sortedBy,
					apellidoNombre:$scope.filterCriteria.apellidoNombre,
					cuil:$scope.filterCriteria.cuil,
					departmentId:$scope.filterCriteria.department.id,
					employmentstatus:$scope.filterCriteria.employmentstatus
			};
			
			return api.employments.search(filterCriteria).then(function (response) {
				$scope.employmentsVOs = response.data;
				var meta = response.data.meta;
				$scope.totalPages = Math.ceil(meta.total/$scope.filterCriteria.pageSize);//round up to next integer
				
				$scope.employmentsCount = meta.total;
				
			}, function () {
				$scope.employments = [];
				$scope.totalPages = 0;
				$scope.employmentsCount = 0;
			});
		};

		//called when navigate to another page in the pagination
		$scope.selectPage = function () {
			//$scope.filterCriteria.pageNumber = page;
			if($scope.filterCriteria.pageNumber <= 0 ){
				$scope.filterCriteria.pageNumber = 1;
			}
						
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
			/*
			 * sorting is disabled until implemented in the server
			 *
			
			$scope.filterCriteria.sortDir = sortDir;
			$scope.filterCriteria.sortedBy = sortedBy;
			$scope.filterCriteria.pageNumber = 1;
			$scope.fetchResult().then(function () {
				//The request fires correctly but sometimes the ui doesn't update, that's a fix
				$scope.filterCriteria.pageNumber = 1;
			});
			
			*/
		};

		//manually select a page to trigger an ajax request to populate the grid on page load
		$scope.selectPage();


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
		
		/* Do not populate any component of EmploymentTransferDirective
		 * unitil we open the dialog */
		$scope.initializeEmploymentTransferDirective = false;
		
		$scope.transferEmployment = function(employment) {
			$scope.employmentToTransfer= angular.copy(employment);
			
			/*Directive modalEmploymentTransfer has to populate its components
			 * like the subdepartments dropdown */
			$scope.initializeEmploymentTransferDirective = true;
			
		};

	}]);


}());