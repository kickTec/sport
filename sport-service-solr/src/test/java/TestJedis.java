import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;

@ContextConfiguration(locations={"classpath:spring-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestJedis {
    private final static Logger logger = LoggerFactory.getLogger(TestJedis.class);

    @Resource
    private JedisCluster jedisCluster;

    @Test
    public void testSolrCloud(){
        try {
            String ret = jedisCluster.set("c", "spring-cluster-redis");
            logger.info("ret:{}",ret);
            String ret1 = jedisCluster.get("c");
            logger.info("ret1:{}",ret1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}