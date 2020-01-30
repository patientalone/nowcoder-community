package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveWordFilterUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTest {
    @Autowired
    SensitiveWordFilterUtil sensitiveWordFilterUtil;
    @Test
    public void testSensiitveFilter(){
        String text="---赌博--这里可以嫖娼吸毒开票 123123 1啊大苏打___--";
        System.out.println(sensitiveWordFilterUtil.replaceSensitiveWord(text));
    }
}
