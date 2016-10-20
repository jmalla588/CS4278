(function() {
    'use strict';

    angular
        .module('myapplicationApp')
        .controller('PreferredContactDeleteController',PreferredContactDeleteController);

    PreferredContactDeleteController.$inject = ['$uibModalInstance', 'entity', 'PreferredContact'];

    function PreferredContactDeleteController($uibModalInstance, entity, PreferredContact) {
        var vm = this;

        vm.preferredContact = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PreferredContact.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
