import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ReadingListItemStore {

    protected final Map<String, List<String>> map;

    public ReadingListItemStore() {
        this.map = new HashMap<>();
    }

    public ReadingListItemStore(String fileName) throws IOException {
        this.map = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String item = line.toLowerCase();
                String key = item.substring(0, 1);
                if (!map.containsKey(key)) {
                    map.put(key, new ArrayList<>());
                }
                map.get(key).add(item);
            }
        } finally {
            reader.close();
        }
    }

    public boolean containsKey(String key) {
        return map.containsKey(key.toLowerCase());
    }

    public void put(String key, String item) {
        key = key.toLowerCase();
        item = item.toLowerCase();
        if (!map.containsKey(key)) {
            map.put(key, new ArrayList<>());
        }
        map.get(key).add(item);
    }

    public String getRandomItem(String key) {
        key = key.toLowerCase();
        List<String> items = map.get(key);
        if (items == null || items.isEmpty()) {
            return null;
        }
        String item = items.get(new Random().nextInt(items.size()));
        return key.toUpperCase() + item.substring(1);
    }
}
