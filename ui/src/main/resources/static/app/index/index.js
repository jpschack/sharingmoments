angular.module('indexApp').controller('IndexContentCtrl', function($stateParams, $location, $state) {
    if ($stateParams.firstLogin) {
        //show modal and inform user about verifing his email address
        //also show some first introduction
    }

    //Redirects
    if ($location.search().redirectUrl && $location.search().status == "succeeded") {
        if ($location.search().redirectUrl == "updatePassword" && $location.search().id && $location.search().token) {
            $state.go($location.search().redirectUrl, { 'id': $location.search().id, 'token': $location.search().token });
        } else {
            $state.go($location.search().redirectUrl);
        }
    }
});

angular.module('indexApp').controller('IndexCtrl', function($state, $location) {});