
eManager.factory('generalFactory', function($http) {

	var factory = {};
	factory.save = function() {
		var v1 = $http.get('/EmergencyManager/rest/territories/save');
		var v2 =  $http.get('/EmergencyManager/rest/users/save');
		var v3 =  $http.get('/EmergencyManager/rest/emergencies/save');
		
		return "Territories: " + v1 + "\nUsers: " + v2 + "\nEmergencies: " + v3;
	};
	
	return factory;
	
});

eManager.factory('territoriesFactory', function($http) {

	var factory = {};
	factory.save = function() {
		return $http.get('/EmergencyManager/rest/territories/save');
	};
	
	factory.getTerritories = function() {
		return $http.get('/EmergencyManager/rest/territories/getTerritories');
	};
	
	factory.getEmTer = function(name) {
		return $http.get('/EmergencyManager/rest/territories/getEmTer/' + name);
	};
	
	factory.getTerritory = function(name) {
		return $http.get('/EmergencyManager/rest/territories/getTerritory/' + name);
	};
	
	factory.createTer = function(territory) {
		return $http.post('/EmergencyManager/rest/territories/createTer', territory);
	};
	
	factory.updateTer = function(territory) {
		return $http.post('/EmergencyManager/rest/territories/updateTer', territory);
	};
	
	return factory;
	
});

eManager.factory('usersFactory', function($http) {
	
	var factory = {};
	factory.save = function() {
		return $http.get('/EmergencyManager/rest/users/save');
	};
	
	factory.getVolunteers = function() {
		return $http.get('/EmergencyManager/rest/users/getVolunteers');
	};
	
	factory.getAdmins = function() {
		return $http.get('/EmergencyManager/rest/users/getAdmins');
	};
	
	factory.getUser = function(username) {
		return $http.get('/EmergencyManager/rest/users/getUser/' + username);
	};
	
	factory.blockUser = function(username) {
		return $http.get('/EmergencyManager/rest/users/blockUser/' + username);
	};
	
	factory.unblockUser = function(username) {
		return $http.get('/EmergencyManager/rest/users/unblockUser/' + username);
	};
	
	factory.getLoggedUser = function() {
		return $http.get('/EmergencyManager/rest/users/getLoggedUser');
	};

	factory.register = function(user) {
		return $http.post('/EmergencyManager/rest/users/register', user);
	};
	
	factory.saveAdmin = function(user) {
		return $http.post('/EmergencyManager/rest/users/saveAdmin', user);
	};
	
	factory.saveVolunteer = function(user) {
		return $http.post('/EmergencyManager/rest/users/saveVolunteer', user);
	};
	
	factory.login = function(user) {
		return $http.post('/EmergencyManager/rest/users/login', user);
	};
	
	factory.logout = function() {
		return $http.get('/EmergencyManager/rest/users/logout');
	};
	
	factory.getMyEmergencies = function(username) {
		return $http.get('/EmergencyManager/rest/users/myEmergencies/' + username);
	};
	
	return factory;
	
});

eManager.factory('emergenciesFactory', function($http) {

	var factory = {};
	factory.save = function() {
		return $http.get('/EmergencyManager/rest/emergencies/save');
	};
	
	factory.getEmergencies = function() {
		return $http.get('/EmergencyManager/rest/emergencies/getEmergencies');
	};
	
	factory.addEmergency = function(emergency) {
		return $http.post('/EmergencyManager/rest/emergencies/addEmergency', emergency);
	};
	
	factory.comment = function(uc) {
		return $http.post('/EmergencyManager/rest/emergencies/comment', uc);
	};
	
	factory.saveVolunteer = function(id, username) {
		return $http.get('/EmergencyManager/rest/emergencies/saveVolunteer/' + id + '/' + username);
	};
	
	factory.archive = function(id) {
		return $http.get('/EmergencyManager/rest/emergencies/archive/' + id);
	};
	
	
	
	return factory;
	
});