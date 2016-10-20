(function() {
    'use strict';
    angular
        .module('myapplicationApp')
        .factory('Student', Student);

    Student.$inject = ['$resource', 'DateUtils'];

    function Student ($resource, DateUtils) {
        var resourceUrl =  'api/students/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.graduationDate = DateUtils.convertLocalDateFromServer(data.graduationDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.graduationDate = DateUtils.convertLocalDateToServer(copy.graduationDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.graduationDate = DateUtils.convertLocalDateToServer(copy.graduationDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
