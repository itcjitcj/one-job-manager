package com.spdbccc.job.manager.test.executor;

import java.sql.*;

public class TestJdbc {

    public static void main(String[] args) throws SQLException {
        Connection connect = getConnect();
        PreparedStatement statement = connect.prepareStatement("show tables");
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()){
            System.out.println(resultSet.getString(1));
        }
    }
    public static Connection getConnect (){

        Connection connection = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://192.168.200.133:3306/one_job_manager?characterEncoding=utf-8&autoReconnect=true&createDatabaseIfNotExist=true&failOverReadOnly=false&useSSL=false&allowPublicKeyRetrieval=true" , "root" , "caoJIE@2023");
        } catch (Exception e){
            e.printStackTrace();
        }

        return connection;
    }
}
