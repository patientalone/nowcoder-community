package com.nowcoder.community;


import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLOutput;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Test
    public  void testSelectUser(){
        User user=userMapper.selectById(101);
        System.out.println(user.toString());

         user=userMapper.selectByName("liubei");
        System.out.println(user.toString());

        user=userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user.toString());
    }
    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }
    @Test
    public void updateUser() {
        int rows = userMapper.updateStatus(150, 1);
        System.out.println(rows);

        rows = userMapper.updateHeader(150, "http://www.nowcoder.com/102.png");
        System.out.println(rows);

        rows = userMapper.updatePassword(150, "hello");
        System.out.println(rows);
    }
    @Test
    public void testSelectPost(){
        List<DiscussPost> list=discussPostMapper.selectDiscussPosts(149,0,10);
        for(DiscussPost discussPost:list){
            System.out.println(discussPost);
        }
        System.out.println(discussPostMapper.selectDiscussPostRows(149));
    }
    @Test
    public void testInsertTicket(){
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*10));
        loginTicket.setTicket("123");
        loginTicketMapper.insertLoginTicket(loginTicket);
    }
    @Test
    public void testSelectTicket(){
        String ticket="123";
        String res=loginTicketMapper.selectByTicket(ticket).toString();
        System.out.println(res);
    }
    @Test
    public void testUpdateTicket(){
        String ticket="123";
        int status=1;
        loginTicketMapper.updateStatus(ticket,status);
    }
    @Test
    public void testInsertDiscussPort(){
        DiscussPost discussPost=new DiscussPost();
        discussPost.setTitle("title");
        discussPost.setCommentCount(0);
        discussPost.setCreateTime(new Date());
        discussPost.setUserId(153);
        discussPost.setScore(10.0);
        discussPost.setType(1);
        discussPost.setStatus(1);
        discussPost.setContent("好好好");
        discussPostMapper.insertDiscussPost(discussPost);
    }
}
