package com.texthandling;

import java.util.HashMap;

/**
 * Created by œ¿’¿–‹ on 26.04.2015.
 */
public class Coll {
    public int level = 0;

    public int occurance = 0;

    private HashMap<Token, Coll> struct;

    public Coll(HashMap<Token, Coll> struct) {
        this.struct = struct;
    }

    public Coll(){
        this.struct = new HashMap<Token, Coll>();
        occurance = 1;
    }

    public Coll(Token token, Coll coll){
        this();
        this.struct.put(token, coll);
        if (coll != null){
            occurance = coll.occurance;
        }
    }

    public HashMap<Token, Coll> get(){
        return struct;
    }

    public boolean Equals(Coll coll){
        if(this.struct.size() != coll.get().size()){
            return false;
        }
        boolean ans = true;

        for (Token token : struct.keySet()) {
            if(!coll.get().containsKey(token)){
                return false;
            }
            ans &= (struct.get(token) == null && coll.get().get(token) == null) || struct.get(token).Equals(coll.get().get(token));
        }
        return ans;
    }

    public void Unite(Coll coll){
        for (Token token : coll.get().keySet()) {
            if(struct.containsKey(token)){
                if(struct.get(token) == null && coll.get().get(token) == null)
                    return;
                struct.get(token).occurance += coll.get().get(token).occurance;
                struct.get(token).Unite(coll.get().get(token));
            } else {
                struct.put(token, coll.get().get(token));
            }
        }
    }




}
