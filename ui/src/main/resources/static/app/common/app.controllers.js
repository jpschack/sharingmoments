angular.module('app.controllers').controller('TabCtrl', function () {
    'use strict';
    
    this.tab = 1;

    this.setTab = function (tabId) {
        this.tab = tabId;
    };

    this.isSet = function (tabId) {
        return this.tab === tabId;
    };
});

angular.module('app.controllers').controller('photoModalCtrl', function($scope, $modalPhotoViewSlider) {
    $scope.openPhotoModal = function(index, photos) {
        $modalPhotoViewSlider.open({
            index: index,
            photos: photos
        });
    };
});