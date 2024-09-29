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
    public StringBuilder readHistory () {
        StringBuilder history = new StringBuilder();
        try (DataInputStream dis = new DataInputStream(new FileInputStream(FILE_PATH_HISTORY))) {
            while(dis.available()>0){
                String logEntry = dis.readUTF();
                history.append(logEntry);
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
}
