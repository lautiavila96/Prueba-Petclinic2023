package domainapp.modules.simple.dom.so.clientes;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNameContaining(final String name);

    Cliente findByName(final String name);
}
