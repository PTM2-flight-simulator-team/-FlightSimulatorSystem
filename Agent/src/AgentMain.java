import Controller.MyController;
import Model.Commands.Command;
import Model.Commands.instructionCommand;
import Model.MyModel;

import java.util.Scanner;

public class AgentMain {
    public static void main(String[] args) {
        MyModel model = new MyModel();
        MyController controller = new MyController(model);
        System.out.println("main thread rip");
//        String line;
//        Scanner in = new Scanner(System.in);
//        while(!(line = in.nextLine()).equals("stop"))
//        {
//            String[] result = line.split(":");
//            if(result[0].equals("set"))
//            {
//                // check if the property is legit -------------------
//                instructionCommand c = (instructionCommand) controller.getModel().getMyCommands().get("instructions");
//                c.setCommand(result[1]);
//                c.execute();
//                System.out.println("setter");
//                continue;
//            }
//            if(result[0].equals("printstream"))
//            {
//                System.out.println("printstream:");
//                controller.getModel().getMyCommands().get("printstream").execute();
//                continue;
//            }
//            if(result[0].equals("analytic"))
//            {
//                System.out.println("analytics:");
//                controller.getModel().getMyCommands().get("analytics").execute();
//                continue;
//            }
//            if(result[0].equals("reset")){
//                System.out.println("reset:");
//                controller.getModel().getMyCommands().get("reset").execute();
//                continue;
//            }
//            if(result[0].equals("shutdown")){
//                System.out.println("shutdown:");
//                controller.getModel().getMyCommands().get("shutdown").execute();
//                continue;
//            }
////            if(result[0].equals("autopilot"))
////            {
////                controller.getNetworkManager().getSocketHandler().getFgHandler().AutoPilot();
////            }
//        }
    }
}