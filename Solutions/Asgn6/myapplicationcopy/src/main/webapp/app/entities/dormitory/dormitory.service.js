(function() {
    'use strict';
    angular
        .module('myapplicationApp')
        .factory('Dormitory', Dormitory);

    Dormitory.$inject = ['$resource'];

    function Dormitory ($resource) {
        var resourceUrl =  'api/dormitories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
