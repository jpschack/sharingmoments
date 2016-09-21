angular.module('loginApp').controller('LoginCtrl', function($scope, $state, $auth) {
    $scope.login = function() {
        var params = { 'username': $scope.username, 'password': $scope.password };
        
        $auth.login(JSON.stringify(params)).then(function (status) {
            if (status) {
                $state.go('/');
            } else {
                alertService('danger', 'SIGNIN.REQUEST_ERROR.SERVER_ERROR');
            }
        }).catch(function (error) {
            if (error.status === 401) {
                alertService('danger', 'SIGNIN.REQUEST_ERROR.CLIENT_ERROR');
            } else {
                alertService('danger', 'SIGNIN.REQUEST_ERROR.SERVER_ERROR');
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