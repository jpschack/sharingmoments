angular.module('signupApp').controller('SignupCtrl', function($scope, $state, $auth, $translate) {
    $scope.register = function() {
        if ($scope.signupForm.$valid) {
            $auth.signup(JSON.stringify($scope.user)).then(function (status) {
                if (status) {
                    $state.go('/', {'firstLogin': true});
                } else {
                    $translate('SIGNUP.REQUEST_ERROR.SERVER_ERROR')
                    .then(function (translatedValue) {
                        alertService('danger', translatedValue);
                    });
                }
            }).catch(function (error) {
                if (error.status === 409 && error.data.message) {
                    alertService('danger', error.data.message);
                } else {
                    $translate('SIGNUP.REQUEST_ERROR.SERVER_ERROR')
                    .then(function (translatedValue) {
                        alertService('danger', translatedValue);
                    });
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
