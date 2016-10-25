(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .controller('SubmissionController', SubmissionController);

    SubmissionController.$inject = ['$scope', '$state', 'Submission', 'SubmissionSearch'];

    function SubmissionController ($scope, $state, Submission, SubmissionSearch) {
        var vm = this;
        
        vm.submissions = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Submission.query(function(result) {
                vm.submissions = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SubmissionSearch.query({query: vm.searchQuery}, function(result) {
                vm.submissions = result;
            });
        }    }
})();
