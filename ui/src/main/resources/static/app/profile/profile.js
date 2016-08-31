angular.module('profileApp').controller('ProfileCtrl', function($scope, $rootScope, $account, $uibModal) {
    $scope.userImageUpdating = false;

    var getUserData = function () {
        $account.getUserData().then(function (user) {
            if (!user.userImage) {
                user.userImage = { url: 'http://placehold.it/200x200' };
            }
            $scope.user = user;
        });
    };
    getUserData();

    $scope.userImageAction = function () {
        var modURL = 'app/profile/update-user-image-modal.html';
        var profileImageActionsModal = $uibModal.open({ scope: $scope, templateUrl: modURL, controller: 'ProfileImageCtrl', windowClass: 'update-user-image-modal' });
    };

    $rootScope.$on("userImageStartUpdating", function() {
        $scope.userImageUpdating = true;
    });

    $rootScope.$on("userImageStoppedUpdating", function() {
        $scope.userImageUpdating = false;
    });
});

angular.module('profileApp').controller('ProfileImageCtrl', function($scope, $rootScope, $uibModalInstance, $account) {
    $uibModalInstance.opened.then(function() {

    });

    $scope.uploadFile = function (event) {
        var files = event.target.files;

        if (files[0].type == 'image/jpeg' || files[0].type == 'image/png') {
            $uibModalInstance.dismiss('cancel');
            $rootScope.$emit("userImageStartUpdating", {});

            $account.uploadUserImage(files[0]).then(function (userImage) {
                $scope.user.userImage = userImage;
            }).finally(function() {
                $rootScope.$emit("userImageStoppedUpdating", {});
            });

        } else {
            //not an image -> modal alert
        }
    };

    $scope.deleteUserImage = function () {
        $uibModalInstance.dismiss('cancel');
        $account.deleteUserImage().then(function () {
            $scope.user.userImage = { url: 'http://placehold.it/200x200' };
        });
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});