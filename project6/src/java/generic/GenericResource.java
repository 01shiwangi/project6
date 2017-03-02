package generic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
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
    Statement mystmt;
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }

    /**
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection getconnection() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.OracleDriver");
        Connection mycon = DriverManager.getConnection("jdbc:oracle:thin:@144.217.163.57:1521:XE", "project6", "cegepgim");
        mystmt = mycon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return mycon;
    }

    @Path("listofbuildings")
    @GET
    @Produces(MediaType.APPLICATION_JSON)

    public String listofbuildings() throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        Connection mycon = getconnection();
        long ut = System.currentTimeMillis() / 1000L;

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        
        String sql = "select * from p6building";
        ResultSet myresult = mystmt.executeQuery(sql);

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            main.clear();
            myresult.beforeFirst();
            main.accumulate("Status", "OK");

            main.accumulate("Timestamp", ut);

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
        }
        String abc = main.toString();

        myresult.close();
        mycon.close();
        mystmt.close();
        return abc;

    }

    @Path("buildingdetail&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String buildingdetail(@PathParam("param1") int bid) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        mystmt = mycon.createStatement();

        String sql = "select * from p6building where building_id=" + bid;
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Building_Id", bid);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Building_Id", bid);
            main.accumulate("Message", "Data for this input doesn't exist.");

        } else {
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            main.accumulate("Building_Id", bid);

            String building_name = myresult.getString("building_name");
            main.accumulate("Building_name", building_name);

            String building_address = myresult.getString("building_address");
            main.accumulate("Building_address", building_address);

            String zipcode = myresult.getString("zip_code");
            main.accumulate("ZipCode", zipcode);

            String building_phone = myresult.getString("building_phone");
            main.accumulate("Building_phone", building_phone);

            String active = myresult.getString("active");
            main.accumulate("Active", active);

        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("listoflocations")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listoflocations() throws ClassNotFoundException, SQLException, SQLFeatureNotSupportedException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        
        String sql = "select * from p6location";
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");
        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

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
        }
        String abc = main.toString();

        myresult.close();
        mystmt.close();
        mycon.close();

        return abc;
    }

    @Path("locationdetail&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String locationdetail(@PathParam("param1") int lid) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        mystmt = mycon.createStatement();

        String sql = "select * from p6location where location_id=" + lid;
        ResultSet myresult = mystmt.executeQuery(sql);

        Statement mystmt2 = mycon.createStatement();

        String sql2 = "select building_name from p6building b, p6location l where b.building_id=l.building_id and l.location_id=" + lid;
        ResultSet myresult2 = mystmt2.executeQuery(sql2);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            main.accumulate("Location_Id", lid);

            int room_no = myresult.getInt("room_no");
            main.accumulate("Room_no", room_no);

            int floor_no = myresult.getInt("floor_no");
            main.accumulate("Floor_no", floor_no);

            int row_no = myresult.getInt("row_no");
            main.accumulate("Row_no", row_no);

            int desk_no = myresult.getInt("desk_no");
            main.accumulate("Desk_no", desk_no);

            String active = myresult.getString("active");
            main.accumulate("Active", active);
          
            myresult.close();
            mystmt.close();

            while (myresult2.next()) {
                String building_name = myresult2.getString("building_name");
                main.accumulate("Building_name", building_name);
            }
        }

        String abc = main.toString();

        myresult2.close();

        mystmt2.close();
        mycon.close();
        return abc;
    }

    @Path("listofdepartments")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofdepartments() throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        
        String sql = "select * from p6department";
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            JSONArray departmentlist = new JSONArray();
            while (myresult.next()) {
                JSONObject departmentlistobj = new JSONObject();

                int department_id = myresult.getInt("department_id");
                departmentlistobj.accumulate("Department_id", department_id);

                String department_abbv = myresult.getString("department_abbv");
                departmentlistobj.accumulate("Department_abbv", department_abbv);

                departmentlist.add(departmentlistobj);
            }

            main.accumulate("Department_List", departmentlist);
        }
        String abc = main.toString();

        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("departmentdetail&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String departmentdetail(@PathParam("param1") int did) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        mystmt = mycon.createStatement();

        String sql = "select * from p6department where department_id=" + did;
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");
        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            main.accumulate("Department_Id", did);

            String department_name = myresult.getString("department_name");
            main.accumulate("Department_name", department_name);

            String department_abbv = myresult.getString("department_abbv");
            main.accumulate("Department_abbv", department_abbv);

            String active = myresult.getString("active");
            main.accumulate("Active", active);

        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("listofitems")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofitems() throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        
        String sql = "select * from p6item";
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");
        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            JSONArray itemlist = new JSONArray();
            while (myresult.next()) {
                JSONObject itemlistobj = new JSONObject();

                int item_id = myresult.getInt("item_id");
                itemlistobj.accumulate("Item_id", item_id);

                String item_name = myresult.getString("item_name");
                itemlistobj.accumulate("Item_name", item_name);

                String status = myresult.getString("status");
                itemlistobj.accumulate("Status", status);

                itemlist.add(itemlistobj);
            }

            main.accumulate("Item_List", itemlist);
        }

        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("itemdetailbyid&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String itemdetailbyid(@PathParam("param1") int iid) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        mystmt = mycon.createStatement();

        String sql = "select * from p6item where item_id=" + iid;
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");
        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            main.accumulate("Item_Id", iid);

            String item_name = myresult.getString("item_name");
            main.accumulate("Item_name", item_name);

            String item_description = myresult.getString("item_description");
            main.accumulate("Item_description", item_description);

            String model = myresult.getString("model");
            main.accumulate("Model", model);

            String status = myresult.getString("status");
            main.accumulate("Status_of_item", status);

            String active = myresult.getString("active");
            main.accumulate("Active", active);

            myresult.close();
            mystmt.close();

            Statement mystmt2 = mycon.createStatement();
            String sql2 = "select c.category_name from p6item i, p6itemcategory c where c.category_id=i.category_id and i.item_id=" + iid;
            ResultSet myresult2 = mystmt2.executeQuery(sql2);

            while (myresult2.next()) {
                String category_name = myresult2.getString("category_name");
                main.accumulate("Category_name", category_name);
            }

            Statement mystmt3 = mycon.createStatement();
            String sql3 = "select b.brand_name from p6item i, p6brand b where b.brand_id=i.brand_id and i.item_id=" + iid;
            ResultSet myresult3 = mystmt3.executeQuery(sql3);

            while (myresult3.next()) {

                String brand_name = myresult3.getString("brand_name");
                main.accumulate("Brand_name", brand_name);
            }

            Statement mystmt4 = mycon.createStatement();
            String sql4 = "select b.building_name from p6item i, p6location l, p6building b where l.location_id=i.location_id and b.building_id=l.building_id and i.item_id=" + iid;
            ResultSet myresult4 = mystmt3.executeQuery(sql4);

            while (myresult4.next()) {
                String building_name = myresult4.getString("building_name");
                main.accumulate("Building_name", building_name);
            }
            myresult2.close();
            mystmt2.close();
            myresult3.close();
            mystmt3.close();
            myresult4.close();
            mystmt4.close();

        }
        String abc = main.toString();

        mycon.close();
        return abc;
    }

    @Path("listofitembybuilding&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofitembybuilding(@PathParam("param1") String bname) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
       
        String sql = "select * from p6item i, p6location l, p6building b where l.location_id=i.location_id and b.building_id=l.building_id and b.building_name='" + bname + "'";
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            main.accumulate("Building_name", bname);

            JSONArray itemlist = new JSONArray();

            while (myresult.next()) {

                JSONObject itemlistobj = new JSONObject();

                int item_id = myresult.getInt("item_id");
                itemlistobj.accumulate("Item_id", item_id);

                String item_name = myresult.getString("item_name");
                itemlistobj.accumulate("Item_name", item_name);

                String status = myresult.getString("status");
                itemlistobj.accumulate("Status_of_item", status);

                itemlist.add(itemlistobj);

            }

            main.accumulate("Item_list", itemlist);
        }

        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("listofitembybrand&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofitembybrand(@PathParam("param1") String bname) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        
        String sql = "select * from p6item i, p6brand b where b.brand_id=i.brand_id and b.brand_name='" + bname + "'";
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");
        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            main.accumulate("Brand_name", bname);

            JSONArray itemlist = new JSONArray();

            while (myresult.next()) {

                JSONObject itemlistobj = new JSONObject();

                int item_id = myresult.getInt("item_id");
                itemlistobj.accumulate("Item_id", item_id);

                String item_name = myresult.getString("item_name");
                itemlistobj.accumulate("Item_name", item_name);

                String status = myresult.getString("status");
                itemlistobj.accumulate("Status_of_item", status);

                itemlist.add(itemlistobj);

            }

            main.accumulate("Item_list", itemlist);
        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("listofitembycategory&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofitembycategory(@PathParam("param1") String cname) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
       
        String sql = "select * from p6item i, p6itemcategory c where c.category_id=i.category_id and c.category_name='" + cname + "'";
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");
        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            main.accumulate("Category_name", cname);

            JSONArray itemlist = new JSONArray();

            while (myresult.next()) {

                JSONObject itemlistobj = new JSONObject();

                int item_id = myresult.getInt("item_id");
                itemlistobj.accumulate("Item_id", item_id);

                String item_name = myresult.getString("item_name");
                itemlistobj.accumulate("Item_name", item_name);

                String status = myresult.getString("status");
                itemlistobj.accumulate("Status_of_item", status);

                itemlist.add(itemlistobj);

            }

            main.accumulate("Item_list", itemlist);
        }

        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("listofitembydescription&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofitembydescription(@PathParam("param1") String desc) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
       
        String sql = "select * from p6item where item_description='" + desc + "'";
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");
        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            main.accumulate("Item_description", desc);

            JSONArray itemlist = new JSONArray();

            while (myresult.next()) {

                JSONObject itemlistobj = new JSONObject();

                int item_id = myresult.getInt("item_id");
                itemlistobj.accumulate("Item_id", item_id);

                String item_name = myresult.getString("item_name");
                itemlistobj.accumulate("Item_name", item_name);

                String status = myresult.getString("status");
                itemlistobj.accumulate("Status_of_item", status);

                itemlist.add(itemlistobj);

            }

            main.accumulate("Item_list", itemlist);
        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("listofitemcategory")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofitemcategory() throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        
        String sql = "select * from p6itemcategory";
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");
        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            JSONArray categorylist = new JSONArray();

            while (myresult.next()) {

                JSONObject categorylistobj = new JSONObject();

                int category_id = myresult.getInt("category_id");
                categorylistobj.accumulate("Category_id", category_id);

                String category_name = myresult.getString("category_name");
                categorylistobj.accumulate("Category_name", category_name);

                Statement mystmt2 = mycon.createStatement();
            String sql2 = "Select i.CATEGORY_ID, count(i.item_id) from p6itemcategory c, p6item i where c.CATEGORY_ID=i.CATEGORY_ID and c.category_id=" + category_id + " group by i.category_ID";
            ResultSet myresult2 = mystmt2.executeQuery(sql2);

            while (myresult2.next()) {
                int nbitems = myresult2.getInt("count(i.item_id)");
                categorylistobj.accumulate("NBItems", nbitems);
            }
            myresult2.close();
            mystmt2.close();

                categorylist.add(categorylistobj);

            }

            main.accumulate("Category_list", categorylist);
        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("itemcategorydetail&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String itemcategorydetail(@PathParam("param1") int cid) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        mystmt = mycon.createStatement();

        String sql = "select * from p6itemcategory where category_id=" + cid;
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");

        } else {
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            main.accumulate("Category_id", cid);

            String category_name = myresult.getString("category_name");
            main.accumulate("Category_name", category_name);

            String description = myresult.getString("description");
            main.accumulate("Description", description);

            String active = myresult.getString("active");
            main.accumulate("Active", active);
           
            myresult.close();
            mystmt.close();

            Statement mystmt2 = mycon.createStatement();
            String sql2 = "Select i.CATEGORY_ID, count(i.item_id) from p6itemcategory c, p6item i where c.CATEGORY_ID=i.CATEGORY_ID and c.category_id=" + cid + " group by i.category_ID";
            ResultSet myresult2 = mystmt2.executeQuery(sql2);

            while (myresult2.next()) {
                int nbitems = myresult2.getInt("count(i.item_id)");
                main.accumulate("NBItems", nbitems);
            }
            myresult2.close();
            mystmt2.close();
        }
        String abc = main.toString();

        mycon.close();
        return abc;
    }

    @Path("listofsoftwares")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofsoftwares() throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        
        String sql = "select * from p6software";
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            JSONArray softwarelist = new JSONArray();

            while (myresult.next()) {

                JSONObject softwarelistobj = new JSONObject();

                int software_id = myresult.getInt("software_id");
                softwarelistobj.accumulate("Software_id", software_id);

                String software_name = myresult.getString("software_name");
                softwarelistobj.accumulate("Software_name", software_name);

                String version = myresult.getString("version");
                softwarelistobj.accumulate("Version", version);

                softwarelist.add(softwarelistobj);

            }

            main.accumulate("Software_list", softwarelist);
        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("softwaredetail&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String softwaredetail(@PathParam("param1") int sid) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        mystmt = mycon.createStatement();

        String sql = "select * from p6software where software_id=" + sid;
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            main.accumulate("Software_Id", sid);

            String software_name = myresult.getString("software_name");
            main.accumulate("Software_name", software_name);

            String license_key = myresult.getString("license_key");
            main.accumulate("License_key", license_key);

            String version = myresult.getString("version");
            main.accumulate("Version", version);

            String active = myresult.getString("active");
            main.accumulate("Active", active);

        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("listofitemsoftware")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofitemsoftware() throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        
        String sql = "select * from p6itemsoftware";
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            JSONArray itemsoftwarelist = new JSONArray();

            while (myresult.next()) {

                JSONObject itemsoftwarelistobj = new JSONObject();

                int item_id = myresult.getInt("item_id");
                itemsoftwarelistobj.accumulate("Item_id", item_id);

                int software_id = myresult.getInt("software_id");
                itemsoftwarelistobj.accumulate("Software_id", software_id);

                String dateofaddedsoftware = myresult.getString("dateofaddedsoftware");
                itemsoftwarelistobj.accumulate("date_of_added_software", dateofaddedsoftware);

                itemsoftwarelist.add(itemsoftwarelistobj);

            }

            main.accumulate("Item_Software_list", itemsoftwarelist);
        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("listofitemdepartment")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofitemdepartment() throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        
        String sql = "select * from p6itemdepartment";
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            JSONArray itemdepartmentlist = new JSONArray();

            while (myresult.next()) {
                JSONObject itemdepartmentlistobj = new JSONObject();

                int item_id = myresult.getInt("item_id");
                itemdepartmentlistobj.accumulate("Item_id", item_id);

                int department_id = myresult.getInt("department_id");
                itemdepartmentlistobj.accumulate("Department_id", department_id);

                String dateofaddeditem = myresult.getString("dateofaddeditem");
                itemdepartmentlistobj.accumulate("date_of_added_item", dateofaddeditem);

                itemdepartmentlist.add(itemdepartmentlistobj);
            }

            main.accumulate("Item_Department_List", itemdepartmentlist);
        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("listofbrands")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofbrands() throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        
        String sql = "select * from p6brand";
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            JSONArray brandlist = new JSONArray();

            while (myresult.next()) {
                JSONObject brandlistobj = new JSONObject();

                int brand_id = myresult.getInt("brand_id");
                brandlistobj.accumulate("Brand_id", brand_id);

                String brand_name = myresult.getString("brand_name");
                brandlistobj.accumulate("Brand_name", brand_name);

                brandlist.add(brandlistobj);
            }

            main.accumulate("Brand_List", brandlist);
        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("listofusers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofusers() throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        
        String sql = "select * from p6users";
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            JSONArray userlist = new JSONArray();

            while (myresult.next()) {
                JSONObject userlistobj = new JSONObject();

                int user_id = myresult.getInt("user_id");
                userlistobj.accumulate("User_id", user_id);

                String username = myresult.getString("username");
                userlistobj.accumulate("Username", username);

                String user_email = myresult.getString("user_mail");
                userlistobj.accumulate("User_email", user_email);

                userlist.add(userlistobj);
            }

            main.accumulate("User_List", userlist);
        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("userdetail&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String userdetail(@PathParam("param1") int uid) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        mystmt = mycon.createStatement();

        String sql = "select * from p6users where user_id=" + uid;
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            main.accumulate("User_Id", uid);

            String username = myresult.getString("username");
            main.accumulate("Username", username);

            String first_name = myresult.getString("first_name");
            main.accumulate("First_name", first_name);

            String last_name = myresult.getString("last_name");
            main.accumulate("Last_name", last_name);

            String user_address = myresult.getString("user_address");
            main.accumulate("User_address", user_address);

            String user_phone = myresult.getString("user_phone");
            main.accumulate("User_phone", user_phone);

            String user_email = myresult.getString("user_mail");
            main.accumulate("User_email", user_email);

            String password = myresult.getString("password");
            main.accumulate("Password", password);

            String active = myresult.getString("active");
            main.accumulate("Active", active);

        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("listofsupportingcall")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofsupportingcall() throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
       
        String sql = "select * from p6supporting_call";
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            JSONArray supportingcalllist = new JSONArray();

            while (myresult.next()) {
                JSONObject supportingcalllistobj = new JSONObject();

                int call_id = myresult.getInt("call_id");
                supportingcalllistobj.accumulate("Call_id", call_id);

                String description_call = myresult.getString("description_call");
                supportingcalllistobj.accumulate("Description_call", description_call);

                String statusofcall = myresult.getString("statusofcall");
                supportingcalllistobj.accumulate("Status_of_call", statusofcall);

                supportingcalllist.add(supportingcalllistobj);
            }

            main.accumulate("Supporting_Call_List", supportingcalllist);
        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("listofsupportingcallbyuserid&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofsupportingcallbyuserid(@PathParam("param1") int uid) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        
        String sql = "select * from p6supporting_call where user_id=" + uid;
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            main.accumulate("User_id", uid);
            JSONArray supportingcalllist = new JSONArray();

            while (myresult.next()) {
                JSONObject supportingcalllistobj = new JSONObject();

                int call_id = myresult.getInt("call_id");
                supportingcalllistobj.accumulate("Call_id", call_id);

                String description_call = myresult.getString("description_call");
                supportingcalllistobj.accumulate("Description_call", description_call);

                String statusofcall = myresult.getString("statusofcall");
                supportingcalllistobj.accumulate("Status_of_call", statusofcall);

                supportingcalllist.add(supportingcalllistobj);
            }

            main.accumulate("Supporting_Call_List", supportingcalllist);
        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("listofsupportingcallbyitemid&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofsupportingcallbyitemid(@PathParam("param1") int iid) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        
        String sql = "select * from p6supporting_call where item_id=" + iid;
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            main.accumulate("Item_id", iid);
            JSONArray supportingcalllist = new JSONArray();

            while (myresult.next()) {
                JSONObject supportingcalllistobj = new JSONObject();

                int call_id = myresult.getInt("call_id");
                supportingcalllistobj.accumulate("Call_id", call_id);

                String description_call = myresult.getString("description_call");
                supportingcalllistobj.accumulate("Description_call", description_call);

                String statusofcall = myresult.getString("statusofcall");
                supportingcalllistobj.accumulate("Status_of_call", statusofcall);

                supportingcalllist.add(supportingcalllistobj);
            }

            main.accumulate("Supporting_Call_List", supportingcalllist);
        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("detailsupportingcallbyuseritemid&{param1}&{param2}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String detailsupportingcallbyuseritemid(@PathParam("param1") int iid, @PathParam("param2") int uid) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        mystmt = mycon.createStatement();

        String sql = "select * from p6supporting_call where item_id=" + iid + " and user_id=" + uid;
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            main.accumulate("Item_id", iid);
            main.accumulate("User_Id", uid);
            JSONArray supportingcalllist = new JSONArray();

            //while (myresult.next()) {
            JSONObject supportingcalllistobj = new JSONObject();

            int call_id = myresult.getInt("call_id");
            supportingcalllistobj.accumulate("Call_id", call_id);

            String description_call = myresult.getString("description_call");
            supportingcalllistobj.accumulate("Description_call", description_call);

            String dateofcall = myresult.getString("dateofcall");
            supportingcalllistobj.accumulate("date_of_call", dateofcall);

            String timeofcall = myresult.getString("timeofcall");
            supportingcalllistobj.accumulate("time_of_call", timeofcall);

            String statusofcall = myresult.getString("statusofcall");
            supportingcalllistobj.accumulate("Status_of_call", statusofcall);

            supportingcalllist.add(supportingcalllistobj);
            //}

            main.accumulate("Supporting_Call_List", supportingcalllist);
        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("listofrequestingcall")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofrequestingcall() throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        
        String sql = "select * from p6requesting_call";
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            JSONArray requestingcalllist = new JSONArray();

            while (myresult.next()) {
                JSONObject requestingcalllistobj = new JSONObject();

                int request_call_id = myresult.getInt("request_call_id");
                requestingcalllistobj.accumulate("Request_Call_id", request_call_id);

                String req_call_description = myresult.getString("req_call_description");
                requestingcalllistobj.accumulate("Requesting_call_description", req_call_description);

                String call_status = myresult.getString("call_status");
                requestingcalllistobj.accumulate("Call_Status", call_status);

                requestingcalllist.add(requestingcalllistobj);
            }

            main.accumulate("Requesting_Call_List", requestingcalllist);
        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("listofrequestingcallbyuserid&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofrequestingcallbyuserid(@PathParam("param1") int uid) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        
        String sql = "select * from p6requesting_call where user_id=" + uid;
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            JSONArray requestingcalllist = new JSONArray();

            while (myresult.next()) {
                JSONObject requestingcalllistobj = new JSONObject();

                int request_call_id = myresult.getInt("request_call_id");
                requestingcalllistobj.accumulate("Request_Call_id", request_call_id);

                String req_call_description = myresult.getString("req_call_description");
                requestingcalllistobj.accumulate("Requesting_call_description", req_call_description);

                String call_status = myresult.getString("call_status");
                requestingcalllistobj.accumulate("Call_Status", call_status);

                requestingcalllist.add(requestingcalllistobj);
            }

            main.accumulate("Requesting_Call_List", requestingcalllist);
        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("detailrequestingcall&{param1}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String detailrequestingcall(@PathParam("param1") int rid) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        mystmt = mycon.createStatement();

        String sql = "select * from p6requesting_call where request_call_id=" + rid;
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            main.accumulate("Request_call_id", rid);

            String req_call_description = myresult.getString("req_call_description");
            main.accumulate("Requesting_call_description", req_call_description);

            String requesting_date = myresult.getString("requesting_date");
            main.accumulate("Requesting_date", requesting_date);

            String requesting_time = myresult.getString("requesting_time");
            main.accumulate("Requesting_time", requesting_time);

            int user_id = myresult.getInt("user_id");
            main.accumulate("User_id", user_id);

            String call_status = myresult.getString("call_status");
            main.accumulate("Call_Status", call_status);

        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("listofrentingitem")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listofrentingitem() throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        
        String sql = "select * from p6renting_item";
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            myresult.beforeFirst();
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            JSONArray rentingitemlist = new JSONArray();

            while (myresult.next()) {
                JSONObject rentingitemlistobj = new JSONObject();

                int user_id = myresult.getInt("user_id");
                rentingitemlistobj.accumulate("User_id", user_id);

                int item_id = myresult.getInt("item_id");
                rentingitemlistobj.accumulate("Item_id", item_id);

                String end_date = myresult.getString("end_date");
                rentingitemlistobj.accumulate("End_date", end_date);

                rentingitemlist.add(rentingitemlistobj);
            }

            main.accumulate("Renting_Item_List", rentingitemlist);
        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }

    @Path("rentingitemdetail&{param1}&{param2}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String rentingitemdetail(@PathParam("param1") int iid, @PathParam("param2") int uid) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        mystmt = mycon.createStatement();

        String sql = "select * from p6renting_item where item_id=" + iid + " and user_id=" + uid;
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            main.accumulate("User_Id", uid);
            main.accumulate("Item_Id", iid);

                String request_date_for_item = myresult.getString("request_date_for_item");
                main.accumulate("Request_date_for_item", request_date_for_item);

                String start_date = myresult.getString("start_date");
                main.accumulate("Start_date", start_date);

                String end_date = myresult.getString("end_date");
                main.accumulate("End_date", end_date);

        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }
    
    @Path("login&{param1}&{param2}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String login(@PathParam("param1") String uname, @PathParam("param2") String pwd) throws ClassNotFoundException, SQLException {

        JSONObject main = new JSONObject();

        long ut = System.currentTimeMillis() / 1000L;

        Connection mycon = getconnection();
        mystmt = mycon.createStatement();

        String sql = "select * from p6users where username='" + uname + "' and password='" + pwd +"'";
        ResultSet myresult = mystmt.executeQuery(sql);

        main.accumulate("Status", "Error");
        main.accumulate("Timestamp", ut);
        main.accumulate("Message", "Connection lost.");

        if (!myresult.next()) {
            main.clear();
            main.accumulate("Status", "Wrong");
            main.accumulate("Timestamp", ut);
            main.accumulate("Message", "Data for this input doesn't exist.");
        } else {
            main.clear();
            main.accumulate("Status", "OK");
            main.accumulate("Timestamp", ut);

            main.accumulate("Username", uname);
            main.accumulate("Password", pwd);

            int user_id=myresult.getInt("user_id");
            main.accumulate("User_Id", user_id);
            
            String first_name = myresult.getString("first_name");
            main.accumulate("First_name", first_name);

            String last_name = myresult.getString("last_name");
            main.accumulate("Last_name", last_name);

            String user_address = myresult.getString("user_address");
            main.accumulate("User_address", user_address);

            String user_phone = myresult.getString("user_phone");
            main.accumulate("User_phone", user_phone);

            String user_email = myresult.getString("user_mail");
            main.accumulate("User_email", user_email);

            String active = myresult.getString("active");
            main.accumulate("Active", active);
               
        }
        String abc = main.toString();
        myresult.close();
        mystmt.close();
        mycon.close();
        return abc;
    }
}
