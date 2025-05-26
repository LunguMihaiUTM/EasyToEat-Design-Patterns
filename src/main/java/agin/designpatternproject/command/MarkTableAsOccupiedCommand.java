package agin.designpatternproject.command;

import agin.designpatternproject.entity.Table;
import agin.designpatternproject.repository.TableRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MarkTableAsOccupiedCommand implements TableAvailabilityCommand {

    private final Table table;
    private final TableRepository tableRepository;


    @Override
    public void execute() {
        table.setIsFree(false);
        tableRepository.save(table);
    }
}



