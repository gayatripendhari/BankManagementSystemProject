
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

import javax.swing.UIDefaults.ActiveValue;

public class Account {
    private static final String FILE_NAME = "AccountData.txt";

    private String accountNumber;
    private double balance;
    private Login usr;

    Account(Login usr, String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.usr = usr;
    }

    Account() {
        System.out.println("Please provide account details!");
    }

    boolean isActive() {
        return usr != null;
    }

    boolean logout() {
        usr = null;
        return true;
    }

    Scanner sc = new Scanner(System.in);

    void deposit(double amount) {
        balance += amount;
        // Saving transaction details to file
        saveTransactionDetails("Deposit", amount);
        updateBalance(accountNumber, balance);
        System.out.println("Deposit of $" + amount + " successful. Current balance is $" + balance);
    }

    void withdraw(double amount) {
        if (balance < amount) {
            System.out.println("Insufficient funds. Please try again.");
            return;
        }
        balance -= amount;
        updateBalance(accountNumber, balance);
        // Saving transaction details to file
        saveTransactionDetails("Withdrawal", amount);
        System.out.println("Withdrawal of $" + amount + " successful. Current balance is $" + balance);
    }

    double viewBalance() {
        return balance;
    }

    void miniStatement() {
        // Reading last 5 transaction details from file
        List<String> transactions = readLastNTransactions(5);
        int x = 5;
        if (transactions.size() < 5)
            x = transactions.size();
        System.out.println("Last " + x + " transaction details transactions :");
        for (String transaction : transactions) {
            System.out.println(transaction);
        }
        transactions.clear();
    }

    void printStatement() {
        // Reading last 5 transaction details from file
        List<String> transactions = printTransactions(5);
        int n = transactions.size();
        if (n == 0) {
            System.out.println("No transactions found.");
            return;
        }
        System.out.println("+-----------------------------------------------------------------------------+");
        System.out.printf("| %-30s | %-15s | %-10s | %-10s |\n", "Date/Time", "Transaction Type", "Amount", "Balance");
        System.out.println("+-----------------------------------------------------------------------------+");
        double balance = 0.0;
        for (int i = n - 1; i >= 0; i--) {
            String transaction = transactions.get(i);
            String[] fields = transaction.split(",");
            String date = fields[3];
            String type = fields[1];
            double amount = Double.parseDouble(fields[2]);
            balance += amount;
            String amountStr = String.format("$%.2f", amount);
            String balanceStr = fields[4].substring(fields[4].indexOf(':') + 2); // extract balance value from string
            balance = Double.parseDouble(balanceStr);
            System.out.printf("| %-30s | %-16s | %-10s | %-10s |\n", date, type, amountStr, balanceStr);
        }
        System.out.println("+-----------------------------------------------------------------------------+");
        transactions.clear();
    }

    private List<String> printTransactions(int n) {
        List<String> transactions = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
            String line;

            while ((line = br.readLine()) != null && transactions.size() < n) {
                String[] transactionData = line.split(",");
                if (transactionData[0].equals(accountNumber)) {
                    transactions.add(line);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    private void saveTransactionDetails(String type, double amount) {
        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            LocalDateTime now = LocalDateTime.now();
            bufferedWriter.write(accountNumber + "," + type + "," + amount + "," + now + ",balance : " + balance);
            bufferedWriter.newLine();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // number of transactions cout all
    private int numberOfTransactions() {
        List<String> transactions = new ArrayList<>();
        int cnt = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
            String line;

            while ((line = br.readLine()) != null) {
                String[] transactionData = line.split(",");
                if (transactionData[0].equals(accountNumber)) {
                    cnt++;
                    transactions.add(transactionData[1] + " of $" + transactionData[2] + " on " + transactionData[3]);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cnt;
    }

    private List<String> readLastNTransactions(int n) {
        List<String> transactions = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
            String line;

            while ((line = br.readLine()) != null && transactions.size() < n) {
                String[] transactionData = line.split(",");
                if (transactionData[0].equals(accountNumber)) {
                    transactions.add(transactionData[1] + " of $" + transactionData[2] + " on " + transactionData[3]);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    private void updateBalance(String accountNumber, double newBalance) {
        try {
            File file = new File("userData.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[7].equals(accountNumber)) {
                    userData[11] = Double.toString(newBalance);
                    line = String.join(",", userData);
                }
                sb.append(line).append("\n");
            }
            br.close();

            FileWriter fw = new FileWriter(file);
            fw.write(sb.toString());
            fw.close();
            System.out.println("Balance updated successfully for account number " + accountNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateLoanBalance(String accountNumber, double Loan, boolean tag) {
        try {
            File file = new File("userData.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[7].equals(accountNumber) && tag) {
                    double x = Double.parseDouble(userData[10]) + Loan;
                    userData[10] = Double.toString(x);
                    x = Double.parseDouble(userData[11]) + Loan;
                    userData[11] = Double.toString(x);
                    userData[9] = Boolean.toString(true);
                    line = String.join(",", userData);
                }
                if (userData[7].equals(accountNumber) && !tag) {
                    double x = Double.parseDouble(userData[10]) - Loan;
                    userData[10] = Double.toString(x);
                    if (x == 0) {
                        userData[9] = Boolean.toString(false);
                    }
                    line = String.join(",", userData);
                }
                sb.append(line).append("\n");
            }
            br.close();

            FileWriter fw = new FileWriter(file);
            fw.write(sb.toString());
            fw.close();
            System.out.println(" Loan Balance updated successfully for account number " + accountNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isLoanable(Login user) {
        if (numberOfTransactions() > 10 && !user.loanStatus)
            return true;
        return false;
    }

    void takeLoan(boolean lStatus) {
        if (lStatus) {
            double amt = 20000.0;
            System.out.println("Appoved loan amount is : " + amt);
            System.out.print("if you want to continue give positive number :) ... ");
            int choice = 0;
            choice = sc.nextInt();
            if (choice < 1)
                return;
            balance = usr.getBalance();
            updateLoanBalance(accountNumber, amt, true);
            saveTransactionDetails("Loan taken ", amt);
        } else {
            System.out.println("Your not eligible for Loan ");
        }
    }

    void giveLoan(boolean lStatus) {
        if (!lStatus) {
            double amt = 20000.0;
            System.out.println("your amount is : " + amt);
            System.out.print("if you want to continue : ");
            int choice = 0;
            choice = sc.nextInt();
            if (choice < 1)
                return;
            updateLoanBalance(accountNumber, amt, false);
            saveTransactionDetails("Loan cleared ", amt);
        } else {
            System.out.println("Your not eligible for Loan ");
        }
    }

    private boolean checkPassword(String a, int b) {
        boolean flg = false;
        try {
            File file = new File("userData.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[7].equals(accountNumber) && userData[1].equals(a)) {
                    userData[1] = Integer.toString(b);
                    System.out.println(b);
                    flg = true;
                    line = String.join(",", userData);

                }
                sb.append(line).append("\n");
            }
            br.close();

            FileWriter fw = new FileWriter(file);
            fw.write(sb.toString());
            fw.close();
            if (flg)
                return flg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    void changePassword() {
        String a;
        int b;
        System.out.print("\nEnter old password: ");
        a = sc.nextLine();
        System.out.println("\nEnter new password: ");
        b = sc.nextInt();
        boolean x = checkPassword(a, b);
        if (x) {
            saveTransactionDetails("Pin changed ", 0);
            System.out.println("password updated successfully");
        } else {
            System.out.println("Wrong password !");
        }
    }

    void bankDetails() {
        System.out.println("Welcome to Komal Secure Bank Pvt. Ltd.");
        System.out.println("We are one of the leading banks in India.");
        System.out.println(
                "Our services include: savings accounts, current accounts, fixed deposits, loans, and credit cards.");
        System.out.println(
                "Our branches are located in various cities across India, including Mumbai, Delhi, Bangalore, Chennai, Kolkata, and Hyderabad.");
        System.out.println("For more information, visit our website at www.komalsecurebank.com.");
    }

    void activeServices() {
        System.out.println("\t- emails");
        System.out.println("\t- SMS");
        if (usr.isDebitCard())
            System.out.println("\t- debit card");
        if (usr.isCreditCard())
            System.out.println("\t- credit card");
        if (usr.isChqueBook())
            System.out.println("\t- cheque Book");
        if (usr.isInternationalTransaction())
            System.out.println("\t- Internationals transactions");

    }

    /*
     * if user give the credentials then check validations
     * if user is new user then create new account
     * check valid user
     * deposit
     * withdrawal
     * view balance
     * miniStatment
     * active services
     * bankDetails
     * change password
     * take or give loan
     * loanable?
     */

}