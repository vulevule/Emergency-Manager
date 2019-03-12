// controller
eManager.controller('mainController', ['$scope', 'emergenciesFactory', 'territoriesFactory', 'usersFactory', function($scope, emergenciesFactory, territoriesFactory, usersFactory) {
	$scope.loggedUser = {};
	$scope.displayEmergencies = true;
	var emLocation = "";
	var imageFile = "";
	var imageFileEm = "";
	
	function getEmergencies() {
    	emergenciesFactory.getEmergencies().success(function (data) {
        	$scope.emergencies = data;
		});
    }
    
    function getTerritories() {
    	territoriesFactory.getTerritories().success(function (data) {
        	$scope.territories = data;
		});
    }
    
    function getVolunteers() {
    	usersFactory.getVolunteers().success(function (data) {
        	$scope.volunteers = data;
		});
    }
    
    function getAdmins() {
    	usersFactory.getAdmins().success(function (data) {
        	$scope.admins = data;
		});
    }
	
    $(window).change(function() {
    	$('.entry .main').height($(window).height() - $('#myHeader').height() - 60);
    });
    
    function init() {
    	
    	usersFactory.getLoggedUser().success(function(data){ 		
    		if (data.username == null) {
    			window.location.href = "http://localhost:9000/EmergencyManager/";
    			return;
    		} else {
    			$scope.loggedUser = data;
    			
    			console.log("Logged: " + $scope.loggedUser.username);
    	    	
    	    	$scope.headerText = $scope.loggedUser.name + " " + $scope.loggedUser.surname;
    	    	if ($scope.loggedUser.territory == null) {
    	    		$scope.roleText = "---- Administrator ----";
    	    	} else {
    	    		$scope.roleText = "---- Volunteer ----";
    	    	}
    	    	
    	    	$("#loggedUserThumbnail").attr("src", "pictures/users/" + $scope.loggedUser.picture);
    		}
    	});
    	
    	$('.entry .main').height($(window).height() - $('#myHeader').height() - 80);
    	
    	getEmergencies();
    	getTerritories();
    	getAdmins();
    	getVolunteers();
    	
    	$scope.displayEmergencies = true;
    };
  
    init();
    
    var position = [45.2671, 19.8335];
    emLocation = position[0] + "|" + position[1];
    
    function initialize() { 
        var latlng = new google.maps.LatLng(position[0], position[1]);
        var myOptions = {
            zoom: 16,
            center: latlng,
            mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        map = new google.maps.Map(document.getElementById("mapCanvas"), myOptions);

        marker = new google.maps.Marker({
            position: latlng,
            map: map,
            title: "Latitude:"+position[0]+" | Longitude:"+position[1]
        });

        google.maps.event.addListener(map, 'click', function(event) {
            var result = [event.latLng.lat(), event.latLng.lng()];
            transition(result);
        });
    }
    
    var numDeltas = 100;
    var delay = 5; //milliseconds
    var i = 0;
    var deltaLat;
    var deltaLng;

    function transition(result){
        i = 0;
        deltaLat = (result[0] - position[0])/numDeltas;
        deltaLng = (result[1] - position[1])/numDeltas;
        moveMarker();
    }
    
    function moveMarker(){
        position[0] += deltaLat;
        position[1] += deltaLng;
        var latlng = new google.maps.LatLng(position[0], position[1]);
        emLocation = position[0] + "|" + position[1];
        marker.setTitle("Latitude:"+position[0]+" | Longitude:"+position[1]);
        marker.setPosition(latlng);
        if(i!=numDeltas){
            i++;
            setTimeout(moveMarker, delay);
        }
    }
    
    initialize();
    
    $scope.viewEmLocation = function(location) {
    	var latLng = location.split("|");
    	var lat = Number(latLng[0]);
    	var lng = Number(latLng[1]);
    	
    	var mapOptions = {
    	          zoom: 8,
    	          center: {lat: lat, lng: lng}
    	        };
    	
    	var locMap = new google.maps.Map(document.getElementById('emLocMap'), mapOptions);
        
    	var locMarker = new google.maps.Marker({
            // The below line is equivalent to writing:
            // position: new google.maps.LatLng(-34.397, 150.644)
            position: {lat: lat, lng: lng},
            map: locMap
        });
    	
    	var infowindow = new google.maps.InfoWindow({
            content: '<p>Emergency Location: ' + marker.getPosition() + '</p>'
          });

		google.maps.event.addListener(locMarker, 'click', function() {
			infowindow.open(locMap, locMarker);
		});
	      
		$("#emLocationModal").css("display", "block");
    };
    
    $scope.hideEmLocation = function() {
    	$("#emLocationModal").css("display", "none");
    }
    
	$scope.logout = function() {
		usersFactory.logout().success(function(data){
			$scope.loggedUser = {};
			window.location.href = "http://localhost:9000/EmergencyManager/";
    	});
	};
	
	$scope.displayUserInfo = function() {
		$("#userInfo").css("display", "block");
		$scope.imageChanged = false;
		usersFactory.getLoggedUser().success(function(data){
			$scope.loggedUser = data;
			
			$("#displayImageUser").attr("src", "pictures/users/" + $scope.loggedUser.picture);
			
			if ($scope.loggedUser.territory != null) {
				usersFactory.getMyEmergencies($scope.loggedUser.username).success(function(data){
					if (data.length == 0) {
						$("#territoryUser").val($scope.loggedUser.territory.name);
					} else {
						$("#territoryUser").css("display", "none");
						$("#territoryLabelUser").text("Territory: " + $scope.loggedUser.territory.name);
					}
				});
				
			} else {
				$("#territoryUser").css("display", "none");
				$("#territoryLabelUser").css("display", "none");
			}
			
		});
	};
	
	$scope.saveChanges = function() {
		var user = {}
		user.username = $("#usernameUser").val();
		user.password = $("#passwordUser").val();
		user.name = $("#firstnameUser").val();
		user.surname = $("#surnameUser").val();
		user.phoneNumber = $("#phoneUser").val();
		user.email = $("#emailUser").val();
		
		if ($scope.imageChanged) {
			user.picture = imageFile;
		} else {
			user.picture = $("#usernameUser").val() + ".jpg";
		}
		
		if ($("#territoryUser").css("display") == "none") {
			usersFactory.saveAdmin(user).success(function(data){
				if (data != null) {
	    			var retVal = usersFactory.save();
	    			console.log("Saving users: " + retVal);
					toast('User ' + user.username + " saved successfully!");
					$scope.loggedUser = data;
	    		}
	    	});
		} else {
			user.state = $scope.loggedUser.state;
			if ($("#territory").val() == null || $("#territory").val() == "") {
				user.territory = $scope.loggedUser.territory.name;
			} else {
				user.territory = $("#territory").val();
			}
			
			
			usersFactory.saveVolunteer(user).success(function(data){
	    		if (data != null) {
	    			var retVal = usersFactory.save();
	    			console.log("Saving users: " + retVal);
					toast('User ' + user.username + " saved successfully!");
					$scope.loggedUser = data;
	    		}
	    	});
		}	
		
		$("#userInfo").css("display", "none");
		location.reload();
	};
	
	$scope.displayViewUserInfo = function(username) {
		usersFactory.getUser(username).success(function(data){
			if (data) {
				$scope.viewUserUsername = data.username;
				$scope.viewUserState = data.state;
				$("#viewUserPicture").attr("src", "pictures/users/" + data.picture);
				$("#viewUserName").text(data.name);
				$("#viewUserSurname").text(data.surname);
				$("#viewUserUsername").text(data.username);
				$("#viewUserEmail").text(data.email);
				$("#viewUserCellphone").text(data.phoneNumber);
				if (data.territory) {
					$("#viewUserTerritory").text(data.territory.name);
					$("#viewUserState").text(data.state);
				}
				if (data.state === "NORMAL") {
					$("#viewUserState").css("color", "#4CAF50");
					$("#viewUserBlockButton").html("Block " + data.username);
					$("#viewUserBlockButton").css("background-color", "#f44336");
				} else if (data.state === "BLOCKED"){
					$("#viewUserState").css("color", "#f44336");
					$("#viewUserBlockButton").html("Unblock " + data.username);
					$("#viewUserBlockButton").css("background-color", "#4CAF50");
				} else {
					$("#viewUserBlockButton").css("display", "none");
				}
				
			} else {
				
			};
		});
		
		$("#viewUserInfo").css("display", "block");
	};
	
	$scope.blockUnblockUser = function(username, state) {
		if (state === "NORMAL") {
			$scope.blockUser(username);
		} else {
			$scope.unblockUser(username);
		}	
	};
	
	$scope.blockUser = function(username) {
		if (confirm("Are you sure?\nThis will depose user from all emergencies he is currently engaged to and prevent his further activity until unblock.")) {
			usersFactory.blockUser(username).success(function(data){
				if (data === "OK") {
					toast("User blocked successfully!");
					getVolunteers();
					getEmergencies();
					var retVal = usersFactory.save();
	    			console.log("Saving users: " + retVal);
					retVal = emergenciesFactory.save();
	    			console.log("Saving emergencies: " + retVal);
	    			
	    			$("#viewUserInfo").css("display", "none");
	    			return;
				} else {				
					alert(data);
				}
			});
		}
	};
	
	$scope.unblockUser = function(username) {
		if (confirm("Are you sure?\nThis will allow user all his previous activities.")) {
			usersFactory.unblockUser(username).success(function(data){
				if (data === "OK") {
					toast("User unblocked successfully!");
					getVolunteers();
					getEmergencies();
					var retVal = usersFactory.save();
	    			console.log("Saving users: " + retVal);
					retVal = emergenciesFactory.save();
	    			console.log("Saving emergencies: " + retVal);
	    			
	    			$("#viewUserInfo").css("display", "none");
	    			return;
				} else {				
					alert(data);
				}
			});
		}
	}
	
	$scope.hideUserInfo = function() {
		$("#userInfo").css("display", "none");
	};
	
	$scope.hideViewUserInfo = function() {
		$("#viewUserInfo").css("display", "none");
	};
	
	$scope.openImageUpload = function() {
		$("#uploadImageUser").click();
	};
	
	$scope.openImageUploadEm = function() {
		$("#uploadImageEm").click();
	};
	
	$scope.handleFile = function(e) {
		
		if (e.target.files.length == 0) {
			toast("No file selected");
			$("#displayImageUser").attr("src", "pictures/users/" + $scope.loggedUser.picture);
			return;
		}
		
		var file = e.target.files[0];
		
		var reader = new FileReader();
		reader.onload = function(ev) {
			console.log(ev.target.result);
			$("#displayImageUser").attr("src", ev.target.result);
			imageFile = ev.target.result;
			$scope.imageChanged = true;
		};
		reader.readAsDataURL(file);
	};
	
	$scope.handleFileEm = function(e) {
		
		if (e.target.files.length == 0) {
			toast("No file selected");
			$("#displayImageEm").attr("src", "pictures/em_standard.jpg");
			return;
		}
		
		var file = e.target.files[0];
		
		var reader = new FileReader();
		reader.onload = function(ev) {
			console.log(ev.target.result);
			$("#displayImageEm").attr("src", ev.target.result);
			imageFileEm = ev.target.result;
		};
		reader.readAsDataURL(file);
	};
	
	
	$scope.displayNewEmForm = function() {		
		$("#nameEm").val("");
		$("#mun").val("");
		$("#desc").val("");
		position = [45.2671, 19.8335];
		$("#displayImageEm").attr("src", "pictures/em_standard.jpg");
		
		$("#newEmForm").css("display", "block");
	};
	
	$scope.hideNewEmForm = function() {
		$("#newEmForm").css("display", "none");
	};
	
	$scope.displayNewTerForm = function() {		
		$("#nameTer").val("");
		$("#areaTer").val("");
		$("#resTer").val("");
		
		$("#newTerForm").css("display", "block");
	};
	
	$scope.hideNewTerForm = function() {
		$("#newTerForm").css("display", "none");
	};
	
	$scope.createTer = function() {
		var ter = {};
		ter.name = $("#nameTer").val();
		ter.areaSize = $("#areaTer").val();
		ter.residentsNumber = $("#resTer").val();
				
		territoriesFactory.createTer(ter).success(function(data){
			if (data) {
				toast("Territory " + data.name + " created successfully!");
				var retVal = territoriesFactory.save();
    			console.log("Saving territories: " + retVal);
    			
    			getTerritories();
    			$("#newTerForm").css("display", "none");
			} else {
				toast("Territory with entered name already exists!");
			}
		});
	};
	
	$scope.displayEditTerInfo = function(name) {		
		territoriesFactory.getTerritory(name).success(function(data){
			if (data) {
				$("#nameTerEdit").val(data.name);
				$("#areaTerEdit").val(data.areaSize);
				$("#resTerEdit").val(data.residentsNumber);
				
				$("#editTerInfo").css("display", "block");
			} else {
				console.log("Territory doesn't exist!");
			}
		});

	};
	
	$scope.hideEditTerInfo = function() {
		$("#editTerInfo").css("display", "none");
	};
	
	$scope.saveTer = function() {
		var ter = {};
		ter.name = $("#nameTerEdit").val();
		ter.areaSize = $("#areaTerEdit").val();
		ter.residentsNumber = $("#resTerEdit").val();
				
		territoriesFactory.updateTer(ter).success(function(data){
			if (data) {
				toast("Territory " + data.name + " saved successfully!");
				var retVal = territoriesFactory.save();
    			console.log("Saving territories: " + retVal);
    			
    			getTerritories();
    			getVolunteers();
    			getEmergencies();
    			$("#editTerInfo").css("display", "none");
			} else {
				console.log("Territory doesn't exist!");
			}
		});
	};
	
	$scope.showComments = function(event) {
		$(event.currentTarget).parent().find(".commentSection").slideToggle();
	};
	
	function comment(text, id) {
		var uc = {};
		uc.emergencyId = id;
		uc.text = text;
		
		emergenciesFactory.comment(uc).success(function(data) {
			if (data === "OK") {
				var retVal = emergenciesFactory.save();
    			console.log("Saving emergencies: " + retVal);
				
				getEmergencies();
			} else {
				console.log(data);
				alert(data);
			}
		});
	}
	
	$scope.sendComment = function(event, id) {
		var text = $(event.currentTarget).parent().find("input").val();
		comment(text, id);
	};
		
	$scope.sendCommentViaEnter = function(event, id) {		
		var text = $(event.currentTarget).val();
		comment(text, id);
	};
	
	$scope.report = function() {
		var em = {};
		
		em.name = $("#nameEm").val();
		em.municipality = $("#mun").val();
		em.description = $("#desc").val();
		em.location = emLocation;
		em.territory = $("#territoryEm").val();
		em.picture = imageFileEm;
		
		if ($('#blue').is(':checked')) {
			em.level = "BLUE";
		} else if ($('#orange').is(':checked')) {
			em.level = "ORANGE";
		} else {
			em.level = "RED";
		}
		
		emergenciesFactory.addEmergency(em).success(function(data) {
			if (data === "OK") {
				var retVal = emergenciesFactory.save();
    			console.log("Saving emergencies: " + retVal);
				toast('Emergency ' + em.name + " added successfully!");
				
				getEmergencies();
				
				$("#newEmForm").css("display", "none");
				//window.location.href = "http://localhost:9000/EmergencyManager/#/main";
			} else {
				console.log(data);
				alert(data);
			}
		});
		
	};
	
	$scope.archive = function(id) {		
		if (confirm("Are you sure?\nThis operation can not be undone.")) {
			emergenciesFactory.archive(id).success(function(data) {
				if (data === "OK") {
					toast("Emergency archived successfully!");
					getEmergencies();
					var retVal = emergenciesFactory.save();
	    			console.log("Saving emergencies: " + retVal);
				} else {				
					alert(data);
				}
			});
		}		
	}
	
	$scope.changeVolunteer = function(event) {
		$(event.currentTarget).parent().parent().find(".emChangeContainer").slideToggle();
	};
	
	$scope.isNull = function(elem) {
		return !elem;
	};
	
	$scope.saveVolunteer = function(event, emId) {
		var username = $(event.currentTarget).parent().find(".emSelectVol").val();
		emergenciesFactory.saveVolunteer(emId, username).success(function(data){
			if (data === "OK") {
				toast("Changes saved successfully!");
				getEmergencies();
				$(event.currentTarget).parent().slideToggle();
				var retVal = emergenciesFactory.save();
    			console.log("Saving emergencies: " + retVal);
			} else {				
				alert(data);
			}
			
		});
	};
	
//	$scope.handleEmergencies = function() {
//		$(".terContainer").slideUp();
//		$(".usersContainer").slideUp();
//		$(".main").slideDown();
//	};
//	
//	$scope.handleUsers = function() {
//		$(".main").slideUp();
//		$(".terContainer").slideUp();
//		$(".usersContainer").slideDown();
//	};
//	
//	$scope.handleTerritories = function() {
//		$(".main").slideUp();
//		$(".usersContainer").slideUp();
//		$(".terContainer").slideDown();
//	};
	
	
	$scope.deleteTer = function(name) {
		territoriesFactory.getEmTer(name).success(function(data){
			var emno = data.length;
			if (emno > 0) {
				toast("This territory still has " + emno + " active emergencies!");
				return;
			} else {
				if (confirm("Are you sure?")) {
					toast("deleting...");
				}
			}
		});
	
	};
	
}]);

eManager.controller('loginController', ['$scope', 'usersFactory', 'territoriesFactory', function($scope, usersFactory, territoriesFactory) {
	var imageFileReg = "";
	
    function init() {
    	console.log('LoginController.Init');
    	
    	usersFactory.getLoggedUser().success(function(data){
    		if (data.username != null) {
    			window.location.href = "http://localhost:9000/EmergencyManager/#/main";
    			return;
    		}
    	});
    	
        usersFactory.getVolunteers().success(function (data) {
        	$scope.volunteers = data;
		});
    
        usersFactory.getAdmins().success(function (data) {
        	$scope.admins = data;
		});
        
        territoriesFactory.getTerritories().success(function (data) {
        	$scope.territories = data;
		});
    }

	init();
	
	$scope.login = function() {
		//toast("Value: " + $("#username").val());
		var user = {}
		user.username = $("#usernamelogin").val();
		user.password = $("#passwordlogin").val();
		usersFactory.login(user).success(function(data) {
			if (data === "OK") {
//				$(".imgcontainer").slideUp("slow", function() {
//					//$("#avatarImage").attr("src", "pictures/users/" + user.username + ".jpg");
//				});
				//$("#imgcontainer").slideDown();
				toast('User ' + user.username + " logged in successfully!");
				window.location.href = "http://localhost:9000/EmergencyManager/#/main";
			} else if (data === "BLOCKED") {
				alert("Your account has been blocked for further activites.\nPlease contact your administrator for support.")
			} else {
				toast("User with entered username or password doesn't exist!");
			}
			
		});	
	}; 
	
	$scope.register = function() {
		var user = {}
		user.username = $("#username").val();
		user.password = $("#password").val();
		user.name = $("#firstname").val();
		user.surname = $("#surname").val();
		user.phoneNumber = $("#phone").val();
		user.email = $("#email").val();
		user.state = "NORMAL";
		user.picture = imageFileReg;
		user.territory = $("#territory").val();
		
		usersFactory.register(user).success(function(data) {
			if (data === "OK") {
				var retVal = usersFactory.save();
    			console.log("Saving users: " + retVal);
				toast('User ' + user.username + " registered successfully!");
				window.location.href = "http://localhost:9000/EmergencyManager/#/main";
			} else {
				toast("User with entered username already exists!");
			}
			
		});	
	};
	
	$scope.openImageUpload = function() {
		$("#uploadImage").click();
	};
	
	$scope.displayLogin = function() {
		$("#loginForm").css("display", "block");
	}
	
	$scope.hideLogin = function() {
		$("#loginForm").css("display", "none");
	}
	
	$scope.displayRegister = function() {
		$("#registerForm").css("display", "block");
	}
	
	$scope.hideRegister = function() {
		$("#registerForm").css("display", "none");
	}
	
	$scope.handleFile = function(e) {
		
		if (e.target.files.length == 0) {
			toast("No file selected");
			$("#displayImage").attr("src", "pictures/img_avatar2.png");
			return;
		}
		
		var file = e.target.files[0];
		
		var reader = new FileReader();
		reader.onload = function(ev) {
			console.log(ev.target.result);
			$("#displayImage").attr("src", ev.target.result);
			imageFileReg = ev.target.result;
		};
		reader.readAsDataURL(file);
	}
}]);