package com.nowcoder.community.service;

import com.nowcoder.community.dao.SensitiveWordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SensitiveWordService {

    @Autowired
    private SensitiveWordMapper sensitiveWordMapper;
    public Set<String>  getSensitiveWordSet(){
        return sensitiveWordMapper.getSensitiveWordSet();
    }
}
