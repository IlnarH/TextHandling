package com.texthandling;

/**
 * Created by œ¿’¿–‹ on 24.04.2015.
 */
public class Token{

    private TokenType condition;
    private String _string;

    public Token(String _string, TokenType condition){
        this._string = _string;
        this.condition = condition;
    }

    public Token(String _string){
        this(_string, TokenType.token);
    }

    public String getString() {
        return _string;
    }

    public TokenType getCondition() {
        return condition;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if(this._string.equals(((Token)obj)._string)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this._string.hashCode();
    }
}
