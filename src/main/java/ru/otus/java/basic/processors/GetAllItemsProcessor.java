package ru.otus.java.basic.processors;

import com.google.gson.Gson;
import ru.otus.java.basic.HttpRequest;
import ru.otus.java.basic.app.Item;
import ru.otus.java.basic.app.ItemsRepository;


import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GetAllItemsProcessor implements RequestProcessor {
    private ItemsRepository itemsRepository;

    public GetAllItemsProcessor(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void execute(HttpRequest request, OutputStream out) throws IOException {
        List<Item> items = itemsRepository.getItems();
        Gson gson = new Gson();
        String itemsJson = gson.toJson(items);
        String response = "" +
                "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json\r\n" +
                "\r\n" +
                itemsJson;
        out.write(response.getBytes(StandardCharsets.UTF_8));
    }
}
