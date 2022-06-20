package Model.dataHolder;

public class MyResponse<T> {
    public T value;
    public ResonseType type;

    public MyResponse(T val, ResonseType type){
        this.value = val;
        this.type = type;
    }
}

