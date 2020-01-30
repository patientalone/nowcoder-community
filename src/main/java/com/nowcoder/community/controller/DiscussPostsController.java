package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostsController {
    @Autowired
    DiscussPostService discussPostService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @RequestMapping(path = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title,String content){
        User user =hostHolder.getUser();
        if(user==null){
            return CommunityUtil.getJsonString(403,"你还没有登录");
        }
        DiscussPost discussPost=new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.insertDiscussPost(discussPost);

        return CommunityUtil.getJsonString(0,"发布成功");
    }

    @RequestMapping(path = "/detail/{discussPostId}",method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model) {
        DiscussPost post=discussPostService.findDiscussPostById(discussPostId);
        User user=userService.findUserById(post.getUserId());
        model.addAttribute("user",user);
        model.addAttribute("post",post);
        return "/site/discuss-detail";
    }
}
