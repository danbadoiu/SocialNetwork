package com.example.domain.validators;


import com.example.domain.Message;


public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidationException {
        String message = "";
        if (entity.getMessage().length() == 0) {
            message += "The mesasge can't be null!";
        }
        if (entity.getTo().equals(entity.getFrom())) {
            message += "You can't send a message to you!";
        }
        if (entity.getTo() < 0L || entity.getFrom() < 0L) {
            message += "The id can't be samller then 0!";
        }
        if (message.length() > 0) {
            throw new ValidationException(message);
        }
    }

}
