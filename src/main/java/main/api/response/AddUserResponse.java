package main.api.response;

import java.util.LinkedHashMap;

public class AddUserResponse {

    private boolean result;
    private LinkedHashMap<String, String> errors = new LinkedHashMap<>();

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public LinkedHashMap<String, String> getErrors() {
        return errors;
    }

    public void setErrors(String name, String pass, String mail) {
        this.errors.put("Имя пользователя: ", name);
        this.errors.put("Пароль пользователя: ", pass);
        this.errors.put("e-mail пользователя: ", mail);
    }
}
