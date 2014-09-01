'use strict';

(function () {

	var module = angular.module('admin-creditsEntries', ['ui.bootstrap',
	                                                 
	                                                  'services.rest.api',
	                                                  'services.notifications',
	                                                  'services.session'
	                                                  ]);


	module.controller('CreditsEntriesGridCtrl', ["$scope", "$rootScope", "api",
	                               "SessionService","LogNotificationService","$q",function ($scope, $rootScope, api, SessionService,LogNotificationService,$q) {
		$scope.SessionService = SessionService;
		$scope.totalPages = 0;
		$scope.creditsEntriesCount = 0;
		$scope.headers = [

		                  {
		                	  title: 'Periodo',
		                	  value: 'creditsPeriod'
		                  },
		                  {
		                	  title: 'Reparticion',
		                	  value: 'department'
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
		                	  title: 'CUIL',
		                	  value: 'cuil',
		                	  klass: 'col-sm-1'

		                  },

		                  {
		                	  title: 'Apellido y Nombre',
		                	  value: 'apellidoNombre'
		                  },
		                  
		                  {
		                	  title: 'Tipo Movimiento',
		                	  value: 'creditsEntryType'
		                  },
		                  {
		                	  title: 'Estado',
		                	  value: 'grantedStatus'
		                  },
		                  {
		                	  title: 'Fecha Inicio',
		                	  value: 'employmentStartDate'
		                  },
		                  {
		                	  title: 'Fecha Fin',
		                	  value: 'employmentEndDate'
		                  },
		                  
		                  {
		                	  title: 'Creditos',
		                	  value: 'numberOfCredits'
		                  },
		                  {
		                	  title: 'Categoria Actual',
		                	  value: 'currentCategory'
		                  },
		                  {
		                	  title: 'Categoria Propuesta',
		                	  value: 'proposedCategory'
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
		                	  title: 'Observaciones',
		                	  value: 'notes'
		                  }];
		
		
		//default criteria that will be sent to the server
		$scope.filterCriteria = {
				pageNumber: 1,
				pageSize: 10,
				sortDir: 'asc',
				sortedBy: 'id',
				employmentstatus:'ACTIVO', //by default retrieve active employments
			
		};
		

		//Will make an ajax call to fill the drop down menu in the filter of the states
		$scope.creditsPeriodsCriteria = {
				pageNumber: 1
		};

		$scope.fetchAvailableCreditsPeriods = function () {
			var deferred = $q.defer();
			
			
			api.creditsPeriods.search($scope.creditsPeriodsCriteria).then(function (response) {
				//-promise fulfilled
				$scope.availableCreditsPeriods = response.data;
				//order ascending by name
				$scope.availableCreditsPeriods = _.sortBy($scope.availableCreditsPeriods, function (obj) { 
					 return obj.name;
					}).reverse(); // reverse to sort in descending order
					 //.value();
				
				//by default select newest credits period
				//$scope.filterCriteria.selectedCreditsPeriod = $scope.availableCreditsPeriods[0].name;
				
				 deferred.resolve();
				 
			}, function (error) {
				//-promise rejected, some error happened
				$scope.filterCriteria.creditsPeriodName = [];
				
				deferred.reject(error);

			});
			
			
			//return a promise
			return deferred.promise;
			
		};

		$scope.fetchAvailableCreditsPeriods().then(function(){
			//by default select newest credits period
			$scope.filterCriteria.creditsPeriodName = $scope.availableCreditsPeriods[0].name;
			
			//manually select a page to trigger an ajax request to populate the grid on page load
			$scope.selectPage();
			
			
			}
				
		);
		
		
		$scope.fetchAvailableCreditEntriesTypes = function () {
			var deferred = $q.defer();
			
			
			api.creditsEntriesTypes.search().then(function (response) {
				//-promise fulfilled
				$scope.creditsEntryTypes = response.data;
				//order ascending by name
				/*$scope.creditsEntryTypes = _.sortBy($scope.availableCreditsPeriods, function (obj) { 
					 return obj.name;
					}).reverse(); // reverse to sort in descending order
					 //.value();*/
				
				//by default select newest credits period
				//$scope.filterCriteria.selectedCreditsPeriod = $scope.availableCreditsPeriods[0].name;
				
				 deferred.resolve();
				 
			}, function (error) {
				//-promise rejected, some error happened
				$scope.creditsEntryTypes = [];
				
				deferred.reject(error);

			});
			
			
			//return a promise
			return deferred.promise;
			
		};
		
		

		
		
		$scope.fetchAvailableCreditEntriesTypes().then(function(){
			//by default select newest credits period
			//$scope.filterCriteria.creditsPeriodName = $scope.availableCreditsPeriods[0].name;
			
			//manually select a page to trigger an ajax request to populate the grid on page load
			//$scope.selectPage();
			
			
			}
				
		);
		
		
		

		//The function that is responsible of fetching the result from the server and setting the grid to the new result
		$scope.fetchResult = function () {
			return api.creditsEntries.search($scope.filterCriteria).then(function (response) {
				//promise fulfilled
				$scope.creditsEntriesVOs = response.data;
				var meta = response.data.meta;
				$scope.totalPages = Math.ceil(meta.total/$scope.filterCriteria.pageSize);//round up to next integer
				
				$scope.creditsEntriesCount = meta.total;
				
			}, function (error) {
				//promise rejected, some error happened
				$scope.creditsEntriesVOs = [];
				$scope.totalPages = 0;
				$scope.creditsEntriesCount = 0;
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
		//$scope.selectPage();


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

	}]);


}());