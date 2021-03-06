/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package hash;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.util.ArrayList;

public class ConsistentHashTest {

    @Test public void test_murmur3_128(){
        System.out.println(String.format("hash算法:%s", "murmur3_128"));
        testWithHf(Hashing.murmur3_128());
    }

    @Test public void test_sipHash24(){
        System.out.println(String.format("hash算法:%s", "sipHash24"));
        testWithHf(Hashing.sipHash24());
    }

    @Test public void test_md5(){
        System.out.println(String.format("hash算法:%s", "md5"));
        testWithHf(Hashing.md5());
    }

    @Test public void test_sha1(){
        System.out.println(String.format("hash算法:%s", "sha1"));
        testWithHf(Hashing.sha1());
    }

    @Test public void test_sha256(){
        System.out.println(String.format("hash算法:%s", "sha256"));
        testWithHf(Hashing.sha256());
    }

    @Test public void test_sha384(){
        System.out.println(String.format("hash算法:%s", "sha384"));
        testWithHf(Hashing.sha384());
    }

    @Test public void test_sha512(){
        System.out.println(String.format("hash算法:%s", "sha512"));
        testWithHf(Hashing.sha512());
    }

    @Test public void test_farmHashFingerprint64(){
        System.out.println(String.format("hash算法:%s", "farmHashFingerprint64"));
        testWithHf(Hashing.farmHashFingerprint64());
    }

    /*
    testWithHf(Hashing.sha1());
    testWithHf(Hashing.sha256());
    testWithHf(Hashing.sha384());
    testWithHf(Hashing.sha512());
    //testWithHf(Hashing.crc32c());
    testWithHf(Hashing.crc32());
    testWithHf(Hashing.adler32());
    testWithHf(Hashing.farmHashFingerprint64());
    */

    private void testWithHf(HashFunction hashFunction){
        // 十台主机
        ArrayList<String> nodes = new ArrayList<String>();
        nodes.add("Node-A");
        nodes.add("Node-B");
        nodes.add("Node-C");
        nodes.add("Node-D");
        nodes.add("Node-E");
        nodes.add("Node-F");
        nodes.add("Node-G");
        nodes.add("Node-H");
        nodes.add("Node-I");
        nodes.add("Node-J");
        ConsistentHash<String> consistentHashing =
            new ConsistentHash(hashFunction, 150, nodes);
        // 100W数据
        Map<String, Integer> stats = new HashMap<>();
        for (String node: nodes){
            stats.put(node, 0);
        }
        for (int i = 0; i < 1000000; i++) {
            String key = "KEY:" + i;
            String node = consistentHashing.get(key);
            stats.put(node, stats.get(node)+1);
        }
        //输出值
        for(Map.Entry<String, Integer> entry : stats.entrySet()){
            String mapKey = entry.getKey();
            Integer mapValue = entry.getValue();
            System.out.println(mapKey+":"+mapValue);
        }
        //标准差
        Collection<Integer> collection = stats.values();
        Integer[] arr = new Integer[collection.size()];
        arr = collection.toArray(arr);

        System.out.println(String.format("标准差是:%f", std(arr)));
    }

    private double std(Integer[] arr){
        int len = arr.length;
        double sum = 0;
        for (int i = 0; i < len; i++) {
            sum += arr[i];
        }
        double avg = sum / len;
        double std = 0;
        for (int i = 0; i < len; i++) {
            std += (arr[i] - avg) * (arr[i] - avg);
        }
        return Math.sqrt(std / len);
    }
}
