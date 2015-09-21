var otp = angular.module('otp', []);

var auth = {};

angular.element(document).ready(function () {
    var keycloakAuth = new Keycloak('config/keycloak.json');
    auth.loggedIn = false;

    keycloakAuth.init({ onLoad: 'login-required' }).success(function () {
        auth.loggedIn = true;
        auth.authz = keycloakAuth;
        auth.logoutUrl = keycloakAuth.authServerUrl + "/realms/otp-demo/tokens/logout?redirect_uri=/otp-demo/index.html";
        otp.factory('Auth', function () {
            return auth;
        });
        angular.bootstrap(document, ["otp"]);
    }).error(function () {
        window.location.reload();
    });
});

otp.controller('GlobalCtrl', function ($scope) {
    $scope.logout = function () {
        console.log('*** LOGOUT');
        auth.loggedIn = false;
        auth.authz = null;
        window.location = auth.logoutUrl;
    };
});