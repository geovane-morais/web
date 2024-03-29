package web.api.app.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.api.app.domain.entity.Cliente;
import web.api.app.domain.entity.Pedido;
import web.api.app.domain.entity.Produto;
import web.api.app.domain.entity.enums.StatusPedido;
import web.api.app.domain.entity.itemPedido;
import web.api.app.domain.repository.Clientes;
import web.api.app.domain.repository.Pedidos;
import web.api.app.domain.repository.Produtos;
import web.api.app.domain.repository.itemPedidos;
import web.api.app.exception.PedidoNaoEncontradoException;
import web.api.app.exception.RegraNegocioException;
import web.api.app.rest.dto.pedidoDTO;
import web.api.app.rest.dto.itemPedidoDTO;
import web.api.app.service.PedidoService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PedidoServiceImpl implements PedidoService {
    private final Pedidos pedidosRepository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final itemPedidos itemsPedidoRepository;

    @Override
    public Pedido salvar(pedidoDTO dto) {
        Cliente cliente = clientesRepository
                .findById(dto.getClienteId())
                .orElseThrow(()-> new RegraNegocioException("Codigo de cliente invalido"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setStatusPedido(StatusPedido.REALIZADO);

        List<itemPedido> itemsPedidoNovo = convertItemsDtoToItemPedido(pedido,dto.getItems());
        pedido.setItemPedidos(itemsPedidoNovo);
        pedidosRepository.save(pedido);
        itemsPedidoRepository.saveAll(itemsPedidoNovo);

        /*Salvando o pedido no cliente*/
        Set<Pedido> pedidosCli = cliente.getPedidos();
        pedidosCli.add(pedido);
        cliente.setPedidos(pedidosCli);
        clientesRepository.save(cliente);

        return pedido;
    }

    private List<itemPedido> convertItemsDtoToItemPedido(Pedido pedido, List<itemPedidoDTO> itemsDTO){
        if(itemsDTO.isEmpty()){
            throw new RegraNegocioException("Lista de itens está vazia");
        }
        return itemsDTO
                .stream()
                .map((itemDTO)->{
                    Produto produto = produtosRepository
                            .findById(itemDTO.getProdutoId())
                            .orElseThrow(()->
                                    new RegraNegocioException("Codigo de produto invalido:" + itemDTO.getProdutoId())
                            );

                    itemPedido itemPedidoIndividual = new itemPedido();
                    itemPedidoIndividual.setQuantidade(itemDTO.getQuantidade());
                    itemPedidoIndividual.setPedido(pedido);
                    itemPedidoIndividual.setProduto(produto);

                    return itemPedidoIndividual;
                }).collect(Collectors.toList());
    }

    @Override
    public Optional<Pedido> obterPedidoCompletoPorId(Integer id) {
        return pedidosRepository.findByIdFetchItems(id);
    }

    @Override
    @Transactional
    public void updateStatusPedido(Integer id, StatusPedido status) {
        Pedido pedido = pedidosRepository
                .findById(id)
                .map(p->{
                    p.setStatusPedido(status);
                    return pedidosRepository.save(p);
                }).orElseThrow(()-> new PedidoNaoEncontradoException());
        System.out.println("Pedido: " + pedido.getStatusPedido());
    }
}
