class Menu {
    static String pattern = "================================";

    static void welcome(){
        System.out.println("================================");
        System.out.println("| Witaj w sklepie internetowym |");
        System.out.println("================================\n");
    }
    static void menuStart() {
        System.out.println(pattern);
        System.out.println("1. Stwórz nowe zamówienie");
        System.out.println("2. Zobacz zamówienie");
        System.out.println("3. Zmień zamówienie");
        System.out.println("4. Wyjście");
        System.out.print("Wybierz opcje: ");
    }

    static void menuNewOrder() {
        System.out.println(pattern);
        System.out.println("1. PRZEGLĄDAJ PRODUKTY");
        System.out.println("2. WYSZUKAJ PRODUKT");
        System.out.println("3. ZAKOŃCZ ZAMÓWIENIE");
        System.out.print("Wybierz opcję: ");
    }

    static void menuShowProducts(int sortOpt) {
        System.out.println(pattern);
        System.out.println("ID | Wybierz produkt, który chcesz zakupić:");
        if (sortOpt == 1) {
            LoadItems.showItemsToBuyByID();
        } else if (sortOpt == 2) {
            LoadItems.showItemsToBuyByDescendingPrice();
        } else if (sortOpt == 3) {
            LoadItems.showItemsToBuyByAscendingPrice();
        } else if (sortOpt == 4) {
            LoadItems.showItemsToBuyByAlphabet();
        }
        System.out.println("0. WRÓĆ");
        System.out.print("Wybierz produkt (ID): ");
    }

    static void menuSortOption() {
        System.out.println(pattern);
        System.out.println("1. Sortowanie według ID");
        System.out.println("2. Sortowanie według ceny (rosnąco)");
        System.out.println("3. Sortowanie według ceny (malejąco)");
        System.out.println("4. Sortowanie alfabetyczne");
        System.out.println("0. WRÓĆ");
        System.out.print("Przegladaj produkty według: ");
    }

    static void menuCheckOrder() {
        System.out.println(pattern);
        System.out.println("Które zamówienie chcesz zobaczyć (numer zamówienia):");
        Order.showOrdersFiles();
    }

    static void menuModifyOrder() {
        System.out.println(pattern);
        System.out.println("Które zamówienie chcesz zmodyfikować (numer zamówienia):");
        Order.showOrdersFiles();
    }

    static void menuModifyOrderOptions() {
        System.out.println(pattern);
        System.out.println("1. Usuń pozycję w zamówieniu");
        System.out.println("2. Zmień ilość");
        System.out.println("3. Dodaj pozycję");
        System.out.println("4. Usuń zamówienie");
        System.out.println("5. Wyjście");
        System.out.print("Wybierz opcję: ");
    }
}
