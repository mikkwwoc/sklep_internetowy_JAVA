import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LoadItems {
    static List<String> storeItems = new ArrayList<>();     //nazwy
    static List<Float> priceItems = new ArrayList<>();      //ceny

    static void loadItemsToBuy() throws IOException {       //wczytuje dane do sklepu z pliku items.txt
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("src/items.txt"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] words = line.split(","); //zakładamy, że każdy element w bazie danych  jest w nowej linii,
                //a nazwa jest oddzielona od ceny przecinkiem
                storeItems.add(words[0]);
                priceItems.add(Float.valueOf(words[1]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Baza danych nie istnieje");
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    static void showItemsToBuyByID() {       //sortuje po id
        int counter = 1;
        for (float priceItem : priceItems) {
            System.out.println(counter + ". " + storeItems.get(counter - 1) + " cena: " + priceItem);
            counter++;
        }
    }

    static void showItemsToBuyByDescendingPrice() {      //sortuje po cenie (malejaco)
        List<Float> sortedPriceItems = priceItems.stream().sorted().toList();
        int index;
        for (float priceItem : sortedPriceItems) {
            index = priceItems.indexOf(priceItem);
            System.out.println((index + 1) + ". " + storeItems.get(index) + " cena: " + priceItem);
        }
    }

    static void showItemsToBuyByAscendingPrice() {       //sortuje po cenie (rosnąco)
        List<Float> sortedPriceItems = priceItems.stream().sorted(Comparator.reverseOrder()).toList();
        int index;
        for (float priceItem : sortedPriceItems) {
            index = priceItems.indexOf(priceItem);
            System.out.println((index + 1) + ". " + storeItems.get(index) + " cena: " + priceItem);
        }
    }

    static void showItemsToBuyByAlphabet() {     //sortuje alfabetycznie
        List<String> sortedNameItems = storeItems.stream().sorted().toList();
        int index;
        for (String nameItem : sortedNameItems) {
            index = storeItems.indexOf(nameItem);
            System.out.println((index + 1) + ". " + nameItem + " cena: " + priceItems.get(index));
        }
    }

    static String getItemName(int whichItem) {
        return storeItems.get(whichItem);
    }       //zwraca nazwe

    static float getItemPrice(int whichItem) {
        return priceItems.get(whichItem);
    }       //zwraca cene

    static int sizeOfStoreItems() {
        return storeItems.size();
    }       //zwraca ilosc przedmiotow w sklepie

    static void searchItem(String itemName) {    //wyszukiwarka przedmiotow
        int counter = 0;
        for (String storeItem : storeItems) {
            boolean isFound = storeItem.toLowerCase().contains(itemName.toLowerCase());     //toLowerCase() zeby wielkosc liter w szukanym wyrazeniu nie miala znaczenia

            if (isFound) {
                counter++;
                int positionOfProduct = storeItems.indexOf(storeItem) + 1;
                System.out.println("Znaleziono: " + storeItem + " - znajdziesz to na pozycji: " + positionOfProduct);
            }
            isFound = false;
        }
        if (counter == 0) {
            System.out.println("Nie znaleziono takiego produktu");
        }
    }
}
