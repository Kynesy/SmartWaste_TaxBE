package it.unisalento.pas.taxbe.repositories;

import it.unisalento.pas.taxbe.domains.Fee;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IFeeRepository extends MongoRepository<Fee, String> {
    List<Fee> findAllByUserId(String userID);
}
