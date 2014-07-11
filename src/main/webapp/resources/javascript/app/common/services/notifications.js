'use strict';

(function () {

	var module = angular.module('services.notifications', ['MessageCenterModule','angular-growl']);

	module.config(['growlProvider', function(growlProvider) {
		growlProvider.globalTimeToLive({success: 5000, error: 10000, warning: 5000, info: 5000})
	}]);

	module.provider("LogNotificationService", function () {
		var provider = {};

		provider.$get = ["messageCenterService","$filter","growl", function(messageCenterService,$filter,growl) {
			var service = {};

			service.log = function(type,message) {
				var formattedDate = $filter('date')(new Date(), 'hh:mm:ss dd/MM');

				var textToShow  = formattedDate+" "+message;

				messageCenterService.add(type, textToShow , { status: messageCenterService.status.permanent });

				switch(type) {
				case 'success':
					growl.success(textToShow);
					break;
				case 'danger':
					growl.error(textToShow);
					break;	            
				case 'warning':
					growl.warning(textToShow);
					break;
				default:
					growl.info(textToShow);
				}

			};

			return service;
		}];

		return provider;
	});



}());