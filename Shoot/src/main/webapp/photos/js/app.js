var module = angular.module('photo', ['ui.bootstrap']);

var auth = {};
var logout = function(){
    console.log('*** LOGOUT');
    auth.loggedIn = false;
    auth.authz = null;
    window.location = auth.logoutUrl;
};


angular.element(document).ready(function ($http) {
    var keycloakAuth = new Keycloak('../config/keycloak.json');
    auth.loggedIn = false;

    keycloakAuth.init({ onLoad: 'login-required' }).success(function () {
        auth.loggedIn = true;
        auth.authz = keycloakAuth;
        auth.logoutUrl = keycloakAuth.authServerUrl + "/realms/shoot-realm/protocol/openid-connect/logout?redirect_uri=/shoot/photos/index.html";
        module.factory('Auth', function() {
            return auth;
        });
        angular.bootstrap(document, ["photo"]);
    }).error(function () {
            window.location.reload();
        });

});

module.controller('GlobalCtrl', function($scope, $http) {
    $scope.photos = [];
    $scope.myInterval = 2000;
    $scope.reloadData = function() {
        $http.get("/shoot/rest/photos").success(function(data) {

            var previousLength = $scope.photos.length
            var photosFromServer = angular.fromJson(data);
            for (var i = previousLength; i < data.length; i++) {
                $scope.photos.push(photosFromServer[i]);
                $http.get("/shoot/rest/photos/images/" + data[i].filename).success(function(binary) {
                    data[i-1].src = "data:image/jpeg;base64," + binary;
                });
            }
        });
        setTimeout($scope.reloadData, 2000);
    };

    $scope.reloadAllData = function() {
        $http.get("/shoot/rest/photos").success(function(data) {
            $scope.photos = angular.fromJson(data);
            data.forEach(function(elt, index){
                $http.get("/shoot/rest/photos/images/" + elt.filename ).success(function(data) {
                    elt.src = "data:image/jpeg;base64," + data;
                });
            })
            setTimeout($scope.reloadData, 2000);
        });
    };
    $scope.logout = logout;
    $scope.reloadAllData();
});


module.factory('authInterceptor', function($q, Auth) {
    return {
        request: function (config) {
            var deferred = $q.defer();
            if (Auth.authz.token) {
                Auth.authz.updateToken(5).success(function() {
                    config.headers = config.headers || {};
                    config.headers.Authorization = 'Bearer ' + Auth.authz.token;

                    deferred.resolve(config);
                }).error(function() {
                        deferred.reject('Failed to refresh token');
                    });
            }
            return deferred.promise;
        }
    };
});

module.config(function($httpProvider) {
    $httpProvider.responseInterceptors.push('errorInterceptor');
    $httpProvider.interceptors.push('authInterceptor');

});

module.factory('errorInterceptor', function($q) {
    return function(promise) {
        return promise.then(function(response) {
            return response;
        }, function(response) {
            if (response.status == 401) {
                console.log('session timeout?');
                logout();
            } else if (response.status == 403) {
                alert("Forbidden");
            } else if (response.status == 404) {
                //alert("Not found");
            } else if (response.status) {
                if (response.data && response.data.errorMessage) {
                    alert(response.data.errorMessage);
                } else {
                    alert("An unexpected server error has occurred");
                }
            }
            return $q.reject(response);
        });
    };
});
