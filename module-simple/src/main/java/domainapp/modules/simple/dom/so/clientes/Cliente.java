package domainapp.modules.simple.dom.so.clientes;


import java.util.Comparator;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.isis.applib.jaxb.PersistentEntityAdapter;
//import org.apache.isis.persistence.jpa.applib.integration.IsisEntityListener;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;

import static org.apache.causeway.applib.annotation.SemanticsOf.IDEMPOTENT;
import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.types.Name;
import domainapp.modules.simple.types.Notes;

import org.apache.isis.persistence.jpa.applib.integration.IsisEntityListener;


@Entity
@Table(
        //schema="pets",
        schema= SimpleModule.SCHEMA,
        uniqueConstraints = {
                @UniqueConstraint(name = "Cliente__name__UNQ", columnNames = {"name"})
        }
)
@NamedQueries({
        @NamedQuery(
                name = Cliente.NAMED_QUERY__FIND_BY_NAME_LIKE,
                query = "SELECT so " +
                        "FROM Cliente so " +
                        "WHERE so.name LIKE :name"
        )
})
@EntityListeners(IsisEntityListener.class)
//@DomainObject(logicalTypeName = "so.Cliente", entityChangePublishing = Publishing.ENABLED)
@DomainObject(entityChangePublishing = Publishing.ENABLED)
@DomainObjectLayout()
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)

public class Cliente implements Comparable<Cliente> {

static final String NAMED_QUERY__FIND_BY_NAME_LIKE = "Cliente.findByNameLike";

@Id
@GeneratedValue(strategy = GenerationType.AUTO)
@Column(name = "id", nullable = false)
private Long id;

@Version
@Column(name = "version", nullable = false)
@PropertyLayout(fieldSetId = "metadata", sequence = "999")
@Getter @Setter
private long version;

public static Cliente withName(final String name) {
        val cliente = new Cliente();
        cliente.setName(name);
        return cliente;
        }

@Inject @Transient RepositoryService repositoryService;
@Inject @Transient TitleService titleService;
@Inject @Transient MessageService messageService;



@Title
@Name
@Column(length = Name.MAX_LEN, nullable = false)
@Getter @Setter @ToString.Include
@PropertyLayout(fieldSetId = "name", sequence = "1")
private String name;

@Notes
@Column(length = Notes.MAX_LEN, nullable = true)
@Getter @Setter
@Property(commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@PropertyLayout(fieldSetId = "name", sequence = "2")
private String notes;


@Action(semantics = IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "name", promptStyle = PromptStyle.INLINE)
public Cliente updateName(
@Name final String name) {
        setName(name);
        return this;
        }
public String default0UpdateName() {
        return getName();
        }
public String validate0UpdateName(String newName) {
        for (char prohibitedCharacter : "&%$!".toCharArray()) {
        if( newName.contains(""+prohibitedCharacter)) {
        return "Character '" + prohibitedCharacter + "' is not allowed.";
        }
        }
        return null;
        }


@Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
@ActionLayout(
        associateWith = "name", position = ActionLayout.Position.PANEL,
        describedAs = "Deletes this object from the persistent datastore")
public void delete() {
final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
        }



private final static Comparator<Cliente> comparator =
        Comparator.comparing(Cliente::getName);

@Override
public int compareTo(final Cliente other) {
        return comparator.compare(this, other);
        }



        }
