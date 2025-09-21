package school.sptech.prova_ac1;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByEmailAndIdNot(String email, Integer id);
    boolean existsByCpfAndIdNot(String cpf, Integer id);
}
