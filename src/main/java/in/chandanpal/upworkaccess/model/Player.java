package in.chandanpal.upworkaccess.model;

public class Player
{
    private long ID;
    private String firstName;
    private String lastName;
    private String email;
    private String cellphone;
    private float rating;
    private String city;
    private String state;
    
    public Player()
    {
        
    }

    public Player(String firstName, String lastName, String email, String cellphone, float rating, String city,
            String state)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cellphone = cellphone;
        this.rating = rating;
        this.city = city;
        this.state = state;
    }
    
    public long getID()
    {
        return ID;
    }
    public void setID(long iD)
    {
        ID = iD;
    }
    public String getFirstName()
    {
        return firstName;
    }
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    public String getLastName()
    {
        return lastName;
    }
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getCellphone()
    {
        return cellphone;
    }
    public void setCellphone(String cellphone)
    {
        this.cellphone = cellphone;
    }
    public float getRating()
    {
        return rating;
    }
    public void setRating(float rating)
    {
        this.rating = rating;
    }
    public String getCity()
    {
        return city;
    }
    public void setCity(String city)
    {
        this.city = city;
    }
    public String getState()
    {
        return state;
    }
    public void setState(String state)
    {
        this.state = state;
    }

}
