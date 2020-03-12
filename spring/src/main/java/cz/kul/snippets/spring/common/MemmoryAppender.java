package cz.kul.snippets.spring.common;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MemmoryAppender extends AppenderSkeleton {

    private List<LoggingEvent> events = new ArrayList<>();

    public List<LoggingEvent> getEvents() {
        return events;
    }

    public void clean() {
        events = new ArrayList<>();
    }

    public List<LoggingEvent> findEvents(String messageRegex) {
        Pattern pattern = Pattern.compile(messageRegex);
        List<LoggingEvent> result = events.stream()
                .filter(x -> pattern.matcher(x.getMessage().toString()).find())
                .collect(Collectors.toList());
        return result;
    }

    public int findEventsCount(String messageRegex) {
        return findEvents(messageRegex).size();
    }

    @Override
    protected void append(LoggingEvent event) {
        events.add(event);
    }

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

}
