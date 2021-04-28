package cz.kul.snippets.jpa.example10_fetchingAllInherintaceHierarchy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
