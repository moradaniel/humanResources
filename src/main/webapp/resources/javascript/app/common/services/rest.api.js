'use strict';

(function() {

	var module = angular.module('services.rest.api', [ 'restangular',
	                                                   'services.session', 
	                                                   'services.notifications' ]);

	module.run([ 'Restangular', 'LogNotificationService',
			function(Restangular, LogNotificationService) {
				Restangular.setErrorInterceptor(function(response) {
					var errorMessageToShow = 'Error in Server!!!!!!!!';
					if (response.data.message) {
						errorMessageToShow = response.data.message;
					}

					LogNotificationService.log('danger', errorMessageToShow);
					return false; // stop the promise chain
				});
			} ]);

	module.factory('RestFullResponse', function(Restangular) {
		return Restangular.withConfig(function(RestangularConfigurer) {
			RestangularConfigurer.setFullResponse(true);
		});
	});

	//We are using Restangular here
	module.factory('api', [	'Restangular',
	                       	'RestFullResponse',
	                       	'$location',
	                       	'SessionService',
			function(Restangular, RestFullResponse, $location, SessionService) {

				RestFullResponse.setDefaultHeaders({
					'Content-Type' : 'application/json;charset=UTF-8'
				});

				//prepend /rest before making any request with restangular
				RestFullResponse.setBaseUrl(prefixContextPath + '/rest');

				// add a response interceptor
				RestFullResponse.addResponseInterceptor(function(data,operation, what, url, response, deferred) {
					var extractedData;

					//-- REMOVE this . Implement a Security service to intercept the 
					//-- HTTP 401 unauthorized error response!!!!!!!!!!!!!!!!!

					if (!data.result) {//session expired redirect to login, replace this with an angular login form
						window.location = prefixContextPath + '/login';

					}

					//-- END REMOVE this . Implement a Security service to intercept the 
					//-- HTTP 401 unauthorized error response!!!!!!!!!!!!!!!!!

					// .. to look for getList operations
					if (operation === "getList") {
						// .. and handle the data and meta data
						extractedData = data.result;
						extractedData.meta = {
							total : data.total,
							success : data.success
						};

						//extractedData.meta = data.data.meta;
					} else {
						extractedData = data.result;
					}
					return extractedData;
				});

				return {
					employmentstatuses : {
						search : function(query) {
							return RestFullResponse.all("employments/statuses")
									.getList(query);
						}
					},
					employments : {
						search : function(query) {
							return RestFullResponse.one('departments',/*SessionService.currentDepartmentId*/
									currentSelectedDepartmentId).all('employments').getList(query);

						},

						save : function(employment) {
							return RestFullResponse.one('departments',/*SessionService.currentDepartmentId*/
									currentSelectedDepartmentId).all('employments').post(employment);

						}

					},
					
					creditsEntries : {
						search : function(query) {
							return RestFullResponse.all('creditsentries').all('findCreditsEntries').getList(query);

						}/*,

						save : function(employment) {
							return RestFullResponse.one('departments',/SessionService.currentDepartmentId/
									currentSelectedDepartmentId).all('employments').post(employment);

						}*/

					},
					
					creditsPeriods : {
						search : function(query) {
							return RestFullResponse.all('creditsPeriods').all('findCreditsPeriods').getList(query);

						}/*,

						save : function(employment) {
							return RestFullResponse.one('departments',/SessionService.currentDepartmentId/
									currentSelectedDepartmentId).all('employments').post(employment);

						}*/

					},
					creditsEntriesTypes : {
						search : function(query) {
							return RestFullResponse.all('creditsEntriesTypes').all('findCreditsEntriesTypes').getList(query);

						}/*,

						save : function(employment) {
							return RestFullResponse.one('departments',/SessionService.currentDepartmentId/
									currentSelectedDepartmentId).all('employments').post(employment);

						}*/

					},
					departments : {
						findAvailableDepartmentsForAccount : function(query) {
							return RestFullResponse.all('departments').all('findAvailableDepartmentsForAccount').getList(query);
						}
					},
					occupationalGroups : {
						search : function(query) {
							return RestFullResponse.all('employments').all('findOccupationalGroups').getList(query);

						}

					},

				};
			} ]);

}());