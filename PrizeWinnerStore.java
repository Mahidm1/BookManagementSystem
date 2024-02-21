import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class PrizeWinnerStore extends ReadingListItemStore {

    // Constructor that calls the super class constructor
    public PrizeWinnerStore() {
        super();
    }

    // Constructor that loads data from a file
    public PrizeWinnerStore(String filename) throws IOException {
        super();
        try (BufferedReader reader = Files.newBufferedReader(Path.of(filename))) {
            // Read all lines from the file and convert them to lowercase
            List<String> items = reader.lines()
                                        .map(String::toLowerCase)
                                        .collect(Collectors.toList());
            // Add each item to the store with a key and value
            for (String item : items) {
                String key = item.substring(0, 1);
                String value = item + " (prize-winner)";
                put(key, value);
            }
        } catch (IOException e) {
            throw new IOException("Failed to load file: " + filename, e);
        }
    }

    // Get a random item from the store with the given key
    // and add "(prize-winner)" to the end of the item
    @Override
    public String getRandomItem(String key) {
        String item = super.getRandomItem(key);
        return (item != null) ? item + " (prize-winner)" : null;
    }
}
