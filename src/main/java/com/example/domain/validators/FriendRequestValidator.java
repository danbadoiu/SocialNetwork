package com.example.domain.validators;

import com.example.domain.FriendRequest;


public class FriendRequestValidator implements Validator<FriendRequest> {
    @Override
    public void validate(FriendRequest entity) throws ValidationException {
        String messages = "";
        if (entity.getId1().equals(entity.getId2()))
            messages += "A friendship can't be created with the same user!";
        if (entity.getId1() < 0L || entity.getId2() < 0L)
            messages += "An id can't be negative!";

        if (messages.length() != 0) {
            throw new ValidationException(messages);
        }
    }
}
