package ru.aston.homework05.models;

public enum NotificationType {
    CREATE("Регистрация аккаунта", "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан."),
    DELETE("Удаление аккаунта", "Здравствуйте! Ваш аккаунт был удалён.");

    private final String subject;
    private final String text;

    NotificationType(String subject, String text) {
        this.subject = subject;
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public static NotificationType fromString(String action) {
        for (NotificationType type : NotificationType.values()) {
            if (type.name().equalsIgnoreCase(action)) {
                return type;
            }
        }
        return null;
    }
}