(function() {
    'use strict';

    angular
        .module('myapplicationApp')
        .controller('DormitoryController', DormitoryController);

    DormitoryController.$inject = ['$scope', '$state', 'Dormitory'];

    function DormitoryController ($scope, $state, Dormitory) {
        var vm = this;
        
        vm.dormitories = [];

        loadAll();

        function loadAll() {
            Dormitory.query(function(result) {
                vm.dormitories = result;
            });
        }
    }
})();
