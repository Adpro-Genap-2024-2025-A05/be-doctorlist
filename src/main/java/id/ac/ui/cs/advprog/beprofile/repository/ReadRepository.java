package id.ac.ui.cs.advprog.beprofile.repository;

import java.util.Iterator;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadRepository<T> {
    T findById(String id);
    Iterator<T> findAll();
}