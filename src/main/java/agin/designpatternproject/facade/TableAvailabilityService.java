package agin.designpatternproject.facade;

import agin.designpatternproject.entity.Table;
import agin.designpatternproject.exception.RestaurantResourceException;
import agin.designpatternproject.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TableAvailabilityService {

    private final TableRepository tableRepository;

    public void validateTableIsFree(Long tableId) {
        if (tableId == null) return;
        boolean isFree = tableRepository.findById(tableId)
                .map(Table::getIsFree)
                .orElseThrow(() -> new RestaurantResourceException("Table not found."));
        if (!isFree) {
            throw new RestaurantResourceException("Selected table is not free.");
        }
    }
}
