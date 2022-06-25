package controller.utils.logs;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import static controller.utils.logs.formatUtils.buildLog;

public class colorFormat extends Formatter
{
        @Override
        public String format(LogRecord record)
        {
                StringBuilder builder = buildLog(record, true);

                return builder.toString();
        }
}