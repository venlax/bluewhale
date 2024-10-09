package com.seecoder.BlueWhale.repository;

import com.seecoder.BlueWhale.enums.CommodityTypeEnum;
import com.seecoder.BlueWhale.po.Commodity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EnumType;
import java.math.BigDecimal;
import java.util.List;

public interface CommodityRepository extends JpaRepository<Commodity, Integer>, CommodityRepositoryCustom {
    List<Commodity> findByStoreId(Integer storeId);


//    @Query("SELECT p FROM Commodity p WHERE " +
//            "(:keyword IS NULL OR p.name LIKE %:keyword%) AND " +
//            "(:type IS NULL OR p.type = :type) AND " +
//            "(:subType IS NULL OR p.subType = :subType) AND " +
//            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
//            "(:maxPrice IS NULL OR p.price <= :maxPrice)")
//    List<Commodity> findByCriteria(
//            @Param("keyword") String keyword,
//            @Param("type") CommodityTypeEnum type,
//            @Param("subType") CommodityTypeEnum.CommoditySubTypeEnum subType,
//            @Param("minPrice") BigDecimal minPrice,
//            @Param("maxPrice") BigDecimal maxPrice
//    );

//    List<Commodity> findByCriteria(
//            String keyword,
//            CommodityTypeEnum type,
//            CommodityTypeEnum.CommoditySubTypeEnum subType,
//            BigDecimal minPrice,
//            BigDecimal maxPrice
//    );
}
