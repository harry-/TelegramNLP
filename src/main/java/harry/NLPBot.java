package harry;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.InvalidObjectException;
import java.util.concurrent.ConcurrentHashMap;

import java.util.Map;

import harry.GoogleCloud;


public class NLPBot extends TelegramLongPollingBot {

  private String mode = "sentiment"; 



  //define bot behaviour 

  @Override
  public void onUpdateReceived(Update update) {        
    // We check if the update has a message and the message has text
    if (update.hasMessage() && update.getMessage().hasText()) {

      Listener listener = new Listener();


      // switches
      if (update.getMessage().getText().equals("entity")) 
        mode="entity";
      
      else if (update.getMessage().getText().equals("sentiment")) 
        mode="sentiment";

      else if (update.getMessage().getText().equals("entities-sentiment")) 
        mode="entities-sentiment";
      
      // commands that produce a reply
      else {
        try {

          listener.theCloudListens(update.getMessage().getFrom().getId(), 
            update.getMessage().getText());
          listener.theCloudListensToSentiments(update.getMessage().getFrom().getId(), 
            update.getMessage().getText());
          listener.checkUser(update.getMessage().getFrom().getId(), 
            update.getMessage().getFrom().getUserName(), 
            update.getMessage().getFrom().getFirstName());

          SendMessage message = new SendMessage();// Create a SendMessage object with mandatory fields
          message.setChatId(update.getMessage().getChatId());

          // "normal" commands or whatever
          if (update.getMessage().getText().equals("hello"))
            message.setText("hello " + update.getMessage().getFrom().getUserName());

          else if (update.getMessage().getText().equals("entity sentiment report")) {
            Report report = new Report();
            message.setText(report.entitySentiment());
          }

          // switch dependent commands that produce a reply
          else { 
            GoogleCloud cloud = new GoogleCloud();

            if (mode.equals("sentiment"))
               message.setText(cloud.getSent(update.getMessage().getText()));

            else if (mode.equals("entity"))
              message.setText(cloud.getEnt(update.getMessage().getText()));

            else if (mode.equals("entities-sentiment"))
              message.setText(cloud.getEntSent(update.getMessage().getText()));
          }
     
          sendMessage(message); // Call method to send the message

        } catch (TelegramApiException e) {
          e.printStackTrace();
        } catch (java.lang.Exception e) {
          e.printStackTrace();
        }
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