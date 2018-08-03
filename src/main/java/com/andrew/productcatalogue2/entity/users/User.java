package com.andrew.productcatalogue2.entity.users;

import lombok.Data;
import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;


/** A User that logs in to the app. Getters and setters are available for all fields.
 *
 * @author Andrew Au-Young
 * @version 1.0.0
 * */
@Entity
@Table(name="users")
@Data
public class User
{
    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
    @SequenceGenerator(name="USERID_SEQ", sequenceName="userId_sequence", initialValue=5)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USERID_SEQ")
    private Integer id;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String password;

    /** Name to be displayed on the user's account */
    @Column(nullable=false)
    private String displayName;

    @ManyToMany(cascade=CascadeType.MERGE)
    @JoinTable(
            name="user_role",
            joinColumns={@JoinColumn(name="USER_ID",
                    referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="ROLE_ID",
                    referencedColumnName="ID")})
    private List<Role> roles;

/** allows the list of roles to be displayed on the webpage in a more presentable way */
    public String rolesToString() {
        List<String> rolesList = roles.stream().map(r->r.getDisplayName()).collect(Collectors.toList());
        return rolesList.toString().replace("[" ,"").replace("]" ,"");

    }


}