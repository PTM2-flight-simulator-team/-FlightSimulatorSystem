import Controller.MyController;
import Model.MyModel;

public class AgentMain {
    public static void main(String[] args) {
        MyModel model = new MyModel();
        MyController controller = new MyController(model);
        System.out.println("main thread rip");
    }
}