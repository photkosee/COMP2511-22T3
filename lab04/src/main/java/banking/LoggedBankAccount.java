package banking;

import java.util.ArrayList;
import java.util.List;

public class LoggedBankAccount extends BankAccount {

    List<String> log = new ArrayList<>();

    @Override
    public void deposit(double num) {
        super.deposit(num);
        log.add("deposit " + num);
    }

    @Override
    public void withdraw(double num) {
        super.withdraw(num);
        log.add("withdraw " + num);
    }
}
