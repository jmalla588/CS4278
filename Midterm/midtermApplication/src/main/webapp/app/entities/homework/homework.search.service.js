(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .factory('HomeworkSearch', HomeworkSearch);

    HomeworkSearch.$inject = ['$resource'];

    function HomeworkSearch($resource) {
        var resourceUrl =  'api/_search/homework/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
