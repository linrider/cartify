package ua.example.cartify.service;

import java.util.List;

public interface CrudService<ID, Element> {
    List<Element> getAll();
    Element getById(ID id);
    void save(Element element);
    void edit(ID id, Element element);
    void deleteById(ID id);
}
