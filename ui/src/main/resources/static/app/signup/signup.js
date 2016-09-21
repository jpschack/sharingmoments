angular.module('signupApp').controller('SignupCtrl', function($scope, $state, $auth) {
    $scope.register = function() {
        if ($scope.signupForm.$valid) {
            $auth.signup(JSON.stringify($scope.user)).then(function (status) {
                if (status) {
                    $state.go('/', {'firstLogin': true});
                } else {
                    alertService('danger', 'SIGNUP.REQUEST_ERROR.SERVER_ERROR');
                }
            }).catch(function (error) {
                if (error.status === 409 && error.data.message) {
                    alertService('danger', error.data.message);
                } else {
                    alertService('danger', 'SIGNUP.REQUEST_ERROR.SERVER_ERROR');
                }
            });
        }
    }

    var alertService = function (type, msg) {
        $scope.alert = { 'type': type, 'msg': msg };
    }
    
    $scope.closeAlert = function () {
        $scope.alert = undefined;
    }
});
