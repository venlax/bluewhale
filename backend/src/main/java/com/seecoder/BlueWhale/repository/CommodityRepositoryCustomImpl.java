package com.seecoder.BlueWhale.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;

import com.seecoder.BlueWhale.enums.CommodityTypeEnum;
import com.seecoder.BlueWhale.po.Commodity;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommodityRepositoryCustomImpl implements CommodityRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Commodity> findByCriteria(
            String keyword,
            CommodityTypeEnum type,
            CommodityTypeEnum.CommoditySubTypeEnum subType,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Commodity> cq = cb.createQuery(Commodity.class);
        Root<Commodity> commodity = cq.from(Commodity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasText(keyword)) {
            predicates.add(cb.like(commodity.get("name"), "%" + keyword + "%"));
        }

        if (type != null) {
            predicates.add(cb.equal(commodity.get("type"), type));
        }

        if (subType != null) {
            predicates.add(cb.equal(commodity.get("subType"), subType));
        }

        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(commodity.get("price"), minPrice));
        }

        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(commodity.get("price"), maxPrice));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }
}
