angular.module('authService').service('$auth', function($rootScope, $http, $cookies) {
    return {
        login: function (params) {
            var url = '/api/v1/uaa/login';
            return $http.post(url, params)
                .then(
                    function (response) {
                        if (response.status === 200 && response.headers('X-AUTH-TOKEN')) {
                            $cookies.put('X-AUTH-TOKEN', response.headers('X-AUTH-TOKEN'));
                            $rootScope.authenticated = true;
                            return true;
                        } else {
                            return false;
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        },
        resetPassword : function (jsonString) {
            var url = '/api/v1/uaa/account/resetPassword';
            return $http.post(url, jsonString)
                .then(
                    function (response) {
                        if (response.status === 200) {
                            return true;
                        } else {
                            return false;
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        },
        updatePassword : function (jsonString) {
            var url = '/api/v1/uaa/account/savePassword';
            return $http.post(url, jsonString)
                .then(
                    function (response) {
                        if (response.status === 200 && response.headers('X-AUTH-TOKEN')) {
                            $cookies.put('X-AUTH-TOKEN', response.headers('X-AUTH-TOKEN'));
                            $rootScope.authenticated = true;
                            return true;
                        } else {
                            return false;
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        },
        signup: function (params) {
            var url = '/api/v1/uaa/account/registration';
            return $http.post(url, params)
                .then(
                    function (response) {
                        if (response.status === 200 && response.headers('X-AUTH-TOKEN')) {
                            $cookies.put('X-AUTH-TOKEN', response.headers('X-AUTH-TOKEN'));
                            $rootScope.authenticated = true;
                            return true;
                        } else {
                            return false;
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        },
        logout: function () {
            $cookies.remove('X-AUTH-TOKEN');
            $rootScope.authenticated = false;
        }
    };
});