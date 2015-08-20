'use strict';

(function () {


	var module = angular.module('admin-employment-transfer', [	'services.rest.api',
	                                                      	'services.notifications'
	                                                      	]);

	/**
	 * Angular uses name-with-dashes for attribute names and camelCase for the corresponding directive name
	 * To find where this directive is used look for: modal-employment-transfer
	 *  modalEmploymentTransfer -> modal-employment-transfer
	 */
	module.directive('modalEmploymentTransfer', ["api","SessionService","LogNotificationService","$q",
	                                           function(api,SessionService,LogNotificationService,$q) {
		return {

			// restrict to an attribute type.
			restrict : 'A',
			/*
			 * 
			 * */

			templateUrl: prefixContextPath +'/resources/javascript/app/admin/employments/admin-employment-transfer.html',
			/*
			 * 
			 * */

			transclude: false,
			/*
			 * 
			 * This code will be run: Before compilation
			 * controller ‘$scope’ and link ‘scope’ are the same thing. 
			 * The difference is parameters sent to the controller get there through Dependency Injection (so calling it ‘$scope’ is required), 
			 * where parameters sent to link are standard order based functions.
			 * */
			controller: function($scope) {
				$scope.modal_id = 'modal_' + Math.floor((Math.random()*100+1));
				$scope.handler = $scope.modal_id;
			},
			/*
			 * 
			 * */
			scope : {
				handler : '=', //twoway binding 
				'employmentToTransfer' : '=employment', //twoway binding employment
				callback: '=callback', //twoway binding
				initialize: '=initialize' //twoway binding
			},
			/*
			 * This code will be run: After compilation
			 *
			 * */
			link : function ($scope, $element, $attrs) {
				
				$scope.selectedSubdepartmentDestination = {};
				
				//Watch for changes to the initialize attribute
                $scope.$watch("initialize", function (initializeNewValue, oldValue) {
                    //scope.showModal(newValue);
                	if(initializeNewValue===true){
	                    $scope.fetchAvailableSubdepartments().then(function(){
	    					
								}
							);
                	}
                });
                
				$scope.transfer = function() {
					var transferObject = {
										employmentToTransfer:$scope.employmentToTransfer, 
										destination:$scope.selectedSubdepartmentDestination
										};
					return api.employments.transfer(transferObject).then(function (response) {
						var destination = "";
						if(_.isEmpty(transferObject.destination)){
							destination = "Centro Sector Externo";
						}else{
							destination = transferObject.destination.nombreCentro+"-"+transferObject.destination.nombreSector;
						}
						
						LogNotificationService.log('success',$scope.employmentToTransfer.person.apellidoNombre +" transferido/a a: "+destination);
						$scope.callback();

					}, function (reasonError) {

						LogNotificationService.log('error',reasonError);//"Error!");

					});
				};
				
				
				$scope.fetchAvailableSubdepartments = function () {
					var deferred = $q.defer();
					
					api.subDepartments.findAvailableSubDepartments().then(function (response) {
						//-promise fulfilled
						$scope.availableSubDepartments = response.data;
						
						 deferred.resolve();
						 
					}, function (error) {
						//-promise rejected, some error happened
						
						deferred.reject(error);

					});
					
					
					//return a promise
					return deferred.promise;
					
				};

				/*
				$scope.fetchAvailableSubdepartments().then(function(){
					
					}
				);*/
				
				$scope.getSubdepartmentFullName = function(subdepartment){
					if(subdepartment){
						return subdepartment.codigoCentro + '-'+subdepartment.codigoSector +' (' +subdepartment.nombreCentro + '--'+subdepartment.nombreSector+')';	
					}
					return "";
				}
						
				$scope.clearDirective = function () {
					
					$scope.initialize = false;
					/*var deferred = $q.defer();
					
					api.subDepartments.findAvailableSubDepartments().then(function (response) {
						//-promise fulfilled
						$scope.availableSubDepartments = response.data;
						
						 deferred.resolve();
						 
					}, function (error) {
						//-promise rejected, some error happened
						
						deferred.reject(error);

					});
					
					
					//return a promise
					return deferred.promise;*/
					
				};
				
				
				
				
				
			}

		};

	}]);




}());