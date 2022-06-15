package Controller.Commands;

import Controller.Controller;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import java.util.Observable;

public class GetFromDBCommand extends Observable implements Command{

    @Override
    public void execute() {}

    public FindIterable<Document> execute(String id){
        return Controller.model.DB.getTSbyPlainID(id);
    }
}
