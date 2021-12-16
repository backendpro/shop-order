package br.com.youtube.shoporder.service;

import br.com.youtube.shoporder.dto.ShopOrderDTO;
import br.com.youtube.shoporder.persistence.model.ShopOrder;
import br.com.youtube.shoporder.persistence.repository.ShopOrderRepository;
import br.com.youtube.shoporder.utils.QueueUtils;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static br.com.youtube.shoporder.utils.QueueUtils.*;

@Service
public class ShopOrderService {

    @Autowired
    private ShopOrderRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void create(ShopOrderDTO orderDTO) {
        ShopOrder shopOrder = repository.save(mapper.map(orderDTO, ShopOrder.class));
        rabbitTemplate.convertAndSend(QUEUE_NAME, shopOrder.getId());
    }

    public Page<ShopOrderDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(s -> mapper.map(s, ShopOrderDTO.class));
    }

    @RabbitListener(queues = QUEUE_NAME)
    private void subscribe(Long id) {
        Optional<ShopOrder> shopOrder = repository.findById(id);

        if (shopOrder.isPresent()) {
            shopOrder.get().setStatus("DONE");
            repository.save(shopOrder.get());
        }
    }
}
