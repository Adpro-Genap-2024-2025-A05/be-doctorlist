package id.ac.ui.cs.advprog.beprofile.repository;

import java.util.Iterator;

public interface ReadRepository<T> {
    T findById(String id);
    Iterator<T> findAll();
}