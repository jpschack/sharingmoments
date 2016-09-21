angular.module('resetPassword').controller('ResetPasswordCtrl', function($scope, $auth) {
    $scope.resetPassword = function() {
        if ($scope.resetPasswordForm.$valid) {
            var params = { 'email': $scope.email };
            $auth.resetPassword(params).then(function (status) {
                if (status) {
                    alertService('success', 'RESETPASSWORD.SUCCESS');
                } else {
                    alertService('danger', 'RESETPASSWORD.REQUEST_ERROR.SERVER_ERROR');
                }
            }).catch(function (error) {
                if (error.status === 404) {
                    alertService('danger', 'RESETPASSWORD.REQUEST_ERROR.SERVER_ERROR');
                } else {
                    alertService('danger', 'RESETPASSWORD.REQUEST_ERROR.SERVER_ERROR');
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