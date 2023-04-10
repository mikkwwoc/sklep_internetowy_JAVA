import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Menu.welcome();
        Scanner in = new Scanner(System.in);
        Scanner in1 = new Scanner(System.in);
        int menuOpt = 0;
        Order.createDirectoryIfNotExists();
        LoadItems.loadItemsToBuy();     //wczytywanie bazy danych

        while (menuOpt != 4) {          //MENU GŁÓWNE
            int whichItem = -1;
            Menu.menuStart();
            menuOpt = in.nextInt();     //WYBÓR OPCJI W MENU GŁÓWNYM

            if (menuOpt == 1) {             //1. STWÓRZ NOWE ZAMÓWIENIE
                while (whichItem != 4) {        //MENU TWORZENIA ZAMÓWIEŃ
                    Menu.menuNewOrder();
                    whichItem = in.nextInt();
                    Order.getOrderId();

                    if (whichItem == 1) {
                        Menu.menuSortOption();      //WYBOR SORTOWANIA PRODUKTOW
                        int sortOpt = in1.nextInt();
                        if (sortOpt == 0) {
                            break;
                        } else if (sortOpt > 4 || sortOpt < 0) {
                            System.out.println("Nie ma takiej opcji");
                            break;
                        }
                        //TWORZENIE ZAMÓWIENIA
                        while (true) {
                            Menu.menuShowProducts(sortOpt);

                            whichItem = in.nextInt();
                            if (whichItem == 0) {  //WYJŚCIE DO MENU TWORZENIA ZAMÓWIEŃ
                                break;
                            } else if (whichItem < 0 || whichItem > LoadItems.sizeOfStoreItems()) {
                                System.out.println("Podano błędne dane");
                            } else {
                                System.out.print("Ile sztuk chcesz zakupić: ");
                                int howMuch = in.nextInt();
                                if (howMuch > 0) {
                                    System.out.println(Order.onePositionInOrderInfo(whichItem - 1, howMuch));
                                    Order.orderAdd(whichItem - 1, howMuch);
                                } else {
                                    System.out.println("Liczba musi być większa od 0.");
                                }
                            }
                        }
                    } else if (whichItem == 2) {        //WYSZUKIWANIE PRODUKTU
                        System.out.println("Wyszukaj produkt: ");
                        String itemName = "";
                        itemName = in1.nextLine();
                        if (!itemName.equals("")) {
                            LoadItems.searchItem(itemName);
                        } else {
                            System.out.println("Nie podano żadnych danych.");
                        }
                    } else if (whichItem == 3) {        //WYJŚCIE DO MENU GŁÓWNEGO
                        System.out.println(Order.allPositionsInOrderInfo());
                        if (!Order.isOrderEmpty()) {
                            System.out.println("Zakończono zakupy! Twoje zamówienie ma id: " + Order.getOrderId());
                            Order.makeOrderFile();
                            Order.clearTempData();
                        }
                        break;
                    } else {    //NIE MA TAKIEJ OPCJI
                        System.out.println("Podano błędne dane");
                    }
                }
            } else if (menuOpt == 2) {      //2. ZOBACZ ZAMÓWIENIE
                Menu.menuCheckOrder();
                if (Order.getOrderFiles().length==0) {
                    System.out.println("Nie ma żadnych zamówień");
                    continue;
                } else {
                    System.out.print("Wybierz zamówienie: ");
                    int whichOrder = in.nextInt();
                    Order.readOrder(whichOrder);
                }
            } else if (menuOpt == 3) {      //3. ZMIEŃ ZAMÓWIENIE
                Menu.menuModifyOrder();
                int whichOrder;
                if (Order.getOrderFiles().length==0) {
                    System.out.println("Nie ma żadnych zamówień");
                    continue;
                } else {
                    System.out.print("Wybierz zamówienie: ");
                    whichOrder = in.nextInt();
                }
                if (!Order.isOrderCorrect(whichOrder)) {
                    System.out.println("Nie ma takiego zamówienia");
                    continue;
                }
                int whichModifyOpt = 0;     //ZMIENNA DO MENU MODYFIKACJI
                while (whichModifyOpt != 5) {
                    Menu.menuModifyOrderOptions();
                    whichModifyOpt = in.nextInt();
                    if (whichModifyOpt == 1) {      //POZYCJA DO USUNIECIA
                        ModifyOrder.loadOrderData(whichOrder);
                        Order.readOrder(whichOrder);
                        System.out.print("Wybierz id pozycji, którą chcesz usunąć: ");
                        int whichPosition = in.nextInt();
                        if (whichPosition - 1 >= ModifyOrder.howMuchPositions() || whichPosition <= 0) {
                            System.out.println("Nie ma takiej pozycji");
                            break;
                        }
                        if (ModifyOrder.onlyOnePosition()) {      //jesli jest tylko jedna pozycja to program pyta czy usunąć zamówienie
                            System.out.println("W tym zamówieniu jest tylko jedna pozycja, czy chcesz usunąć zamówienie?\n1. TAK\n2. NIE");
                            System.out.print("Wybierz opcję: ");
                            int choice = in.nextInt();
                            if (choice == 1) {
                                ModifyOrder.deleteOrder(whichOrder);
                            } else {
                                break;
                            }
                        } else {
                            ModifyOrder.deletePosition(whichPosition - 1);
                            ModifyOrder.updateOrderFile(whichOrder);
                        }
                        break;
                    } else if (whichModifyOpt == 2) { //ZMIANA ILOSCI PRODUKTU
                        ModifyOrder.loadOrderData(whichOrder);
                        Order.readOrder(whichOrder);
                        System.out.print("Wybierz id pozycji, w której chcesz zmienić ilość produktu: ");
                        int whichPosition = in.nextInt();
                        if (whichPosition - 1 >= ModifyOrder.howMuchPositions() || whichPosition < 1) {
                            System.out.println("Nie ma takiej pozycji");
                            break;
                        }
                        System.out.print("Wybierz ilość: ");
                        int howMuch = in.nextInt();
                        ModifyOrder.changeAmount(whichPosition - 1, howMuch);
                        ModifyOrder.updateOrderFile(whichOrder);

                    } else if (whichModifyOpt == 3) {  //DODAWANIE PRODUKTU DO ZAMOWIENIA
                        ModifyOrder.loadOrderData(whichOrder);
                        Order.readOrder(whichOrder);
                        Menu.menuShowProducts(1);
                        whichItem = in.nextInt();
                        if (whichItem == 0) {
                            break;
                        } else if (whichItem < 0 || whichItem > LoadItems.sizeOfStoreItems()) {
                            System.out.println("Podano błędne dane");
                        } else {
                            System.out.print("Ile sztuk chcesz zakupić: ");
                            int howMuch = in.nextInt();
                            if(howMuch>0) {
                                System.out.println(Order.onePositionInOrderInfo(whichItem - 1, howMuch));
                                ModifyOrder.addItem(whichItem - 1, howMuch);
                                ModifyOrder.updateOrderFile(whichOrder);
                            }else{
                                System.out.println("Ilość musi być większa od 0");
                                break;
                            }
                        }
                    } else if (whichModifyOpt == 4) {  //USUWANIE PRODUKTU
                        ModifyOrder.deleteOrder(whichOrder);
                        break;
                    }
                }
            }
        }
    }
}