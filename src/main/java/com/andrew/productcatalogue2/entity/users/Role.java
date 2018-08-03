package com.andrew.productcatalogue2.entity.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


/** A role that a user can have. Getters/Setter exist for all fields.
 *
 * @author Andrew Au-Young
 * @version 1.0.0
 * */
@Entity
@Table(name="roles")
@Getter
public class Role {

    public enum RoleValue {

        ROLE_ADMIN("Admin"),
        ROLE_SUPER("Super");

        /** A UI friendly, readable version of the enum value*/
        private String displayName;

        RoleValue(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }


    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Setter
    private Integer id;

    @Column(nullable=false, unique=true)
    private String name;

    @Column(nullable=false, unique=true)
    @Setter
    private String displayName;

    @ManyToMany(mappedBy="roles")
    @Setter
    private List<User> users;

    /**
     * @param roleValue A value of type {@link RoleValue}
     */
    public void setName(Role.RoleValue roleValue) {
        name = roleValue.toString();
    }

}
