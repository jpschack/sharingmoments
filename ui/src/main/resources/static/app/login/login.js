angular.module('loginApp').controller('LoginCtrl', function($scope, $state, $auth, $translate) {
    $scope.login = function() {
        var params = { 'username': $scope.username, 'password': $scope.password };
        
        $auth.login(JSON.stringify(params)).then(function (status) {
            if (status) {
                $state.go('/');
            } else {
                $translate('SIGNIN.REQUEST_ERROR.SERVER_ERROR')
                .then(function (translatedValue) {
                    alertService('danger', translatedValue);
                });
            }
        }).catch(function (error) {
            if (error.status === 401) {
                $translate('SIGNIN.REQUEST_ERROR.CLIENT_ERROR')
                .then(function (translatedValue) {
                    alertService('danger', translatedValue);
                });
            } else {
                $translate('SIGNIN.REQUEST_ERROR.SERVER_ERROR')
                .then(function (translatedValue) {
                    alertService('danger', translatedValue);
                });
            }

            $scope.password = null;
        });
    }

    var alertService = function (type, msg) {
        $scope.alert = { 'type': type, 'msg': msg };
    }
    
    $scope.closeAlert = function () {
        $scope.alert = undefined;
    }
});