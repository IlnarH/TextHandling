package com.texthandling;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by ������ on 26.04.2015.
 */
public class Coll implements Serializable {
    public int level = 0;

    public int occurance = 0;

//    private HashMap<Token, Coll> struct;

    private Token node;

    private HashMap<Coll, Coll> childs;


//    public Coll(HashMap<Token, Coll> struct) {
//        this.struct = struct;
//    }

    public Coll(){
        this.node = new Token("");
        this.childs = new HashMap<Coll, Coll>();
        occurance = 1;
    }

    public Coll(Token token, Coll coll){
        this();
        this.node = token;
        if (coll != null){
            occurance = coll.occurance;
            this.childs.put(coll, coll);
        } else {
            this.childs = null;
        }
    }

    public Coll(Token token){
        this();
        this.node = token;
        this.childs = null;
    }

    public HashMap<Coll, Coll> getChilds(){
        return childs;
    }

    public Token getNode(){
        return this.node;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return this.node.equals(((Coll)obj).node);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.node.hashCode();
    }

    //    public boolean Equals(Coll coll){
//        if(this.struct.size() != coll.get().size()){
//            return false;
//        }
//        boolean ans = true;
//
//        for (Token token : struct.keySet()) {
//            if(!coll.get().containsKey(token)){
//                return false;
//            }
//            ans &= (struct.get(token) == null && coll.get().get(token) == null) || struct.get(token).Equals(coll.get().get(token));
//        }
//        return ans;
//    }

//    public void Unite(Coll coll){
//        for (Token token : coll.get().keySet()) {
//            if(struct.containsKey(token)){
//                if(struct.get(token) == null && coll.get().get(token) == null)
//                    return;
//                struct.get(token).occurance += coll.get().get(token).occurance;
//                struct.get(token).Unite(coll.get().get(token));
//            } else {
//                struct.put(token, coll.get().get(token));
//            }
//        }
//    }

    public void Unite(Coll coll){
        if (!this.node.equals(coll.node))
            return;
        if (this.childs == null && coll.childs == null){
            occurance += 1;
            return;
        }
        for (Coll childNode : coll.getChilds().keySet()){
            if(childs.containsKey(childNode)){
                childs.get(childNode).Unite(childNode);
                occurance += childNode.occurance;
            } else {
                childs.put(childNode, null);
                occurance += childNode.occurance;
            }
        }
    }





}
