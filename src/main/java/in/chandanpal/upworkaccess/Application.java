package in.chandanpal.upworkaccess;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.chandanpal.upworkaccess.model.Event;
import in.chandanpal.upworkaccess.model.Player;
import in.chandanpal.upworkaccess.services.EventsService;
import in.chandanpal.upworkaccess.services.PlayerService;

@Service
public class Application
{
    
    @Autowired
    PlayerService playerService;
    
    @Autowired
    EventsService eventsService;
    
    public void run() {
        initUI();
    }
    
    private void initUI() {
        
        //create top frame
        JFrame topFrame = new JFrame("Import Players");
        topFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        topFrame.setSize(600, 230);
        
        // Event Name and Year
        JLabel jlEventName = new JLabel("Event Name:");
        JLabel jlYear = new JLabel("Year:");
        JTextField jtfEventName = new JTextField(20);
        ArrayList<String> years_tmp = new ArrayList<>();
        for(int years = 1980; years <= Calendar.getInstance().get(Calendar.YEAR); years++) {
            years_tmp.add(years+"");
        }
        JComboBox<Object> jcbYear = new JComboBox<>(years_tmp.toArray());
        
        //player file chooser
        JLabel jlFile = new JLabel("Player Excel File:");
        JTextField jtfFile = new JTextField(30);
        JButton jbBrowseFile = new JButton("Browse..");
        jbBrowseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                browseActionPerformed(evt, jtfFile);            
            }
        });
        
        // Message Label
        JLabel jlMessage = new JLabel();
        
        // Cancel Button
        JButton bCancel = new JButton("Cancel");
        bCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                topFrame.dispose();
            }
        });
        
        // OK button
        JButton bOk = new JButton("OK");
        bOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                okButtonAction(jtfFile, jcbYear, jtfEventName, jlMessage, bCancel);
            }
        });
        
        // create layout
        JPanel tPanel = new JPanel(new GridLayout(3,0, 5, 5));
        
        // fields panel
        JPanel fieldsPannel = new JPanel();
        fieldsPannel.add(jlEventName);
        fieldsPannel.add(jtfEventName);
        fieldsPannel.add(jlYear);
        fieldsPannel.add(jcbYear);
        
        // file Panel
        JPanel filePanel = new JPanel();
        filePanel.add(jlFile);
        filePanel.add(jtfFile);
        filePanel.add(jbBrowseFile);
        
        tPanel.add(fieldsPannel);
        tPanel.add(filePanel);
        tPanel.add(jlMessage);
        
        
        // bottom button panel
        JPanel bPanel = new JPanel();
        bPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bPanel.add(bOk);
        bPanel.add(bCancel);
        
        
        //add components to frame
        topFrame.getContentPane().add(BorderLayout.NORTH, tPanel);
        topFrame.getContentPane().add(BorderLayout.SOUTH, bPanel);
        
        //show the frame
        topFrame.setLocationRelativeTo(null);
        topFrame.setResizable(false);
        topFrame.setVisible(true);
    }
    
    
    //File browser action
    private void browseActionPerformed(ActionEvent evt, JTextField jtfFile) {
        String curPath = new File("").getAbsolutePath();
        JFileChooser fileChooser = new JFileChooser(curPath);
        if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
            jtfFile.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }
    
    // OK Button Action
    private void okButtonAction(JTextField jtfFile, JComboBox<Object> jtfYear, JTextField jtfEventName, JLabel jlMessage, JButton bCancel)
    {
        String playerFilePath = jtfFile.getText();
        Integer year = (Integer) jtfYear.getSelectedItem();
        String eventName = jtfEventName.getText();
        jlMessage.setText("<html><br/><br/>Process started...</html>");
        String strMessage = "Process Finished...";
        try
        {
            startUploadPlayers(playerFilePath, year, eventName);
        }
        catch (Exception e)
        {
            strMessage = e.getMessage();
        }
        jlMessage.setText("<html><br/><br/>" + strMessage + "</html>");
        bCancel.setText("Close");
    }
    
    
    private void startUploadPlayers(String playersFilePath, Integer year, String eventName) throws IOException
    {
        FileInputStream excelFile = new FileInputStream(new File(playersFilePath));
        
        // save event
        Event event = new Event(year, eventName);
        eventsService.saveEvent(event);
        
        Workbook workbook = new HSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0);
        Map<String, Integer> colMap = getColumnMap(sheet);
        
        int totalRows = sheet.getPhysicalNumberOfRows();
        for(int x = 1; x<=totalRows; x++)
        {
            Row row = sheet.getRow(x);
            String firstName = row.getCell(colMap.get("FirstName")).getStringCellValue();
            String lastName = row.getCell(colMap.get("LastName")).getStringCellValue();
            String email = row.getCell(colMap.get("Email")).getStringCellValue();
            String cellphone = row.getCell(colMap.get("Cellphone")).getStringCellValue();
            String rating = row.getCell(colMap.get("Rating")).getStringCellValue();
            String city = row.getCell(colMap.get("City")).getStringCellValue();
            String state = row.getCell(colMap.get("State")).getStringCellValue();
            
            float fRating = Float.parseFloat(rating);
            
            // check if player is existing
            Player player = playerService.findUniquePlayer(firstName, lastName, email, cellphone);
            
            if (player == null)
            {
                player = new Player(firstName, lastName, email, cellphone, fRating, city, state);
                Player newPlayer = playerService.save(player);
                System.out.println(newPlayer);
            }
            else
            {
                
            }
        }
        workbook.close();
    }

    private Map<String, Integer> getColumnMap(Sheet sheet)
    {
        Map<String, Integer> map = new HashMap<>();
        Row row = sheet.getRow(0);
        int minColIx = row.getFirstCellNum();
        int maxColIx = row.getLastCellNum();
        for(int colIx=minColIx; colIx<maxColIx; colIx++) {
            Cell cell = row.getCell(colIx);
            map.put(cell.getStringCellValue(), cell.getColumnIndex());
        }
        return map;
    }

}
