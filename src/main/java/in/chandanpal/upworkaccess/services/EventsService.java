package in.chandanpal.upworkaccess.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import in.chandanpal.upworkaccess.model.Event;

@Service("eventsService")
public class EventsService
{
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    public Event findEventByID(long ID) {
        List<Event> lEvents = jdbcTemplate.query("select ? from events where id=?", new Object[] {"*", ID}, new RowMapper<Event>(){

            @Override
            public Event mapRow(ResultSet rs, int rowNum) throws SQLException
            {
                Event event = new Event();
                event.setID(rs.getLong("ID"));
                event.setEvent_Name(rs.getString("Event_Name"));
                event.setYear(rs.getInt("Year"));
                return event;
            }
        });
        if (lEvents != null && lEvents.size() >0)
            return lEvents.get(0);
        else
            return null;
    }
    
    public Event saveEvent(Event event)
    {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO EVENTS (YEAR, EVENT_NAME) values(?, ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, event.getYear());
                ps.setString(2, event.getEvent_Name());
                return ps;
            }
        }, keyHolder);
        return findEventByID(keyHolder.getKey().longValue());
    }

}
