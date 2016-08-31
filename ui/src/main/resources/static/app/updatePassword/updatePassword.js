angular.module('updatePassword').controller('UpdatePasswordCtrl', function($scope, $state, $stateParams, $auth, $translate) {
    if (!$stateParams.id || !$stateParams.token) {
        $state.go('/');
    }

    $scope.setPassword = function() {
        if ($scope.updatePasswordForm.$valid) {
            var requestObject = {'password' : $scope.password, 'id': $stateParams.id, 'token': $stateParams.token };

            $auth.updatePassword(requestObject).then(function (response) {
                $translate('UPDATEPASSWORD.SUCCESS')
                .then(function (translatedValue) {
                    alertService('success', translatedValue);
                });
                $scope.passwordChangedSuccessfull = true;
            }).catch(function (error) {
                $translate('UPDATEPASSWORD.REQUEST_ERROR.SERVER_ERROR')
                .then(function (translatedValue) {
                    alertService('danger', translatedValue);
                });
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