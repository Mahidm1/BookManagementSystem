import java.io.IOException;

public class ReadingListItemStoreTest {

    public static void main(String[] args) {

        ReadingListItemStore store = new ReadingListItemStore();

        // Add mappings
        store.put("a", "Allen Ginsburg");
        store.put("be", "Ben Jonson");
        store.put("be", "Beryl Bainbridge");
        store.put("a", "Anne Brontë");

        // Test containsKey method
        assert store.containsKey("a");
        assert store.containsKey("be");
        assert !store.containsKey("c");

        // Test getRandomItem method
        assert store.getRandomItem("a").matches("[Aa].*");
        assert store.getRandomItem("A").matches("[Aa].*");
        assert store.getRandomItem("be").matches("[Bb][e].*");
        assert store.getRandomItem("BE").matches("[Bb][e].*");
        assert store.getRandomItem("c") == null;

        // Test constructor with file
        ReadingListItemStore storeFromFile = null;
        try {
            storeFromFile = new ReadingListItemStore("items.txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        assert storeFromFile.getRandomItem("a").matches("[Aa].*");
        assert storeFromFile.getRandomItem("z").matches("[Zz].*");
        assert storeFromFile.getRandomItem("b").matches("[Bb].*");
        assert storeFromFile.getRandomItem("c") == null;
    }
}
