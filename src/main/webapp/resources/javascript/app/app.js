'use strict';

/*
 * Config phase vs Run phase
 * http://stackoverflow.com/questions/18276189/angularjs-uncaught-error-unknown-provider
 * 
 * 
 * */

//Define a function scope, variables used inside it will NOT be globally visible.
(function () {


	var lodash = angular.module('lodash', []);
	lodash.factory('_', function() {
		return window._; // assumes lodash has already been loaded on the page
	});

	var
	//the HTTP headers to be used by all requests
	httpHeaders,

	//the message to be shown to the user
	message,

	//Define the main module.
	//The module is accessible everywhere using "angular.module('hrangularspring')", therefore global variables can be avoided totally.
	as = angular.module('hrangularspring', ['ngRoute',
	                                        'ngResource',
	                                        'lodash',
	                                        'ngSanitize', 
	                                        'ui.select',
	                                        'admin-employments',
	                                        /*'admin-employments2',*/
	                                        'admin-employment-edit',
	                                        'admin-creditsEntries',
	                                        'admin-departments']);

	as.config(['$routeProvider'/*, '$httpProvider'*/, function($routeProvider/*, $httpProvider*/) {

	}]);


	as.run(function ($rootScope, $http) {
		//make contextPath accessible to root scope and therefore all scopes
		$rootScope.getPrefixContextPath = function () {
			return prefixContextPath;
		};

	});


}());