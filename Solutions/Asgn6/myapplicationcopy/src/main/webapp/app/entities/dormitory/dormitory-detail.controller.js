(function() {
    'use strict';

    angular
        .module('myapplicationApp')
        .controller('DormitoryDetailController', DormitoryDetailController);

    DormitoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Dormitory', 'Student'];

    function DormitoryDetailController($scope, $rootScope, $stateParams, previousState, entity, Dormitory, Student) {
        var vm = this;

        vm.dormitory = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myapplicationApp:dormitoryUpdate', function(event, result) {
            vm.dormitory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
