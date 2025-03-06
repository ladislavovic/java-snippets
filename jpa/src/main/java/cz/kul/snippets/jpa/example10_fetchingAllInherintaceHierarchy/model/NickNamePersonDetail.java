package cz.kul.snippets.jpa.example10_fetchingAllInherintaceHierarchy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class NickNamePersonDetail extends PersonDetail<String> {

    @Column
    private String nickName;

    public String getDetail() {
        return nickName;
    }

    public void setDetail(String nickName) {
        this.nickName = nickName;
    }

}
