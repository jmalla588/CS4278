(function() {
    'use strict';

    angular
        .module('myapplicationApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('preferred-contact', {
            parent: 'entity',
            url: '/preferred-contact',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PreferredContacts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/preferred-contact/preferred-contacts.html',
                    controller: 'PreferredContactController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('preferred-contact-detail', {
            parent: 'entity',
            url: '/preferred-contact/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PreferredContact'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/preferred-contact/preferred-contact-detail.html',
                    controller: 'PreferredContactDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PreferredContact', function($stateParams, PreferredContact) {
                    return PreferredContact.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'preferred-contact',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('preferred-contact-detail.edit', {
            parent: 'preferred-contact-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/preferred-contact/preferred-contact-dialog.html',
                    controller: 'PreferredContactDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PreferredContact', function(PreferredContact) {
                            return PreferredContact.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('preferred-contact.new', {
            parent: 'preferred-contact',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/preferred-contact/preferred-contact-dialog.html',
                    controller: 'PreferredContactDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nameOfChoice: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('preferred-contact', null, { reload: 'preferred-contact' });
                }, function() {
                    $state.go('preferred-contact');
                });
            }]
        })
        .state('preferred-contact.edit', {
            parent: 'preferred-contact',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/preferred-contact/preferred-contact-dialog.html',
                    controller: 'PreferredContactDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PreferredContact', function(PreferredContact) {
                            return PreferredContact.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('preferred-contact', null, { reload: 'preferred-contact' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('preferred-contact.delete', {
            parent: 'preferred-contact',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/preferred-contact/preferred-contact-delete-dialog.html',
                    controller: 'PreferredContactDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PreferredContact', function(PreferredContact) {
                            return PreferredContact.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('preferred-contact', null, { reload: 'preferred-contact' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
