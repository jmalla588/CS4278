(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .controller('GradeDialogController', GradeDialogController);

    GradeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Grade', 'Submission', 'Instructor'];

    function GradeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Grade, Submission, Instructor) {
        var vm = this;

        vm.grade = entity;
        vm.clear = clear;
        vm.save = save;
        vm.submissions = Submission.query({filter: 'grade-is-null'});
        $q.all([vm.grade.$promise, vm.submissions.$promise]).then(function() {
            if (!vm.grade.submission || !vm.grade.submission.id) {
                return $q.reject();
            }
            return Submission.get({id : vm.grade.submission.id}).$promise;
        }).then(function(submission) {
            vm.submissions.push(submission);
        });
        vm.instructors = Instructor.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.grade.id !== null) {
                Grade.update(vm.grade, onSaveSuccess, onSaveError);
            } else {
                Grade.save(vm.grade, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('midtermApplicationApp:gradeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
