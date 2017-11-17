package by.matrosov;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()){

            Message message = update.getMessage();
            String message_txt = update.getMessage().getText();

            if (message_txt.equals("/start")){

                sendReply(message,"go");

            }else if (message_txt.equals("/pic")){//как вариант, не используя sendPhoto

                sendReply(message,"https://cs5.pikabu.ru/images/big_size_comm/2015-11_3/1447440183173454809.jpg");

            }else if (message_txt.equals("/markup")){

                sendMsgWithKeyboard(message,"Magic");

            }else if (message_txt.equals("/hide")){

                sendMsgWithHiddenKeyBoard(message,"keyboard hidden");
            }
        }else if(update.hasMessage() && update.getMessage().hasPhoto()){

            List<PhotoSize> photos = update.getMessage().getPhoto();

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
                    .setChatId(update.getMessage().getChatId())
                    .setPhoto(f_id)
                    .setCaption(caption);

            try {
                sendPhoto(sP);
            } catch (TelegramApiException e) {
                e.printStackTrace();
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

    private void sendMsgWithKeyboard(Message message, String text){
        SendMessage s = new SendMessage();
        s.setChatId(message.getChatId());
        s.setText(text);

        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        s.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add("Комманда 1");
        keyboardFirstRow.add("Комманда 2");

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add("Комманда 3");
        keyboardSecondRow.add("Комманда 4");
        keyboardSecondRow.add("Комманда 5");

        // 3 строчка клавиатуры
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add("Какая длинная кнопка");

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);

        // устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);

        try {
            execute(s);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMsgWithHiddenKeyBoard(Message message,String text){
        SendMessage s = new SendMessage();
        s.setChatId(message.getChatId());
        s.setText(text);

        ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
        s.setReplyMarkup(keyboardMarkup);
        try {
            execute(s);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
