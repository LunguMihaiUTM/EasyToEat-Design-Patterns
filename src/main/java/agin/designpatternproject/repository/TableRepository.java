package agin.designpatternproject.repository;

import agin.designpatternproject.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TableRepository extends JpaRepository<Table, Long> {
    List<Table> getTablesByLocationId(Long locationId);

    @Modifying
    @Query("update tables t set t.isFree = ?2 where t.id = ?1")
    void updateTableFreeStatus(Long id, Boolean status);

    @Modifying
    @Query("update tables t set t.isFree = true where t.isFree = false")
    void freeTable();
}
