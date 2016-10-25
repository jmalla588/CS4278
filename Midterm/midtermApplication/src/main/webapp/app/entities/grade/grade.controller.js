(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .controller('GradeController', GradeController);

    GradeController.$inject = ['$scope', '$state', 'Grade', 'GradeSearch'];

    function GradeController ($scope, $state, Grade, GradeSearch) {
        var vm = this;
        
        vm.grades = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Grade.query(function(result) {
                vm.grades = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            GradeSearch.query({query: vm.searchQuery}, function(result) {
                vm.grades = result;
            });
        }    }
})();
