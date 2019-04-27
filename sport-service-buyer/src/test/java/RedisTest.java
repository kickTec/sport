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

    @Test
    public void testJedisSet(){
        String code = jedis.set("testKey", "testValue");
        System.out.println(code);
        String testValue = jedis.get("testKey");
        System.out.println(testValue);
    }
}
