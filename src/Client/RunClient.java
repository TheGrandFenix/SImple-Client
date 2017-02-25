package Client;

public class RunClient {
    public static void main(String[] args) {
        System.out.println("Starting chat client...");
        ClientCore clientProcess = new ClientCore("localhost", 2508);
        new Thread(clientProcess).start();
    }
}
