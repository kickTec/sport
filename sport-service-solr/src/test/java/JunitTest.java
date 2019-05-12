import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.ConcurrentHashMap;

@RunWith(JUnit4.class)
public class JunitTest {
    @Test
    public void testSplit(){
        String price1 = "10-20";
        String price2 = "30";
        String[] price1Array = price1.split("-");
        String[] price2Array = price2.split("-");
        Assert.assertEquals("价格中包含-",price1Array[0],"10");
        Assert.assertEquals("价格中不包含-",price2Array[0],"30");
    }

    @Test
    public void testConcurrentHashMap(){
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("id","007");
    }
}