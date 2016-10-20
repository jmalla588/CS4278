(function() {
    'use strict';

    angular
        .module('myapplicationApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dormitory', {
            parent: 'entity',
            url: '/dormitory',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Dormitories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dormitory/dormitories.html',
                    controller: 'DormitoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('dormitory-detail', {
            parent: 'entity',
            url: '/dormitory/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Dormitory'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dormitory/dormitory-detail.html',
                    controller: 'DormitoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Dormitory', function($stateParams, Dormitory) {
                    return Dormitory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'dormitory',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('dormitory-detail.edit', {
            parent: 'dormitory-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dormitory/dormitory-dialog.html',
                    controller: 'DormitoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Dormitory', function(Dormitory) {
                            return Dormitory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dormitory.new', {
            parent: 'dormitory',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dormitory/dormitory-dialog.html',
                    controller: 'DormitoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dormName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dormitory', null, { reload: 'dormitory' });
                }, function() {
                    $state.go('dormitory');
                });
            }]
        })
        .state('dormitory.edit', {
            parent: 'dormitory',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dormitory/dormitory-dialog.html',
                    controller: 'DormitoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Dormitory', function(Dormitory) {
                            return Dormitory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dormitory', null, { reload: 'dormitory' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dormitory.delete', {
            parent: 'dormitory',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dormitory/dormitory-delete-dialog.html',
                    controller: 'DormitoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Dormitory', function(Dormitory) {
                            return Dormitory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dormitory', null, { reload: 'dormitory' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
