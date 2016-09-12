angular.module('indexApp').controller('IndexContentCtrl', function($scope, $stateParams, $location, $state, $uibModal) {
    if ($location.search().registrationConfirm == "succeeded") {
        var modURL = 'app/index/registration-confirm-modal.html';
        var newMomentsModal = $uibModal.open({ scope: $scope, templateUrl: modURL, controller: 'RegistrationConfirmModalCtrl', size: 'sm' });
    }

    if ($stateParams.firstLogin) {
        var modURL = 'app/index/first-login-alert-modal.html';
        var newMomentsModal = $uibModal.open({ scope: $scope, templateUrl: modURL, controller: 'FirstLoginAlertModalCtrl', size: 'sm' });
    }
});

angular.module('indexApp').controller('RegistrationConfirmModalCtrl', function($scope, $uibModalInstance) {
    $scope.close = function () {
        $uibModalInstance.dismiss('cancel');
    };
});

angular.module('indexApp').controller('FirstLoginAlertModalCtrl', function($scope, $uibModalInstance) {
    $scope.close = function () {
        $uibModalInstance.dismiss('cancel');
    };
});