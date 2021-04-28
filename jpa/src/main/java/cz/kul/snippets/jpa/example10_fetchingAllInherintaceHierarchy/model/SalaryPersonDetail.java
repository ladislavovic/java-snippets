package cz.kul.snippets.jpa.example10_fetchingAllInherintaceHierarchy.model;

import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfiles;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@FetchProfiles({
    @FetchProfile(
        name = SalaryPersonDetail.FP_SALARY,
        fetchOverrides = { @FetchProfile.FetchOverride(entity = SalaryPersonDetail.class, association="salary", mode= FetchMode.JOIN)})
})
public class SalaryPersonDetail extends PersonDetail<Salary> {

    public static final String FP_SALARY = "salary_person_detail__salary";

    @ManyToOne(fetch = FetchType.LAZY)
    private Salary salary;

    public Salary getDetail() {
        return salary;
    }
    
    public void setDetail(Salary salary) {
        this.salary = salary;
    }

}
