package com.seecoder.BlueWhale.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seecoder.BlueWhale.po.Store;

public interface StoreRepository extends JpaRepository<Store,Integer> {
    // 这里可以添加一些自定义的查询方法，但是JpaRepository已经提供了很多现成的方法，比如save(), findById(), deleteById()等。
    Boolean existsByName(String name);
}
