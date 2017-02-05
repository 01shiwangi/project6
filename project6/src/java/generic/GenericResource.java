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
    @Produces(MediaType.APPLICATION_XML)
    public String listofbuildings() throws ClassNotFoundException, SQLException {
    
    JSONObject main=new JSONObject();
    
        main.accumulate("Status", "OK");

        long ut = System.currentTimeMillis() / 1000L;
        main.accumulate("Timestamp", ut);

        Class.forName("oracle.jdbc.OracleDriver");
        Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "cegepgim");
        Statement mystmt = mycon.createStatement();
        
        String sql="select * from p6building";
        ResultSet myresult = mystmt.executeQuery(sql);
        JSONArray buildinglist = new JSONArray();
        while(myresult.next())
        {
            JSONObject buildinglistobj=new JSONObject();
            
            int building_id=myresult.getInt("building_id");
            buildinglistobj.accumulate("Building_Id", building_id);
            System.out.print(building_id);
        }
        
        
        
        
        
        
        String abc = main.toString();
        return abc;
    }

   
}
