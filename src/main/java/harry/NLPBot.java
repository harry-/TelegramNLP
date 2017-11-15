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
import harry.DerbyDB;


public class NLPBot extends TelegramLongPollingBot {

  private StringBuilder mode = new StringBuilder("sentiment"); 



  //define bot behaviour 

  @Override
  public void onUpdateReceived(Update update) {        
    // We check if the update has a message and the message has text
    if (update.hasMessage() && update.getMessage().hasText()) {

      DerbyDB db = new DerbyDB();

      String output = CommandHandler.all(update.getMessage().getText(), mode)

      try {

      SendMessage message = new SendMessage();// Create a SendMessage object with mandatory fields
      message.setChatId(update.getMessage().getChatId());


      if (message.getText() != null && !message.getText().isEmpty())
        sendMessage(message); // Call method to send the message

    } catch (TelegramApiException e) {
      e.printStackTrace();
    } catch (java.lang.Exception e) {
      e.printStackTrace();
    }
  }

  public static String displayHelp()
  {
    String helpMessage = "";

    helpMessage += "This bot listens to all messages in channels that it has been added to, analyzes them using the Google Natural Language API (https://cloud.google.com/natural-language/) and stores the result in a database. You can use the following commands to interact with it:\n";
    helpMessage += "\nlist users\nDisplays all users that are available in the database.\n";
    helpMessage += "\nreport <user>\nDisplay an nlp report on a specific user.\n";
    helpMessage += "\nall reports\nDisplay all available user reports\n";
    helpMessage += "\n\nIn addition you can also use the bot as a basic interface to some functions of the Google Language API. By default the bot will provide the sentiment analysis result for messages you send to it. Use the following commands to switch to a different mode:\n";
    helpMessage += "\nentity sentiment\nUse this command to switch to entity sentiment analysis mode. The bot will display the results of the google entity sentiment analysis in reply to your text messages from now on.\n";
    helpMessage += "\nentity\nSwitch to entity analysis mode. The bot will display the results of the google entity analysis.\n";
    helpMessage += "\nsentiment\nSwitch back to sentiment analysis (the default mode)\n";

    return helpMessage;
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