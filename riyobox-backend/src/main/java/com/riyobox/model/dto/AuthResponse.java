package com.riyobox.model.dto;

import com.riyobox.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private boolean success;
    private String token;
    private UserDTO user;
    private String message;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDTO {
        private String id;
        private String email;
        private String name;
        private String subscriptionPlan;

        public static UserDTO fromUser(User user) {
            return UserDTO.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .subscriptionPlan(user.getSubscriptionPlan())
                    .build();
        }
    }
}
