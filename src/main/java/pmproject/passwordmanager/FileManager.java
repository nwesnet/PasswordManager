package pmproject.passwordmanager;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class FileManager {
    private String FILE_PATH_LOGIN = "Login.txt";
    private String FILE_PATH_ACCOUNTS = "Accounts.txt";
    private String FILE_PATH_HISTORY = "History.bin";

    public FileManager () {
        createFileIfNotExists(FILE_PATH_LOGIN);
        createFileIfNotExists(FILE_PATH_ACCOUNTS);
        createFileIfNotExists(FILE_PATH_HISTORY);
    }
    public void createFileIfNotExists (String filePath) {
        File file = new File(filePath);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public Login readFromLoginFile() {
        String username = null;
        String password = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH_LOGIN))){
            String line;
            while ((line = reader.readLine()) != null){
                if (line.startsWith("Login: ")){
                    username = line.substring(7);
                }
                else if (line.startsWith("Password: ")){
                    password = line.substring(10);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (username != null && password != null) {
            return new Login(username,password);
        }
        else {
            return null;
        }
    }
    public void writeToLoginFile (String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH_LOGIN))){
            writer.write("Login: " + username + "\n");
            writer.write("Password: " + password + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<Account> readAccount () {
        List<Account> accounts = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH_ACCOUNTS))){
            String line;
            while((line = reader.readLine()) != null){
                String resource = line.substring(line.indexOf(':')+2);
                String login = reader.readLine().substring(7);
                String password = reader.readLine().substring(10);
                LocalDate date = LocalDate.parse(reader.readLine().substring(6));
                accounts.add(new Account(resource,login,password,date));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accounts;
    }
    public void writeAccountToFile (String resource, String login, String password, LocalDate date) {
        String accountDetails = String.format("Resource: %s%nLogin: %s%nPassword: %s%nTime: %s%n", resource, login, password, date);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH_ACCOUNTS,true))){
            writer.write(accountDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void writeAccountsToFile(List<Account> accounts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH_ACCOUNTS, false))) {
            for (Account account : accounts) {
                writer.write("Resource: " + account.getResource() + "\n");
                writer.write("Login: " + account.getLogin() + "\n");
                writer.write("Password: " + account.getPassword() + "\n");
                writer.write("Time: " + account.getDate().toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<String> readHistory () {
        List<String> history = new ArrayList<>();
        try (DataInputStream dis = new DataInputStream(new FileInputStream(FILE_PATH_HISTORY))) {
            while(dis.available()>0){
                String logEntry = dis.readUTF();
                history.add(logEntry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }
    public void writeHistoryToFile (String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        String logEntry = String.format("%s - %s%n",timestamp,message);
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(FILE_PATH_HISTORY,true))) {
            dos.writeUTF(logEntry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void successfulLogin () {
        writeHistoryToFile("Successful login");
    }
    public void failedLogin () {
        writeHistoryToFile("Failed login");
    }
    public void theDataHasBeenChanged (String fielType, String resourceName) {
        writeHistoryToFile(String.format("The %s of %s account has been changed", fielType, resourceName));
    }
    public void accountAdded (String resourceName) {
        writeHistoryToFile(String.format("%s account added",resourceName));
    }
    public void updateLogin(String resourceName, String newLogin) {
        List<Account> accounts = readAccount(); // Read existing accounts
        for (Account account : accounts) {
            if (account.getResource().equals(resourceName)) {
                account.setLogin(newLogin); // Update login
            }
        }
        writeAccountsToFile(accounts); // Rewrite the file with updated accounts
    }
    public void updatePassword(String resourceName, String newPassword) {
        List<Account> accounts = readAccount(); // Read existing accounts
        for (Account account : accounts) {
            if (account.getResource().equals(resourceName)) {
                account.setPassword(newPassword); // Update password
            }
        }
        writeAccountsToFile(accounts); // Rewrite the file with updated accounts
    }
    public void updateResource(String oldResource, String newResource) {
        List<Account> accounts = readAccount();
        for (Account account : accounts) {
            if (account.getResource().equals(oldResource)) {
                account.setResource(newResource);
            }
        }
        writeAccountsToFile(accounts);
    }
    public void deleteAccount(String resourceName){
        List<Account> accounts = readAccount();
        accounts.removeIf(account -> account.getResource().equals(resourceName));
        writeAccountsToFile(accounts);
    }

}
