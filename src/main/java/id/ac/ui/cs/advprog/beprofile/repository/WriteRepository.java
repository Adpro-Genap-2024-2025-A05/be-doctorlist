package id.ac.ui.cs.advprog.beprofile.repository;
import org.springframework.stereotype.Repository;

@Repository
public interface WriteRepository<T> {
    T create(T object);
    T update(String id, T object);
    T delete(String id);
}
