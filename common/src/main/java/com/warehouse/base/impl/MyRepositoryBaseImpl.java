package com.warehouse.base.impl;

import com.warehouse.base.MyBaseRepository;
import com.warehouse.base.object.MyObjBase;
import com.warehouse.base.object.MySearchObjBase;
import jakarta.persistence.*;
import jakarta.persistence.Query;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@Transactional(readOnly = true)
public abstract class MyRepositoryBaseImpl implements MyBaseRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public MyObjBase add(MyObjBase param) throws Exception {
        log.debug("add() begin - class={}, id={}", param.getClass().getSimpleName(), param.getId());
        Object o = this.convertToEntity(param);
        log.debug("add() converted to entity: {}", o);
        this.entityManager.persist(o);
        this.saveAuditLog("CREATE", param);
        log.debug("add() end - persisted id={}", param.getId());
        return param;
    }

    @Transactional
    @Override
    public MyObjBase update(MyObjBase param) throws Exception {
        log.debug("update() begin - class={}, id={}", param.getClass().getSimpleName(), param.getId());
        Object o = this.convertToEntity(param);
        log.debug("update() converted to entity: {}", o);
        this.entityManager.merge(o);
        log.debug("update() end - merged id={}", param.getId());
        return param;
    }

    @Transactional
    @Override
    public MyObjBase delete(MyObjBase param) throws Exception {
        log.debug("delete() begin - class={}, id={}", param.getClass().getSimpleName(), param.getId());

        Object o = this.convertToEntity(param);
        boolean alreadyManaged = this.entityManager.contains(o);
        log.debug("delete() entity already managed={}, will {}", alreadyManaged, alreadyManaged ? "remove directly" : "merge then remove");
        this.entityManager.remove(alreadyManaged ? o : this.entityManager.merge(o));

        log.debug("delete() end - removed id={}", param.getId());
        return param;
    }

    @Override
    public MyObjBase findByID(MyObjBase param) throws Exception {
        log.debug("findByID() begin - class={}, id={}", param.getClass().getSimpleName(), param.getId());

        if (param.getId() == null || param.getId().isEmpty()) {
            log.warn("findByID() called with null/empty id for class={}", param.getClass().getSimpleName());
            throw new Exception("Cannot findByID: ID is null or empty");
        }

        Object o = this.convertToEntity(param);
        Class<?> entityClass = o.getClass();
        try {
            Integer id = Integer.parseInt(param.getId());
            o = this.entityManager.find(entityClass, id);
            log.debug("findByID() looked up by Integer id={}", id);
        } catch (NumberFormatException ex) {
            o = this.entityManager.find(entityClass, param.getId());
            log.debug("findByID() looked up by String id={}", param.getId());
        }

        if (o == null) {
            log.debug("findByID() no {} row found for id={}", entityClass.getSimpleName(), param.getId());
        } else {
            log.debug("findByID() found row: {}", o);
        }

        Object result = this.convertToObj(o);
        log.debug("findByID() end - converted result is {}", result == null ? "null" : "non-null");
        return (MyObjBase) result;
    }

    protected String getQueryString(MySearchObjBase param) throws Exception {
        return "";
    }

    protected String getCountString(MySearchObjBase param) throws Exception {
        return "";
    }

    protected void setQuery(Query query, MySearchObjBase param) throws Exception {

    }

    @Override
    public MySearchObjBase list(MySearchObjBase param) throws Exception {
        log.debug("list() begin - class={}, pageNo={}, pageSize={}",
                param.getClass().getSimpleName(), param.getPageNo(), param.getPageSize());

        Long count = getCountForQuery(param);
        log.debug("list() total matching rows (before pagination) = {}", count);

        param.setTotalRow(count.intValue());

        BigDecimal total = new BigDecimal(count.intValue() + ".00");
        BigDecimal size = new BigDecimal(param.getPageSize() + ".00");
        int lastPageNumber = (int) (Math.ceil(total.divide(size, 2, RoundingMode.HALF_UP).doubleValue()));
        param.setTotalPage(lastPageNumber);
        log.debug("list() totalPage calculated = {}", lastPageNumber);

        int offset = getOffet(param.getPageNo(), param.getPageSize());
        String queryString = getQueryString(param);
        log.debug("list() JPQL = [{}], offset={}", queryString, offset);

        Query query = entityManager.createQuery(queryString);
        this.setQuery(query, param);

        if (param.getPageSize() != -1) {
            query.setFirstResult(offset);
            query.setMaxResults(param.getPageSize());
        }
        List list = query.getResultList();
        log.debug("list() raw JPA result rows = {}", list.size());

        param.setResultList(new ArrayList<>());

        for (Object o : list) {
            Object obj = convertToObj(o);
            param.getResultList().add(obj);
        }
        log.debug("list() end - converted resultList size = {}", param.getResultList().size());
        return param;
    }

    private Long getCountForQuery(MySearchObjBase param) throws Exception {
        String countString = getCountString(param);
        log.debug("getCountForQuery() JPQL = [{}]", countString);
        Query query = entityManager.createQuery(countString);
        this.setQuery(query, param);
        Long count = (Long) query.getSingleResult();
        log.debug("getCountForQuery() result = {}", count);
        return count;
    }

    private int getOffet(int page, int size) {
        return (page - 1) * size;
    }

    protected abstract Object convertToEntity(Object obj) throws Exception;

    protected abstract Object convertToObj(Object model) throws Exception;

    protected abstract void saveAuditLog(String action, Object param) throws Exception;
}
