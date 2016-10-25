(function() {
    'use strict';
    angular
        .module('midtermApplicationApp')
        .factory('Homework', Homework);

    Homework.$inject = ['$resource', 'DateUtils'];

    function Homework ($resource, DateUtils) {
        var resourceUrl =  'api/homework/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.due = DateUtils.convertDateTimeFromServer(data.due);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
