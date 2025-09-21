package school.sptech.prova_ac1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository repository;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> buscarTodos() {
        List<Usuario> usuarios = repository.findAll();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        if (repository.existsByEmail(usuario.getEmail()) ||
                repository.existsByCpf(usuario.getCpf())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Usuario salvo = repository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        Optional<Usuario> usuario = repository.findById(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtro-data")
    public ResponseEntity<List<Usuario>> buscarPorDataNascimento(
            @RequestParam LocalDate nascimento) {
        List<Usuario> filtrados = repository.findAll()
                .stream()
                .filter(u -> u.getDataNascimento().isAfter(nascimento))
                .toList();

        if (filtrados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(filtrados);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(
            @PathVariable Integer id,
            @RequestBody Usuario usuarioAtualizado) {

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        if (repository.existsByEmailAndIdNot(usuarioAtualizado.getEmail(), id) ||
                repository.existsByCpfAndIdNot(usuarioAtualizado.getCpf(), id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        usuarioAtualizado.setId(id);
        Usuario salvo = repository.save(usuarioAtualizado);
        return ResponseEntity.ok(salvo);
    }
}
