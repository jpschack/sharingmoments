angular.module('updatePassword').controller('UpdatePasswordCtrl', function($scope, $state, $stateParams, $auth) {
    if (!$stateParams.id || !$stateParams.token) {
        $state.go('/');
    }

    $scope.setPassword = function() {
        if ($scope.updatePasswordForm.$valid) {
            var requestObject = {'password' : $scope.password, 'id': $stateParams.id, 'token': $stateParams.token };

            $auth.updatePassword(requestObject).then(function (response) {
                alertService('success', 'UPDATEPASSWORD.SUCCESS');
                $scope.passwordChangedSuccessfull = true;
            }).catch(function (error) {
                alertService('danger', 'UPDATEPASSWORD.REQUEST_ERROR.SERVER_ERROR');
            });
        }
    };

    var alertService = function (type, msg) {
        $scope.alert = { 'type': type, 'msg': msg };
    };
    
    $scope.closeAlert = function () {
        $scope.alert = undefined;
    };
});