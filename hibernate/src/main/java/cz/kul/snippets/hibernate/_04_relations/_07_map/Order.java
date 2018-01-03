/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.hibernate._04_relations._07_map;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 *
 * @author kulhalad
 */
@Entity(name = "order_map")
public class Order implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id_order_map")
    private Long id;
    
    /**
     * By @MapKey you specify property name, which will be used
     * as map key. If you leave the annotation empty, the primary
     * key of Detail1 will be used.
     */
    @OneToMany(mappedBy = "order", orphanRemoval = true)
    @MapKey(name = "value")
    @Cascade(CascadeType.ALL)
    private Map<String, Detail1> details1Map = new HashMap<>();
    
    /**
     * By @MapKeyColumn you specify column name, which will be used
     * as map key.
     */
    @OneToMany(mappedBy = "order", orphanRemoval = true)
    @MapKeyColumn(name = "detail2_value")
    @Cascade(CascadeType.ALL)
    private Map<String, Detail2> details2Map = new HashMap<>();
    
    /**
     * By @MapKeyJoinColumn you specify the entity, which will be map key.
     * The entity must be related to mapped child entity (value of the map).
     */
    @OneToMany(mappedBy = "order", orphanRemoval = true)
    @MapKeyJoinColumn(name = "id_detail_type")
    @Cascade(CascadeType.ALL)
    private Map<DetailType, Detail3> details3Map=  new HashMap<>();

    public Map<DetailType, Detail3> getDetails3Map() {
        return details3Map;
    }

    public void setDetails3Map(Map<DetailType, Detail3> details3Map) {
        this.details3Map = details3Map;
    }

    
    public Map<String, Detail2> getDetails2Map() {
        return details2Map;
    }

    public void setDetails2Map(Map<String, Detail2> details2Map) {
        this.details2Map = details2Map;
    }
    
    public Order() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, Detail1> getDetails1Map() {
        return details1Map;
    }

    public void setDetails1Map(Map<String, Detail1> details1Map) {
        this.details1Map = details1Map;
    }
    
    public void addDetail1(Detail1 d) {
        d.setOrder(this);
        details1Map.put(d.getValue(), d);
    }
    
    public void addDetail2(Detail2 d) {
        d.setOrder(this);
        details2Map.put(d.getValue(), d);
    }
    
    public void addDetail3(Detail3 d) {
        d.setOrder(this);
        details3Map.put(d.getDetailType(), d);
    }
    
    
    
}
