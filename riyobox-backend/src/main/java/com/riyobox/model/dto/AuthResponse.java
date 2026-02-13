package com.riyobox.model.dto;

import com.riyobox.model.User;

public class AuthResponse {
    private boolean success;
    private String token;
    private UserDTO user;
    private String message;

    public AuthResponse() {}

    public AuthResponse(boolean success, String token, UserDTO user, String message) {
        this.success = success;
        this.token = token;
        this.user = user;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static class UserDTO {
        private String id;
        private String email;
        private String name;
        private String subscriptionPlan;

        public UserDTO() {}

        public UserDTO(String id, String email, String name, String subscriptionPlan) {
            this.id = id;
            this.email = email;
            this.name = name;
            this.subscriptionPlan = subscriptionPlan;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSubscriptionPlan() { return subscriptionPlan; }
        public void setSubscriptionPlan(String subscriptionPlan) { this.subscriptionPlan = subscriptionPlan; }

        public static UserDTO fromUser(User user) {
            return new UserDTO(user.getId(), user.getEmail(), user.getName(), user.getSubscriptionPlan());
        }
    }

    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    public static class AuthResponseBuilder {
        private boolean success;
        private String token;
        private UserDTO user;
        private String message;

        public AuthResponseBuilder success(boolean success) { this.success = success; return this; }
        public AuthResponseBuilder token(String token) { this.token = token; return this; }
        public AuthResponseBuilder user(UserDTO user) { this.user = user; return this; }
        public AuthResponseBuilder message(String message) { this.message = message; return this; }
        public AuthResponse build() { return new AuthResponse(success, token, user, message); }
    }
}
