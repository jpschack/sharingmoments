angular.module('resetPassword').controller('ResetPasswordCtrl', function($scope, $auth, $translate) {
    $scope.resetPassword = function() {
        if ($scope.resetPasswordForm.$valid) {
            var params = { 'email': $scope.email };
            $auth.resetPassword(params).then(function (status) {
                if (status) {
                    $translate('RESETPASSWORD.SUCCESS')
                    .then(function (translatedValue) {
                        alertService('success', translatedValue);
                    });
                } else {
                    $translate('RESETPASSWORD.REQUEST_ERROR.SERVER_ERROR')
                    .then(function (translatedValue) {
                        alertService('danger', translatedValue);
                    });
                }
            }).catch(function (error) {
                if (error.status === 404) {
                    $translate('RESETPASSWORD.REQUEST_ERROR.SERVER_ERROR')
                    .then(function (translatedValue) {
                        alertService('danger', translatedValue);
                    });
                } else {
                    $translate('RESETPASSWORD.REQUEST_ERROR.SERVER_ERROR')
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