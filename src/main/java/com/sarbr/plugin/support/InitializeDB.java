package com.sarbr.plugin.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarbr.plugin.Main;
import com.sarbr.plugin.exception.PluginException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 初始化数据
 * @author Sarbr
 */
@Component
public class InitializeDB {

    private final Logger logger = LoggerFactory.getLogger(InitializeDB.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private ObjectMapper objectMapper;

    /**
     *  根据指定的node从数据文件中获取对应的数据
     * @param node 文件节点
     * @param clazz 对应的类
     * @param <T>  泛型
     */
    public <T>List<T> createByDataJson(String node, Class<T> clazz){
        List<T> list = new ArrayList<>();
        try {
            final String json = FileUtils.fileRead(new File(Main.getURL("db/data.json").toURI()).getPath());
            for (JsonNode jsonNode : objectMapper.readTree(json).get(node)) {
                list.add(objectMapper.readValue(jsonNode.toString(), clazz));
            }
        } catch (Exception e) {
            logger.error("初始化数据信息失败！", e);
        }
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Modifying
    public void createTable(){
        try {
            final String sqlTable = FileUtils.fileRead(new File(Main.getURL("db/schema.sql").toURI()).getPath());
            entityManager.createNativeQuery(sqlTable).executeUpdate();
        } catch (Exception e) {
            throw new PluginException("初始化表信息失败");
        }
    }
}
