(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .controller('SubmissionDialogController', SubmissionDialogController);

    SubmissionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Submission', 'Homework', 'Grade', 'Student'];

    function SubmissionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Submission, Homework, Grade, Student) {
        var vm = this;

        vm.submission = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.homework = Homework.query();
        vm.grades = Grade.query();
        vm.students = Student.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.submission.id !== null) {
                Submission.update(vm.submission, onSaveSuccess, onSaveError);
            } else {
                Submission.save(vm.submission, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('midtermApplicationApp:submissionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
