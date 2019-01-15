package com.bianli24.wikison;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseUtil.class);

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://rm-wz9645q.mysql.rds.aliyuncs.com:3306/bi_pivot?useUnicode=true&characterEncoding=utf-8&tinyInt1isBit=false&useSSL=false&useInformationSchema=true";
    private static final String USERNAME = "dev";
    private static final String PASSWORD = "fe4f70c";

    private static final String SQL = "SELECT * FROM ";// 数据库操作

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("can not load jdbc driver", e);
        }
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Properties props = new Properties();
            props.setProperty("user", USERNAME);
            props.setProperty("password", PASSWORD);
            props.setProperty("remarks", "true"); //设置可以获取remarks信息
            props.setProperty("useInformationSchema", "true");//设置可以获取tables remarks信息
            conn = DriverManager.getConnection(URL, props);
        } catch (SQLException e) {
            LOGGER.error("get connection failure", e);
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     *
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("close connection failure", e);
            }
        }
    }

    /**
     * 获取数据库下的所有表名和字段注释
     */
    public static List<String> getTableInfo() {
        List<String> tableNames = new ArrayList<>();
        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            //打开文件
            File file =new File("write.sh");
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(file.getName());
            fileWritter.write("curl -u yourusername:yourpassword -X PUT -H 'Content-Type: application/json' -d '{\"id\":\"14123063\",\"type\":\"page\", " +
                    "\"title\":\"pivot层\", \"space\":{\"key\":\"BI\"}, " +
                    "\"body\":{\"storage\":{\"value\": " +
                    "\"");

            //获取数据库的元数据
            DatabaseMetaData db = conn.getMetaData();
            //从元数据中获取到所有的表名
            rs = db.getTables(null, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                //获取表名
                String tableName = rs.getString(3);
                String tableComment = rs.getString(5);
                System.out.println("<h1>" + tableName + "     " + tableComment + "</h1>");
                fileWritter.write("<h1>" + tableName + "     " + tableComment + "</h1>");
                //获取字段名
                PreparedStatement pStemt = null;
                String tableSql = SQL + tableName;
                //表列注释
                List<String> columnComments = new ArrayList<>();//列名注释集合
                ResultSet crs = null;
                pStemt = conn.prepareStatement(tableSql);
                crs = pStemt.executeQuery("show full columns from " + tableName);
                while (crs.next()) {
                    String comment = crs.getString("Comment");
                    comment = comment.replace("\r\n", "");
                    columnComments.add(comment);
                }

                pStemt = conn.prepareStatement(tableSql);
                //结果集元数据
                ResultSetMetaData rsmd = pStemt.getMetaData();
                //表列数
                fileWritter.write("<table>");
                int size = rsmd.getColumnCount();
                for (int i = 0; i < size; i++) {
                    fileWritter.write("<tr>");
                    String columnName = rsmd.getColumnName(i + 1);
                    String columnTypeName = rsmd.getColumnTypeName(i + 1);
                    System.out.println(columnName + "    " + columnTypeName + "    " + columnComments.get(i));
                    fileWritter.write("<td>" + columnName + "</td>");
                    fileWritter.write("<td>" + columnTypeName + "</td>");
                    fileWritter.write("<td>" + columnComments.get(i) + "</td>");
                    fileWritter.write("</tr>");
                }
                fileWritter.write("</table>");

            }

            fileWritter.write("\", " +
                    "\"representation\":\"storage\"}}, " +
                    "\"version\":{\"number\":"
                    + JsonUtil.getVersion() +
                    "}}'  http://192.168.1.1:8090/rest/api/content/14123063"
            );


            fileWritter.flush();
            fileWritter.close();
        } catch (SQLException e) {
            LOGGER.error("getTableNames failure", e);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                closeConnection(conn);
            } catch (SQLException e) {
                LOGGER.error("close ResultSet failure", e);
            }
        }
        return tableNames;
    }
}
