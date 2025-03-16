package com.TheOffice.theOffice.classes;

import java.util.List;

public class LocalList {
    private List<Local> localList;

    public LocalList(){
    }
    public LocalList(List<Local> localList) {
        this.localList = localList;
    }
    public List<Local> getLocalList(){
        return localList;
    }

    public void setLocalList(List<Local> localList){
        this.localList = localList;
    }

    @Override
    public String toString() {
        return "LocalList{" +
                "locals=" + localList +
                '}';
    }
}
