angular.module('weblamp').controller('loginController', function ($scope, $rootScope, $http, $localStorage, $location) {
    const contextPath = 'http://localhost:8192/weblamp';

    $rootScope.auth = function () {
            $rootScope.user=$scope.user;
//            var date = new Date();
//            timezoneOffset = date.getTimezoneOffset() / 60 * (-1);
//            $rootScope.user.timezoneOffset = timezoneOffset;
//            console.log($rootScope.user);
            $http.post(contextPath+'/auth', $rootScope.user)
                .then(function successCallback(response) {
                    if (response.data.token) {
                        $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                        $localStorage.springWebUser = {username: $rootScope.user.username, token: response.data.token};

                        $rootScope.user.username = null;
                        $rootScope.user.password = null;
                        $rootScope.loadOrder();
                        if($rootScope.wayForAuth!=null) {
                            $location.url($rootScope.wayForAuth);
                        } else {
                            $location.url('/');
                        }
                    }
                }, function errorCallback(response) {
                    alert("Неверный логин/пароль");
                });
            };
});