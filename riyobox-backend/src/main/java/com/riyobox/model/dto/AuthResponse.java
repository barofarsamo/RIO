package com.riyobox.model.dto;

import com.riyobox.model.User;

public class AuthResponse {
    private boolean success;
    private String token;
    private String message;
    private UserDTO user;

    public AuthResponse() {}

    public static AuthResponseBuilder builder() { return new AuthResponseBuilder(); }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }

    public static class UserDTO {
        private String id;
        private String email;
        private String name;
        private String subscriptionPlan;
        private boolean enabled;

        public UserDTO() {}
        public static UserDTOBuilder builder() { return new UserDTOBuilder(); }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSubscriptionPlan() { return subscriptionPlan; }
        public void setSubscriptionPlan(String subscriptionPlan) { this.subscriptionPlan = subscriptionPlan; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public static UserDTO fromUser(User user) {
            if (user == null) return null;
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setName(user.getName());
            dto.setSubscriptionPlan(user.getSubscriptionPlan());
            dto.setEnabled(user.isEnabled());
            return dto;
        }

        public static class UserDTOBuilder {
            private UserDTO dto = new UserDTO();
            public UserDTOBuilder id(String id) { dto.id = id; return this; }
            public UserDTOBuilder email(String email) { dto.email = email; return this; }
            public UserDTOBuilder name(String name) { dto.name = name; return this; }
            public UserDTOBuilder subscriptionPlan(String p) { dto.subscriptionPlan = p; return this; }
            public UserDTOBuilder enabled(boolean e) { dto.enabled = e; return this; }
            public UserDTO build() { return dto; }
        }
    }

    public static class AuthResponseBuilder {
        private AuthResponse r = new AuthResponse();
        public AuthResponseBuilder success(boolean s) { r.success = s; return this; }
        public AuthResponseBuilder token(String t) { r.token = t; return this; }
        public AuthResponseBuilder message(String m) { r.message = m; return this; }
        public AuthResponseBuilder user(UserDTO u) { r.user = u; return this; }
        public AuthResponse build() { return r; }
    }
}
