(function() {
    'use strict';

    angular
        .module('myapplicationApp')
        .controller('PreferredContactDialogController', PreferredContactDialogController);

    PreferredContactDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PreferredContact', 'Student'];

    function PreferredContactDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PreferredContact, Student) {
        var vm = this;

        vm.preferredContact = entity;
        vm.clear = clear;
        vm.save = save;
        vm.students = Student.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.preferredContact.id !== null) {
                PreferredContact.update(vm.preferredContact, onSaveSuccess, onSaveError);
            } else {
                PreferredContact.save(vm.preferredContact, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myapplicationApp:preferredContactUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
