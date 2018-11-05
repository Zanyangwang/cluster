package test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class UserDB {

    /**
     * Table Name
     */
    private static final String TABLE_NAME = "todo_user";

    /*
     * テスト処理
     * @param args
     */
    public static void main(String[] args){
        UserDB userDB = new UserDB();


        try{
            userDB.create();

            userDB.execute(args);

        }catch (Throwable t){
            t.printStackTrace();
        }finally {
            userDB.close();
        }
    }

    /**
     * Connection
     */
    private Connection _connection;


    /**
     * Statement
     */
    private Statement _statement;

    /**
     *  コンストラクタ
     */
    public UserDB(){
        _connection = null;
        _statement = null;
    }

    public void create()throws ClassNotFoundException, SQLException{
        Class.forName("com.mysql.jdbc.Driver");
        _connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sample1?characterEncoding=UTF-8&serverTimezone=JST",
                "root","password");
        _statement = _connection.createStatement();


    }

    public void close() {
        if(_statement != null) {
            try{
                _statement.close();
            }catch(SQLException e) {
                ;
            }
            _statement = null;
        }
        if(_connection != null) {
            try{
                _connection.close();
            }catch(SQLException e) {
                ;
            }
            _connection = null;
        }
    }

    /**
     * 実行します。
     * @param args
     * @throws SQLException
     */
    public void execute(String[] args)
            throws SQLException {
        String command = args[0];
        if("select".equals(command)) {
            executeSelect();
        }else if("insert".equals(command)) {
            executeInsert(args[1], args[2], args[3]);
        }else if("update".equals(command)) {
            executeUpdate(args[1], args[2], args[3]);
        }else if("delete".equals(command)) {
            executeDelete(args[1]);
        }
    }

    private void executeSelect()
            throws SQLException{
        ResultSet resultSet = _statement.executeQuery("SELECT * FROM " + TABLE_NAME);
        try{
            boolean br = resultSet.first();
            if(br == false) {
                return;
            }
            do{
                String id = resultSet.getString("ID");
                String name = resultSet.getString("NAME");
                String password = resultSet.getString("PASSWORD");

                System.out.println("id: " + id + ", name: " + name + ", password: " + password);
            }while(resultSet.next());
        }finally{
            resultSet.close();
        }
    }


    /**
     * INSERT処理を実行します。
     * @param id
     * @param name
     * @param password
     */
    private void executeInsert(String id, String name, String password)
            throws SQLException{
        // SQL文を発行
        int updateCount = _statement.executeUpdate("INSERT INTO " + TABLE_NAME +
                " (ID,NAME,PASSWORD) VALUES ('"+id+"','"+name+"','"+password+"')");
        System.out.println("Insert: " + updateCount);
    }

    /**
     * UPDATE処理を実行します。
     * @param id
     * @param name
     * @param password
     */
    private void executeUpdate(String id, String name, String password)
            throws SQLException{
        // SQL文を発行
        int updateCount = _statement.executeUpdate("UPDATE " + TABLE_NAME +
                " SET NAME='"+name+"', PASSWORD='"+password+
                "' WHERE ID='" + id + "'");
        System.out.println("Update: " + updateCount);
    }

    /**
     * DELETE処理を実行します。
     * @param id
     */
    private void executeDelete(String id)
            throws SQLException{
        // SQL文を発行
        int updateCount = _statement.executeUpdate("DELETE FROM " + TABLE_NAME + " WHERE ID='" + id + "'");
        System.out.println("Delete: " + updateCount);
    }

}
