import java.sql.*;

public class ItemDB {
    private static final String _tableName = "todo_item";
    private Connection _connection;
    private Statement _statement;

    public ItemDB() {
        _connection = null;
        _statement = null;
    }
    public static void main(String[] args){
        ItemDB itemDB = new ItemDB();

        try{
            itemDB.open();
            itemDB.execute(args);
        }catch (Throwable t){
            t.printStackTrace();
        }finally {
            itemDB.close();
        }
    }

    /**
     * コネクション作成
     * @throws ClassNotFoundException
     * @throws SQLException
     *
     */
    public void open() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        // TODO : "characterEncoding=UTF-8&serverTimezone=JST"を入れないと何故か落ちるorz
        _connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/todo_db?characterEncoding=UTF-8&serverTimezone=JST",
                "root", "password");
        _statement = _connection.createStatement();

    }

    /**
     * コネクション破棄
     */
    public void close()  {
        if(_statement != null) {
            try{
                _statement.close();
            }catch(SQLException e) {
            }
            _statement = null;
        }

        if(_connection != null) {
            try{
                _connection.close();
            }catch(SQLException e) {
            }
            _connection = null;
        }
    }

    /**
     * 実行（引数に対応したCRUD処理を実行する）
     * @param args　引数の配列
     * @throws SQLException
     */
    public void execute(String[] args)
            throws SQLException {
        String command = args[0];
        if("select".equals(command)) {
            executeSelect();
        }else if("insert".equals(command)) {
            // FinishedDateは任意。存在しなければnull扱い
            String finishedDate = null;
            if(args.length > 4) {
                finishedDate = args[4];
            }
            executeInsert(args[1], args[2], args[3], finishedDate);
        }else if("update".equals(command)) {
            // FinishedDateは任意。存在しなければnull扱い
            String finishedDate = null;
            if(args.length > 5) {
                finishedDate = args[5];
            }
            executeUpdate(args[1], args[2], args[3], args[4], finishedDate);
        }else if("delete".equals(command)) {
            executeDelete(args[1]);
        }
    }


    private void executeSelect() throws SQLException {
        ResultSet resultSet = _statement.executeQuery("SELECT * FROM " + _tableName);
        try {
            boolean br = resultSet.first();
            if (br == false) {
                return;
            }
            do {
                Integer id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                String user = resultSet.getString("USER");
                Date expireDate = resultSet.getDate("EXPIRE_DATE");
                Date finishedDate = resultSet.getDate("FINISHED_DATE");

                System.out.println("id: " + id + ", name: " + name + ", user: " + user +
                                    ", expireDate: " + expireDate + ", finishedDate: " + finishedDate);

            } while (resultSet.next());
        } finally {
            resultSet.close();
        }
    }


    private void executeInsert(String name, String user, String expireDate, String finishedDate)
            throws SQLException{
        // SQL文を発行
        String finishedDateValue = "null";
        if(finishedDate != null) {
            finishedDateValue = "'" + finishedDate + "'";
        }
        int updateCount = _statement.executeUpdate("INSERT INTO " + _tableName + " (NAME,USER,EXPIRE_DATE,FINISHED_DATE) VALUES ('"+name+"','"+user+"','"+expireDate+"', " + finishedDateValue + ")");
        System.out.println("Insert: " + updateCount);
    }


    private void executeUpdate(String id, String name, String user, String expireDate, String finishedDate)
            throws SQLException{
        // SQL文を発行
        String finishedDateValue = "null";
        if(finishedDate != null) {
            finishedDateValue = "'" + finishedDate + "'";
        }
        int updateCount = _statement.executeUpdate("UPDATE " + _tableName + " SET NAME='"+name+"', USER='"+user+"', EXPIRE_DATE='"+expireDate+"', FINISHED_DATE="+finishedDateValue+" WHERE ID='" + id + "'");
        System.out.println("Update: " + updateCount);
    }


    private void executeDelete(String id)
            throws SQLException{
        // SQL文を発行
        int updateCount = _statement.executeUpdate("DELETE FROM " + _tableName + " WHERE ID='" + id + "'");
        System.out.println("Delete: " + updateCount);
    }
}

