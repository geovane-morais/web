package web.api.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import web.api.app.domain.entity.Cliente;
import web.api.app.domain.repositorio.Clientes;

import java.util.List;

@SpringBootApplication
public class AppApplication {

	@Bean
	public CommandLineRunner init(@Autowired Clientes clientes){
		return args -> {
			System.out.println("\n***Inserir no BANCO***");
			clientes.salvar(new Cliente("Geovane"));
			clientes.salvar(new Cliente("Douglas"));
			List<Cliente> todosCLientes = clientes.obterTodos();
			todosCLientes.forEach(System.out::println);

			System.out.println("\n***Atualizar no BANCO***");
			todosCLientes.forEach(c -> {
				c.setNome(c.getNome() + " *Atualizado*");
				clientes.atualizar(c);
			});
			todosCLientes = clientes.obterTodos();
			todosCLientes.forEach(System.out::println);

			System.out.println("\n***Filtro no select***");
			clientes.buscarPorNome("Geo").forEach(System.out::println);

			System.out.println("\n***Deletando Clientes***");
			todosCLientes.forEach(c -> {
				clientes.deletar(c);
			});
			todosCLientes = clientes.obterTodos();
			if(todosCLientes.isEmpty())
				System.out.println("Nenhum cliente encontrado");
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}
}