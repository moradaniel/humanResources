'use strict';

(function () {

	var module = angular.module('admin-reports', ['ui.bootstrap',
	                                                  'admin-employment-edit',
	                                                  'services.rest.api',
	                                                  'services.notifications',
	                                                  'services.session'
	                                                  ]);
	
	
	/**
	 * Controllers Inheritance
	 * http://blog.mgechev.com/2013/12/18/inheritance-services-controllers-in-angularjs/
	 * 
	 */
	function BaseCtrl($scope,$http,LogNotificationService) {
		this.$scope = $scope;
		this.$http	= $http;
		this.LogNotificationService = LogNotificationService;
		
		this.name = 'foobar';
		
				
	    // An array of our form fields with configuration
	    // and options set. We make reference to this in
	    // the 'fields' attribute on the  element
	    /*this.rentalFields = [
	        {
	            key: 'outputFormat',
	            type: 'input',
	            templateOptions: {
	                type: 'text',
	                label: 'Tipo de Salida',
	                placeholder: 'Ingrese formato de salida: xls o pdf',
	                required: true
	            }
	        }
	    ];*/
		
	};
	
	BaseCtrl.prototype.parentMethod = function () {
	  //body
	};
	
	
	BaseCtrl.prototype.buildForm = function () {
				this.schema = {
		
					    "type": "object",
					    "title": "Report",
					    "properties": {
					      "outputFormat": {
					        "title": "Formato de Salida",
					        "type": "string"
					      }/*,
					      "email": {
					        "title": "Email",
					        "type": "string",
					        "pattern": "^\\S+@\\S+$",
					        "description": "Email will be used for evil."
					      },
					      "comment": {
					        "title": "Comment",
					        "type": "string",
					        "maxLength": 20,
					        "validationMessage": "Don't be greedy!"
					      }
					    },
					    "required": [
					      "outputFormat"/*,
					      "comment"*/
					    //]
					    }
					  };
				
				
				var self = this;
				//add properties (comes from children reports)
				
				_.forEach(this.getCustomSchemaProperties(), function(value) {
					//add custom properties tu base properties
					_.assign(self.schema.properties, value);
				});
				
			
			/*this.form = [
			               "*",
			               {
			                 type: "submit",
			                 title: "Generar Reporte Form"
			               }
			             ];*/
				
				
				
			
			//add common fields
			this.form = [
			        'outputFormat',
			      ];
			
		
			//add custom fields (comes from children reports)
			
			_.forEach(this.getCustomSchemaFormFields(), function(value) {
				self.form.push(value);
			});
			
			
			//add submit button
			var submitButton =  {
		        type: "submit",
		        title: "Generar Reporte"
		      };
		
		    this.form.push(submitButton);
			 
			 this.model = {};
	  

	};
	
	BaseCtrl.prototype.sendPost = function() {
        /*var data = $.param({
        json: JSON.stringify({
            name: $scope.newName
        })
    });*/
  /*
	var selectedReportParameters = {
		//fileName : 	$scope.newName,
		//code : "creditsSummaryReport",
		//outputFormat : "xls"
		//outputFormat : "pdf"
	};*/
	
	
    // First we broadcast an event so all fields validate themselves
    //$scope.$broadcast('schemaFormValidate');

    // Then we check if the form is valid
    //if (form.$valid) {
      // ... do whatever you need to do with your data.
    //}
    
	var url = "runReportAngular";
    
    var expectedMediaType = "application/vnd.ms-excel";
    if(this.selectedReportOptions.outputFormat === "pdf"){
    	expectedMediaType = "application/pdf";
    }
    //var expectedMediaType = "application/pdf";

    var responseType = 'arraybuffer';

    var requestData = this.selectedReportOptions;
    
    var self = this;
    
    this.$http({
        url: url,
        method: 'POST',
        responseType: 'arraybuffer',
        data: requestData, //this is your json data string
        headers: {
            'Content-type': 'application/json',
            //'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
            'Accept': expectedMediaType
            
        }
    }).success(function(data){
        var blob = new Blob([data], {
            //type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        	type : expectedMediaType
        });
        saveAs(blob, self.selectedReportOptions.code + '.'+self.selectedReportOptions.outputFormat);
        
        //self.LogNotificationService.log('success',"Reporte generado");
        
    }).error(function(errorData){
        //Some error log
    	//console.log(errorData);
    	self.LogNotificationService.log('error',errorData);
    });
};
			
		
		function ChildCtrl($scope,$http,LogNotificationService) {
			  			
			  BaseCtrl.call(this,$scope,$http,LogNotificationService);
			  this.name = 'baz';
			  
			  var creditsSummaryFields = 
		        /*{
		            key: 'periods',
		            type: 'input',
		            templateOptions: {
		                type: 'text',
		                label: 'Periodos',
		                placeholder: 'Ingrese periodo: 2015',
		                required: true
		            }
		        }*/
			  {
				        key: 'periods',
				        type: 'multiselect',
				        templateOptions: {
				            label: 'Periodos',
				            // Call our province service to get a list
				            // of provinces and territories
				            options: [
		                                 {
		                                     "name": "2014",
		                                     "value":"2014"
		                                 },
		                                 {
		                                     "name":"2015",
		                                     "value":"2015"
		                                 }]
				        }
				    } ;
			  
			  //add custom fields to base fields
			  
			  this.creditsSummaryCustomFields = ["opcion2"];
			  
			  
			  this.schemaProperties = [];
			  
			  
			  this.schemaProperties.push(
				{"opcion2":
							{
						        "title": "Opcion 2",
						        "type": "string",
						        "required":true
						      }
				}	  
			  );
			  
			  		  
			  
			  this.selectedReportOptions={
						code:"creditsSummaryReport",
						//outputFormat:"xls",
						startDate:null,
						endDate:null,
						periods:[],
						departmentsIds:[],
						opcion2:null
			};
			  
			  this.buildForm();
			  
		}
		
		ChildCtrl.prototype = Object.create(BaseCtrl.prototype);
		
		ChildCtrl.prototype.childMethod = function () {
			  this.parentMethod();
			  //body
		};
		
		ChildCtrl.prototype.getCustomSchemaProperties = function () {
			return this.schemaProperties;
		};
		
		ChildCtrl.prototype.getCustomSchemaFormFields = function () {
			return this.creditsSummaryCustomFields;
		};

			
		
		function Child2Ctrl($scope,$http,LogNotificationService) {
  			
			  BaseCtrl.call(this,$scope,$http,LogNotificationService);
			  this.name = 'baz2';
			  
			  
			  //add custom fields to base fields
			  
			  this.creditsSummaryCustomFields = ["opcion3"];
			  
			  
			  this.schemaProperties = [];
			  
			  
			  this.schemaProperties.push(
				{"opcion3":
							{
						        "title": "Opcion 3",
						        "type": "string",
						        "required":true
						      }
				}	  
			  );
			  
			  		  
			  
			  this.selectedReportOptions={
						code:"creditsSummary2Report",
						//outputFormat:"xls",
						startDate:null,
						endDate:null,
						periods:[],
						departmentsIds:[],
						opcion3:null
			};
			  
			  this.buildForm();
			  
		}
		
		Child2Ctrl.prototype = Object.create(BaseCtrl.prototype);
		
		Child2Ctrl.prototype.childMethod = function () {
			  this.parentMethod();
			  //body
		};
		
		Child2Ctrl.prototype.getCustomSchemaProperties = function () {
			return this.schemaProperties;
		};
		
		Child2Ctrl.prototype.getCustomSchemaFormFields = function () {
			return this.creditsSummaryCustomFields;
		};
		

		module.controller('BaseCtrl', BaseCtrl);
		module.controller('ChildCtrl', ChildCtrl);
		module.controller('Child2Ctrl', Child2Ctrl);
			
		
		
		
		
		/*
	module.controller('BaseReportCtrl', ["$scope","$http", "$rootScope", "api",
		                               "SessionService","LogNotificationService",function ($scope,$http, $rootScope, api, SessionService,LogNotificationService) {
		
		  var vm = this;
		  		  
		  vm.optionsForAvailableReports = {
					employeeAdditionsPromotionsReport:{
						outputFormats:["pdf"],
						periods:[2015],
						departmentsIds:[13]
					},
					summmaryOfCreditsReport:{
						outputFormats:["xls,pdf"],
						periods:[2015],
						departmentsIds:[13]
					}
			};
		  
		  vm.selectedReportOptions={};
		  //vm.selectedReportOptions=getSelectedReportOptions();
		  //{
				/*	code:"summmaryOfCreditsReport",
					outputFormat:"xls",
					startDate:null,
					endDate:null,
					periods:[],
					departmentsIds:[]*/
		  //};

		  /*
		  function doSomething1() {}
		  function doSomething2() {}
		  function doSomething3() {}
		  function doSomething4() {}
		  function doSomething5() {}
		  function doSomething6() {}

		  // exports
		  vm.doSomething1 = doSomething1;
		  vm.doSomething2 = doSomething2;
		  vm.doSomething3 = doSomething3;
		  vm.doSomething4 = doSomething4;
		  vm.doSomething5 = doSomething5;
		  vm.doSomething6 = doSomething6;*/
		  
		 // vm.sendPost = function() {
		        /*var data = $.param({
		            json: JSON.stringify({
		                name: $scope.newName
		            })
		        });*/
			  /*
		    	var selectedReportParameters = {
		    		//fileName : 	$scope.newName,
		    		//code : "creditsSummaryReport",
		    		//outputFormat : "xls"
		    		//outputFormat : "pdf"
		    	};*/
		    	
	//	    	var url = "runReportAngular";
		        /*$http.post("runReportAngular", reportParameters).success(function(responseData, status) {
		            $scope.hello = responseData;
		        });*/
		/*        
		        var expectedMediaType = "application/vnd.ms-excel";
		        if(vm.selectedReportOptions.outputFormat === "pdf"){
		        	expectedMediaType = "application/pdf";
		        }
		        //var expectedMediaType = "application/pdf";

		        var responseType = 'arraybuffer';

		        var requestData = this.selectedReportOptions;
		        

		        
		        
		        $http({
		            url: url,
		            method: 'POST',
		            responseType: 'arraybuffer',
		            data: requestData, //this is your json data string
		            headers: {
		                'Content-type': 'application/json',
		                //'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
		                'Accept': expectedMediaType
		                
		            }
		        }).success(function(data){
		            var blob = new Blob([data], {
		                //type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
		            	type : expectedMediaType
		            });
		            saveAs(blob, this.selectedReportOptions.code + '.'+this.selectedReportOptions.outputFormat);
		        }).error(function(errorData){
		            //Some error log
		        	console.log(errorData);
		        });
		    };  
		
	}]);

	module.controller("CreditsSummaryReportCtrl", function ($scope, $controller) {
	    var vm = this;
		
	    vm.selectedReportOptions={
					code:"summmaryOfCreditsReport",
					outputFormat:"xls",
					startDate:null,
					endDate:null,
					periods:[],
					departmentsIds:[]
		};
		  
	    //vm.getItems = RESTDataService.getItemsA;
	    
	    //this controller extends from BaseReportCtrl
	    angular.extend(vm, $controller('BaseReportCtrl', {$scope: $scope}));
	});*/

	module.controller('ReportsCtrl', ["$scope","$http", "$rootScope", "api",
	                               "SessionService","LogNotificationService",function ($scope,$http, $rootScope, api, SessionService,LogNotificationService) {
		
		var optionsForAvailableReports = {
				employeeAdditionsPromotionsReport : {
					outputFormats:["pdf"],
					periods:[2015],
					departmentsIds:[13]
				},
				summmaryOfCreditsReport : {
					outputFormats:["xls"],
					periods:[2015],
					departmentsIds:[13]
				}
		};
						
		$scope.selectedReport={
			code:"summmaryOfCreditsReport",
			outputFormat:"xls",
			period:2015,
			departmentsIds:[13]
		};
		
	    $scope.hello = {name: "Boaz"};
	    $scope.newName = "";
	    
	    $scope.sendPost = function() {
	        /*var data = $.param({
	            json: JSON.stringify({
	                name: $scope.newName
	            })
	        });*/
	    	var selectedReportParameters = {
	    		fileName : 	$scope.newName,
	    		code : "creditsSummaryReport",
	    		//outputFormat : "xls"
	    		outputFormat : "pdf"
	    	};
	    	
	    	var url = "runReportAngular";
	        /*$http.post("runReportAngular", reportParameters).success(function(responseData, status) {
	            $scope.hello = responseData;
	        });*/
	        
	        var expectedMediaType = "application/vnd.ms-excel";
	        if(selectedReportParameters.outputFormat === "pdf"){
	        	expectedMediaType = "application/pdf";
	        }
	        //var expectedMediaType = "application/pdf";

	        var responseType = 'arraybuffer';

	        var requestData = selectedReportParameters;
	        
/*	        function openSaveAsDialog(filename, content, mediaType) {
	            var blob = new Blob([content], {type: mediaType});
	            saveAs(blob, filename);
	        }*/
	        
	        /*$http.post(url, requestData, {
	           // (...)
	        }, responseType: responseType).then(function (response) {
	            var filename = reportParameters.fileName+".xls";
	            openSaveAsDialog(filename, response.data, expectedMediaType);
	        });*/
	        
	        
	        $http({
	            url: url,
	            method: 'POST',
	            responseType: 'arraybuffer',
	            data: requestData, //this is your json data string
	            headers: {
	                'Content-type': 'application/json',
	                //'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
	                'Accept': expectedMediaType
	                
	            }
	        }).success(function(data){
	            var blob = new Blob([data], {
	                //type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
	            	type : expectedMediaType
	            });
	            saveAs(blob, selectedReportParameters.fileName + '.'+selectedReportParameters.outputFormat);
	        }).error(function(errorData){
	            //Some error log
	        	console.log(errorData);
	        });
	    };  
	    
		
		
		
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
		                	  value: 'notes'
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
				pageSize: 10,
				sortDir: 'asc',
				sortedBy: 'id',
				employmentstatus:'ACTIVO' //by default retrieve active employments
		};

		//The function that is responsible of fetching the result from the server and setting the grid to the new result
		$scope.fetchResult = function () {
			return api.employments.search($scope.filterCriteria).then(function (response) {
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