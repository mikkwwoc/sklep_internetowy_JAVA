import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ModifyOrder {
    static List<String> orderedItems = new ArrayList<>();       //nazwy
    static List<Float> pricesOfItem = new ArrayList<>();        //ceny(1szt)
    static List<Integer> amountOfItems = new ArrayList<>();     //ilosc
    static List<Float> pricesOfPosition = new ArrayList<>();    //ceny(calosc)

    static void loadOrderData(int whichOrder) throws IOException {    //wczytanie danych z zamowienia do list w celu ich modyfikacji
        clearData();
        String file = "order" + whichOrder + ".dat";
        RandomAccessFile RAF = null;
        try {
            RAF = new RandomAccessFile("orders/" + file, "r");
            boolean endOfFile=false;
            while (true) {
                orderedItems.add(RAF.readUTF());
                pricesOfItem.add(RAF.readFloat());
                amountOfItems.add(RAF.readInt());
                pricesOfPosition.add(RAF.readFloat());
            }
        } catch (EOFException | FileNotFoundException e) {
            System.out.println("Wczytano dane pomyślnie");
        } finally {
            if (RAF != null) {
                RAF.close();
            }
        }
    }
    static int howMuchPositions(){      //ile pozycji
        return orderedItems.size();
    }
    static boolean onlyOnePosition(){       //true jesli jest tylko jedna pozycja
        return orderedItems.size() == 1;
    }
    static void deletePosition(int whichPosition){      //usuwanie pozycji
        orderedItems.remove(whichPosition);
        pricesOfPosition.remove(whichPosition);
        amountOfItems.remove(whichPosition);
        pricesOfItem.remove(whichPosition);
    }
    static void clearData(){        //czyszczenie danych z list aby mozna bylo wczytac dane nastepnego zamowienienia
        orderedItems.clear();
        pricesOfPosition.clear();
        amountOfItems.clear();
        pricesOfItem.clear();
    }
    static void updateOrderFile(int whichOrder) throws IOException {    //nadpisuje plik juz ze zmienionymi danymi
        deleteOrder(whichOrder);
        saveUpdateOrderFile(whichOrder);
        clearData();
    }
    public static void saveUpdateOrderFile(int whichOrder) throws IOException {     //ZAPISYWANIE DO .DAT
        String fileName = "order"+whichOrder+".dat";
        String path="orders/";
        RandomAccessFile RAF = new RandomAccessFile((path+fileName),"rw");
        int counter=1;
        for (String orderedItem : orderedItems) {
            RAF.writeUTF(orderedItem);                          //nazwa
            RAF.writeFloat(pricesOfItem.get(counter-1));        //cena pojedyncza
            RAF.writeInt(amountOfItems.get(counter-1));         //ilosc
            RAF.writeFloat(pricesOfPosition.get(counter-1));    //cena pozycji
            counter++;
        }
        RAF.close();
    }
    static void changeAmount(int whichPosition, int howMuch){       //zmiana ilosci produktu
        if(howMuch<1){
            System.out.println("Ilość musi być większa od 0");
        }else{
            amountOfItems.set(whichPosition, howMuch);
            pricesOfPosition.set(whichPosition, Float.valueOf((amountOfItems.get(whichPosition)))*pricesOfItem.get(whichPosition)); //nowa cena
        }

    }

    static void deleteOrder(int whichOrder){        //usuwanie zamowienia
        Path path = FileSystems.getDefault().getPath("orders/order" + whichOrder+".dat");
        try {
            Files.delete(path);
        } catch (NoSuchFileException x) {
            System.err.format("%s: nie ma takiego" + " pliku lub katalogu%n", path);
        } catch (IOException x) {
            System.err.println(x);
        }
    }


    public static void addItem(int whichItem, int howMuch) {    //dodawanie przedmiotu do zamowienia
        orderedItems.add(LoadItems.getItemName(whichItem));
        pricesOfPosition.add(LoadItems.getItemPrice(whichItem)*howMuch);
        pricesOfItem.add(LoadItems.getItemPrice(whichItem));
        amountOfItems.add(howMuch);
    }
}
