package ee.joonasvali.spaceblaster.event;

public class Event extends Statistics {

  public EventType type;
  public long eventTimestamp;

  public Event(Statistics statistics, EventType type) {
    this.type = type;
    this.eventTimestamp = System.currentTimeMillis();
    statistics.copyTo(this);
  }

  public EventType getType() {
    return type;
  }

  public long getEventTimestamp() {
    return eventTimestamp;
  }

  @Override
  public String toString() {
    return "Event " + type + " " + eventTimestamp;
  }
}
