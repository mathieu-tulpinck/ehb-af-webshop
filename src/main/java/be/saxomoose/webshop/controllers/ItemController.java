package be.saxomoose.webshop.controllers;

import be.saxomoose.webshop.models.Item;
import be.saxomoose.webshop.repositories.CategoryRepository;
import be.saxomoose.webshop.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;

@Controller
public class ItemController
{
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    @Autowired
    public ItemController(CategoryRepository categoryRepository, ItemRepository itemRepository)
    {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
    }

    private String currentCategory;
    private Collection<Item> items;

    @GetMapping("/items")
    public ModelAndView index()
    {
        var categories= categoryRepository.findAll();
        var modelAndView = new ModelAndView("items/index");
        modelAndView.addObject("categories", categories);

        if (currentCategory == null || currentCategory.isEmpty()) {
            items = itemRepository.findAll();
            currentCategory = "All items";
            modelAndView.addObject("items", items);
        }

        return modelAndView;
    }

    @GetMapping("/items/details/{id}")
    public ModelAndView details(@PathVariable("id") Long id)
    {
        var item = itemRepository.findById(id);

        if (item.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        var modelAndView = new ModelAndView("items/details");
        modelAndView.addObject("item", item.get());

        return modelAndView;
    }
}
