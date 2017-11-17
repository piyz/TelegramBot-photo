package by.matrosov;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Comparator;
import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()){

            Message message = update.getMessage();
            String message_txt = update.getMessage().getText();

            if (message_txt.equals("/start")){

                sendReply(message,"go");

            }
            //не работает.....
            else if(update.hasMessage() && update.getMessage().hasPhoto()){

                List<PhotoSize> photos = message.getPhoto();
                
                String f_id = photos.stream()
                        .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                        .findFirst()
                        .orElse(null).getFileId();

                int f_width = photos.stream()
                        .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                        .findFirst()
                        .orElse(null).getWidth();

                int f_height = photos.stream()
                        .sorted(Comparator.comparing(PhotoSize::getFileSize).reversed())
                        .findFirst()
                        .orElse(null).getHeight();

                String caption = "file_id: " + f_id +
                        "\nwidth: " + Integer.toString(f_width) +
                        "\nheight: " + Integer.toString(f_height);

                SendPhoto sP = new SendPhoto()
                        .setChatId(message.getChatId())
                        .setPhoto(f_id)
                        .setCaption(caption);

                try {
                    sendPhoto(sP);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            //как вариант, не используя sendPhoto
            }else if (message_txt.equals("/pic")){

                sendReply(message,"https://cs5.pikabu.ru/images/big_size_comm/2015-11_3/1447440183173454809.jpg");
            }
        }
    }

    public String getBotUsername() {
        return "";
    }

    public String getBotToken() {
        return "";
    }

    private void sendReply(Message message, String txt){
        SendMessage s = new SendMessage().setChatId(message.getChatId()).setText(txt);
        try {
            execute(s);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
