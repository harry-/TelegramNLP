package harry;

// import ruben's telegram api implementation

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;
import org.telegram.telegrambots.logging.BotsFileHandler;

// import local classes

import harry.NLPBot;
import harry.GoogleCloud;

public class NLP {
  public static void main(String... args) throws Exception {
  
    //telegram bot test

    ApiContextInitializer.init();

    TelegramBotsApi botsApi = new TelegramBotsApi();

    try {
      botsApi.registerBot(new NLPBot());
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }

    //google cloud test

    GoogleCloud cloud = new GoogleCloud();

    cloud.getSent();

  }
}