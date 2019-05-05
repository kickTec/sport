import com.kenick.sport.service.buyer.BuyerService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class RedisTest {
    @Resource
    private Jedis jedis;

    @Resource
    private BuyerService buyerService;

    @Test
    public void testJedisHget(){
        String ret = buyerService.redisHget("test", "key1");
        Assert.assertEquals(null,ret);

        buyerService.redisHSet("test2", "key2", "value2");
        String ret2 = buyerService.redisHget("test2", "key2");
        Assert.assertEquals("value2",ret2);
    }

    @Test
    public void testJedis(){
        String setRet = jedis.set("testKey", "testValue");
        System.out.println("setRet:"+setRet);

        String testValue = jedis.get("testKey");
        System.out.println("testValue:"+testValue);
    }

    @Test
    public void testIncr(){
        Long orderId = jedis.incr("orderId");
        System.out.println(orderId);
    }
}
