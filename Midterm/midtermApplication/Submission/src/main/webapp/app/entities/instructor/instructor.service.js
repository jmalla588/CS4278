(function() {
    'use strict';
    angular
        .module('midtermApplicationApp')
        .factory('Instructor', Instructor);

    Instructor.$inject = ['$resource'];

    function Instructor ($resource) {
        var resourceUrl =  'api/instructors/:id';

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
