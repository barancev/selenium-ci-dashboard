package ru.stqa.selenium;

import java.sql.*;

public class H2Storage {

  private static H2Storage singleton;

  public static H2Storage getInstance() {
    if (singleton == null) {
      singleton = new H2Storage();
    }
    return singleton;
  }

  private H2Storage() {
    try {
      Class.forName("org.h2.Driver");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    try (Connection conn = getConnection()) {
      Statement st = conn.createStatement();
      st.execute("CREATE TABLE IF NOT EXISTS builds(id VARCHAR(255) PRIMARY KEY, number VARCHAR(255), " +
        "status VARCHAR(255), result VARCHAR(255), startedAt VARCHAR(255), finishedAt VARCHAR(255), " +
        "branch VARCHAR(255), commit VARCHAR(255))");
      st.execute("CREATE TABLE IF NOT EXISTS jobs(id VARCHAR(255) PRIMARY KEY, buildId VARCHAR(255)," +
        "number VARCHAR(255), status VARCHAR(255), result VARCHAR(255), " +
        "startedAt VARCHAR(255), finishedAt VARCHAR(255), allowFailure BOOLEAN, " +
        "os VARCHAR(255), language VARCHAR(255), env VARCHAR(255))");
      conn.commit();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private Connection getConnection() throws SQLException {
    return DriverManager.getConnection("jdbc:h2:./db/travis", "sa", "");
  }

  public void store(TravisBuild build) {
    try (Connection conn = getConnection()) {
      PreparedStatement select = conn.prepareStatement("SELECT COUNT(*) AS total FROM builds WHERE id=?");
      select.setString(1, build.getId());
      ResultSet rs = select.executeQuery();
      rs.next();

      if (rs.getInt("total") > 0) {
        PreparedStatement pst = conn.prepareStatement("UPDATE builds SET number=?, status=?, result=?," +
          "startedAt=?, finishedAt=?, branch=?, commit=? WHERE id=?");
        pst.setString(1, build.getNumber());
        pst.setString(2, build.getStatus());
        pst.setString(3, build.getResult());
        pst.setString(4, build.getStartedAt());
        pst.setString(5, build.getFinishedAt());
        pst.setString(6, build.getBranch());
        pst.setString(7, build.getCommit());
        pst.setString(8, build.getId());
        pst.executeUpdate();
        conn.commit();

      } else {
        PreparedStatement pst = conn.prepareStatement("INSERT INTO builds VALUES(?,?,?,?,?,?,?,?)");
        pst.setString(1, build.getId());
        pst.setString(2, build.getNumber());
        pst.setString(3, build.getStatus());
        pst.setString(4, build.getResult());
        pst.setString(5, build.getStartedAt());
        pst.setString(6, build.getFinishedAt());
        pst.setString(7, build.getBranch());
        pst.setString(8, build.getCommit());
        pst.execute();
        conn.commit();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void store(TravisJob job) {
    try (Connection conn = getConnection()) {
      PreparedStatement select = conn.prepareStatement("SELECT COUNT(*) AS total FROM jobs WHERE id=?");
      select.setString(1, job.getId());
      ResultSet rs = select.executeQuery();
      rs.next();

      if (rs.getInt("total") > 0) {
        PreparedStatement pst = conn.prepareStatement("UPDATE jobs SET buildId=?, number=?, status=?, result=?," +
          "startedAt=?, finishedAt=?, allowFailure=?, os=?, language=?, env=? WHERE id=?");
        pst.setString(1, job.getBuildId());
        pst.setString(2, job.getNumber());
        pst.setString(3, job.getStatus());
        pst.setString(4, job.getResult());
        pst.setString(5, job.getStartedAt());
        pst.setString(6, job.getFinishedAt());
        pst.setBoolean(7, job.isAllowFailure());
        pst.setString(8, job.getOs());
        pst.setString(9, job.getLanguage());
        pst.setString(10, job.getEnv());
        pst.setString(11, job.getId());
        pst.executeUpdate();
        conn.commit();

      } else {
        PreparedStatement pst = conn.prepareStatement("INSERT INTO jobs VALUES(?,?,?,?,?,?,?,?,?,?,?)");
        pst.setString(1, job.getId());
        pst.setString(2, job.getBuildId());
        pst.setString(3, job.getNumber());
        pst.setString(4, job.getStatus());
        pst.setString(5, job.getResult());
        pst.setString(6, job.getStartedAt());
        pst.setString(7, job.getFinishedAt());
        pst.setBoolean(8, job.isAllowFailure());
        pst.setString(9, job.getOs());
        pst.setString(10, job.getLanguage());
        pst.setString(11, job.getEnv());
        pst.execute();
        conn.commit();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}
