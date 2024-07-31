package ru.otus.java.basic.processors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.otus.java.basic.HttpRequest;
import ru.otus.java.basic.app.ItemsRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DeleteItemProcessor implements RequestProcessor {
    private static final Logger logger = LogManager.getLogger(DeleteItemProcessor.class);
    private ItemsRepository itemsRepository;

    public DeleteItemProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream out) throws IOException {
        logger.info("Processing DELETE request for URI: {}", request.getUri());
        String idParam = request.getParameter("id");
        if (idParam == null) {
            String response = "HTTP/1.1 400 Bad Request\r\n\r\nMissing 'id' parameter";
            out.write(response.getBytes(StandardCharsets.UTF_8));
            logger.warn("Missing 'id' parameter in request");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            boolean deleted = itemsRepository.deleteItem(id);
            if (deleted) {
                String response = "HTTP/1.1 200 OK\r\n\r\nItem deleted successfully";
                out.write(response.getBytes(StandardCharsets.UTF_8));
                logger.info("Item with id {} deleted successfully", id);
            } else {
                String response = "HTTP/1.1 404 Not Found\r\n\r\nItem not found";
                out.write(response.getBytes(StandardCharsets.UTF_8));
                logger.warn("Item with id {} not found", id);
            }
        } catch (NumberFormatException e) {
            String response = "HTTP/1.1 400 Bad Request\r\n\r\nInvalid 'id' parameter";
            out.write(response.getBytes(StandardCharsets.UTF_8));
            logger.error("Invalid 'id' parameter: {}", idParam, e);
        }
    }
}