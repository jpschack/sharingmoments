angular.module('errorApp').controller('ErrorCtrl', function($scope, $stateParams) {
    var init = function () {
        if ($stateParams.status >= 500) {
            $scope.errorMessage = 'ERROR.MESSAGE.500';
        } else if($stateParams.status == 403) {
            $scope.errorMessage = 'ERROR.MESSAGE.403';
        } else if($stateParams.status == 400 || $stateParams.status == 404) {
            $scope.errorMessage = 'ERROR.MESSAGE.404';
        } else {
            $scope.errorMessage = 'ERROR.MESSAGE.500';
        }
    }
    init();
});