var app = angular.module('taskManagerApp', []);

app.controller('CategoryController', function($scope, $http, $timeout) {
    // Dane zostały już wstrzyknięte przez Thymeleaf

    // Inicjalizacja obiektu nowej kategorii
    $scope.newCategory = '';
    $scope.editCategoryId = '';
    $scope.editCategoryName = '';

    // Zmienne do kontrolowania komunikatów o duplikatach
    $scope.isDuplicateAdd = false;
    $scope.isDuplicateEdit = false;

    var editModal;

    // Inicjalizacja modala po załadowaniu DOM z timeoutem
    $timeout(function() {
        editModal = new bootstrap.Modal(document.getElementById('editModal'));
    }, 500);

    // Funkcja sprawdzająca czy nazwa kategorii już istnieje
    $scope.checkDuplicateName = function(name, excludeId) {
        if (!name) return false;

        return $scope.categories.some(function(category) {
            // Przy edycji pomijamy kategorię, którą edytujemy
            if (excludeId && category.id === excludeId) return false;
            return category.name.toLowerCase() === name.toLowerCase();
        });
    };

    // Sprawdzanie duplikatu przy dodawaniu
    $scope.checkAddDuplicate = function() {
        $scope.isDuplicateAdd = $scope.checkDuplicateName($scope.newCategory);
    };

    // Sprawdzanie duplikatu przy edycji
    $scope.checkEditDuplicate = function() {
        $scope.isDuplicateEdit = $scope.checkDuplicateName($scope.editCategoryName, $scope.editCategoryId);
    };

    // Funkcja otwierająca modal edycji
    $scope.openEditModal = function(category) {
        $scope.editCategoryId = category.id;
        $scope.editCategoryName = category.name;
        $scope.isDuplicateEdit = false;

        $timeout(function() {
            editModal.show();
        });
    };

    // Funkcja obsługująca formularz usuwania
    $scope.deleteCategory = function(event, category) {
        event.preventDefault();
        var form = event.target;
        form.action = form.action + category.id;
        form.submit();
    };

    // Funkcja obsługująca formularz edycji
    $scope.updateCategory = function(event) {
        if ($scope.isDuplicateEdit) {
            event.preventDefault();
            return;
        }

        event.preventDefault();
        var form = event.target;
        form.action = form.action + $scope.editCategoryId;
        form.submit();
    };
});