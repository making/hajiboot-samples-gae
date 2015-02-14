package com.example.repository;

import com.example.domain.Customer;
import com.google.appengine.api.datastore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerRepository {
    @Autowired
    DatastoreService datastoreService;

    static final String ENTITY_NAME = "Customer";


    public List<Customer> findAllOrderByName() {
        Query query = new Query(ENTITY_NAME)
                .addSort("firstName", Query.SortDirection.DESCENDING)
                .addSort("lastName", Query.SortDirection.DESCENDING);

        List<Customer> customers = new ArrayList<>();
        for (Entity entity : datastoreService.prepare(query).asIterable()) {
            customers.add(fromEntity(entity));
        }
        return customers;
    }

    public Customer findOne(Long id) {
        Key key = KeyFactory.createKey(ENTITY_NAME, id);
        Entity entity = null;
        try {
            entity = datastoreService.get(key);
        } catch (EntityNotFoundException ignored) {
        }
        return fromEntity(entity);
    }

    public Customer save(Customer customer) {
        Entity entity = toEntity(customer);
        datastoreService.put(entity);
        customer.setId(entity.getKey().getId());
        return null;
    }

    public void delete(Long id) {
        datastoreService.delete(KeyFactory.createKey(ENTITY_NAME, id));
    }

    Customer fromEntity(Entity entity) {
        return new Customer(entity.getKey().getId(),
                (String) entity.getProperty("firstName"),
                (String) entity.getProperty("lastName"));
    }

    Entity toEntity(Customer customer) {
        Entity entity;
        if (customer.getId() != null) {
            entity = new Entity(ENTITY_NAME, customer.getId());
        } else {
            entity = new Entity(ENTITY_NAME);
        }
        entity.setProperty("firstName", customer.getFirstName());
        entity.setProperty("lastName", customer.getLastName());
        return entity;
    }
}
