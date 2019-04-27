package com.kenick.ad;

import com.kenick.sport.pojo.ad.Position;
import com.kenick.sport.service.product.AdService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

@ContextConfiguration(locations={"classpath:spring-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class AdTest {
    @Resource
    private AdService adService;

    @Test
    public void testSelectPosition(){
        List<Position> positions = adService.selectPositionListByParentId(30L);
        System.out.println(positions);
    }

    @Test
    public void testTransferAdToJson(){
        String json = adService.selectAdListByPositionIdForPortal(89L);
        System.out.println(json);
    }
}
