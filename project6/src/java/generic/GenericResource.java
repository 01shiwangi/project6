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
    
    
    
    @Path("listofdepartments")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofdepartments() throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        main.accumulate("Status", "OK");

        long ut = System.currentTimeMillis() / 1000L;
        main.accumulate("Timestamp", ut);

        Class.forName("oracle.jdbc.OracleDriver");
        Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "cegepgim");
        Statement mystmt = mycon.createStatement();

        String sql = "select * from p6department";
        ResultSet myresult = mystmt.executeQuery(sql);
        JSONArray departmentlist = new JSONArray();
        while (myresult.next()) {
            JSONObject departmentlistobj = new JSONObject();

            int department_id = myresult.getInt("department_id");
            departmentlistobj.accumulate("Department_id", department_id);

            String department_abbv=myresult.getString("department_abbv");
            departmentlistobj.accumulate("Department_abbv", department_abbv);

            departmentlist.add(departmentlistobj);
        }

        main.accumulate("Department_List", departmentlist);

        String abc = main.toString();
        return abc;
    }
    
    
    
    @Path("departmentdetail&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String departmentdetail(@PathParam ("param1") int did) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        main.accumulate("Status", "OK");

        long ut = System.currentTimeMillis() / 1000L;
        main.accumulate("Timestamp", ut);

        Class.forName("oracle.jdbc.OracleDriver");
        Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "cegepgim");
        Statement mystmt = mycon.createStatement();

        String sql = "select * from p6department where department_id="+did;
        ResultSet myresult = mystmt.executeQuery(sql);
        
        main.accumulate("Department_Id", did);
        
        while (myresult.next()) {

            String department_name=myresult.getString("department_name");
            main.accumulate("Department_name", department_name);
            
            String department_abbv=myresult.getString("department_abbv");
            main.accumulate("Department_abbv", department_abbv);
            
        }
        
        String abc = main.toString();
        return abc;
    }
    
    
    
    @Path("listofitems")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofitems() throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        main.accumulate("Status", "OK");

        long ut = System.currentTimeMillis() / 1000L;
        main.accumulate("Timestamp", ut);

        Class.forName("oracle.jdbc.OracleDriver");
        Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "cegepgim");
        Statement mystmt = mycon.createStatement();

        String sql = "select * from p6item";
        ResultSet myresult = mystmt.executeQuery(sql);
        JSONArray itemlist = new JSONArray();
        while (myresult.next()) {
            JSONObject itemlistobj = new JSONObject();

            int item_id = myresult.getInt("item_id");
            itemlistobj.accumulate("Item_id", item_id);

            String item_name=myresult.getString("item_name");
            itemlistobj.accumulate("Item_name", item_name);

            String status=myresult.getString("status");
            itemlistobj.accumulate("Status", status);
            
            itemlist.add(itemlistobj);
        }

        main.accumulate("Item_List", itemlist);

        String abc = main.toString();
        return abc;
    }
    
    
    
    @Path("itemdetailbyid&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String itemdetailbyid(@PathParam ("param1") int iid) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        main.accumulate("Status", "OK");

        long ut = System.currentTimeMillis() / 1000L;
        main.accumulate("Timestamp", ut);

        Class.forName("oracle.jdbc.OracleDriver");
        Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "cegepgim");
        Statement mystmt = mycon.createStatement();

        String sql = "select * from p6item where item_id="+iid;
        ResultSet myresult = mystmt.executeQuery(sql);
        
        main.accumulate("Item_Id", iid);
        
        while (myresult.next()) {

            String item_name=myresult.getString("item_name");
            main.accumulate("Item_name", item_name);
            
            String item_description=myresult.getString("item_description");
            main.accumulate("Item_description", item_description);
            
            String model=myresult.getString("model");
            main.accumulate("Model", model);
            
            String status=myresult.getString("status");
            main.accumulate("Status_of_item", status);
            
        }
        
        Statement mystmt2 = mycon.createStatement();
        String sql2 = "select c.category_name from p6item i, p6itemcategory c where c.category_id=i.category_id and i.item_id="+iid;
        ResultSet myresult2 = mystmt2.executeQuery(sql2);
        
        while(myresult2.next())
        {
            String category_name=myresult2.getString("category_name");
            main.accumulate("Category_name", category_name);
        }
        
        Statement mystmt3 = mycon.createStatement();
        String sql3 = "select b.brand_name from p6item i, p6brand b where b.brand_id=i.brand_id and i.item_id="+iid;
        ResultSet myresult3 = mystmt3.executeQuery(sql3);
        
        while(myresult3.next())
        {
            String brand_name=myresult3.getString("brand_name");
            main.accumulate("Brand_name", brand_name);
        }
        
        Statement mystmt4 = mycon.createStatement();
        String sql4 = "select b.building_name from p6item i, p6location l, p6building b where l.location_id=i.location_id and b.building_id=l.building_id and i.item_id="+iid;
        ResultSet myresult4 = mystmt3.executeQuery(sql4);
        
        while(myresult4.next())
        {
            String building_name=myresult4.getString("building_name");
            main.accumulate("Building_name", building_name);
        }
        
        String abc = main.toString();
        return abc;
    }
    
    
    
    @Path("listofitembybuilding&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofitembybuilding(@PathParam ("param1") String bname) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        main.accumulate("Status", "OK");

        long ut = System.currentTimeMillis() / 1000L;
        main.accumulate("Timestamp", ut);

        Class.forName("oracle.jdbc.OracleDriver");
        Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "cegepgim");
        Statement mystmt = mycon.createStatement();

        String sql = "select * from p6item i, p6location l, p6building b where l.location_id=i.location_id and b.building_id=l.building_id and b.building_name='"+bname+"'";
        ResultSet myresult = mystmt.executeQuery(sql);
        
        main.accumulate("Building_name", bname);
        
        JSONArray itemlist=new JSONArray();
        
        while (myresult.next()) {
            
            JSONObject itemlistobj=new JSONObject();
            
            int item_id=myresult.getInt("item_id");
            itemlistobj.accumulate("Item_id", item_id);

            String item_name=myresult.getString("item_name");
            itemlistobj.accumulate("Item_name", item_name);
            
            String status=myresult.getString("status");
            itemlistobj.accumulate("Status_of_item", status);
            
            itemlist.add(itemlistobj);
            
        }
        
        main.accumulate("Item_list", itemlist);
        
        String abc = main.toString();
        return abc;
    }
    
    
    
    @Path("listofitembybrand&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofitembybrand(@PathParam ("param1") String bname) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        main.accumulate("Status", "OK");

        long ut = System.currentTimeMillis() / 1000L;
        main.accumulate("Timestamp", ut);

        Class.forName("oracle.jdbc.OracleDriver");
        Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "cegepgim");
        Statement mystmt = mycon.createStatement();

        String sql = "select * from p6item i, p6brand b where b.brand_id=i.brand_id and b.brand_name='"+bname+"'";
        ResultSet myresult = mystmt.executeQuery(sql);
        
        main.accumulate("Brand_name", bname);
        
        JSONArray itemlist=new JSONArray();
        
        while (myresult.next()) {
            
            JSONObject itemlistobj=new JSONObject();
            
            int item_id=myresult.getInt("item_id");
            itemlistobj.accumulate("Item_id", item_id);

            String item_name=myresult.getString("item_name");
            itemlistobj.accumulate("Item_name", item_name);
            
            String status=myresult.getString("status");
            itemlistobj.accumulate("Status_of_item", status);
            
            itemlist.add(itemlistobj);
            
        }
        
        main.accumulate("Item_list", itemlist);
        
        String abc = main.toString();
        return abc;
    }
    
    
    
    @Path("listofitembycategory&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofitembycategory(@PathParam ("param1") String cname) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        main.accumulate("Status", "OK");

        long ut = System.currentTimeMillis() / 1000L;
        main.accumulate("Timestamp", ut);

        Class.forName("oracle.jdbc.OracleDriver");
        Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "cegepgim");
        Statement mystmt = mycon.createStatement();

        String sql = "select * from p6item i, p6itemcategory c where c.category_id=i.category_id and c.category_name='"+cname+"'";
        ResultSet myresult = mystmt.executeQuery(sql);
        
        main.accumulate("Category_name", cname);
        
        JSONArray itemlist=new JSONArray();
        
        while (myresult.next()) {
            
            JSONObject itemlistobj=new JSONObject();
            
            int item_id=myresult.getInt("item_id");
            itemlistobj.accumulate("Item_id", item_id);

            String item_name=myresult.getString("item_name");
            itemlistobj.accumulate("Item_name", item_name);
            
            String status=myresult.getString("status");
            itemlistobj.accumulate("Status_of_item", status);
            
            itemlist.add(itemlistobj);
            
        }
        
        main.accumulate("Item_list", itemlist);
        
        String abc = main.toString();
        return abc;
    }
    
    
    
    
    @Path("listofitembydescription&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofitembydescription(@PathParam ("param1") String desc) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        main.accumulate("Status", "OK");

        long ut = System.currentTimeMillis() / 1000L;
        main.accumulate("Timestamp", ut);

        Class.forName("oracle.jdbc.OracleDriver");
        Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "cegepgim");
        Statement mystmt = mycon.createStatement();

        String sql = "select * from p6item where item_description='"+desc+"'";
        ResultSet myresult = mystmt.executeQuery(sql);
        
        main.accumulate("Item_description", desc);
        
        JSONArray itemlist=new JSONArray();
        
        while (myresult.next()) {
            
            JSONObject itemlistobj=new JSONObject();
            
            int item_id=myresult.getInt("item_id");
            itemlistobj.accumulate("Item_id", item_id);

            String item_name=myresult.getString("item_name");
            itemlistobj.accumulate("Item_name", item_name);
            
            String status=myresult.getString("status");
            itemlistobj.accumulate("Status_of_item", status);
            
            itemlist.add(itemlistobj);
            
        }
        
        main.accumulate("Item_list", itemlist);
        
        String abc = main.toString();
        return abc;
    }
    
    
    
    
    @Path("listofitemcategory")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofitemcategory() throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        main.accumulate("Status", "OK");

        long ut = System.currentTimeMillis() / 1000L;
        main.accumulate("Timestamp", ut);

        Class.forName("oracle.jdbc.OracleDriver");
        Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "cegepgim");
        Statement mystmt = mycon.createStatement();

        String sql = "select * from p6itemcategory";
        ResultSet myresult = mystmt.executeQuery(sql);
        
        JSONArray categorylist=new JSONArray();
        
        while (myresult.next()) {
            
            JSONObject categorylistobj=new JSONObject();
            
            int category_id=myresult.getInt("category_id");
            categorylistobj.accumulate("Category_id", category_id);

            String category_name=myresult.getString("category_name");
            categorylistobj.accumulate("Category_name", category_name);
            
            int nbitems=myresult.getInt("nbitems");
            categorylistobj.accumulate("NBItems", nbitems);
            
            categorylist.add(categorylistobj);
            
        }
        
        main.accumulate("Category_list", categorylist);
        
        String abc = main.toString();
        return abc;
    }
    
    
    
    
    @Path("itemcategorydetail&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String itemcategorydetail(@PathParam ("param1") int cid) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        main.accumulate("Status", "OK");

        long ut = System.currentTimeMillis() / 1000L;
        main.accumulate("Timestamp", ut);

        Class.forName("oracle.jdbc.OracleDriver");
        Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "cegepgim");
        Statement mystmt = mycon.createStatement();

        String sql = "select * from p6itemcategory where category_id="+cid;
        ResultSet myresult = mystmt.executeQuery(sql);
        
        main.accumulate("Category_id", cid);
        
        while (myresult.next()) {
            
            String category_name=myresult.getString("category_name");
            main.accumulate("Category_name", category_name);
            
            String description=myresult.getString("description");
            main.accumulate("Description", description);
            
        }
        
        Statement mystmt2 = mycon.createStatement();
        String sql2 = "Select i.CATEGORY_ID, count(i.item_id) from p6itemcategory c, p6item i where c.CATEGORY_ID=i.CATEGORY_ID and c.category_id="+cid+" group by i.category_ID";
        ResultSet myresult2 = mystmt2.executeQuery(sql2);
        
        while(myresult2.next())
        {
            int nbitems=myresult2.getInt("count(i.item_id)");
            main.accumulate("NBItems", nbitems);
        }
        
        String abc = main.toString();
        return abc;
    }
    
    
    
    
    @Path("listofsoftwares")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofsoftwares() throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        main.accumulate("Status", "OK");

        long ut = System.currentTimeMillis() / 1000L;
        main.accumulate("Timestamp", ut);

        Class.forName("oracle.jdbc.OracleDriver");
        Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "cegepgim");
        Statement mystmt = mycon.createStatement();

        String sql = "select * from p6software";
        ResultSet myresult = mystmt.executeQuery(sql);
        
        JSONArray softwarelist=new JSONArray();
        
        while (myresult.next()) {
            
            JSONObject softwarelistobj=new JSONObject();
            
            int software_id=myresult.getInt("software_id");
            softwarelistobj.accumulate("Software_id", software_id);

            String software_name=myresult.getString("software_name");
            softwarelistobj.accumulate("Software_name", software_name);
            
            String version=myresult.getString("version");
            softwarelistobj.accumulate("Version", version);
            
            softwarelist.add(softwarelistobj);
            
        }
        
        main.accumulate("Software_list", softwarelist);
        
        String abc = main.toString();
        return abc;
    }
    
    
    
    
    @Path("softwaredetail&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String softwaredetail(@PathParam ("param1") int sid) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        main.accumulate("Status", "OK");

        long ut = System.currentTimeMillis() / 1000L;
        main.accumulate("Timestamp", ut);

        Class.forName("oracle.jdbc.OracleDriver");
        Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "cegepgim");
        Statement mystmt = mycon.createStatement();

        String sql = "select * from p6software where software_id="+sid;
        ResultSet myresult = mystmt.executeQuery(sql);
        
        main.accumulate("Software_Id", sid);
        
        while (myresult.next()) {
            
            String software_name=myresult.getString("software_name");
            main.accumulate("Software_name", software_name);
            
            String license_key=myresult.getString("license_key");
            main.accumulate("License_key", license_key);
            
            String version=myresult.getString("version");
            main.accumulate("Version", version);
            
        }
        
        String abc = main.toString();
        return abc;
    } 
    
}
