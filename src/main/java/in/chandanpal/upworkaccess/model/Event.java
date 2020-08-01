package in.chandanpal.upworkaccess.model;

public class Event
{
    private long ID;
    private int Year;
    private String Event_Name;
    
    public Event()
    {
        
    }

    public Event(int year, String event_Name)
    {
        Year = year;
        Event_Name = event_Name;
    }
    
    public long getID() {
        return ID;
    }
    public void setID(long ID) {
        this.ID = ID;
    }
    public int getYear()
    {
        return Year;
    }
    public void setYear(int year)
    {
        Year = year;
    }
    public String getEvent_Name()
    {
        return Event_Name;
    }
    public void setEvent_Name(String event_Name)
    {
        Event_Name = event_Name;
    }

}
