/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * REST Web Service
 *
 * @author John
 */
@Path("project6")
public class GenericResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }

    @Path("listofbuildings")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofbuildings() throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        main.accumulate("Status", "OK");

        long ut = System.currentTimeMillis() / 1000L;
        main.accumulate("Timestamp", ut);

        Class.forName("oracle.jdbc.OracleDriver");
        Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "cegepgim");
        Statement mystmt = mycon.createStatement();

        String sql = "select * from p6building";
        ResultSet myresult = mystmt.executeQuery(sql);
        JSONArray buildinglist = new JSONArray();
        while (myresult.next()) {
            JSONObject buildinglistobj = new JSONObject();

            int building_id = myresult.getInt("building_id");
            buildinglistobj.accumulate("Building_Id", building_id);
            System.out.print(building_id);

            String building_name = myresult.getString("building_name");
            buildinglistobj.accumulate("Building_name", building_name);

            String building_phone = myresult.getString("building_phone");
            buildinglistobj.accumulate("Building_phone", building_phone);

            buildinglist.add(buildinglistobj);
        }

        main.accumulate("Building_List", buildinglist);

        String abc = main.toString();
        return abc;
    }
    
    
    @Path("buildingdetail&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String buildingdetail(@PathParam ("param1") int bid) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        main.accumulate("Status", "OK");

        long ut = System.currentTimeMillis() / 1000L;
        main.accumulate("Timestamp", ut);

        Class.forName("oracle.jdbc.OracleDriver");
        Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "cegepgim");
        Statement mystmt = mycon.createStatement();

        String sql = "select * from p6building where building_id="+bid;
        ResultSet myresult = mystmt.executeQuery(sql);
        
        main.accumulate("Building_Id", bid);
        
        while (myresult.next()) {
            
            String building_name = myresult.getString("building_name");
            main.accumulate("Building_name", building_name);

            String building_address = myresult.getString("building_address");
            main.accumulate("Building_address", building_address);
            
            String zipcode = myresult.getString("zip_code");
            main.accumulate("ZipCode", zipcode);
            
            String building_phone = myresult.getString("building_phone");
            main.accumulate("Building_phone", building_phone);

        }

        String abc = main.toString();
        return abc;
    }


    
    @Path("listoflocations")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listoflocations() throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        main.accumulate("Status", "OK");

        long ut = System.currentTimeMillis() / 1000L;
        main.accumulate("Timestamp", ut);

        Class.forName("oracle.jdbc.OracleDriver");
        Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "cegepgim");
        Statement mystmt = mycon.createStatement();

        String sql = "select * from p6location";
        ResultSet myresult = mystmt.executeQuery(sql);
        JSONArray locationlist = new JSONArray();
        while (myresult.next()) {
            JSONObject locationlistobj = new JSONObject();

            int location_id = myresult.getInt("location_id");
            locationlistobj.accumulate("Location_Id", location_id);

            int floor_no = myresult.getInt("floor_no");
            locationlistobj.accumulate("Floor_no", floor_no);
            
            int room_no = myresult.getInt("room_no");
            locationlistobj.accumulate("Room_no", room_no);

            locationlist.add(locationlistobj);
        }

        main.accumulate("Location_List", locationlist);

        String abc = main.toString();
        return abc;
    }
    
    
    
    @Path("locationdetail&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String locationdetail(@PathParam ("param1") int lid) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        main.accumulate("Status", "OK");

        long ut = System.currentTimeMillis() / 1000L;
        main.accumulate("Timestamp", ut);

        Class.forName("oracle.jdbc.OracleDriver");
        Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "cegepgim");
        Statement mystmt = mycon.createStatement();

        String sql = "select * from p6location where location_id="+lid;
        ResultSet myresult = mystmt.executeQuery(sql);
        
        main.accumulate("Location_Id", lid);
        
        while (myresult.next()) {

            int floor_no = myresult.getInt("floor_no");
            main.accumulate("Floor_no", floor_no);
            
            int room_no = myresult.getInt("room_no");
            main.accumulate("Room_no", room_no);

            int row_no = myresult.getInt("row_no");
            main.accumulate("Row_no", row_no);
            
            int desk_no = myresult.getInt("desk_no");
            main.accumulate("Desk_no", desk_no);
            
        }

        Statement mystmt2 = mycon.createStatement();

        String sql2 = "select building_name from p6building b, p6location l where b.building_id=l.building_id and l.location_id="+lid;
        ResultSet myresult2 = mystmt2.executeQuery(sql2);
        
        while(myresult2.next())
        {
            String building_name=myresult2.getString("building_name");
            main.accumulate("Building_name", building_name);
        } 
        
        String abc = main.toString();
        return abc;
    }
    
    
    
    
}
