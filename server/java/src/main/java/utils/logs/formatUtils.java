package utils.logs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;

public class formatUtils{

    // ANSI escape code
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_WHITE = "\u001B[37m";
    //    private static final String ANSI_BLACK = "\u001B[30m";
//    private static final String ANSI_BLUE = "\u001B[34m";
//    private static final String ANSI_PURPLE = "\u001B[35m";


    private static final String ANSI_CYAN = "\u001B[36m";


    protected static StringBuilder buildLog(LogRecord record, boolean colored){
        // This example will print date/time, class, and log level in yellow,
        // followed by the log message and it's parameters in white .
        StringBuilder builder = new StringBuilder();
        if(colored)
            builder.append(ANSI_YELLOW);

        builder.append(record.getLongThreadID());
        builder.append("::");

        builder.append("[");
        builder.append(calcDate(record.getMillis()));
        builder.append("]");

        builder.append(" [");
        builder.append(record.getSourceClassName());
        builder.append("]");

        builder.append(" [");
        if(colored)
            chooseColor(builder, record);
        builder.append(record.getLevel().getName());
        if(colored)
            builder.append(ANSI_YELLOW);
        builder.append("]");

        if(colored)
            builder.append(ANSI_WHITE);
        builder.append(" - ");
        builder.append(record.getMessage());

        Object[] params = record.getParameters();

        if (params != null)
        {
            builder.append("\t");
            for (int i = 0; i < params.length; i++)
            {
                builder.append(params[i]);
                if (i < params.length - 1)
                    builder.append(", ");
            }
        }
        if(colored)
            builder.append(ANSI_RESET);

        builder.append("\n");

        return builder;
    }

    private static void chooseColor(StringBuilder builder, LogRecord record){
        switch (record.getLevel().getName()) {
            case "SEVERE", "WARNING" -> builder.append(ANSI_RED);
            case "INFO" -> builder.append(ANSI_YELLOW);
            case "CONFIG" -> builder.append(ANSI_CYAN);
            case "FINE", "FINER", "FINEST" -> builder.append(ANSI_GREEN);
        }
    }

    private static String calcDate(long millisecs) {
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date resultdate = new Date(millisecs);
        return date_format.format(resultdate);
    }


}
