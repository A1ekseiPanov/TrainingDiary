package ru.panov;

import ru.panov.util.ConnectionUtil;
import ru.panov.util.LiquibaseUtil;

public class TrainingDiaryApp {
    public static void main(String[] args) {
        LiquibaseUtil.update(ConnectionUtil.get());
        Context context = new Context();
        context.run();
    }
}