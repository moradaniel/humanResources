'use strict';

(function () {

	var module = angular.module('hierarchical-retained-credits', ['ui.bootstrap',
	                                                  'admin-employment-edit',
	                                                  'services.rest.api',
	                                                  'services.notifications',
	                                                  'services.session'
	                                                  ]);


	module.controller('HierarchicalRetainedCreditsGridCtrl', ["$scope", "$rootScope", "api",
	                               "SessionService","LogNotificationService",function ($scope, $rootScope, api, SessionService,LogNotificationService) {
		
		
		var columnsDefs = [
		     			    {name:'department.code',displayName: 'Codigo', width:'150', resizable: true},
		     			    {name:'department.name',displayName: 'Reparticion', width:'300', resizable: true},
		     			    {name:'description',displayName: 'Descripcion', width:'900', resizable: true, enableSorting: false}/*,
		     			    {field:'age'}, // showing backwards compatibility with 2.x.  you can use field in place of name
		     			    {name: 'address.city'},
		     			    {name: 'age again', field:'age'}*/
		     			  ];
		
		$scope.gridOptions_2018 = {};
		
		$scope.gridOptions_2018.columnDefs = columnsDefs;
		  
		$scope.hierchicalRetainedCreditsCriteria_2018 = {
			departmentId:""+currentSelectedDepartment.id,
			creditsPeriodName: "2018"
		};
			
			$scope.fetchHierchicalRetainedCredits_2018 = function () {
				return api.hierchicalRetainedCredits.search($scope.hierchicalRetainedCreditsCriteria_2018).then(function (response) {
					$scope.gridOptions_2018.data = response.data;

				}, function () {
					$scope.gridOptions_2018.data = [];

				});
			};

		$scope.fetchHierchicalRetainedCredits_2018();
		
		//-------------------------------------------------------------------------------------------------------------------	
	
		
		$scope.gridOptions_2017 = {};
		
		$scope.gridOptions_2017.columnDefs = columnsDefs;
		  
		$scope.hierchicalRetainedCreditsCriteria_2017 = {
			departmentId:""+currentSelectedDepartment.id,
			creditsPeriodName: "2017"
		};
			
			$scope.fetchHierchicalRetainedCredits_2017 = function () {
				return api.hierchicalRetainedCredits.search($scope.hierchicalRetainedCreditsCriteria_2017).then(function (response) {
					$scope.gridOptions_2017.data = response.data;

				}, function () {
					$scope.gridOptions_2017.data = [];

				});
			};

		$scope.fetchHierchicalRetainedCredits_2017();
		
		//-------------------------------------------------------------------------------------------------------------------	
		
		  
		$scope.gridOptions_2016 = {};
			
		$scope.gridOptions_2016.columnDefs = columnsDefs;
		  
		$scope.hierchicalRetainedCreditsCriteria_2016 = {
			departmentId:""+currentSelectedDepartment.id,
			creditsPeriodName: "2016"
		};
			
			$scope.fetchHierchicalRetainedCredits_2016 = function () {
				return api.hierchicalRetainedCredits.search($scope.hierchicalRetainedCreditsCriteria_2016).then(function (response) {
					$scope.gridOptions_2016.data = response.data;

				}, function () {
					$scope.gridOptions_2016.data = [];

				});
			};

			$scope.fetchHierchicalRetainedCredits_2016();
			
		//-------------------------------------------------------------------------------------------------------------------	
			
		  
		$scope.gridOptions_2015 = {};
			
		$scope.gridOptions_2015.columnDefs = columnsDefs;
		  
		$scope.hierchicalRetainedCreditsCriteria_2015 = {
			departmentId:""+currentSelectedDepartment.id,
			creditsPeriodName: "2015"
		};
			
			$scope.fetchHierchicalRetainedCredits_2015 = function () {
				return api.hierchicalRetainedCredits.search($scope.hierchicalRetainedCreditsCriteria_2015).then(function (response) {
					$scope.gridOptions_2015.data = response.data;

				}, function () {
					$scope.gridOptions_2015.data = [];

				});
			};

			$scope.fetchHierchicalRetainedCredits_2015();
			
		  
		//-------------------------------------------------------------------------------------------------------------------
		  	
			$scope.gridOptions_2014 = {
			  };
			 
			  $scope.gridOptions_2014.columnDefs = columnsDefs;
			  
		  
				$scope.hierchicalRetainedCreditsCriteria_2014 = {
						departmentId:""+currentSelectedDepartment.id,
						creditsPeriodName: "2014"
				};
				
				$scope.fetchHierchicalRetainedCredits_2014 = function () {
					return api.hierchicalRetainedCredits.search($scope.hierchicalRetainedCreditsCriteria_2014).then(function (response) {
						$scope.gridOptions_2014.data = response.data;
						
					}, function () {
						$scope.gridOptions_2014.data = [];

					});
				};

				$scope.fetchHierchicalRetainedCredits_2014();
		

	}]);


}());