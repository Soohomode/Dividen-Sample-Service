package com.sample.persist;

import com.sample.persist.entity.DividendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DividendRepository extends JpaRepository<DividendEntity, Long> { // <레포지토리가 활용하게될 엔티티, 엔티티의 PK 의 타입>

}
