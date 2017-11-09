package harry;

// import ruben's telegram api implementation

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;
import org.telegram.telegrambots.logging.BotsFileHandler;

import java.sql.SQLException;

import harry.NLPBot;
import harry.DerbyDB;

public class NLP {
  public static void main(String... args) {
  
    //Initialize the database
  	DerbyDB db = new DerbyDB();
    try {
      db.checkDB();
      db.initializeTables();
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }

    ApiContextInitializer.init();

    TelegramBotsApi botsApi = new TelegramBotsApi();

    try {
      botsApi.registerBot(new NLPBot());
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }
}