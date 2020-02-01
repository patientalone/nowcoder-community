package com.nowcoder.community.util;

import com.nowcoder.community.service.SensitiveWordService;
import org.aspectj.lang.annotation.After;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;


/**
 * 敏感词过滤
 *
 * @author TanJianJun
 */
@Component
public class SensitiveWordFilterUtil {
    @Autowired
    private SensitiveWordService sensitiveWordService;
    // 日志  
    private static final Logger LOG = LoggerFactory.getLogger(SensitiveWordFilterUtil.class);
    // 敏感词库  
    private static HashMap<Object, Object> sensitiveWordMap = null;
    // 默认编码格式  
    private static final String ENCODING = "UTF-8";
    // 敏感词库的路径  
    private static final InputStream in = SensitiveWordFilterUtil.class.getClassLoader().getResourceAsStream(
            "sensitive-words.txt");
    // 替换敏感词字符  
    private static final String REPLACE_SIGN = "**";
    // 敏感词内容  
    private static String sensitiveContent = "";

    /**
     * 初始化敏感词库
     */


    private void init() {
        // 读取文件  
        Set<String> keyWords = new HashSet<>(sensitiveWordService.getSensitiveWordSet());
        // 创建敏感词库  
        sensitiveWordMap = new HashMap<>(keyWords.size());
        for (String keyWord : keyWords) {
            createKeyWord(keyWord);
        }
    }

    /**
     * 构建敏感词库
     *
     * @param keyWord
     */
    @SuppressWarnings("unchecked")
    private void createKeyWord(String keyWord) {
        if (sensitiveWordMap == null) {
            LOG.error("sensitiveWordMap 未初始化!");
            return;
        }
        Map<Object, Object> nowMap = sensitiveWordMap;
        for (Character c : keyWord.toCharArray()) {
            Object obj = nowMap.get(c);
            if (obj == null) {
                Map<Object, Object> childMap = new HashMap<Object, Object>();
                childMap.put("isEnd", "false");
                nowMap.put(c, childMap);
                nowMap = childMap;
            } else {
                nowMap = (Map<Object, Object>) obj;
            }
        }
        nowMap.put("isEnd", "true");
    }

    /**
     * 读取敏感词文件
     *
     * @return
     */
    private Set<String> readSensitiveWords() {
        Set<String> keyWords = new HashSet<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, ENCODING));
            String line;
            while ((line = reader.readLine()) != null) {
                sensitiveContent = sensitiveContent + line + "\n";
                if (line.startsWith("#") || "".equals(line.trim())) {
                    continue;
                }
                List<String> arr = Arrays.asList(line.trim().split("、"));
                keyWords.addAll(arr);
            }
        } catch (UnsupportedEncodingException e) {
            LOG.error("敏感词库文件转码失败!");
        } catch (FileNotFoundException e) {
            LOG.error("敏感词库文件不存在!");
        } catch (IOException e) {
            LOG.error("敏感词库文件读取失败!");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                reader = null;
            }
        }
        return keyWords;
    }

    /**
     * 取得敏感词
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> getSensitiveWord(String text) {
        if (sensitiveWordMap == null) {
            init();
        }
        // 加上一个空格，是为了匹配最后一个敏感词  
        text = text + " ";
        List<String> sensitiveWords = new ArrayList<String>();
        Map<Object, Object> nowMap = sensitiveWordMap;
        for (int i = 0; i < text.length(); i++) {
            Character word = text.charAt(i);
            Object obj = nowMap.get(word);
            if (obj == null) {
                continue;
            }
            int j = i + 1;
            Map<Object, Object> childMap = (Map<Object, Object>) obj;
            while (j < text.length()) {
                if ("true".equals(childMap.get("isEnd"))) {
                    sensitiveWords.add(text.substring(i, j));
                }
                obj = childMap.get(text.charAt(j));
                if (obj != null) {
                    childMap = (Map<Object, Object>) obj;
                } else {
                    break;
                }
                j++;
            }
        }
        return sensitiveWords;
    }

    /**
     * 替换敏感词
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public String replaceSensitiveWord(String text) {
        if (sensitiveWordMap == null) {
            init();
        }
        // 替换敏感词后的文本  
        String newText = text;
        // 加上一个空格，是为了匹配最后一个敏感词  
        text = text + " ";
        Map<Object, Object> nowMap = sensitiveWordMap;
        for (int i = 0; i < text.length(); i++) {
            Character word = text.charAt(i);
            Object obj = nowMap.get(word);
            if (obj == null) {
                continue;
            }
            int j = i + 1;
            Map<Object, Object> childMap = (Map<Object, Object>) obj;
            while (j < text.length()) {
                if ("true".equals(childMap.get("isEnd"))) {
                    newText = newText.replace(text.substring(i, j), REPLACE_SIGN);
                }
                obj = childMap.get(text.charAt(j));
                if (obj != null) {
                    childMap = (Map<Object, Object>) obj;
                } else {
                    break;
                }
                j++;
            }
        }
        return newText;
    }

    /**
     * 读取敏感词文件内容
     *
     * @return
     */
    public String getSensitiveWordContent() {
        if ("".equals(sensitiveContent)) {
            init();
        }
        return sensitiveContent;
    }
}  