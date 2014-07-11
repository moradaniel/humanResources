'use strict';

(function () {

	var module = angular.module('services.session', []);

	module.service("SessionService", [function(){

		this.departments = [
		                    {"id": "0", "name": "-- Seleccione Reparticion --"},
		                    {"id": "215", "name": "ADMINISTRACION PARAJE DIFUNTA CORREA"},
		                    {"id": "31", "name": "ADMINISTRACION PARQUE PROVINICAL ISCHIGUALASTO"},
		                    {"id": "154", "name": "AGENCIA SAN JUAN DE DESARROLLO DE INVERSIONES"},
		                    {"id": "46", "name": "ASESORIA LETRADA DE GOBIERNO"}
		                    ];

		this.currentSelectedDepartmentId = "0";

		this.currentDepartmentIdChange = function(selectedDepartmentId) {
			this.currentSelectedDepartmentId = selectedDepartmentId;
		};

		this.getCurrentDepartment = function() {
			//return this.departments[this.currentSelectedDepartmentId];
			return this.getDepartmentById(this.currentSelectedDepartmentId);
		};

		this.getDepartmentById = function(id){
			//myArray = [{'id':'73','foo':'bar'},{'id':'45','foo':'bar'},etc.]
			var obj = _.find(this.departments, function(obj) { return obj.id === id; });
			return obj;

		};


	}]);

}());