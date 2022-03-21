package com.sarbr.plugin.mapper;

import com.sarbr.plugin.entity.SysParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysParamMapper extends JpaRepository<SysParam, Long> {

    @Query(value = "select sp.val FROM SysParam sp where sp.key=:code")
    String getByCode(@Param(value = "code")String code);

    @Query(value = "select new SysParam(sp.title, sp.key) FROM SysParam sp where sp.type=:type")
    List<SysParam> getByType(@Param(value = "type")String type);
}
