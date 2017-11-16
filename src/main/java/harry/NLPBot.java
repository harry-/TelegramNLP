package harry;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;

import org.telegram.telegrambots.logging.BotsFileHandler;

import java.io.InvalidObjectException;
import java.util.concurrent.ConcurrentHashMap;

import java.util.Map;

import harry.GoogleCloud;
import harry.DerbyDB;

public class NLPBot extends TelegramLongPollingBot {

  private StringBuilder mode = new StringBuilder("sentiment"); 


  public static void main(String... args) {
  
    //Initialize the database
    DerbyDB db = new DerbyDB();
    db.checkDB();

    ApiContextInitializer.init();

    TelegramBotsApi botsApi = new TelegramBotsApi();

    try {
      botsApi.registerBot(new NLPBot());
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
  }

  //define bot behaviour 

  @Override
  public void onUpdateReceived(Update update) {        
    // We check if the update has a message and the message has text
    if (update.hasMessage() && update.getMessage().hasText()) {

      DerbyDB db = new DerbyDB();

      String output = new CommandHandler().all( update.getMessage().getText(), 
                                                mode, 
                                                update.getMessage().getFrom().getUserName(),
                                                update.getMessage().getChat().isUserChat(), 
                                                update.getMessage().getFrom().getId(), 
                                                update.getMessage().getFrom().getFirstName());
      try {

        SendMessage message = new SendMessage();// Create a SendMessage object with mandatory fields
        message.setChatId(update.getMessage().getChatId());
        message.setText(output);

        if (message.getText() != null && !message.getText().isEmpty())
          sendMessage(message); // Call method to send the message

      } catch (TelegramApiException e) {
        e.printStackTrace();
      } catch (java.lang.Exception e) {
        e.printStackTrace();
      }
    }
  }

  //import bot credentials from environment variables

  @Override
  public String getBotUsername() {
   
    Map<String, String> env = System.getenv();
    String value = (String) env.get("TELEGRAM_BOT");

    return value;
  }

  @Override
  public String getBotToken() {
    
    Map<String, String> env = System.getenv();
    String value = (String) env.get("TELEGRAM_TOKEN");

    return value;
  }

}