package agin.designpatternproject.command;

import agin.designpatternproject.entity.Table;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MarkTableAsFreeCommand implements TableAvailabilityCommand {

    private final Table table;

    @Override
    public void execute() {
        table.setIsFree(true);
    }
}