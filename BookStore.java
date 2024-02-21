import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BookStore extends ReadingListItemStore {

    public BookStore() {
        super();
    }

    public BookStore(String filename) throws IOException {
        super();
        try (BufferedReader reader = Files.newBufferedReader(Path.of(filename))) {
            List<String> items = reader.lines()
                                        .map(String::toLowerCase)
                                        .collect(Collectors.toList());
            for (String item : items) {
                String[] parts = item.split(" ");
                String key = parts[0].substring(0, 1).toUpperCase();
                String value = parts[0] + " by " + parts[1];
                int year = Integer.parseInt(parts[2]);
                if (year >= 1990) {
                    value += " (contemporary)";
                } else if (year >= 1900) {
                    value += " (modern)";
                } else {
                    value += " (classic)";
                }
                put(key, value);
            }
        } catch (IOException e) {
            throw new IOException("Failed to load file: " + filename, e);
        }
    }

    @Override
    public String getRandomItem(String key) {
        List<String> items = map.get(key.toUpperCase());
        if (items != null) {
            // get a random item from the list
            String item = items.get((int) (Math.random() * items.size()));
    
            // split the item into title/author and publication year
            String[] parts = item.split("\\s+");
            String titleAuthor = String.join(" ", Arrays.copyOfRange(parts, 0, parts.length - 1));
            int year = Integer.parseInt(parts[parts.length - 1]);
    
            // determine the category of the book
            String category;
            if (year >= 1990) {
                category = "(contemporary)";
            } else if (year >= 1900) {
                category = "(modern)";
            } else {
                category = "(classic)";
            }
    
            // return the title/author with the category appended
            return titleAuthor + " " + category;
        } else {
            return null;
        }
    }

    @Override
    public void put(String key, String item) {
        // Convert the key to uppercase
        key = key.toUpperCase();
        // Add the item to the list
        List<String> itemList = map.getOrDefault(key, new ArrayList<>());
        itemList.add(item);
        map.put(key, itemList);
    }

}
