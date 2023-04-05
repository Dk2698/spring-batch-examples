package com.kumar.processor;

import com.kumar.model.User;
import org.springframework.batch.item.ItemProcessor;

public class UserItemProcessor implements ItemProcessor<User, User> {
    @Override
    public User process(User item) throws Exception {
        User user = new User();
        user.setId(item.getId());
        user.setFirstName(item.getFirstName());
        user.setLastName(item.getLastName());
        user.setEmail(item.getEmail());
        System.out.println(user);
        return item;
    }
}
