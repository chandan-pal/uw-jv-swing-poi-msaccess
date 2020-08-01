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

import in.chandanpal.upworkaccess.model.Player;

@Service
public class PlayerService
{
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    public Player findPlayerByID(long ID) {
        List<Player> lEvents = jdbcTemplate.query("select ? from players where id=?", new Object[] {"*", ID}, new RowMapper<Player>(){

            @Override
            public Player mapRow(ResultSet rs, int rowNum) throws SQLException
            {
                Player player = new Player();
                player.setID(rs.getLong("ID"));
                player.setFirstName(rs.getString("FirstName"));
                player.setLastName(rs.getString("LastName"));
                player.setEmail(rs.getString("Email"));
                player.setCellphone(rs.getString("Cellphone"));
                player.setRating(rs.getFloat("Rating"));
                player.setCity(rs.getString("City"));
                player.setState(rs.getString("State"));
                return player;
            }
        });
        if (lEvents != null && lEvents.size() >0)
            return lEvents.get(0);
        else
            return null;
    }
    
    public Player findUniquePlayer(String firstName, String lastName, String email, String cellphone)
    {
        List<Player> lEvents = jdbcTemplate.query
        (
            "select ? from players where firstname=? AND lastname=? AND email=? AND cellphone=?",
            new Object[] {"*", firstName, lastName, email, cellphone}, new RowMapper<Player>(){
            @Override
            public Player mapRow(ResultSet rs, int rowNum) throws SQLException
            {
                Player player = new Player();
                player.setID(rs.getLong("ID"));
                player.setFirstName(rs.getString("FirstName"));
                player.setLastName(rs.getString("LastName"));
                player.setEmail(rs.getString("Email"));
                player.setCellphone(rs.getString("Cellphone"));
                player.setRating(rs.getFloat("Rating"));
                player.setCity(rs.getString("City"));
                player.setState(rs.getString("State"));
                return player;
            }}
        );
        if (lEvents != null && lEvents.size()>0)
            return lEvents.get(0);
        else
            return null;
    }
    

    public Player save(Player player)
    {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement
                (
                    "INSERT INTO EVENTS (FIRSTNAME, LASTNAME, EMAIL, CELLPHONE, RATING, CITY, STATE, FULL_NAME) values(?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, player.getFirstName());
                ps.setString(2, player.getLastName());
                ps.setString(2, player.getEmail());
                ps.setString(2, player.getCellphone());
                ps.setFloat(2, player.getRating());
                ps.setString(2, player.getCity());
                ps.setString(2, player.getState());
                ps.setString(2, player.getFirstName() + " " + player.getLastName());
                return ps;
            }
        }, keyHolder);
        return findPlayerByID(keyHolder.getKey().longValue());
    }

}
