package agin.designpatternproject.repository;

import agin.designpatternproject.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByMenuId(Long menuId);
    void deleteByMenuId(Long id);
}
