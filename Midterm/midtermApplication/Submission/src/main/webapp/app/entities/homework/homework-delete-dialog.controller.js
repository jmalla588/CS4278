(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .controller('HomeworkDeleteController',HomeworkDeleteController);

    HomeworkDeleteController.$inject = ['$uibModalInstance', 'entity', 'Homework'];

    function HomeworkDeleteController($uibModalInstance, entity, Homework) {
        var vm = this;

        vm.homework = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Homework.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
