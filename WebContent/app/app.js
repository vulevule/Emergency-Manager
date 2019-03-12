// napravimo modul
var eManager = angular.module('eManager', ['ngRoute']);

eManager.config(function($routeProvider) {
	$routeProvider.when('/',
	{
		//controller: 'productsController',// inace je podeseno ng-controller atributom
		templateUrl: 'partials/loginRegister.html'
	}).when('/main',
	{
		//controller: 'shoppingCartController', // inace je podeseno ng-controller atributom
		templateUrl: 'partials/Main.html'
	})
});

eManager.config(function($logProvider){
    $logProvider.debugEnabled(true);
});