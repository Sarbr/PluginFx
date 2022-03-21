package com.sarbr.plugin.mapper;

import com.sarbr.plugin.entity.Rss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RssMapper extends JpaRepository<Rss, Long> {

    @Query(nativeQuery=true, value = "select p.id, p.order_no as orderNo, p.name, p.url, p.state FROM Rss p WHERE p.state = ?1 LIMIT 0,4")
    List<Rss> getSelectedRss(@Param(value = "code")String code);
}
