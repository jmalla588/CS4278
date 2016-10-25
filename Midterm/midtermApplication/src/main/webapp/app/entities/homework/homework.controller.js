(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .controller('HomeworkController', HomeworkController);

    HomeworkController.$inject = ['$scope', '$state', 'Homework', 'HomeworkSearch'];

    function HomeworkController ($scope, $state, Homework, HomeworkSearch) {
        var vm = this;
        
        vm.homework = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Homework.query(function(result) {
                vm.homework = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            HomeworkSearch.query({query: vm.searchQuery}, function(result) {
                vm.homework = result;
            });
        }    }
})();
