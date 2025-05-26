package agin.designpatternproject.service.impl;

import agin.designpatternproject.command.MarkTableAsFreeCommand;
import agin.designpatternproject.command.MarkTableAsOccupiedCommand;
import agin.designpatternproject.command.TableAvailabilityCommand;
import agin.designpatternproject.command.TableInvoker;
import agin.designpatternproject.entity.Table;
import agin.designpatternproject.exception.RestaurantResourceException;
import agin.designpatternproject.repository.TableRepository;
import agin.designpatternproject.service.TableService;

public class TableServiceImpl implements TableService {

    private static TableServiceImpl instance;

    private final TableRepository tableRepository;

    private TableServiceImpl(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public static synchronized TableServiceImpl getInstance(TableRepository tableRepository) {
        if (instance == null) {
            instance = new TableServiceImpl(tableRepository);
        }
        return instance;
    }

    @Override
    public String markTable(Long tableId, boolean available) {
        Table table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RestaurantResourceException("Table not found"));

        TableAvailabilityCommand command = available
                ? new MarkTableAsFreeCommand(table)
                : new MarkTableAsOccupiedCommand(table, tableRepository);

        TableInvoker invoker = new TableInvoker(command);
        invoker.run();

        tableRepository.save(table);
        return "Table " + tableId + " was marked as " + (available ? "FREE" : "OCCUPIED");
    }
}
