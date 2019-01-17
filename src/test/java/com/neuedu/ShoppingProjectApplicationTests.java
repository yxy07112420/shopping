package com.neuedu;

import com.neuedu.utils.BigDecimalUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShoppingProjectApplicationTests {

    @Test
    public void contextLoads() {

        System.out.println(BigDecimalUtils.add(2.123,4.2131));
        System.out.println(BigDecimalUtils.sub(21.2321,4.233));
        System.out.println(BigDecimalUtils.mul(21.23,4.21));
        System.out.println(BigDecimalUtils.div(24.4,4));
    }

}

