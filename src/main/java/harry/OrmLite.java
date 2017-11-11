package harry;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
//import com.j256.ormlite.stmt.PreparedQuery;
//import com.j256.ormlite.stmt.QueryBuilder;
//import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.ConnectionSource;
//import com.j256.ormlite.table.TableUtils;
 
public class OrmLite {

  private static String databaseName="data/nlpdb";
  private static String dbUrl = "jdbc:derby:"+databaseName;

  public static Dao<Telegramuser, String> getTelegramUserDao() {

    Dao<Telegramuser, String> telegramuserDao = null;

    try {
      ConnectionSource connectionSource = new JdbcConnectionSource(dbUrl);
      telegramuserDao = DaoManager.createDao(connectionSource, Telegramuser.class);
    } catch (java.sql.SQLException e)
    {
      System.err.println(e.getMessage());
    }
    return telegramuserDao;
  }

  public static String getGenderByHandle(String handle) {

    String gender = "";

    Dao<Telegramuser, String> userdao = getTelegramUserDao();
    for (Telegramuser tuser : userdao) {
      if (tuser.getHandle().equals(handle))
        gender = tuser.getGender();
    }
    return gender;
  }
}
 