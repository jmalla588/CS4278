(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .factory('SubmissionSearch', SubmissionSearch);

    SubmissionSearch.$inject = ['$resource'];

    function SubmissionSearch($resource) {
        var resourceUrl =  'api/_search/submissions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
