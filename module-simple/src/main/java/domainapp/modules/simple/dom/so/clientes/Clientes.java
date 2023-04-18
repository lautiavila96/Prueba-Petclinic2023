package domainapp.modules.simple.dom.so.clientes;

import java.util.List;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import domainapp.modules.simple.dom.so.clientes.Cliente;
import domainapp.modules.simple.dom.so.clientes.ClienteRepository;



import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.BookmarkPolicy;

import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.query.Query;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

import lombok.RequiredArgsConstructor;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.types.Name;

@Named(SimpleModule.NAMESPACE + ".Clientes")
@DomainService(nature = NatureOfService.VIEW)
@Priority(PriorityPrecedence.EARLY)
@RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class Clientes {

    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final ClienteRepository clienteRepository;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Cliente create(
            @Name final String name) {
        return repositoryService.persist(Cliente.withName(name));
    }


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public List<Cliente> findByNameLike(
            @Name final String name) {
        return repositoryService.allMatches(
                Query.named(Cliente.class, Cliente.NAMED_QUERY__FIND_BY_NAME_LIKE)
                        .withParameter("name", "%" + name + "%"));
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT, promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public List<Cliente> findByName(
            @Name final String name
    ) {
        return clienteRepository.findByNameContaining(name);
    }


    public Cliente findByNameExact(final String name) {
        return clienteRepository.findByName(name);
    }



    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<Cliente> listAll() {
        return clienteRepository.findAll();
    }



    public void ping() {
        jpaSupportService.getEntityManager(Cliente.class)
                .mapEmptyToFailure()
                .mapSuccessAsNullable(entityManager -> {
                    final TypedQuery<Cliente> q = entityManager.createQuery(
                                    "SELECT p FROM Cliente p ORDER BY p.name",
                                    Cliente.class)
                            .setMaxResults(1);
                    return q.getResultList();
                })
                .ifFailureFail();
    }


}
