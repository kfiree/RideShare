package utils.logs;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import static utils.logs.formatUtils.buildLog;

public class LineFormat extends Formatter {

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = buildLog(record, false);

        return builder.toString();
    }

}