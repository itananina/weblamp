angular.module('weblamp').controller('signUpController', function ($scope, $rootScope, $http, $localStorage, $location) {
    const contextPath = 'http://localhost:8192/weblamp';

    $rootScope.signUp = function () {
        $rootScope.user=$scope.user;
        $http.post(contextPath+'/sign-up', $rootScope.user)
            .then(function successCallback(response) {
                if (response.data.token) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.springWebUser = {username: $rootScope.user.username, token: response.data.token};

                    $rootScope.user.username = null;
                    $rootScope.user.password = null;
                    $rootScope.user.email = null;
                    $rootScope.loadOrder();
                    if($rootScope.wayForAuth!=null) {
                        $location.url($rootScope.wayForAuth);
                    } else {
                        $location.url('/');
                    }
                }
            }, function errorCallback(response) {
                console.log(response.data);
                alert(response.data.message);
                $scope.user = null;
            });
    };

});