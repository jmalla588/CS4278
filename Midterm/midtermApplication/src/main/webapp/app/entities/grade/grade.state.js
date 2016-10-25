(function() {
    'use strict';

    angular
        .module('midtermApplicationApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('grade', {
            parent: 'entity',
            url: '/grade',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Grades'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/grade/grades.html',
                    controller: 'GradeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('grade-detail', {
            parent: 'entity',
            url: '/grade/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Grade'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/grade/grade-detail.html',
                    controller: 'GradeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Grade', function($stateParams, Grade) {
                    return Grade.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'grade',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('grade-detail.edit', {
            parent: 'grade-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/grade/grade-dialog.html',
                    controller: 'GradeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Grade', function(Grade) {
                            return Grade.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('grade.new', {
            parent: 'grade',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/grade/grade-dialog.html',
                    controller: 'GradeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                score: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('grade', null, { reload: 'grade' });
                }, function() {
                    $state.go('grade');
                });
            }]
        })
        .state('grade.edit', {
            parent: 'grade',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/grade/grade-dialog.html',
                    controller: 'GradeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Grade', function(Grade) {
                            return Grade.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('grade', null, { reload: 'grade' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('grade.delete', {
            parent: 'grade',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/grade/grade-delete-dialog.html',
                    controller: 'GradeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Grade', function(Grade) {
                            return Grade.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('grade', null, { reload: 'grade' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
