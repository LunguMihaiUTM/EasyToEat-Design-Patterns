package agin.designpatternproject.command;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TableInvoker {
    private final TableAvailabilityCommand command;

    public void run() {
        command.execute();
    }
}
