angular.module('accountApp').controller('AccountCtrl', function($scope, $state, $account) {
    $scope.fetchContent = function() {
        $account.getUserData().then(function (user) {
            $scope.user = user;
        });
    }
    $scope.fetchContent();
});

angular.module('accountApp').controller('EditAccountCtrl', function($scope, $account, $uibModal, $translate) {
    $scope.saveAccount = function() {
        if ($scope.accountForm.$valid) {
            $account.updateUserData(JSON.stringify($scope.user)).then(function (user) {
                $scope.user = user;

                $translate('ACCOUNT.NAVIGATION.ACCOUNT.UPDATE_SUCCESS')
                .then(function (translatedValue) {
                    alertService('success', translatedValue);
                });
            }).catch(function (error) {
                if (error.status === 403) {
                    $translate('ACCOUNT.REQUEST_ERROR.CLIENT_ERROR')
                    .then(function (translatedValue) {
                        alertService('danger', translatedValue);
                    });
                    
                    if (error.data.errorList != null && error.data.errorList.length > 0) {
                        for (var i = 0; i < error.data.errorList.length; i++) {
                            if (error.data.errorList[i].field == 'email') {
                                $scope.accountForm.email.$setValidity("taken", false);
                            } else if (error.data.errorList[i].field == 'username') {
                                $scope.accountForm.username.$setValidity("taken", false);
                            }
                        }
                    }
                } else {
                    $translate('ACCOUNT.REQUEST_ERROR.SERVER_ERROR')
                    .then(function (translatedValue) {
                        alertService('danger', translatedValue);
                    });
                }
            });
        }
    };

    $scope.deleteAccount = function() {
        var modURL = 'app/account/delete-account-modal.html';
        var deleteAccountModal = $uibModal.open({ scope: $scope, templateUrl: modURL, controller: 'DeleteAccountCtrl', size: 'sm' });
    };

    var alertService = function (type, msg) {
        $scope.alert = { 'type': type, 'msg': msg };
    };
    
    $scope.closeAlert = function () {
        $scope.alert = undefined;
    };
});

angular.module('accountApp').controller('DeleteAccountCtrl', function($scope, $rootScope, $state, $account, $uibModalInstance, $cookies) {
    $scope.delete = function() {
        $account.deleteAccount().then(function () {
            $cookies.remove('X-AUTH-TOKEN');
            $rootScope.authenticated = false;
            $state.go('/');
        });
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});

angular.module('accountApp').controller('PrivacyCtrl', function($scope, $http, $account) {
    var init = function () {
        $scope.user = $account.getUser();

        if ($scope.user === undefined || $scope.user == null) {
            $account.getUserData().then(function (user) {
                $scope.user = user;
            });
        }
    }
    init();
});

angular.module('accountApp').directive('bootstrapSwitch', function($account) {
    return {
        restrict: 'A',
        require: '?ngModel',
        link: function(scope, element, attrs, ngModel) {
            element.bootstrapSwitch();

            element.on('switchChange.bootstrapSwitch', function(event, state) {
                if (ngModel) {
                    scope.$apply(function() {
                        ngModel.$setViewValue(state);
                    });
                    $account.updatePrivacy(JSON.stringify(scope.user.privateAccount)).then(function () {});
                }
            });

            scope.$watch(attrs.ngModel, function(newValue, oldValue) {
                if (newValue) {
                    element.bootstrapSwitch('state', true, true);
                } else {
                    element.bootstrapSwitch('state', false, true);
                }
            });
        }
    };
});

angular.module('accountApp').controller('PasswordCtrl', function($scope, $account, $translate) {
    $scope.changePassword = function() {
        if ($scope.passwordForm.$valid) {
            var passwordParams = { 'oldPassword': $scope.oldPassword, 'password': $scope.password };
            $account.updateUserPassword(JSON.stringify(passwordParams)).then(function () {
                $translate('ACCOUNT.NAVIGATION.PASSWORD.UPDATE_SUCCESS')
                .then(function (translatedValue) {
                    alertService('success', translatedValue);
                });
            }).catch(function (error) {
                if (error.status === 403) {
                    $translate('ACCOUNT.REQUEST_ERROR.CLIENT_ERROR')
                    .then(function (translatedValue) {
                        alertService('danger', translatedValue);
                    });
                    $scope.passwordForm.oldPassword.$setValidity("incorrect", false);
                } else {
                    $translate('ACCOUNT.REQUEST_ERROR.SERVER_ERROR')
                    .then(function (translatedValue) {
                        alertService('danger', translatedValue);
                    });
                }
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

angular.module('accountApp').service('$account', function($http) {
    var user = null;

    return {
        getUser : function () {
            return user;
        },
        getUserData : function () {
            var url = '/api/v1/resource/account';
            return $http.get(url)
                .then(
                    function (response) {
                        if (response.status === 200 && response.data) {
                            user = response.data;
                            return user;
                        } else {
                            return null;
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        },
        updateUserData : function (jsonString) {
            var url = '/api/v1/resource/account';
            return $http.put(url, jsonString)
                .then(
                    function (response) {
                        if (response.status === 200 && response.data.dataObject) {
                            user = response.data.dataObject;
                            return user;
                        } else {
                            return null;
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        },
        updatePrivacy : function (privateAccount) {
            var url = '/api/v1/resource/account/privacy';
            var privacy = { 'privateAccount': privateAccount };
            return $http.put(url, privacy)
                .then(
                    function (response) {
                        if (response.status === 200 && response.data) {
                            user = response.data;
                            return user;
                        } else {
                            return null;
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        },
        updateUserPassword : function (jsonString) {
            var url = '/api/v1/resource/account/password';
            return $http.put(url, jsonString)
                .then(
                    function (response) {
                        if (esponse.status === 200) {
                            return true;
                        } else {
                            return false;
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        },
        deleteAccount : function () {
            var url = '/api/v1/resource/account';
            return $http.delete(url)
                .then(
                    function (response) {
                        if (response.status === 200) {
                            return true;
                        } else {
                            return false;
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        },
        uploadUserImage : function (file) {
            var url = '/api/v1/resource/account/userImage';
            var fileData = new FormData();
            fileData.append('file', file);

            return $http.post(url, fileData, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                })
                .then(
                    function (response) {
                        if (response.status === 200 && response.data) {
                            user.userImage = response.data;
                            return user.userImage;
                        } else {
                            return null;
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        },
        deleteUserImage : function () {
            var url = '/api/v1/resource/account/userImage';
            return $http.delete(url)
                .then(
                    function (response) {
                        if (response.status === 200) {
                            return true;
                        } else {
                            return false;
                        }
                },
                function (httpError) {
                    throw { 'status': httpError.status , 'data': httpError.data };
                });
        }
    };
});