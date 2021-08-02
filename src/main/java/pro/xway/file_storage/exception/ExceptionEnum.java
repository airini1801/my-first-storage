package pro.xway.file_storage.exception;

import java.util.Optional;

public enum ExceptionEnum {
    FILE_NOT_FOUND("Файл не найден"),
    FILE_NOT_UPLOAD("Файл не загружен"),
    CATEGORY_ID_NOT_EXIST("Id категории не найдено"),
    PASSWORD_NOT_EQUAL("Пароль не совпадает"),
    USER_IS_EXIST("Такой логин уже используется"),
    EMAIL_IS_EXIST("Такой e-mail уже используется");




    private final String message;

    ExceptionEnum(String message) {
        this.message = message;
    }

    public static Optional<ExceptionEnum> search(String exceptionName) {
        for (ExceptionEnum value : ExceptionEnum.values()) {
            if (exceptionName.equals(value.name())) {
                return Optional.of(value);
            }
        }

        return Optional.empty();
    }


    public String getMessage() {
        return message;
    }


}
