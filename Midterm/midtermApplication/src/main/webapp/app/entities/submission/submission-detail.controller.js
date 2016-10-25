(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .controller('SubmissionDetailController', SubmissionDetailController);

    SubmissionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Submission', 'Homework', 'Grade', 'Student'];

    function SubmissionDetailController($scope, $rootScope, $stateParams, previousState, entity, Submission, Homework, Grade, Student) {
        var vm = this;

        vm.submission = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('midtermApplicationApp:submissionUpdate', function(event, result) {
            vm.submission = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
