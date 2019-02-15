package com.company;

import java.io.*;
import java.util.*;
import java.text.*;
import java.nio.file.*;
public class Main {

    private static Scanner scan = new Scanner(System.in);

    private static final String accountsFile = "accounts.txt";
    private static final String logFile = "log.txt";

    static class comp implements Comparator<bankAccount>
    {
        public int compare(bankAccount b1, bankAccount b2)
        {
            return (b1.balance < b2.balance) ? 1 : -1;
        }
    }

    public static bankAccount GetAccount(String name) throws IOException
    {
        List<String> lines = Files.readAllLines(new File("accounts.txt").toPath());
        for (int i = 0; i < lines.size(); i++)
        {
            String[] p = lines.get(i).split(":");
            bankAccount b = new bankAccount(p[0], Double.parseDouble(p[1]));
            if (b.name.equalsIgnoreCase(name)) return b;
        }
        return null;
    }

    public static void UpdateAccount(bankAccount acc) throws IOException
    {
        File file = new File("accounts.txt");
        List<String> lines = Files.readAllLines(file.toPath());
        for (int i = 0; i < lines.size(); i++)
        {
            String[] p = lines.get(i).split(":");
            bankAccount b = new bankAccount(p[0], Double.parseDouble(p[1]));
            if (acc.name.equals(b.name))
            {
                lines.set(i, acc.name + ":" + acc.balance);
            }
        }
        Files.write(file.toPath(), lines);
    }

	public static void AppendToFile(String file, String text) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.append(text);
        writer.close();
    }

    public static void AccountCreation() throws IOException
    {
        NumberFormat fmt = NumberFormat.getNumberInstance();
        fmt.setMinimumFractionDigits(2);
        fmt.setMaximumFractionDigits(2);
        String name;
        do {
            Scanner kbReader = new Scanner(System.in);
            System.out.print("Please enter the name to whom the account belongs. (\"Exit\" to abort) ");
            name = kbReader.nextLine();
            if (!name.equalsIgnoreCase("EXIT") && !name.equalsIgnoreCase("DEBUG")) {
                System.out.print("Please enter the amount of the deposit. ");
                double amount = kbReader.nextDouble();
                System.out.println(" ");

                bankAccount theAccount = new bankAccount(name, amount);
                AppendToFile(accountsFile, theAccount.name + ":" + theAccount.balance + "\n");
            }
        } while (!name.equalsIgnoreCase("EXIT"));
        PromptInput();
    }

    public static String GetTime()
    {
        return "[" + new Date().toString().substring(11, 19) + "]";
    }

    public static void Deposit(bankAccount acc) throws IOException
    {
        System.out.println("Enter the amount to deposit");
        int am = scan.nextInt();
        acc.balance += am;
        UpdateAccount(acc);
        System.out.println("Deposited $" + am + " to account " + acc.name);
        AppendToFile(logFile,  GetTime() + " Deposited $" + am + " to account " + acc.name + "\n");
        PromptInput();
    }

    public static void Withdraw(bankAccount acc) throws IOException
    {
        System.out.println("Enter the amount to withdraw");
        int am = scan.nextInt();
        acc.balance -= am;
        UpdateAccount(acc);
        System.out.println("Withdrew $" + am + " from account " + acc.name);
        AppendToFile(logFile,  GetTime() + " Withdrew $" + am + " from account " + acc.name + "\n");
        PromptInput();
    }

    public static void Drain(bankAccount acc) throws IOException
    {
        System.out.println("Drained $" + acc.balance + " from account " + acc.name + " to Mr.Pennebacker's account\n");
        AppendToFile(logFile, GetTime() + " Drained $" + acc.balance + " from account " + acc.name + " to Mr.Pennebacker's account\n");
        bankAccount mrp = GetAccount("mr.pennebacker");
        if (mrp == null)
        {
            AppendToFile(accountsFile, "mr.pennebacker:" + acc.balance);
        }
        else
        {
            mrp.balance += acc.balance;
            UpdateAccount(mrp);
        }
        acc.balance = 0;
        UpdateAccount(acc);
        PromptInput();
    }

    public static void EditAccount() throws IOException
    {
        System.out.println("Select an account to manage\n");
        List<String> lines = Files.readAllLines(new File("accounts.txt").toPath());
        for (int i = 0; i < lines.size(); i++)
        {
            String line = lines.get(i);
            if (line.length() > 0)
            {
                System.out.println((i + 1) + ") " + line.split(":")[0]);
            }
        }

        int input = scan.nextInt();
        bankAccount acc = GetAccount(lines.get(input - 1).split(":")[0]);

        System.out.println("Editing account: " + acc.name + "\nCurrent balance: $" + acc.balance + "\n1. Deposit\n2. Withdraw\n3. Drain to Mr. Pennebacker");
        int input2 = scan.nextInt();

        switch (input2)
        {
            case 1:
                Deposit(acc);
                break;
            case 2:
                Withdraw(acc);
                break;
            case 3:
                Drain(acc);
                break;
        }
    }

    public static void DebugLog() throws IOException
    {
        List<String> logs = Files.readAllLines(new File(logFile).toPath());
        if (logs.size() > 0)
        {
            for (String line : logs)
            {
                System.out.println(line);
            }
        }
        else
        {
            System.out.println("There are no logs to show.");
        }
        DebugMode();
    }

    public static void ClearLogs() throws IOException
    {
        PrintWriter writer = new PrintWriter(logFile);
        writer.print("");
        writer.close();
        System.out.println("Cleared debug logs.");
    }

    public static void DebugMode() throws IOException
    {
        System.out.println("Welcome to jBank debug mode\n1. View log of transactions\n2. Clear transaction log\n3. Exit to main menu");
        switch (scan.nextInt())
        {
            case 1:
                DebugLog();
                break;
            case 2:
                ClearLogs();
                break;
            case 3:
                PromptInput();
                break;
        }
    }

    public static void PromptInput() throws IOException
    {
        System.out.println("Welcome to jBank\n1. Create Accounts\n2. Deposit/Withdraw\n3. Enter debug mode");

        switch (scan.nextInt())
        {
            case 1:
                AccountCreation();
                break;
            case 2:
                EditAccount();
                break;
            case 3:
                DebugMode();
                break;
        }
    }
	
    public static void main(String[] args) throws IOException
    {
        PromptInput();
    }
}
