
import java.util.Scanner;

public class Bank {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Login login = new Login();
        Account account = null;
        boolean loggedIn = false;

        while (true) {
            System.out.println("------------------------------------------------");
            System.out.println("____ Welcome to the bank!____");
            System.out.println("\t1. Login");
            System.out.println("\t2. Create new account");
            System.out.println("\t0. Exit");
            System.out.print("------------------------------------------------\n\n ... ");
            int choice = input.nextInt();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (choice == 1) {
                System.out.println("Enter your username:");
                String username = input.next();

                System.out.println("Enter your password:");
                String password = input.next();

                if (login.validUser(username, password)) {
                    login = new Login(username, password);
                    account = new Account(login, login.getAccount(), login.getBalance());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    loggedIn = true;
                } else {
                    System.out.println("Invalid username or password!  :) ");
                }
            } else if (choice == 2) {
                System.out.println("now Answer The following questions in order to create a new account : ");
                login.createAccount();

            } else if (choice == 0) {
                System.out.println("_ _ _ _ _ _ _  Goodbye! :)  _ _ _ _ _ _ _  \n\n");
                break;
            } else {
                System.out.println("Invalid choice!  :) ");
            }

            if (loggedIn) {
                while (account.isActive()) {
                    System.out.println("\n# __ What would you like to do? __");
                    System.out.println("------------------------------------------------");
                    System.out.println("\t1. Deposit");
                    System.out.println("\t2. Withdrawal");
                    System.out.println("\t3. View balance");
                    System.out.println("\t4. Mini statement");
                    System.out.println("\t5. Active services");
                    System.out.println("\t6. Bank details");
                    System.out.println("\t7. Change password");
                    System.out.println("\t8. Take or give loan");
                    System.out.println("\t9. Check loan eligibility");
                    System.out.println("\t0. Logout");
                    System.out.print("------------------------------------------------\n\n ... ");

                    choice = input.nextInt();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (choice == 1) {
                        System.out.println("Enter the amount to deposit:");
                        double amount = input.nextDouble();
                        account.deposit(amount);
                    } else if (choice == 2) {
                        System.out.println("Enter the amount to withdraw:");
                        double amount = input.nextDouble();
                        account.withdraw(amount);
                    } else if (choice == 3) {
                        System.out.println("Your balance is: " + account.viewBalance());
                    } else if (choice == 4) {
                        account.miniStatement();
                        account.printStatement();
                    } else if (choice == 5) {
                        System.out.println("The following services are active: ");
                        account.activeServices();
                    } else if (choice == 6) {
                        account.bankDetails();
                    } else if (choice == 7) {
                        account.changePassword();
                    } else if (choice == 8) {

                        System.out.println("      --------------------------");
                        System.out.println("\t1.take loan \n\t2.give loan ");
                        System.out.print("      --------------------------\n\t... ");
                        int ch = input.nextInt();
                        if (ch == 1)
                            account.takeLoan(account.isLoanable(login));
                        else if (ch == 2) {
                            account.giveLoan(account.isLoanable(login));
                        } else {
                            System.out.println("Invalid choice! :) ");
                        }

                    } else if (choice == 9) {
                        boolean loanEligibility = account.isLoanable(login);
                        if (loanEligibility) {
                            System.out.println("Congratulations! You are eligible");
                        } else {
                            System.out.println("Sorry, you are not eligible !  :)");
                        }
                    } else if (choice == 0) {
                        account.logout();
                    } else {
                        System.out.println("Invalid choice! :) ");
                    }
                }
            }
        }
    }
}
