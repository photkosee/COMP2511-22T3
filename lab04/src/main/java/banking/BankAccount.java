package banking;

/**
 * BankAccount
 *
 * @author Phot Koseekrainiramon (5387411)
 * @version 08/10/2022
 *
 */
public class BankAccount {
    
    private double balance;

    public double getBalance() {
        return balance;
    }

    /**
     * @pre num >= 0
     * @post balance >= 0
     */
    public void deposit(double num) {
        this.balance += num;
    }

    /**
     * @pre num >= balance
     * @post balance >= 0
     */
    public void withdraw(double num) {
        this.balance -= num;
    }
}
