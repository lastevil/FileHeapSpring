angular.module('fileApp', []).controller('indexController', function ($scope, $http) {
    const contextPath = 'http://localhost:8189/fileApp/api/v1/files';
    let currentPage = 1;
    let lastPage = 0;
    let filterOn = false;

    $scope.loadFilesList = function (page) {
        $http({
            url: contextPath,
            method: 'GET',
            params: {
                page: page
            }
        }).then(function (response) {
            $scope.filesList = response.data;
            lastPage = response.data.totalPages;
            var arr = [];
            for (var i = 0; i < lastPage; i++) {
                arr[i] = i + 1;
            }
            $scope.countPage = arr;
        });
    };

    $scope.deleteFile = function (fileId) {
        $http.delete(contextPath + '/' + fileId)
            .then(function (response) {
                $scope.loadFilesList(currentPage);
            });
    };

    $scope.uploadFile = async function () {
        let formData = new FormData();
        formData.append("data", document.getElementById("fileupload").files[0]);
        await fetch(contextPath, {
            method: "POST",
            body: formData
        }).then(function (response) {
            alert(response.text());
            $scope.loadFilesList(currentPage);
        });
    }

    $scope.setCurrentPage = function (i) {
        currentPage = i;
        $scope.loadFilesList(currentPage);
    };

    $scope.tryToAuth = function () {
        $http.post('http://localhost:8189/fileApp/api/v1/login/auth', $scope.user)
            .then(function successCallback(response) {
                if (response.data.token) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    localStorage.setItem('springWebUser', response.data.token);
                    $scope.user.username = null;
                    $scope.user.password = null;
                    $scope.loadFilesList(currentPage);
                }
            }, function errorCallback(response) {

            });
    };

    $scope.tryToLogout = function () {
        $scope.clearUser();
        if ($scope.user.username) {
            $scope.user.username = null;
        }
        if ($scope.user.password) {
            $scope.user.password = null;
        }
    };

    $scope.clearUser = function () {
        localStorage.clear();
        $http.defaults.headers.common.Authorization = '';
    };

    $scope.isUserLoggedIn = function () {
        if (localStorage.getItem('springWebUser') != null) {
            return true;
        } else {
            return false;
        }
    };

    if (localStorage.getItem('springWebUser') != null) {

        $scope.loadFilesList(currentPage);
    }
});