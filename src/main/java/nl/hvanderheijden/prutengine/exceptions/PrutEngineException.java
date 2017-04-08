package nl.hvanderheijden.prutengine.exceptions;


public class PrutEngineException extends Exception {
    private String msg;

    public PrutEngineException(final String msg){
        this.msg = msg;
    }


    @Override
    public String getMessage(){
        return msg;
    }


}
