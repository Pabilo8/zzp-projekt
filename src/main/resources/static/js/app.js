var app = angular.module('taskManagerApp', []);

app.controller('CategoryController', function($scope, $http, $timeout) {
    // Dane zostały już wstrzyknięte przez Thymeleaf

    // Funkcja filtrująca kategorie
    $scope.filterCategories = function(searchText) {
        if (!searchText) return $scope.categories;
        return $scope.categories.filter(function(category) {
            return category.name.toLowerCase().includes(searchText.toLowerCase());
        });
    };

    // Inicjalizacja obiektu nowej kategorii
    $scope.newCategory = '';
    $scope.editCategoryId = '';
    $scope.editCategoryName = '';

    var editModal;

    // Inicjalizacja modala po załadowaniu DOM z timeoutem
    $timeout(function() {
        editModal = new bootstrap.Modal(document.getElementById('editModal'));
    }, 500);

    // Funkcja otwierająca modal edycji
    $scope.openEditModal = function(category) {
        $scope.editCategoryId = category.id;
        $scope.editCategoryName = category.name;

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
        event.preventDefault();
        var form = event.target;
        form.action = form.action + $scope.editCategoryId;
        form.submit();
    };
});