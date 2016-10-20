(function() {
    'use strict';

    angular
        .module('myapplicationApp')
        .controller('DormitoryDeleteController',DormitoryDeleteController);

    DormitoryDeleteController.$inject = ['$uibModalInstance', 'entity', 'Dormitory'];

    function DormitoryDeleteController($uibModalInstance, entity, Dormitory) {
        var vm = this;

        vm.dormitory = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Dormitory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
