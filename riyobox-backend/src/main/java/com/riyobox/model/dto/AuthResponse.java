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
    private String message;
    private UserDTO user;

    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    public static class AuthResponseBuilder {
        private boolean success;
        private String token;
        private String message;
        private UserDTO user;

        public AuthResponseBuilder success(boolean success) { this.success = success; return this; }
        public AuthResponseBuilder token(String token) { this.token = token; return this; }
        public AuthResponseBuilder message(String message) { this.message = message; return this; }
        public AuthResponseBuilder user(UserDTO user) { this.user = user; return this; }

        public AuthResponse build() {
            return new AuthResponse(success, token, message, user);
        }
    }

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

        public static UserDTOBuilder builder() {
            return new UserDTOBuilder();
        }

        public static class UserDTOBuilder {
            private String id;
            private String email;
            private String name;
            private String subscriptionPlan;

            public UserDTOBuilder id(String id) { this.id = id; return this; }
            public UserDTOBuilder email(String email) { this.email = email; return this; }
            public UserDTOBuilder name(String name) { this.name = name; return this; }
            public UserDTOBuilder subscriptionPlan(String subscriptionPlan) { this.subscriptionPlan = subscriptionPlan; return this; }

            public UserDTO build() {
                return new UserDTO(id, email, name, subscriptionPlan);
            }
        }
    }
}
