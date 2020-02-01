package com.nowcoder.community.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Mapper
@Component
public interface SensitiveWordMapper {
    Set<String> getSensitiveWordSet();
}
