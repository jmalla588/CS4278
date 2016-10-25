(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('homework', {
            parent: 'entity',
            url: '/homework',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Homework'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/homework/homework.html',
                    controller: 'HomeworkController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('homework-detail', {
            parent: 'entity',
            url: '/homework/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Homework'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/homework/homework-detail.html',
                    controller: 'HomeworkDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Homework', function($stateParams, Homework) {
                    return Homework.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'homework',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('homework-detail.edit', {
            parent: 'homework-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/homework/homework-dialog.html',
                    controller: 'HomeworkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Homework', function(Homework) {
                            return Homework.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('homework.new', {
            parent: 'homework',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/homework/homework-dialog.html',
                    controller: 'HomeworkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                due: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('homework', null, { reload: 'homework' });
                }, function() {
                    $state.go('homework');
                });
            }]
        })
        .state('homework.edit', {
            parent: 'homework',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/homework/homework-dialog.html',
                    controller: 'HomeworkDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Homework', function(Homework) {
                            return Homework.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('homework', null, { reload: 'homework' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('homework.delete', {
            parent: 'homework',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/homework/homework-delete-dialog.html',
                    controller: 'HomeworkDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Homework', function(Homework) {
                            return Homework.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('homework', null, { reload: 'homework' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
