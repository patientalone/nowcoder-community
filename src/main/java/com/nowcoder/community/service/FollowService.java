package com.nowcoder.community.service;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.RedisKeyUtil;
import org.apache.tomcat.util.buf.UEncoder;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class FollowService implements CommunityConstant  {
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserService userService;

    public void follow(int userId,int entityType,int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey= RedisKeyUtil.getFolloweeKey(userId,entityType);
                String followerKey=RedisKeyUtil.getFollwerKey(entityType,entityId);

                operations.multi();
                operations.opsForZSet().add(followeeKey,entityId,System.currentTimeMillis());
                operations.opsForZSet().add(followerKey,userId,System.currentTimeMillis());

                return operations.exec();
            }
        });
    }
    public void unfollow(int userId,int entityType,int entityId){
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String followeeKey= RedisKeyUtil.getFolloweeKey(userId,entityType);
                String followerKey=RedisKeyUtil.getFollwerKey(entityType,entityId);

                operations.multi();
                operations.opsForZSet().remove(followeeKey,entityId);
                operations.opsForZSet().remove(followerKey,userId);

                return operations.exec();
            }
        });
    }

    //查询关注的实体的数量
    public long findFolloweeCount(int userId,int entityType){
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    //查询实体粉丝的数量
    public long findFollowerCount(int entityType,int entityId){
        String followerKey=RedisKeyUtil.getFollwerKey(entityType,entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    //查询用户是否关注这个实体
    public boolean hasFollowed(int userId,int entityType,int entityId){
        String followeeKey=RedisKeyUtil.getFolloweeKey(userId,entityType);
        return redisTemplate.opsForZSet().score(followeeKey,entityId)!=null;
    }

    //查询某用户关注的人
    public List<Map<String,Object>> findFollowees(int userID,int offset,int limit){
        String followeekey=RedisKeyUtil.getFolloweeKey(userID,ENTITY_TYPE_User);
        Set<Integer> targetIds=redisTemplate.opsForZSet().reverseRange(followeekey,offset,offset+limit-1);
        if(targetIds==null){
            return null;
        }
        List<Map<String,Object>> list=new ArrayList<>();
        for(Integer targetId:targetIds){
            Map<String,Object> map=new HashMap<>();
            User user=userService.findUserById(targetId);
            map.put("user",user);
            Double score=redisTemplate.opsForZSet().score(followeekey,targetId);
            map.put("followTime",new Date(score.longValue()));
            list.add(map);
        }
        return list;
    }
    //查询某用户的粉丝
    public List<Map<String,Object>> findFollowers(int userID,int offset,int limit){
        String followerkey=RedisKeyUtil.getFollwerKey(ENTITY_TYPE_User,userID);
        Set<Integer> targetIds=redisTemplate.opsForZSet().reverseRange(followerkey,offset,offset+limit-1);
        if(targetIds==null){
            return null;
        }
        List<Map<String,Object>> list=new ArrayList<>();
        for(Integer targetId:targetIds){
            Map<String,Object> map=new HashMap<>();
            User user=userService.findUserById(targetId);
            map.put("user",user);
            Double score=redisTemplate.opsForZSet().score(followerkey,targetId);
            map.put("followTime",new Date(score.longValue()));
            list.add(map);
        }
        return list;
    }

}
