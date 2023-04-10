import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Order {
    static int orderId;
    static List<String> orderedItems = new ArrayList<>();       //nazwy
    static List<Float> pricesOfItem = new ArrayList<>();        //ceny(1szt)
    static List<Integer> amountOfItems = new ArrayList<>();     //ilosc
    static List<Float> pricesOfPosition = new ArrayList<>();    //ceny(calosc)

    public static String[] getOrderFiles() {        //wczytanie nazw plikow w folderze /orders do tablicy
        File file = new File("orders");
        return file.list();
    }
    public static void createDirectoryIfNotExists() throws IOException {      //stworzenie folderu orders jesli nie istnieje
        try{
            Path path = Paths.get("orders");
            Files.createDirectory(path);
        }catch(IOException ignored){}
    }

    public static int getOrderId() {               //sprawdzanie id zamowien i nadawanie id nastepnego zamowienia
        String[] fileList = getOrderFiles();
        int idCounter = 1;
        String checkName = "order";
        if (fileList == null) {
            System.out.println("Nie ma żadnych produktów");
        } else {
            for (String name : fileList) {
                if (!(checkName + idCounter + ".dat").equals(name)) {
                    break;
                }
                idCounter++;
                //      System.out.println(name);
            }
        }
        orderId = idCounter;
        return orderId;
    }

    public static void showOrdersFiles() {      //listowanie plikow zamowien w folderze orders
        String[] fileList = getOrderFiles();
        if (fileList == null) {
            System.out.println("Nie ma żadnych zamówień");
        } else {
            for (String file : fileList) {
                System.out.println("|--> " + file);
            }
        }
    }

    public static boolean isOrderCorrect(int whichOrder) {      //sprwadzanie czy takie zamowienie istnieje
        String[] fileList = getOrderFiles();
        String fileName = "order" + whichOrder + ".dat";
        boolean exist = false;
        for (String file : fileList) {
            //  System.out.println(file);
            if (fileName.equals(file)) {
                exist = true;
            }
        }
        return exist;
    }

    public static void orderAdd(int whichItem, int howMuch) {       //dodawanie przedmiotu do "koszyka"
        orderedItems.add(LoadItems.getItemName(whichItem));
        pricesOfPosition.add(positionPrice(whichItem, howMuch));
        pricesOfItem.add(LoadItems.getItemPrice(whichItem));
        amountOfItems.add(howMuch);
    }

    public static float positionPrice(int whichItem, int howMuch) {     //wyliczanie calkowitej ceny cena (1szt) * ilosc
        return LoadItems.getItemPrice(whichItem) * howMuch;
    }

    public static String onePositionInOrderInfo(int whichItem, int howMuch) {       //pokazywanie pozycji dodanej do koszyka
        return "Nazwa: " + LoadItems.getItemName(whichItem) + " | cena: "
                + LoadItems.getItemPrice(whichItem) + " | ilość: " + howMuch + " | cena całkowita: "
                + positionPrice(whichItem, howMuch);
    }

    public static String allPositionsInOrderInfo() {        //pokazywanie wszystkich pozycji w zamowieniu
        String info = "ID  |  NAZWA  |  CENA  |  ILOŚĆ  |  CENA CAŁKOWITA";
        StringBuilder sB = new StringBuilder(info);
        int counter = 1;
        for (String orderedItem : orderedItems) {
            sB.append("\r\n" + counter + "  |  " + orderedItem + "  |  " + pricesOfItem.get(counter - 1) +
                    "  |  " + amountOfItems.get(counter - 1) + "  |  " + pricesOfPosition.get(counter - 1));
            counter++;
        }
        info = sB.toString();
        if (counter == 1) {
            info = "Nie zamówiłeś żadnego produktu";
        }
        return info;
    }
    // ZAPISYWANIE DO .TXT
//    public static void makeOrderFile() throws IOException {
//        String fileName = "order"+getOrderId()+".txt";
//        File orderFile = new File(fileName);
//        String path="C:/Users/wwojc/Documents/JAVA/projekt/sklep_internetowy/orders/";
//        RandomAccessFile RAF = new RandomAccessFile((path+fileName),"rw");
//
//        RAF.write(allPositionsInOrderInfo().getBytes());
//        RAF.close();
//    }
    public static void makeOrderFile() throws IOException {         //ZAPISYWANIE DO .DAT
        String fileName = "order" + getOrderId() + ".dat";
        String path = "orders/";
        RandomAccessFile RAF = new RandomAccessFile((path + fileName), "rw");
        int counter = 1;
        for (String orderedItem : orderedItems) {
            RAF.writeUTF(orderedItem);                              //nazwa
            RAF.writeFloat(pricesOfItem.get(counter - 1));          //cena pojedyncza
            RAF.writeInt(amountOfItems.get(counter - 1));           //ilosc
            RAF.writeFloat(pricesOfPosition.get(counter - 1));       //cena pozycji
            counter++;
        }
        RAF.close();
    }

    public static void clearTempData() {        //usuwanie tymczasowych danych po zakonczeniu zamowienia aby produkty w zamowieniach sie nie dublowały
        orderedItems.clear();
        pricesOfPosition.clear();
        amountOfItems.clear();
        pricesOfItem.clear();
    }

    public static boolean isOrderEmpty() {      //sprawdzanie czy zamowienie jest puste
        return orderedItems == null || orderedItems.isEmpty();
    }

    //    OTWIERANIE PLIKU .TXT
//    public static void readOrder(int whichOrder) throws IOException{
//        BufferedReader br = null;
//        String file="order"+whichOrder+".txt";
//        try {
//            br = new BufferedReader(new FileReader("C:/Users/wwojc/Documents/JAVA/projekt/sklep_internetowy/orders/"+file));
//            String line;
//
//            while ((line = br.readLine()) != null) {
//                System.out.println(line);
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("Baza danych nie istnieje");
//        } finally {
//            if (br != null) {
//                br.close();
//            }
//        }
//    }
    public static void readOrder(int whichOrder) throws IOException {  //wczytanie zamowienia z pliku
        String file = "order" + whichOrder + ".dat";
        String nameOfItem;
        float priceOfItem;
        int amountOfItems;
        float priceOfPosition;
        RandomAccessFile RAF = null;
        try {
            RAF = new RandomAccessFile("orders/" + file, "r");
            String info = "ID  |  NAZWA  |  CENA  |  ILOŚĆ  |  CENA CAŁKOWITA";
            System.out.println(info);
            int counter = 1;
            while (true) {
                nameOfItem = RAF.readUTF();
                priceOfItem = RAF.readFloat();
                amountOfItems = RAF.readInt();
                priceOfPosition = RAF.readFloat();
                System.out.println(counter + "  |  " + nameOfItem + "  |  " + priceOfItem + "  |  "
                        + amountOfItems + "  |  " + priceOfPosition);
                counter++;
            }
        } catch (EOFException e) {
            System.out.println("Koniec zamówienia");
        } catch (FileNotFoundException e) {
            System.out.println("Nie ma takiego zamówienia");
        } finally {
            if (RAF != null) {
                RAF.close();
            }
        }
    }
}
