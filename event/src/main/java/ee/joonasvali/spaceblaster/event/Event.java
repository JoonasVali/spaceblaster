package ee.joonasvali.spaceblaster.event;

public class Event extends Statistics {

  private EventType type;
  private long timestamp;

  public Event(Statistics statistics, EventType type) {
    this.type = type;
    this.timestamp = System.currentTimeMillis();
    statistics.copyTo(this);
  }

  public EventType getType() {
    return type;
  }

  public long getTimestamp() {
    return timestamp;
  }

  @Override
  public String toString() {
    return "Event " + type + " " + timestamp;
  }
}
