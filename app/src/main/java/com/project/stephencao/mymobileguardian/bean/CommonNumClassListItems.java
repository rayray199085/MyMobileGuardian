package com.project.stephencao.mymobileguardian.bean;

import java.util.List;

public class CommonNumClassListItems {
    private String name;
    private int id;
    private List<CommonNumChildClassListItems> commonNumChildClassListItemsList;

    public List<CommonNumChildClassListItems> getCommonNumChildClassListItemsList() {
        return commonNumChildClassListItemsList;
    }

    public void setCommonNumChildClassListItemsList(List<CommonNumChildClassListItems> commonNumChildClassListItemsList) {
        this.commonNumChildClassListItemsList = commonNumChildClassListItemsList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
