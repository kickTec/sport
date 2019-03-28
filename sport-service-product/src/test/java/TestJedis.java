import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

@ContextConfiguration(locations={"classpath:spring-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestJedis {
    @Resource Jedis jedis;

    @Test
    public void testJedisSet(){
        String pno = jedis.set("pno", "1000");
        System.out.println(pno);
    }

    @Test
    public void testJedisGet(){
        String pno = jedis.get("pno");
        System.out.println(pno);
    }

    @Test
    public void testJedisIncr(){
        System.out.println(jedis.get("pno"));
        Long pno = jedis.incr("pno");
        System.out.println(pno);
        System.out.println(jedis.get("pno"));
    }
}
